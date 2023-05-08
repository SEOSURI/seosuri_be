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
@Table(name = "small_unit")
public class SmallUnit {
    @Column(name="small_unit_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="mid_unit_id")
    private MiddleUnit midUnit;

    @Column(name="small_unit_number")
    private Long number;

    @Column(name="small_unit_title")
    private String title;

    @OneToMany(mappedBy = "smallUnit")
    private List<Category> categories = new ArrayList<>();
}
