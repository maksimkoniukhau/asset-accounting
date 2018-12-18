package com.maks.assetaccounting.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@Data
@NoArgsConstructor
@RequiredArgsConstructor
@ToString(exclude = "company")
@EqualsAndHashCode(of = "id")
public class Asset {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NonNull
    private String name;

    @NotNull
    private Date creationDate = new Date();

    @NonNull
    private Date transferDate;

    @NonNull
    private double cost;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;
}
