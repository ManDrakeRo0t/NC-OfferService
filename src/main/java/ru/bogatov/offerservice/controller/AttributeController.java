package ru.bogatov.offerservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.bogatov.offerservice.configiration.JwtProvider;
import ru.bogatov.offerservice.entity.Attribute;
import ru.bogatov.offerservice.service.AttributeService;

import java.util.List;

@RestController
@RequestMapping("/attributes")
public class AttributeController {
    AttributeService attributeService;
    @Autowired
    JwtProvider jwtProvider;

    public AttributeController(@Autowired AttributeService attributeService){
        this.attributeService = attributeService;
    }
    @GetMapping("/m2m")
    public String getM2m(){
        return jwtProvider.generateM2mToken();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Object> getAttributeById(@PathVariable String id){
        try{
            return ResponseEntity.ok().body(attributeService.getAttribute(id));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    @GetMapping("")
    public List<Attribute> getAttributes(){
        return attributeService.getAll();
    }
    @PostMapping("")
    public Attribute createAttribute(@RequestBody Attribute attribute){
        return attributeService.saveAttribute(attribute);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteAttribute(@PathVariable String id){
        try{
            attributeService.deleteAttribute(id);
            return ResponseEntity.ok("was deleted");
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}") //todo gateWay service
    public ResponseEntity<Attribute> editAttribute(@PathVariable String id,@RequestBody Attribute attribute){
        try{
            return ResponseEntity.ok(attributeService.editAttribute(id,attribute));
        }catch (RuntimeException e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

}

