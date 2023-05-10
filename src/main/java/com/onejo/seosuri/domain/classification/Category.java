package com.onejo.seosuri.domain.classification;

import com.onejo.seosuri.domain.problem.ProblemTemplate;
import com.onejo.seosuri.domain.testpaper.TestPaper;
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
@Table(name = "category")
public class Category {
    @Column(name="category_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="small_unit_id")
    private SmallUnit smallUnit;

    @Enumerated(EnumType.STRING)
    @Column(name="category_title")
    private CategoryTitle title;

    @OneToMany(mappedBy = "category")
    private List<TestPaper> testPapers = new ArrayList<>();

    @OneToMany(mappedBy = "category")
    private List<ProblemTemplate> problemTemplates = new ArrayList<>();
}
