package com.maks.assetaccounting.dto.user;

import com.maks.assetaccounting.HasId;
import com.maks.assetaccounting.entity.Role;
import lombok.Data;

import java.util.Set;

@Data
public class UserToFrontDto implements HasId {

    private Long id;

    private String username;

    private boolean active;

    private Set<Role> roles;
}
