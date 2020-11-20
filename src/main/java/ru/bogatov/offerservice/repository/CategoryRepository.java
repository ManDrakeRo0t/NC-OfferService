package ru.bogatov.offerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bogatov.offerservice.entity.Category;

import java.util.Optional;
import java.util.UUID;

public interface CategoryRepository extends JpaRepository<Category, UUID> {
    Optional<Category> findCategoriesByName(String name);
}
