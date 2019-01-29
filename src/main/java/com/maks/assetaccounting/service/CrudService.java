package com.maks.assetaccounting.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CrudService<T> {

    T create(final T obj);

    T get(final Long id);

    T update(final T obj, final Long id);

    T delete(final Long id);

    List<T> getAll();

    void deleteAll(final List<T> userDtoList);

    Page<T> findAnyMatching(final Optional<String> filter, final Pageable pageable);

    long countAnyMatching(final Optional<String> filter);
}
