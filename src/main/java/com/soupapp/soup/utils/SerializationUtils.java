package com.soupapp.soup.utils;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;


public class SerializationUtils {

    public static String serializeObject(Object object) {
        try {
            return new ObjectMapper().writeValueAsString(object);
        }
        catch(JsonProcessingException ex) {
            return null;
        }
    }
}
