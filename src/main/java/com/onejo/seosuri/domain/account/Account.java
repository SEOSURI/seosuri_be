package com.onejo.seosuri.domain.account;

import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicInsert
@Getter @Setter @Entity
@Table(name = "TB_ACCOUNT") // indexes 기능도 고려해보기
public class Account {
        @Column(name="ACCOUNT_IDX")
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name="ACCOUNT_EMAIL", unique = true)
        private String email;

        @Column(name="ACCOUNT_NAME")
        private String name;

        @Column(name="ACCOUNT_PWD")
        private String password;

        @Enumerated(EnumType.STRING)
        @Column(name="ACCOUNT_ROLE")
        private AccountType accountType;

}
