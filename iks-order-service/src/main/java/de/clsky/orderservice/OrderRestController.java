package de.clsky.orderservice;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderRestController {
    private final OrderRepository orderRepository;

    @PostMapping(path = "/orders", consumes = {MediaType.APPLICATION_JSON_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE})
    public ResponseEntity<MyOrder> placeOrder(@RequestBody MyOrder order) {

        if ("not_valid".equals(order.getItemName())) {
            return ResponseEntity.badRequest().build();
        }
        orderRepository.save(order);
        return ResponseEntity.ok(order);

    }
}

