package ru.bogatov.offerservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.bogatov.offerservice.entity.Attribute;
import ru.bogatov.offerservice.entity.AttributesValues;
import ru.bogatov.offerservice.repository.AttributeRepository;
import ru.bogatov.offerservice.repository.AttributeValuesRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AttributeService {
    AttributeRepository attributeRepository;
    AttributeValuesRepository attributeValuesRepository;

    public AttributeService(@Autowired AttributeRepository attributeRepository,
                            @Autowired AttributeValuesRepository attributeValuesRepository){
        this.attributeRepository = attributeRepository;
        this.attributeValuesRepository = attributeValuesRepository;
    }

    public List<AttributesValues> saveAttributes(List<AttributesValues> list){
        list.forEach(e -> e.setAttribute(saveAttribute(e.getAttribute())));
        return attributeValuesRepository.saveAll(list);
    }

    public Attribute saveAttribute(Attribute attribute){
        Optional<Attribute> optionalAttribute = attributeRepository.findAttributeByName(attribute.getName());
        return optionalAttribute.orElseGet(() -> attributeRepository.save(attribute));

    }

    public void deleteValues(List<AttributesValues> list){
        attributeValuesRepository.deleteAll(list);
    }

    public void deleteAttribute(String id) throws RuntimeException{
        Attribute attribute = findAttribute(id);
        List<AttributesValues> valuesList = attributeValuesRepository.findAll();
        valuesList.forEach(value -> {
            if(value.getAttribute().getId().equals(attribute.getId())){
                throw new RuntimeException("this attribute is used");
            }
        });
        attributeRepository.delete(attribute);
    }

    public List<Attribute> getAll(){
        return attributeRepository.findAll();
    }

    public Attribute getAttribute(String id) throws RuntimeException{
        return findAttribute(id);
    }

    public Attribute editAttribute(String id,Attribute attribute) throws RuntimeException{
        Attribute attributeFromDb = findAttribute(id);
        attributeFromDb.update(attribute);
        return attributeRepository.save(attributeFromDb);

    }

    public Attribute findAttribute(String id){
        Optional<Attribute> optionalAttribute = attributeRepository.findAttributeById(UUID.fromString(id));
        if(!optionalAttribute.isPresent()){
            throw new RuntimeException("cant find Attribute with id : " + id);
        }else{
            return optionalAttribute.get();
        }
    }




}
