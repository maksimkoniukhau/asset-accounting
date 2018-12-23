package com.maks.assetaccounting.converter.user;

import com.maks.assetaccounting.converter.DtoEntityConverter;
import com.maks.assetaccounting.dto.user.UserToBackDto;
import com.maks.assetaccounting.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Service
public class UserToBackConverter implements DtoEntityConverter<UserToBackDto, User> {

    @Override
    public UserToBackDto convertToDto(final User user) {
        final UserToBackDto userToBackDto = new UserToBackDto();
        BeanUtils.copyProperties(user, userToBackDto);
        return userToBackDto;
    }

    @Override
    public User convertToEntity(final UserToBackDto userToBackDto) {
        final User user = new User();
        BeanUtils.copyProperties(userToBackDto, user);
        return user;
    }
}
