package com.maks.assetaccounting.dto;

import com.maks.assetaccounting.HasId;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class CompanyDto implements HasId {

    private Long id;

    private String name;

    private List<AssetDto> assetDtos;
}
