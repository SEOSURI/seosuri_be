package com.onejo.seosuri.domain.word;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@Getter @Setter @Entity
@Table(name = "word_name")
public class WordName {
    @Column(name="word_name_id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long wordNameId;

    @Column(name="name_content")
    private String content;
}
