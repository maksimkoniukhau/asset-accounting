package com.maks.assetaccounting.service;

import com.maks.assetaccounting.converter.CompanyConverter;
import com.maks.assetaccounting.dto.CompanyDto;
import com.maks.assetaccounting.entity.Company;
import com.maks.assetaccounting.repository.CompanyRepository;
import com.maks.assetaccounting.service.company.CompanyServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
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
}
