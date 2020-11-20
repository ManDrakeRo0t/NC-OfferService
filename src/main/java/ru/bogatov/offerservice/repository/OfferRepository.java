package ru.bogatov.offerservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.bogatov.offerservice.entity.Offer;

import java.util.Optional;
import java.util.UUID;

public interface OfferRepository extends JpaRepository<Offer, UUID> {
    public Optional<Offer> findOfferById(UUID id);

    public Offer getOfferById(UUID id);


}
