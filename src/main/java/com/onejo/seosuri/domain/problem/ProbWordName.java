package com.onejo.seosuri.domain.problem;

import com.onejo.seosuri.domain.BaseTimeEntity;
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
@Table(name = "prob_word_name")
public class ProbWordName extends BaseTimeEntity{
    @Column(name="prob_word_name_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="prob_id")
    private Problem prob;

    @Column(name="obj1")
    private String obj1;

    @Column(name="obj2")
    private String obj2;

    @Column(name="obj3")
    private String obj3;

    @Column(name="obj4")
    private String obj4;

    @Column(name="obj5")
    private String obj5;
}
