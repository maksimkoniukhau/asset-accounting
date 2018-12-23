package com.maks.assetaccounting.dto;

import com.maks.assetaccounting.HasId;
import lombok.Data;

import java.util.Date;

@Data
public class AssetDto implements HasId {

    private Long id;

    private String name;

    private Date creationDate;

    private Date transferDate;

    private double cost;

    private String companyName;
}
