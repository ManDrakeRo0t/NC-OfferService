package ru.bogatov.offerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bogatov.offerservice.entity.Attribute;

import java.util.Optional;
import java.util.UUID;

public interface AttributeRepository extends JpaRepository<Attribute, UUID> {
    public Optional<Attribute> findAttributeByName(String name);

    public Attribute getAttributeById(UUID id);

    public Optional<Attribute> findAttributeById(UUID id);
}

