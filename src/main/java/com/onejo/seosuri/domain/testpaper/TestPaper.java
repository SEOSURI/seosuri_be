package com.onejo.seosuri.domain.testpaper;

import com.onejo.seosuri.domain.BaseTimeEntity;
import com.onejo.seosuri.domain.classification.Category;
import com.onejo.seosuri.domain.problem.Problem;
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
@Table(name = "test_paper")
public class TestPaper extends BaseTimeEntity {
    @Column(name="test_paper_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    private Category category;

    @OneToMany(mappedBy = "testPaper")
    private List<Problem> problems = new ArrayList<>();
}
