package com.maks.assetaccounting.service;

import com.maks.assetaccounting.dto.user.UserToBackDto;
import com.maks.assetaccounting.dto.user.UserToFrontDto;
import com.maks.assetaccounting.entity.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService, CrudService<UserToFrontDto, UserToBackDto> {

    User getByName(String username);
}
