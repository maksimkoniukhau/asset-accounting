package com.maks.assetaccounting.service.user;

import com.maks.assetaccounting.dto.UserDto;
import com.maks.assetaccounting.service.CrudService;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.List;

public interface UserService extends UserDetailsService, CrudService<UserDto, UserDto> {

    UserDto getByName(final String username);

    UserDto changeUserPassword(final UserDto userDto, final Long id, final String password);

    void deleteAll(final List<UserDto> userDtoList);
}
