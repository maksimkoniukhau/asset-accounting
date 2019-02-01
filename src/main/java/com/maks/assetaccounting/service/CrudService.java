package com.maks.assetaccounting.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface CrudService<T> {

    T create(final T obj, final String username);

    T get(final Long id, final Long authUserId);

    T update(final T obj, final Long id, final String username);

    T delete(final Long id, final Long authUserId);

    List<T> getAll(final Long authUserId);

    void deleteAll(final List<T> userDtoList, final Long authUserId);

    Page<T> findAnyMatching(final Optional<String> filter, final Pageable pageable, final Long authUserId);

    long countAnyMatching(final Optional<String> filter, final Long authUserId);
}
