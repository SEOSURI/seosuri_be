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
@Table(name = "account") // indexes 기능도 고려해보기
public class Account {
        @Column(name="account_id")
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(name="account_email", unique = true)
        private String email;

        @Column(name="account_name")
        private String name;

        @Column(name="account_pwd")
        private String password;

        @Enumerated(EnumType.STRING)
        @Column(name="account_role")
        private AccountType accountType;

}
