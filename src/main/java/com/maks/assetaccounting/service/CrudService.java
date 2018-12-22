package com.maks.assetaccounting.service;

import java.util.List;

public interface CrudService<T> {

    T create(T obj);

    T get(T obj);

    T update(T obj);

    T delete(T obj);

    List<T> getAll();
}
