package com.maks.assetaccounting.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Company extends AbstractEntity {
    @NonNull
    @NotBlank
    @Column(name = "name", unique = true)
    private String name;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL)
    private List<Asset> assets;
}
