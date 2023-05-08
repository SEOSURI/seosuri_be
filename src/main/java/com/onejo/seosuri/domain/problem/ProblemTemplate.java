package com.onejo.seosuri.domain.problem;

import com.onejo.seosuri.domain.BaseTimeEntity;
import com.onejo.seosuri.domain.classification.Category;
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
@Table(name = "problem_template")
public class ProblemTemplate extends BaseTimeEntity {
    @Column(name="prob_temp_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="category_id")
    private Category category;

    @Column(name="prob_temp_content")
    private String content;

    @Column(name="prob_temp_level")
    private String level;

    @Column(name="prob_temp_answer")
    private String answer;

    @Column(name="prob_temp_explanation")
    private String explanation;

    @OneToMany(mappedBy = "probTemp")
    private List<Problem> problems = new ArrayList<>();
}
