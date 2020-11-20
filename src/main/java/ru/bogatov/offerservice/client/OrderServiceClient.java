package ru.bogatov.offerservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.bogatov.offerservice.entity.Offer;

@FeignClient(name = "order-service",url="http://localhost:8092/orders")
public interface OrderServiceClient {
    @PostMapping("/create")
    public ResponseEntity<Object> createOrder(@RequestHeader(value = "Authorization", required = true) String token,
                                              @RequestBody Offer offer);
}
