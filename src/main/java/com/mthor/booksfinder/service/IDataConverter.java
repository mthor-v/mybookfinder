package com.mthor.booksfinder.service;

public interface IDataConverter {

    <T> T getData(String json, Class<T> gClass);

}
