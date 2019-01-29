package com.maks.assetaccounting.vaadin.dataproviders;

import com.maks.assetaccounting.dto.AbstractDto;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.data.provider.QuerySortOrder;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.vaadin.artur.spring.dataprovider.FilterablePageableDataProvider;

import java.util.List;

import static com.maks.assetaccounting.vaadin.utils.DataProvidersUtil.DEFAULT_SORT_ORDERS;

@Data
public abstract class AbstractDataProvider<T extends AbstractDto> extends FilterablePageableDataProvider<T, String> {

    private final Label footerLabel;
    private final Class<T> dtoType;

    public AbstractDataProvider(final Class<T> dtoType) {
        this.dtoType = dtoType;
        this.footerLabel = new Label();
        footerLabel.getStyle().set("font-weight", "bold");
    }

    @Override
    protected abstract Page<T> fetchFromBackEnd(final Query<T, String> query, final Pageable pageable);

    @Override
    protected abstract int sizeInBackEnd(final Query<T, String> query);

    @Override
    protected List<QuerySortOrder> getDefaultSortOrders() {
        return DEFAULT_SORT_ORDERS;
    }

    @Override
    public Object getId(final T item) {
        return item.getId();
    }

    protected void setFooterLabel(final Query<T, String> query, final int count) {
        if (query.getFilter().isPresent() && !query.getFilter().get().isEmpty()) {
            footerLabel.setText("Found: " + count + " " + getName());
        } else {
            footerLabel.setText("Total: " + count + " " + getName());
        }
    }

    private String getName() {
        String simpleName = dtoType.getSimpleName().replace("Dto", "").toLowerCase();
        if (simpleName.equals("company")) {
            return "companies";
        }
        return simpleName + "s";
    }
}
