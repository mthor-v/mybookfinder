package com.mthor.booksfinder.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class DataConverterImpl implements IDataConverter {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public <T> T getData(String json, Class<T> gClass) {
        try {
            return objectMapper.readValue(json, gClass);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
