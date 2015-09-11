package com.lxnj.car_filter.utils;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * Created by Yiyong on 4/13/15.
 */
public class ObjectMapperFactory {
    private static ObjectMapper mapper;
    public static ObjectMapper getMapperInstance(){
        if(mapper == null)
            mapper = new ObjectMapper();
        return mapper;
    }
}
