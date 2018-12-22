package com.maks.assetaccounting.service;

import com.maks.assetaccounting.model.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService extends UserDetailsService {

    User getByName(String username);
}
