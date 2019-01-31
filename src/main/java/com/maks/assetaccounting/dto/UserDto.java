package com.maks.assetaccounting.dto;

import com.maks.assetaccounting.entity.Role;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Data
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
@ToString(callSuper = true)
public class UserDto extends AbstractDto {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    private String firstName;

    private String lastName;

    @NotBlank
    @Email
    private String email;

    @NotNull
    private boolean active = true;

    private Set<Role> roles;

    private List<CompanyDto> companyDtos;

    private List<AssetDto> assetDtos;
}
