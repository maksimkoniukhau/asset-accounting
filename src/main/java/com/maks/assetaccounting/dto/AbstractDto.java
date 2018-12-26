package com.maks.assetaccounting.dto;

import lombok.Data;

@Data
public abstract class AbstractDto {

    private Long id;

    boolean isNew() {
        return getId() == null;
    }
}
