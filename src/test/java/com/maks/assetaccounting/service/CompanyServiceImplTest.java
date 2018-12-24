package com.maks.assetaccounting.service;

import com.maks.assetaccounting.converter.CompanyConverter;
import com.maks.assetaccounting.dto.AssetDto;
import com.maks.assetaccounting.dto.CompanyDto;
import com.maks.assetaccounting.entity.Company;
import com.maks.assetaccounting.repository.CompanyRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CompanyServiceImplTest {
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private CompanyConverter companyConverter;
    @Mock
    private Company company;
    @Mock
    private CompanyDto companyDto;
    @InjectMocks
    private CompanyServiceImpl companyServiceImpl;

    @Test
    public void testCreate() {
        when(companyConverter.convertToEntity(any(CompanyDto.class))).thenReturn(company);
        when(companyConverter.convertToDto(any(Company.class))).thenReturn(companyDto);
        when(companyRepository.save(any(Company.class))).thenReturn(company);

        assertEquals(companyDto, companyServiceImpl.create(companyDto));

        verify(companyRepository, times(1)).save(any(Company.class));
        verify(companyConverter, times(1)).convertToEntity(any(CompanyDto.class));
        verify(companyConverter, times(1)).convertToDto(any(Company.class));
    }

    @Test
    public void testGet() {
        when(companyConverter.convertToDto(any(Company.class))).thenReturn(companyDto);
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(company));

        assertEquals(companyDto, companyServiceImpl.get(33L));

        verify(companyRepository, times(1)).findById(anyLong());
        verify(companyConverter, times(1)).convertToDto(any(Company.class));
    }

    //Mockito doesn't mock static methods
    @Test
    public void testUpdate() {
        final CompanyDto oracleDto = new CompanyDto();
        oracleDto.setId(3L);
        oracleDto.setName("Oracle");

        when(companyConverter.convertToEntity(any(CompanyDto.class))).thenReturn(company);
        when(companyConverter.convertToDto(any(Company.class))).thenReturn(oracleDto);
        when(companyRepository.save(any(Company.class))).thenReturn(company);

        assertEquals(oracleDto, companyServiceImpl.update(oracleDto, 3L));

        verify(companyRepository, times(1)).save(any(Company.class));
        verify(companyConverter, times(1)).convertToEntity(any(CompanyDto.class));
        verify(companyConverter, times(1)).convertToDto(any(Company.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithIllegalArgument() {
        companyServiceImpl.update(companyDto, 5L);
    }

    @Test
    public void testDelete() {
        when(companyConverter.convertToDto(any(Company.class))).thenReturn(companyDto);
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(company));

        assertEquals(companyDto, companyServiceImpl.delete(anyLong()));

        verify(companyRepository, times(1)).findById(anyLong());
        verify(companyRepository, times(1)).deleteById(anyLong());
        verify(companyConverter, times(1)).convertToDto(any(Company.class));
    }

    @Test
    public void testFindCompaniesWithAssetsInAscendingOrderIncludingTestGetAll() {
        final List<CompanyDto> companies = new ArrayList<>();

        final CompanyDto dto = new CompanyDto();
        final AssetDto assetDto = new AssetDto();
        assetDto.setCost(55);
        final AssetDto assetDto1 = new AssetDto();
        assetDto1.setCost(33);
        final List<AssetDto> list = new ArrayList<>();
        list.add(assetDto);
        list.add(assetDto1);
        dto.setAssetDtos(list);

        companies.add(dto);

        when(companyRepository.findAll()).thenReturn(null);
        when(companyConverter.convertListToDto(any())).thenReturn(companies);

        final List<CompanyDto> returnList = companyServiceImpl.findCompaniesWithAssetsInAscendingOrder();

        assertEquals(companies, returnList);
        assertEquals(1, returnList.size());
        assertTrue(returnList.contains(dto));
        assertNotEquals(returnList.get(0).getAssetDtos().get(0), assetDto);

        verify(companyRepository, times(1)).findAll();
        verify(companyConverter, times(1)).convertListToDto(any());
    }
}
