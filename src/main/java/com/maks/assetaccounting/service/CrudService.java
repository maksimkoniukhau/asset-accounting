package com.maks.assetaccounting.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CrudService<T1, T2> {

    T1 create(final T2 obj);

    T1 get(final Long id);

    T1 update(final T2 obj, final Long id);

    T1 delete(final Long id);

    List<T1> getAll();

    void deleteAll(final List<T2> userDtoList);

    Page<T1> findAnyMatching(final Optional<String> filter, final Pageable pageable);

    long countAnyMatching(final Optional<String> filter);
}
