package com.dns.resttestbuilder.entity;

import java.io.IOException;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.exception.NotValidJSONFormatException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Component
@Converter
public class HashMapConverter implements AttributeConverter<Object, String> {
 
	
    @Override
    public String convertToDatabaseColumn( Object customerInfo) {
 
        String customerInfoJson = null;
        try {
            customerInfoJson = new ObjectMapper().writeValueAsString(customerInfo);
        } catch (final JsonProcessingException e) {
        	throw new NotValidJSONFormatException("Error parsing as string format for DB of: "+customerInfo);
        }
        return customerInfoJson;
    }
 
    @Override
    public  Object convertToEntityAttribute(String customerInfoJSON) {
 
    	Object customerInfo = null;
        try {
            customerInfo = new ObjectMapper().readValue(customerInfoJSON, Object.class);
        } catch (final IOException e) {
        	throw new NotValidJSONFormatException(customerInfoJSON, Object.class);
        }
 
        return customerInfo;
    }
 
}