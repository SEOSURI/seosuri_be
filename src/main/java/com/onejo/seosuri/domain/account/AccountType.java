package com.onejo.seosuri.domain.account;

public enum AccountType {
    ADMIN("관리자"),
    CUSTOMER("사용자");

    private String krName;
    AccountType(String krName) {
        this.krName = krName;
    }

    public String getKrName() {
        return this.krName;
    }
}
