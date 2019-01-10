package com.maks.assetaccounting.converter;

import com.maks.assetaccounting.dto.AssetDto;
import com.maks.assetaccounting.entity.Asset;
import com.maks.assetaccounting.entity.Company;
import com.maks.assetaccounting.repository.CompanyRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AssetConverterTest {
    @Mock
    private CompanyRepository companyRepository;
    @InjectMocks
    private AssetConverter assetConverter;

    @Test
    public void testConvertToDto() {
        assertEquals(assetConverter.convertToDto(getAsset()), getAssetDto());
    }

    @Test
    public void testConvertToEntity() {
        when(companyRepository.findByName(anyString())).thenReturn(getCompany());

        assertEquals(assetConverter.convertToEntity(getAssetDto()), getAsset());
    }

    private Asset getAsset() {
        final Asset asset = new Asset();
        asset.setId(1L);
        asset.setName("Car");
        asset.setCost(30000);
        asset.setCompany(getCompany());
        asset.setCreationDate(ZonedDateTime.of(LocalDateTime
                .of(2018, 12, 25, 10, 10), ZoneId.systemDefault()));
        asset.setTransferDate(ZonedDateTime.of(LocalDateTime
                .of(2018, 12, 25, 10, 10), ZoneId.systemDefault()));
        return asset;
    }

    private AssetDto getAssetDto() {
        final AssetDto dto = new AssetDto();
        dto.setId(1L);
        dto.setName("Car");
        dto.setCost(30000);
        dto.setCompanyName(getCompany().getName());
        dto.setCreationDate(ZonedDateTime.of(LocalDateTime
                .of(2018, 12, 25, 10, 10), ZoneId.systemDefault()));
        dto.setTransferDate(ZonedDateTime.of(LocalDateTime
                .of(2018, 12, 25, 10, 10), ZoneId.systemDefault()));
        return dto;
    }

    private Company getCompany() {
        final Company company = new Company();
        company.setId(1L);
        company.setName("Oracle");
        return company;
    }
}
