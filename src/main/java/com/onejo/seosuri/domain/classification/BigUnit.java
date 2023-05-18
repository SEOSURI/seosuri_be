package com.onejo.seosuri.domain.classification;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@Getter @Setter @Entity
@Table(name = "big_unit")
public class BigUnit {
    @Column(name="big_unit_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="sch_year_id")
    private SchoolYear schoolYear;

    @Column(name="big_unit_number")
    private Long number;

    @Column(name="big_unit_title")
    private String title;

    @Column(name="big_unit_semester")
    private Long semester;

    @OneToMany(mappedBy = "bigUnit")
    private List<MiddleUnit> middleUnits = new ArrayList<>();

}
