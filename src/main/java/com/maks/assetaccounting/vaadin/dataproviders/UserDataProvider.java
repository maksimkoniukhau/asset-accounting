package com.maks.assetaccounting.vaadin.dataproviders;

import com.maks.assetaccounting.dto.UserDto;
import com.maks.assetaccounting.service.user.UserService;
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
public class UserDataProvider extends FilterablePageableDataProvider<UserDto, String> {

    private final UserService userService;
    public final Label footerLabel;

    @Autowired
    public UserDataProvider(final UserService userService) {
        this.userService = userService;
        this.footerLabel = new Label();
        footerLabel.getStyle().set("font-weight", "bold");
    }

    @Override
    protected Page<UserDto> fetchFromBackEnd(final Query<UserDto, String> query, final Pageable pageable) {
        return userService.findAnyMatching(query.getFilter(), pageable);
    }

    @Override
    protected List<QuerySortOrder> getDefaultSortOrders() {
        return DEFAULT_SORT_ORDERS;
    }

    @Override
    protected int sizeInBackEnd(final Query<UserDto, String> query) {
        int count = (int) userService.countAnyMatching(query.getFilter());
        setFooterLabel(query, count);
        return count;
    }

    @Override
    public Object getId(final UserDto item) {
        return item.getId();
    }

    private void setFooterLabel(final Query<UserDto, String> query, final int count) {
        if (query.getFilter().isPresent() && !query.getFilter().get().isEmpty()) {
            footerLabel.setText("Found: " + count + " users");
        } else {
            footerLabel.setText("Total: " + count + " users");
        }
    }
}
