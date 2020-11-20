package ru.bogatov.offerservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.bogatov.offerservice.dto.LoginRequest;

@FeignClient(name = "authentication-service",url = "http://localhost:8091/auth")
public interface AuthenticationServiceClient {
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest loginRequest);
}
