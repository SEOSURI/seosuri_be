package com.onejo.seosuri.controller.dto.account;

import com.onejo.seosuri.domain.account.Account;
import com.onejo.seosuri.domain.account.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDto {
    @Schema(description = "", example = "")
    private String name;
    @Schema(description = "", example = "")
    private String email;
    @Schema(description = "", example = "")
    private String password;
    @Schema(description = "", example = "")
    private AccountType accountType;

    public Account toEntity(){
        return Account.builder()
                .name(name)
                .email(email)
                .password(password)
                .accountType(accountType)
                .build();
    }
}
