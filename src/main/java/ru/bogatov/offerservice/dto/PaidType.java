package ru.bogatov.offerservice.dto;


import lombok.Data;

import java.util.UUID;

@Data
public class PaidType {
    private UUID id;
    private String name;
}
