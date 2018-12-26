package com.maks.assetaccounting.service;

import java.util.List;

public interface CrudService<T1, T2> {

    T1 create(final T2 obj);

    T1 get(final Long id);

    T1 update(final T2 obj, final Long id);

    T1 delete(final Long id);

    List<T1> getAll();
}
