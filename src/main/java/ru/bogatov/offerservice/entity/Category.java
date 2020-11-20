package ru.bogatov.offerservice.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "categories")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private String name;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Offer> offerList;

    public Category(){}
}
