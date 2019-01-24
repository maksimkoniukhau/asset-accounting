package com.maks.assetaccounting.vaadin.dataproviders;

import com.maks.assetaccounting.dto.AssetDto;
import com.maks.assetaccounting.service.asset.AssetService;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Stream;

import static com.maks.assetaccounting.vaadin.utils.DataProvidersUtil.getListSortOrders;

@SpringComponent
@UIScope
@Data
public class AssetDataProvider extends AbstractBackEndDataProvider<AssetDto, String> {

    private final AssetService assetService;
    private String companyName;

    @Autowired
    public AssetDataProvider(final AssetService assetService) {
        this.assetService = assetService;
    }

    @Override
    protected Stream<AssetDto> fetchFromBackEnd(final Query<AssetDto, String> query) {
        final List<Sort.Order> sortOrders = getListSortOrders(query);
        final PageRequest pageRequest = PageRequest.of(query.getOffset(), query.getLimit(), Sort.by(sortOrders));

        if (companyName != null) {
            return assetService.getAllByCompanyName(query.getFilter(), companyName, pageRequest)
                    .stream();
        } else {
            return assetService.findAnyMatching(query.getFilter(), pageRequest)
                    .stream();
        }
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
