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
@Table(name = "middle_unit")
public class MiddleUnit {
    @Column(name="mid_unit_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="big_unit_id")
    private BigUnit bigUnit;

    @Column(name="mid_unit_number")
    private Long number;

    @Column(name="mid_unit_title")
    private String title;

    @OneToMany(mappedBy = "midUnit")
    private List<Category> categories = new ArrayList<>();
}
