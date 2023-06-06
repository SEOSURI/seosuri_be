package com.onejo.seosuri.domain.classification;

public enum CategoryID {

    AGE(1L),
    COLOR_TAPE(2L),
    UNKNOWN_NUM(3L),
    GEOMETRY_APP_CALC(4L);

    private Long categoryID;

    CategoryID(Long categoryID){ this.categoryID = categoryID; }

    public Long getCategoryID() {return this.categoryID;}
}
