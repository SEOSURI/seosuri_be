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
@Table(name = "school_year")
public class SchoolYear {
    @Column(name="sch_year_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="sch_year_school")
    private String school;

    @Column(name="sch_grade")
    private int grade;

    @OneToMany(mappedBy = "schoolYear")
    private List<BigUnit> bigUnits = new ArrayList<>();

}
