package com.maks.assetaccounting.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class AssetDto extends AbstractDto {
    @NotBlank
    private String name;

    private ZonedDateTime creationDate;

    @NotNull
    private ZonedDateTime transferDate;

    private double cost;

    private String companyName;

    private String username;
}
