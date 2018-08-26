package com.barath.app.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

public class JacksonUtils {

    private static final Logger logger = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private static final ObjectMapper mapper = new ObjectMapper();

    public static String toJson(Object inputObj){

        try{
            return mapper.writeValueAsString(inputObj);
        }catch (Exception ex){
            logger.error("exception in conversion {}",ex.getCause());
            ex.printStackTrace();
        }

        return new String();

    }

    public static Object fromJson(String json, Class<?> clazz){

        try {
            return mapper.readValue(json, clazz);
        } catch (Exception ex){
            logger.error(" exception in converting json to object {}",ex.getMessage());
            ex.printStackTrace();
        }
        return new Object();
    }
}
