package com.maks.assetaccounting.util;

import com.maks.assetaccounting.HasId;

public class ValidationUtil {

    private ValidationUtil() {
    }

    public static void assureIdConsistent(HasId bean, long id) {
        if (bean.getId() != id) {
            throw new IllegalArgumentException(bean + " must be with id=" + id);
        }
    }
}
