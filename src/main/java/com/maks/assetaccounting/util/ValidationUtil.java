package com.maks.assetaccounting.util;

import com.maks.assetaccounting.dto.AbstractDto;

import javax.persistence.EntityNotFoundException;

public class ValidationUtil {

    private ValidationUtil() {
    }

    public static <T> T checkNotFound(final T object, final String msg) {
        checkNotFound(object != null, msg);
        return object;
    }

    public static void checkNotFound(final boolean found, final String msg) {
        if (!found) {
            throw new EntityNotFoundException("Not found entity with " + msg);
        }
    }

    public static void assureIdConsistent(final AbstractDto dto, final long id) {
        if (dto.getId() != id) {
            throw new IllegalArgumentException(dto + " must be with id=" + id);
        }
    }
}
