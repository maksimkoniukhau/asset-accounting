package com.maks.assetaccounting.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@ToString(callSuper = true, exclude = "company")
@EqualsAndHashCode(callSuper = true)
public class Asset extends AbstractEntity {
    @NonNull
    @NotBlank
    private String name;

    @NotNull
    private Date creationDate = new Date();

    @NonNull
    private Date transferDate;

    @NonNull
    private double cost;

    @ManyToOne
    @JoinColumn(name = "company_id")
    @NonNull
    private Company company;

    private int numberOfTransition;
}
