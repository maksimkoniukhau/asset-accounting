package com.maks.assetaccounting.converter;

import com.maks.assetaccounting.dto.UserDto;
import com.maks.assetaccounting.entity.Role;
import com.maks.assetaccounting.entity.User;
import com.maks.assetaccounting.repository.UserRepository;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserConverter implements DtoEntityConverter<UserDto, User> {

    private final UserRepository userRepository;

    @Autowired
    public UserConverter(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDto convertToDto(final User user) {
        if (user != null) {
            final UserDto userDto = new UserDto();
            BeanUtils.copyProperties(user, userDto, "password");
            userDto.setPassword("");
            return userDto;
        }
        return null;
    }

    @Override
    public User convertToEntity(final UserDto userDto) {
        if (userDto != null) {
            final User user = new User();
            BeanUtils.copyProperties(userDto, user, "password");
            userRepository.findById(userDto.getId()).ifPresent(byId -> user.setPassword(byId.getPassword()));
            return user;
        }
        return null;
    }

    public User convertToCreateEntity(final UserDto userDto) {
        if (userDto != null) {
            final User user = new User();
            if (userDto.getRoles() == null || userDto.getRoles().isEmpty()) {
                BeanUtils.copyProperties(userDto, user, "roles");
                user.setRoles(Collections.singleton(Role.ROLE_USER));
            } else {
                BeanUtils.copyProperties(userDto, user);
            }
            return user;
        }
        return null;
    }
}
