package ru.bogatov.offerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bogatov.offerservice.entity.AttributesValues;
import sun.font.AttributeValues;

import java.util.UUID;

public interface AttributeValuesRepository extends JpaRepository<AttributesValues, UUID> {
}
