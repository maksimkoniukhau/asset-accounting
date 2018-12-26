package com.maks.assetaccounting.repository;

import com.maks.assetaccounting.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(final String username);
}
