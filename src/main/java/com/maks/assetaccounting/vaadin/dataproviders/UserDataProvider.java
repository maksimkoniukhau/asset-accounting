package com.maks.assetaccounting.vaadin.dataproviders;

import com.maks.assetaccounting.dto.UserDto;
import com.maks.assetaccounting.service.user.UserService;
import com.vaadin.flow.data.provider.AbstractBackEndDataProvider;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.stream.Stream;

import static com.maks.assetaccounting.vaadin.utils.DataProvidersUtil.getListSortOrders;

@SpringComponent
@UIScope
public class UserDataProvider extends AbstractBackEndDataProvider<UserDto, String> {

    private final UserService userService;

    @Autowired
    public UserDataProvider(final UserService userService) {
        this.userService = userService;
    }

    @Override
    protected Stream<UserDto> fetchFromBackEnd(final Query<UserDto, String> query) {
        final List<Sort.Order> sortOrders = getListSortOrders(query);

        return userService.findAnyMatching(query.getFilter(),
                PageRequest.of(query.getOffset(), query.getLimit(), Sort.by(sortOrders)))
                .stream();
    }

    @Override
    protected int sizeInBackEnd(final Query<UserDto, String> query) {
        return (int) userService.countAnyMatching(query.getFilter());
    }

    @Override
    public Object getId(final UserDto item) {
        return item.getId();
    }
}
