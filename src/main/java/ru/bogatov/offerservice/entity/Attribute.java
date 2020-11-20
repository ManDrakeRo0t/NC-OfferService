package ru.bogatov.offerservice.entity;

import lombok.Data;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Data
@Table(name = "attribute")
@Entity
public class Attribute {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    private String description;
    @OneToMany( fetch = FetchType.EAGER)
    private List<AttributesValues> attributesValues;

    public void update(Attribute attribute){
        if(attribute.name != null) this.name = attribute.name;
        if(attribute.description != null) this.description = attribute.description;
    }

    public Attribute() {
    }
}
