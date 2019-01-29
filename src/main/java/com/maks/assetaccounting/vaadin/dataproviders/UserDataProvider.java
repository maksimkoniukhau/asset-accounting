package com.maks.assetaccounting.vaadin.dataproviders;

import com.maks.assetaccounting.dto.UserDto;
import com.maks.assetaccounting.service.user.UserService;
import com.vaadin.flow.data.provider.Query;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@SpringComponent
@UIScope
@Data
public class UserDataProvider extends AbstractDataProvider<UserDto> {

    private final UserService userService;

    @Autowired
    public UserDataProvider(final UserService userService) {
        super(UserDto.class);
        this.userService = userService;
    }

    @Override
    protected Page<UserDto> fetchFromBackEnd(final Query<UserDto, String> query, final Pageable pageable) {
        return userService.findAnyMatching(query.getFilter(), pageable);
    }

    @Override
    protected int sizeInBackEnd(final Query<UserDto, String> query) {
        final int count = (int) userService.countAnyMatching(query.getFilter());
        setFooterLabel(query, count);
        return count;
    }
}
