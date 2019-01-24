package com.maks.assetaccounting.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public abstract class AbstractDto {

    @EqualsAndHashCode.Include
    private Long id;

    boolean isNew() {
        return getId() == null;
    }
}
