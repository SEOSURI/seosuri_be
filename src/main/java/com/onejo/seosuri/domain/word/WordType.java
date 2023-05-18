package com.onejo.seosuri.domain.word;

public enum WordType {
    NAME("이름"),
    FAMILY("가족"),
    NO_HUMAN("인외"),
    JOB("직업");


    private String wordType;

    WordType(String categoryTitle){ this.wordType = categoryTitle; }

    public String getCategoryName() {return this.wordType;}
}
