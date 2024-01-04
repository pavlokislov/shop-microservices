package com.pkislov.orderservice.service;

import com.pkislov.orderservice.dto.OrderLineItemsDto;
import com.pkislov.orderservice.dto.OrderRequest;
import com.pkislov.orderservice.event.OrderPlacedEvent;
import com.pkislov.orderservice.exception.ProductIsNotInStock;
import com.pkislov.orderservice.model.Order;
import com.pkislov.orderservice.model.OrderLineItems;
import com.pkislov.orderservice.repository.OrderRepository;
import io.micrometer.observation.Observation;
import io.micrometer.observation.ObservationRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private static final String PRODUCT_IS_NOT_IN_STUCK_ERROR = "Product is not in stock, please try again later";
    private static final String INVENTORY_URI = "http://inventory-service/api/inventory";
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    private final ObservationRegistry observationRegistry;
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;

    public String placeOrder(final OrderRequest orderRequest) {
        Order order = new Order();
        order.setOrderNumber(UUID.randomUUID().toString());

        List<OrderLineItems> orderLineItems = getOrderLineItems(orderRequest.getOrderLineItemsDtoList());

        order.setOrderListItemsList(orderLineItems);

        Observation inventoryServiceObservation = Observation.createNotStarted("inventory-service-lookup", this.observationRegistry);
        inventoryServiceObservation.lowCardinalityKeyValue("call", "inventory-service");

        return inventoryServiceObservation.observe(() -> {
            Boolean result = webClientBuilder.build()
                    .get()
                    .uri(INVENTORY_URI,
                            uriBuilder -> uriBuilder.queryParam("skuCode", getSkuCodes(orderLineItems))
                                    .build())
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

            if (Boolean.TRUE.equals(result)) {
                orderRepository.save(order);
                kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()));
                return "Order Placed Successfully";
            } else {
                throw new ProductIsNotInStock(PRODUCT_IS_NOT_IN_STUCK_ERROR);
            }
        });
    }

    private List<OrderLineItems> getOrderLineItems(List<OrderLineItemsDto> orderLineItemsDtoList) {
        return orderLineItemsDtoList.stream()
                .map(this::mapToDto)
                .toList();
    }

    private List<String> getSkuCodes(List<OrderLineItems> orderLineItems) {
        return orderLineItems.stream()
                .map(OrderLineItems::getSkuCode)
                .toList();
    }

    private OrderLineItems mapToDto(final OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = new OrderLineItems();
        orderLineItems.setPrice(orderLineItemsDto.getPrice());
        orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
        orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
        return orderLineItems;
    }
}
