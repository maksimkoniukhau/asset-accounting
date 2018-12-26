package com.maks.assetaccounting.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class AssetDto extends AbstractDto {
    @NotBlank
    private String name;

    private Date creationDate;

    private Date transferDate;

    private double cost;

    private String companyName;
}
