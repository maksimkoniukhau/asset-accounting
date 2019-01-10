package com.maks.assetaccounting.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.ZonedDateTime;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@ToString(callSuper = true, exclude = "company")
@EqualsAndHashCode(callSuper = true)
public class Asset extends AbstractEntity {
    @NonNull
    @NotBlank
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "creation_date")
    private ZonedDateTime creationDate = ZonedDateTime.now();

    @NonNull
    @Column(name = "transfer_date")
    private ZonedDateTime transferDate;

    @NonNull
    @Column(name = "cost")
    private double cost;

    @ManyToOne
    @JoinColumn(name = "company_id")
    @NonNull
    private Company company;

    @Column(name = "number_of_transition")
    private int numberOfTransition;
}
