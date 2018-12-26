package com.maks.assetaccounting.util;

import com.maks.assetaccounting.dto.AbstractDto;

public class ValidationUtil {

    private ValidationUtil() {
    }

    public static void assureIdConsistent(final AbstractDto dto, final long id) {
        if (dto.getId() != id) {
            throw new IllegalArgumentException(dto + " must be with id=" + id);
        }
    }
}
