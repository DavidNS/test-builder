package com.dns.resttestbuilder;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

import org.springframework.stereotype.Component;

import com.dns.resttestbuilder.exception.NotValidJSONFormatException;
import com.google.gson.Gson;

@Component
@Converter
public class JSONObjectConverter implements AttributeConverter<Object, String> {
 
	
    @Override
    public String convertToDatabaseColumn(Object customerInfo) {
        try {
            return  new Gson().toJson(customerInfo);
        } catch (Exception e) {
        	throw new NotValidJSONFormatException("Error parsing as string format for DB of: "+customerInfo);
        }
    }
 
    @Override
    public  Object convertToEntityAttribute(String customerInfoJSON) {
        try {
            return  new Gson().fromJson(customerInfoJSON, Object.class);
        } catch (Exception e) {
        	throw new NotValidJSONFormatException(customerInfoJSON, Object.class);
        }

    }
 
}