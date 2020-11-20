package ru.bogatov.offerservice.controller;

import com.sun.deploy.net.HttpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.bogatov.offerservice.client.AuthenticationServiceClient;
import ru.bogatov.offerservice.client.CustomerServiceClient;
import ru.bogatov.offerservice.dto.LoginRequest;
import ru.bogatov.offerservice.dto.PaidType;
import ru.bogatov.offerservice.entity.Offer;
import ru.bogatov.offerservice.service.OfferService;

import javax.servlet.http.HttpServletRequest;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/offers")
@PreAuthorize("hasAnyAuthority('USER')")
public class OfferController {

    private OfferService offerService;

    public OfferController(@Autowired OfferService offerService){
        this.offerService = offerService;
    }

    @PostMapping("")
    public Offer createOffer(@RequestBody Offer offer){
        return offerService.createOffer(offer);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Offer> getOffer(@PathVariable String id, HttpServletRequest request){
        String s = request.getHeaders("Authorization").nextElement();
        try{
            return ResponseEntity.ok().body(offerService.getOfferById(id));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PostMapping("/{id}/to-order")
    public ResponseEntity<Object> createOrder(@PathVariable String id, @RequestBody LoginRequest customer) throws URISyntaxException {
        return offerService.makeOrder(id,customer);
    }
    @GetMapping("/for-customer/{id}")
    public List<Offer> getOffersForCustomer(@PathVariable String id){
        return offerService.getOffersForCustomer(id);
    }

    @GetMapping("")
    public List<Offer> getOffers(){
        return offerService.getOffers();
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteOffer(@PathVariable String id){
        try{
            return ResponseEntity.ok("was deleted");
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @PutMapping("/{id}")
    public ResponseEntity<Offer> updateOffer(@PathVariable String id,@RequestBody Offer offer){
        try{
            return ResponseEntity.ok().body(offerService.editOffer(id,offer));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
 }
