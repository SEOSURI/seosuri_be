package com.onejo.seosuri.domain.word;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@Getter @Setter @Entity
@Table(name = "word")
public class Word {
    @Column(name="word_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="word_content")
    private String content;

    @Column(name="word_num_start")
    private Long numStart;

    @Column(name="word_num_end")
    private Long numEnd;

    @Column(name="word_extra_condition")
    private String condition;

    @Column(name="word_type")
    private String type;

//    @Enumerated(EnumType.STRING)
//    @Column(name="word_type")
//    private WordType type;
}
