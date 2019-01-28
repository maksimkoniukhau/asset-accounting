package com.maks.assetaccounting.vaadin.dataproviders;

import com.maks.assetaccounting.dto.CompanyDto;
import com.maks.assetaccounting.service.company.CompanyService;
import com.vaadin.flow.component.html.Label;
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
public class CompanyDataProvider extends FilterablePageableDataProvider<CompanyDto, String> {

    private final CompanyService companyService;
    private String reportFilter;
    private final Label footerLabel;

    @Autowired
    public CompanyDataProvider(final CompanyService companyService) {
        this.companyService = companyService;
        this.footerLabel = new Label();
        footerLabel.getStyle().set("font-weight", "bold");
    }

    @Override
    protected Page<CompanyDto> fetchFromBackEnd(final Query<CompanyDto, String> query, final Pageable pageable) {
        if (reportFilter != null) {
            switch (reportFilter) {
                case "Most Assets":
                    return companyService.findWithTheMostAssets(query.getFilter(), pageable);
                default:
                    return companyService.findAnyMatching(query.getFilter(), pageable);
            }
        } else {
            return companyService.findAnyMatching(query.getFilter(), pageable);
        }
    }

    @Override
    protected List<QuerySortOrder> getDefaultSortOrders() {
        return DEFAULT_SORT_ORDERS;
    }

    @Override
    protected int sizeInBackEnd(final Query<CompanyDto, String> query) {
        int count = (int) companyService.countAnyMatching(query.getFilter());
        footerLabel.setText("Total: " + count + " companies");
        return count;
    }

    @Override
    public Object getId(final CompanyDto item) {
        return item.getId();
    }
}
