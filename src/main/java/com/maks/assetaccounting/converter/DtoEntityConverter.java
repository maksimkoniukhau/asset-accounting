package com.maks.assetaccounting.converter;

import java.util.List;
import java.util.stream.Collectors;

public interface DtoEntityConverter<T, E> {

    T convertToDto(final E entity);

    E convertToEntity(final T dto);

    default List<T> convertListToDto(final List<E> entities) {
        if (entities != null && !entities.isEmpty()) {
            return entities.stream()
                    .map(this::convertToDto)
                    .collect(Collectors.toList());
        }
        return null;
    }

    default List<E> convertListToEntity(final List<T> dtos){
        if (dtos != null && !dtos.isEmpty()) {
            return dtos.stream()
                    .map(this::convertToEntity)
                    .collect(Collectors.toList());
        }
        return null;
    }
}
