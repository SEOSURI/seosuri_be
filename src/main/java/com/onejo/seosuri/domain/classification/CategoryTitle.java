package com.onejo.seosuri.domain.classification;

public enum CategoryTitle {
    AGE("나이_구하기"),
    COLOR_TAPE("이은_색테이프"),
    UNKNOWN_NUM("어떤수"),
    GEOMETRY_APP_CALC("도형_혼합계산_응용");

    private String categoryTitle;

    CategoryTitle(String categoryTitle){ this.categoryTitle = categoryTitle; }

    public String getCategoryName() {return this.categoryTitle;}
}
