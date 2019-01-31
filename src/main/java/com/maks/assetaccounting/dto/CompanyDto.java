package com.maks.assetaccounting.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class CompanyDto extends AbstractDto {
    @NotBlank
    private String name;

    private List<AssetDto> assetDtos;

    private String username;
}
