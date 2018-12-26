package com.maks.assetaccounting.dto;

import com.maks.assetaccounting.entity.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class UserDto extends AbstractDto {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private boolean active;

    private Set<Role> roles;
}
