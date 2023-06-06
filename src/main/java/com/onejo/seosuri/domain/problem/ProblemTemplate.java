package com.onejo.seosuri.domain.problem;

import com.onejo.seosuri.domain.BaseTimeEntity;
import com.onejo.seosuri.domain.classification.Category;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@DynamicUpdate
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

    @Column(name="prob_temp_content", columnDefinition = "TEXT")
    private String content;

    @Column(name="prob_temp_level")
    private String level;

    @Column(name="prob_temp_answer")
    private String answer;

    @Column(name="prob_temp_explanation", columnDefinition = "TEXT")
    private String explanation;

    @Column(name="prob_sentence_category_list")
    private String sentenceCategoryList;

    @Column(name="prob_expression_category_list")
    private String exprCategoryList;

    @Column(name="prob_var_sign_list")
    private String varSignList;

    @Column(name="prob_bool_use_year_var1_list")
    private String useYear1List;

    @Column(name="prob_bool_use_year_var2_list")
    private String useYear2List;

    @Column(name="prob_bool_use_multiplication_list")
    private String useMultList;

    @Column(name="prob_bool_use_add_or_minus_list")
    private String useAddMinusList;

    @OneToMany(mappedBy = "probTemp")
    private List<Problem> problems = new ArrayList<>();
}
