package com.onejo.seosuri.service.algorithm;

public class VarElementary5th {

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // template
    public String content_template = "";
    public String explanation_template = "";
    public String answer_template = "";

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 실제 문제
    public String real_content = "";
    public String real_explanation = "";
    public String real_answer = "";

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 템플릿 생성시만 이용

    // boolean 변수값 template 생성시 이용
    public boolean[] useYear1_ls;
    public boolean[] useYear2_ls;
    public boolean[] useMult_ls;
    public boolean[] useAddMinus_ls;


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // template 생성, 실제 문제 생성 시 이용

    // 상황문장 유형id
    public int[] sentence_category_id_ls;

    // sign
    public int[] var_sign_ls;  // input


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 실제 문제 생성 시에만 이용

    // min, max 범위
    public int[] var_min_value_ls; // input
    public int[] var_max_value_ls; // input
    public int[] name_var_min_value_ls;    // input
    public int[] name_var_max_value_ls;    // input

    // 이름
    public String[] name_ls;

    // 숫자값
    public int[] name_var_ls;   // 결과
    public int[] var_ls;   // 결과

}
