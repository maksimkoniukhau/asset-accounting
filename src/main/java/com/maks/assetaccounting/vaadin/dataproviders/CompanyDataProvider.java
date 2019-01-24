package com.maks.assetaccounting.vaadin.dataproviders;

import com.maks.assetaccounting.dto.CompanyDto;
import com.maks.assetaccounting.service.company.CompanyService;
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
public class CompanyDataProvider extends AbstractBackEndDataProvider<CompanyDto, String> {

    private final CompanyService companyService;
    private String reportFilter;

    @Autowired
    public CompanyDataProvider(final CompanyService companyService) {
        this.companyService = companyService;
    }

    @Override
    protected Stream<CompanyDto> fetchFromBackEnd(final Query<CompanyDto, String> query) {
        final List<Sort.Order> sortOrders = getListSortOrders(query);
        final PageRequest pageRequest = PageRequest.of(query.getOffset(), query.getLimit(), Sort.by(sortOrders));

        if (reportFilter != null) {
            switch (reportFilter) {
                case "Most Assets":
                    return companyService.findWithTheMostAssets(query.getFilter(), pageRequest)
                            .stream();
                default:
                    return companyService.findAnyMatching(query.getFilter(), pageRequest)
                            .stream();
            }
        } else {
            return companyService.findAnyMatching(query.getFilter(), pageRequest)
                    .stream();
        }
    }

    @Override
    protected int sizeInBackEnd(final Query<CompanyDto, String> query) {
        return (int) companyService.countAnyMatching(query.getFilter());
    }

    @Override
    public Object getId(final CompanyDto item) {
        return item.getId();
    }
}
