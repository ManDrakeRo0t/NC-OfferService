package ru.bogatov.offerservice.entity;


import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "offers")
public class Offer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    private float price;
    private UUID paidTypeId; //customer service
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "categoty_id")
    private Category category;
    @OneToMany( fetch = FetchType.EAGER)
    private List<AttributesValues> attributesValues;

    public void update(Offer offer){
        if(offer.name != null) this.name = offer.name;
        if(offer.price != 0f) this.price = offer.price;
    }


    public Offer(){}
}
