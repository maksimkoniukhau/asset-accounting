package com.maks.assetaccounting.service;

import com.maks.assetaccounting.converter.AssetConverter;
import com.maks.assetaccounting.dto.AssetDto;
import com.maks.assetaccounting.entity.Company;
import com.maks.assetaccounting.repository.AssetRepository;
import com.maks.assetaccounting.repository.CompanyRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AssetServiceImplTest {
    @Mock
    private AssetRepository assetRepository;
    @Mock
    private CompanyRepository companyRepository;
    @Mock
    private AssetConverter assetConverter;
    @Mock
    private Company company;
    @Mock
    private List<AssetDto> dtoList;
    @InjectMocks
    private AssetServiceImpl assetServiceImpl;

    @Test
    public void testCreate() {
        assetServiceImpl.create(new AssetDto());

        verify(assetRepository, times(1)).save(any());
        verify(assetConverter, times(1)).convertToEntityForCreate(any());
        verify(assetConverter, times(1)).convertToDto(any());
    }

    @Test
    public void testGet() {
        assetServiceImpl.get(anyLong());

        verify(assetRepository, times(1)).findById(anyLong());
        verify(assetConverter, times(1)).convertToDto(any());
    }

    //Mockito doesn't mock static methods
    @Test
    public void testUpdate() {
        final AssetDto dto = new AssetDto();
        dto.setId(1L);

        assetServiceImpl.update(dto, 1L);

        verify(assetRepository, times(1)).save(any());
        verify(assetConverter, times(1)).convertToEntity(any());
        verify(assetConverter, times(1)).convertToDto(any());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testUpdateWithIllegalArgument() {
        final AssetDto dto = new AssetDto();
        dto.setId(1L);
        assetServiceImpl.update(dto, 2L);
    }

    @Test
    public void testDelete() {
        assetServiceImpl.delete(anyLong());

        verify(assetRepository, times(1)).findById(anyLong());
        verify(assetConverter, times(1)).convertToDto(any());
        verify(assetRepository, times(1)).deleteById(anyLong());
    }

    @Test
    public void testGetAll() {
        assetServiceImpl.getAll();

        verify(assetRepository, times(1)).findAll();
        verify(assetConverter, times(1)).convertListToDto(anyList());
    }

    @Test
    public void testGeneration() {
        when(companyRepository.findById(anyLong())).thenReturn(Optional.of(company));

        assetServiceImpl.generation(anyLong());

        verify(companyRepository, times(1)).findById(anyLong());
        verify(assetRepository, times(1)).saveAll(anyList());
        verify(assetConverter, times(1)).convertListToDto(anyList());
    }

    @Test
    public void testTransition() {
        assetServiceImpl.transition(dtoList, anyLong());

        verify(companyRepository, times(1)).findById(anyLong());
        verify(assetConverter, times(1)).convertListToEntity(anyList());
        verify(assetRepository, times(1)).saveAll(anyList());
        verify(assetConverter, times(1)).convertListToDto(anyList());
    }

    @Test
    public void testGetMarketable() {
        assetServiceImpl.getMarketable();

        verify(assetRepository, times(1)).findFirst50ByOrderByNumberOfTransitionDesc();
        verify(assetConverter, times(1)).convertListToDto(anyList());
    }

    @Test
    public void testGetExpensiveAndMarketable() {
        assetServiceImpl.getExpensiveAndMarketable();

        verify(assetRepository, times(1)).findFirst50ByOrderByCostDescNumberOfTransitionDesc();
        verify(assetConverter, times(1)).convertListToDto(anyList());
    }
}
