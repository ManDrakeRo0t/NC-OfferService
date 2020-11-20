package ru.bogatov.offerservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.bogatov.offerservice.client.AuthenticationServiceClient;
import ru.bogatov.offerservice.client.CustomerServiceClient;
import ru.bogatov.offerservice.client.OrderServiceClient;
import ru.bogatov.offerservice.client.PaidTypeServiceClient;
import ru.bogatov.offerservice.configiration.JwtProvider;
import ru.bogatov.offerservice.dto.LoginRequest;
import ru.bogatov.offerservice.dto.PaidType;
import ru.bogatov.offerservice.entity.AttributesValues;
import ru.bogatov.offerservice.entity.Category;
import ru.bogatov.offerservice.entity.Offer;
import ru.bogatov.offerservice.repository.CategoryRepository;
import ru.bogatov.offerservice.repository.OfferRepository;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class OfferService {
    private OfferRepository offerRepository;
    private AttributeService attributeService;
    private CategoryRepository categoryRepository;
    private PaidTypeServiceClient paidTypeServiceClient;
    private AuthenticationServiceClient authenticationServiceClient;
    private OrderServiceClient orderServiceClient;
    private JwtProvider jwtProvider;
    private CustomerServiceClient customerServiceClient;

    public OfferService(@Autowired OfferRepository offerRepository,
                        @Autowired AttributeService attributeService,
                        @Autowired CategoryRepository categoryRepository,
                        @Autowired PaidTypeServiceClient paidTypeServiceClient,
                        @Autowired OrderServiceClient orderServiceClient,
                        @Autowired AuthenticationServiceClient authenticationServiceClient,
                        @Autowired JwtProvider jwtProvider,
                        @Autowired CustomerServiceClient customerServiceClient){
        this.offerRepository = offerRepository;
        this.attributeService = attributeService;
        this.categoryRepository = categoryRepository;
        this.paidTypeServiceClient = paidTypeServiceClient;
        this.authenticationServiceClient = authenticationServiceClient;
        this.orderServiceClient = orderServiceClient;
        this.jwtProvider= jwtProvider;
        this.customerServiceClient = customerServiceClient;
    }

    public ResponseEntity<Object> makeOrder(String id, LoginRequest loginRequest) throws URISyntaxException {
        String token = authenticationServiceClient.login(loginRequest).getBody();
        if(token != null){
            Offer offer = getOfferById(id);
            if(orderServiceClient.createOrder(token,offer).getStatusCode().equals(HttpStatus.CREATED)){
                return ResponseEntity.status(200).build();
            }
        }
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    public Offer createOffer(Offer offer){
        offer.setCategory(createCategory(offer.getCategory().getName()));
        List<AttributesValues> attributesValuesList = attributeService.saveAttributes(offer.getAttributesValues());
        offer.setAttributesValues(attributesValuesList);
        return offerRepository.save(offer);
    }

    public List<Offer> getOffersForCustomer(String id){
        List<PaidType> paidTypes = customerServiceClient.getCustomersPaidTypes(jwtProvider.generateM2mToken(),id);
        List<Offer> offers = getOffers();
        List<Offer> availableOffer = new ArrayList<>();
        for(Offer o : offers){
            for(PaidType p : paidTypes){
                if(o.getPaidTypeId().equals(p.getId())) availableOffer.add(o);
            }
        }
        return availableOffer;
    }

    public Offer getOfferById(String id) throws RuntimeException{
        return findOfferById(id);
    }

    public List<Offer> getOffers(){
        return offerRepository.findAll();
    }

    public void deleteOffer(String id) throws RuntimeException{
        Offer offer = findOfferById(id);
        attributeService.deleteValues(offer.getAttributesValues());
        offerRepository.delete(findOfferById(id));
    }

    public Offer editOffer(String id,Offer offer) throws RuntimeException{
        Offer offerFromDb = findOfferById(id);
        offerFromDb.update(offer);
        if(offer.getCategory() != null) offerFromDb.setCategory(createCategory(offer.getCategory().getName()));
        if(offer.getAttributesValues() != null){
            attributeService.deleteValues(offerFromDb.getAttributesValues());
            offerFromDb.setAttributesValues(attributeService.saveAttributes(offer.getAttributesValues()));
        }
        return offerRepository.save(offerFromDb);
    }

    private Category createCategory(String name){
        Optional<Category> optionalCategory = categoryRepository.findCategoriesByName(name);
        if(optionalCategory.isPresent()){
            return optionalCategory.get();
        }
        Category category = new Category();
        category.setName(name);
        return categoryRepository.save(category);
    }

    private Offer findOfferById(String id) throws RuntimeException{
        Optional<Offer> optionalOffer = offerRepository.findOfferById(UUID.fromString(id));
        if(!optionalOffer.isPresent()){
            throw new RuntimeException("cant find offer with id : " + id);
        }else{
            return optionalOffer.get();
        }

    }
}
