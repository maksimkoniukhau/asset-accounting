package com.maks.assetaccounting.repository;

import com.maks.assetaccounting.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByUsername(final String username);

    Page<User> findBy(final Pageable page);

    Page<User> findByUsernameLikeIgnoreCase(final String name, final Pageable page);

    int countByUsernameLikeIgnoreCase(final String name);
}
