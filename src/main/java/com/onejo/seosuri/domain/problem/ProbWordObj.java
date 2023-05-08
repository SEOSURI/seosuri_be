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
@Table(name = "prob_word_obj")
public class ProbWordObj extends BaseTimeEntity{
    @Column(name="prob_word_obj_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="prob_id")
    private Problem prob;

    @Column(name="name1")
    private String name1;

    @Column(name="name2")
    private String name2;

    @Column(name="name3")
    private String name3;

    @Column(name="name4")
    private String name4;

}
