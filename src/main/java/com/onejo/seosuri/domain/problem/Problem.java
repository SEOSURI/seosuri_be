package com.onejo.seosuri.domain.problem;

import com.onejo.seosuri.domain.BaseTimeEntity;
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
@Table(name = "problem")
public class Problem extends BaseTimeEntity {
    @Column(name="prob_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="test_paper_id")
    private TestPaper testPaper;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="prob_temp_id")
    private ProblemTemplate probTemp;

    @Column(name="prob_number")
    private Long probNum;

    @Column(name="prob_content")
    private String content;

    @Column(name="prob_level")
    private String level;

    @Column(name="prob_answer")
    private String answer;

    @Column(name="prob_explanation")
    private String explanation;

    @OneToMany(mappedBy = "prob")
    private List<ProbWordName> probWordNames = new ArrayList<>();

    @OneToMany(mappedBy = "prob")
    private List<ProbWordObj> probWordObjs = new ArrayList<>();
}
