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
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @Column(name = "creation_date", nullable = false, columnDefinition = "TIMESTAMP default CURRENT_TIMESTAMP")
    private ZonedDateTime creationDate = ZonedDateTime.now();

    @NonNull
    @NotNull
    @Column(name = "transfer_date", nullable = false)
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
