package com.maks.assetaccounting.converter.user;

import com.maks.assetaccounting.converter.DtoEntityConverter;
import com.maks.assetaccounting.dto.user.UserToFrontDto;
import com.maks.assetaccounting.entity.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserToFrontConverter implements DtoEntityConverter<UserToFrontDto, User> {

    @Override
    public UserToFrontDto convertToDto(final User user) {
        final UserToFrontDto userToFrontDto = new UserToFrontDto();
        BeanUtils.copyProperties(user, userToFrontDto);
        return userToFrontDto;
    }

    @Override
    public User convertToEntity(final UserToFrontDto userToFrontDto) {
        throw new UnsupportedOperationException("We can't convert userToFrontDto to UserEntity");
    }

    @Override
    public List<User> convertListToEntity(final List<UserToFrontDto> userToFrontDtos) {
        throw new UnsupportedOperationException("We can't convert userToFrontDtos to UserEntities");
    }
}
