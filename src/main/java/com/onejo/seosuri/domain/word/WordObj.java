package com.onejo.seosuri.domain.word;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@Getter @Setter @Entity
@Table(name = "word_obj")
public class WordObj {
    @Column(name="word_obj_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wordObjId;

    @Column(name="obj_content")
    private String content;
}
