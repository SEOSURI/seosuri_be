package com.onejo.seosuri.domain.word;

public enum WordType {
    이름("이름"),
    가족("가족"),
    인외("인외"),
    직업("직업");


    private String wordType;

    WordType(String wordType){ this.wordType = wordType; }

    public String getWordType() {return this.wordType;}
}
