package ru.bogatov.offerservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.UUID;

@Data
@Entity
@Table(name = "attribute_values")
public class AttributesValues {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "attribute_id")
    private Attribute attribute;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "offer_id")
    private Offer offer;
    private String stringValue;
    private Integer integerValue;

    public AttributesValues() {
    }
}