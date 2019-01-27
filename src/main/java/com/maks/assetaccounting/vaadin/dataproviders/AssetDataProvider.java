package com.maks.assetaccounting.vaadin.dataproviders;

import com.maks.assetaccounting.dto.AssetDto;
import com.maks.assetaccounting.service.asset.AssetService;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.vaadin.artur.spring.dataprovider.FilterablePageableDataProvider;

import java.util.List;

import static com.maks.assetaccounting.vaadin.utils.DataProvidersUtil.DEFAULT_SORT_ORDERS;

@SpringComponent
@UIScope
@Data
public class AssetDataProvider extends FilterablePageableDataProvider<AssetDto, String> {

    private final AssetService assetService;
    private String companyName;

    @Autowired
    public AssetDataProvider(final AssetService assetService) {
        this.assetService = assetService;
    }

    @Override
    protected Page<AssetDto> fetchFromBackEnd(final Query<AssetDto, String> query, final Pageable pageable) {
        if (companyName != null) {
            return assetService.getAllByCompanyName(query.getFilter(), companyName, pageable);
        } else {
            return assetService.findAnyMatching(query.getFilter(), pageable);
        }
    }

    @Override
    protected List<QuerySortOrder> getDefaultSortOrders() {
        return DEFAULT_SORT_ORDERS;
    }

    @Override
    protected int sizeInBackEnd(final Query<AssetDto, String> query) {
        if (companyName != null) {
            return assetService.countByCompanyName(query.getFilter(), companyName);
        } else {
            return (int) assetService.countAnyMatching(query.getFilter());
        }
    }

    @Override
    public Object getId(final AssetDto item) {
        return item.getId();
    }
}
