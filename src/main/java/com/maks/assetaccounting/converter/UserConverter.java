package com.maks.assetaccounting.converter;

import com.maks.assetaccounting.dto.UserDto;
import com.maks.assetaccounting.entity.Role;
import com.maks.assetaccounting.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserConverter implements DtoEntityConverter<UserDto, User> {

    @Override
    public UserDto convertToDto(final User user) {
        final UserDto userDto = new UserDto();
        BeanUtils.copyProperties(user, userDto, "password");
        userDto.setPassword("");
        return userDto;
    }

    @Override
    public User convertToEntity(final UserDto userDto) {
        final User user = new User();
        BeanUtils.copyProperties(userDto, user);
        return user;
    }

    public User convertToSaveEntity(final UserDto userDto) {
        final User user = new User();
        BeanUtils.copyProperties(userDto, user, "active", "roles");
        user.setActive(true);
        user.setRoles(Collections.singleton(Role.USER));
        return user;
    }
}
