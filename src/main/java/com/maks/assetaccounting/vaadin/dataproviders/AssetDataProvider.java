package com.maks.assetaccounting.vaadin.dataproviders;

import com.maks.assetaccounting.dto.AssetDto;
import com.maks.assetaccounting.service.asset.AssetService;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import static com.maks.assetaccounting.util.SecurityUtil.getAuthUserId;

@SpringComponent
@UIScope
@Data
public class AssetDataProvider extends AbstractDataProvider<AssetDto> {

    private final AssetService assetService;
    private String companyName;

    public AssetDataProvider(final AssetService assetService) {
        super(AssetDto.class);
        this.assetService = assetService;
    }

    @Override
    protected Page<AssetDto> fetchFromBackEnd(final Query<AssetDto, String> query, final Pageable pageable) {
        if (companyName != null) {
            return assetService.getAllByCompanyName(query.getFilter(), companyName, pageable, getAuthUserId());
        } else {
            return assetService.findAnyMatching(query.getFilter(), pageable, getAuthUserId());
        }
    }

    @Override
    protected int sizeInBackEnd(final Query<AssetDto, String> query) {
        if (companyName != null) {
            final int count = assetService.countByCompanyName(query.getFilter(), companyName, getAuthUserId());
            setFooterLabel(query, count);
            return count;
        } else {
            final int count = (int) assetService.countAnyMatching(query.getFilter(), getAuthUserId());
            setFooterLabel(query, count);
            return count;
        }
    }
}
