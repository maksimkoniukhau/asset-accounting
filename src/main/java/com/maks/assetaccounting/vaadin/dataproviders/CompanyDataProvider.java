package com.maks.assetaccounting.vaadin.dataproviders;

import com.maks.assetaccounting.dto.CompanyDto;
import com.maks.assetaccounting.service.company.CompanyService;
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
public class CompanyDataProvider extends AbstractDataProvider<CompanyDto> {

    private final CompanyService companyService;
    private String reportFilter;

    public CompanyDataProvider(final CompanyService companyService) {
        super(CompanyDto.class);
        this.companyService = companyService;
    }

    @Override
    protected Page<CompanyDto> fetchFromBackEnd(final Query<CompanyDto, String> query, final Pageable pageable) {
        if (reportFilter != null) {
            switch (reportFilter) {
                case "Most Assets":
                    return companyService.findWithTheMostAssets(query.getFilter(), pageable, getAuthUserId());
                default:
                    return companyService.findAnyMatching(query.getFilter(), pageable, getAuthUserId());
            }
        } else {
            return companyService.findAnyMatching(query.getFilter(), pageable, getAuthUserId());
        }
    }

    @Override
    protected int sizeInBackEnd(final Query<CompanyDto, String> query) {
        final int count = (int) companyService.countAnyMatching(query.getFilter(), getAuthUserId());
        setFooterLabel(query, count);
        return count;
    }
}
