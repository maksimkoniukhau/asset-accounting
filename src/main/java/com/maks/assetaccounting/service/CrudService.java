package com.maks.assetaccounting.service;

import java.util.List;

public interface CrudService<T1, T2> {

    T1 create(T2 obj);

    T1 get(Long id);

    T1 update(T2 obj, Long id);

    T1 delete(Long id);

    List<T1> getAll();
}
