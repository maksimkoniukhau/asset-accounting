package com.maks.assetaccounting.converter;

import com.maks.assetaccounting.dto.CompanyDto;
import com.maks.assetaccounting.entity.Company;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collections;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class CompanyConverterTest {
    @Mock
    private AssetConverter assetConverter;
    @InjectMocks
    private CompanyConverter companyConverter;

    @Test
    public void testConvertToDto() {
        assertEquals(companyConverter.convertToDto(getCompany()), getCompanyDto());
    }

    @Test
    public void testConvertToEntity() {
        assertEquals(companyConverter.convertToEntity(getCompanyDto()), getCompany());
    }

    private Company getCompany() {
        final Company company = new Company();
        company.setId(1L);
        company.setName("Oracle");
        company.setAssets(Collections.emptyList());
        return company;
    }

    private CompanyDto getCompanyDto() {
        final CompanyDto dto = new CompanyDto();
        dto.setId(1L);
        dto.setName("Oracle");
        dto.setAssetDtos(Collections.emptyList());
        return dto;
    }
}
