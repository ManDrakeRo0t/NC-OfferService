package ru.bogatov.offerservice.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import ru.bogatov.offerservice.dto.PaidType;

import java.util.List;

@FeignClient(name = "paid-type",url = "http://localhost:8091/paid-types")
public interface PaidTypeServiceClient {
    @GetMapping("/{id}")
    public PaidType getById(@RequestHeader(value = "Authorization", required = true) String authorizationHeader, @PathVariable String id);

    @GetMapping("")
    public List<PaidType> getAll(@RequestHeader(value = "Authorization", required = true) String authorizationHeader);
}
