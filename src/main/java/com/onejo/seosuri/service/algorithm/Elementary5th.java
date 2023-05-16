// 이곳에 문제 생성 알고리즘을 넣어두고, ProblemSerice에서 필요한 method를 호출하면 될 듯 싶습니다..

package com.onejo.seosuri.service.algorithm;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/*
템플릿 생성 완료

남은 일
0. 난이도 기준 마련
    - 템플릿 난이도
        - 상황 문장 갯수
        - 상황 문장 난이도
            useYear
            useMult
            useAddMinus
        - 상황 문장 셔플 여부
    - 숫자 난이도
        - 자리수 : 한자리, 두자리, 세자리
1. 랜덤 숫자 뽑기 - 무한루프 문제
2. 숫자에 맞는 단어 뽑기
3. 템플릿 -> 실제 문장으로 변경
 */

public class Elementary5th {
    Random random;
    static final int PLUS_SIGN = 0;
    static final int MINUS_SIGN = 1;

    // 상황 문장 id
    static final int AGE_CATEGORY_ID_YX = 0;
    static final int AGE_CATEGORY_ID_SUM_DIFFERENCE = 1;

    // 변수명 string 규칙
    static final String NAME_VAR_STR = "name_var";    // age 변수 : {age0}, {age1}, {age2}, ...
    static final String NAME_STR = "name";  // name 변수 : {name0}, {name1}, {name2}, ...
    static final String VAR_STR = "var";    // 상수 변수 : {var0}, {var1}, {var2}, ...
    static final String YEAR_STR = "year";  // year 변수
    static final String VAR_START = "{";
    static final String VAR_END = "}";
    static final String EXPRESSION_START = "[";
    static final String EXPRESSION_END = "]";
    static final String BRACKET_START = "(";    // expression 내부에서 사용하는 괄호(시작)
    static final String BRACKET_END = ")";      // expression 내부에서 사용하는 괄호(끝)
    static final String PLUS_SYM = "+";
    static final String MINUS_SYM = "-";
    static final String MULT_SYM = "*";
    static final String DIVIDE_SYM = "/";
    static final String EQUAL_SYM = "=";
    static final String BLANK_SYM = " ";
    static final String EQUAL_BLANK_SYM = "\n= ";
    static final String PLUS_BLANK_STR = " + ";
    static final String MINUS_BLANK_STR = " - ";
    static final String MULT_BLANK_STR = " * ";
    static final String DIVIDE_BLANK_STR = " / ";


    // string tokens
    String blank_token = " ";
    String ui_token = "의";
    String gwa_token = "과";
    String wa_token = "와";
    String neun_token = "는";
    String eun_token = "은";
    String eul_token = "을";
    String reul_token = "를";
    String eseo_token = "에서";

    String bae_token = "배 한 것";
    String than_token = "보다";
    String same_token = "같습니다.";
    String more_amount_token = "많습니다.";
    String less_amount_token = "적습니다.";
    String sum_token = "합한";
    String difference_token = "뺀";
    String value_token = "값";

    String age_category_token = "나이";
    String age_unit_token = "살";
    String year_unit_token = "년";
    String time_after_token = "후";
    String time_before_token = "전";
    //String time_past_token = "년 후";

    private static final int SENTENCE_CATEGORY_NUM = 2;  // 상황 문장 유형 갯수

    private String content_template = "";
    private String explanation_template = "";
    private String answer_template = "";
    private int[] sentence_category_id_ls;
    private int[] var_sign_ls;  // input
    private int[] var_min_value_ls; // input
    private int[] var_max_value_ls; // input
    private int[] name_var_min_value_ls;    // input
    private int[] name_var_max_value_ls;    // input
    private String[] name_ls;
    private int[] name_var_ls;   // 결과
    private int[] var_ls;   // 결과
    boolean[] useYear_ls;
    boolean[] useMult_ls;
    boolean[] useAddMinus_ls;

    // 나이 구하기 문제 알고리즘
    public void ageProblem(){
        random = new Random(); //랜덤 객체 생성(디폴트 시드값 : 현재시간)
        random.setSeed(System.currentTimeMillis()); //시드값 설정을 따로 할수도 있음

        int prob_sentence_num = 5;  // 상황 문장 갯수 - 문제 난이도에 따라 값 달라짐
        int var_num_per_sentence = 4;   // var1, var2, year1, year2
        int name_var_num = prob_sentence_num + 1;
        int var_num = prob_sentence_num * var_num_per_sentence;
        sentence_category_id_ls = new int[prob_sentence_num];    // 각 상황문장이 어떤 유형의 문장인지를 저장한 배열
        for(int i = 0; i < prob_sentence_num; i++){
            sentence_category_id_ls[i] = random.nextInt(SENTENCE_CATEGORY_NUM);
        }
        int answer_inx = random.nextInt(name_var_num);  // 구하는 나이의 인덱스
        int condition_inx = (random.nextInt(name_var_num - 1) + answer_inx) % name_var_num;   // 조건으로 값이 주어진 나이의 인덱스, answer_inx와 다른 인덱스가 되도록 설정

        useYear_ls = new boolean[prob_sentence_num];
        useMult_ls = new boolean[prob_sentence_num];
        useAddMinus_ls = new boolean[prob_sentence_num];
        var_sign_ls = new int[var_num];

        // 랜덤 값 부여
        for(int i = 0; i < prob_sentence_num; i++){
            int var_index = i * var_num_per_sentence;
            useYear_ls[i] =random.nextBoolean();
            useMult_ls[i] = random.nextBoolean();
            useAddMinus_ls[i] = random.nextBoolean();
        }
        for(int i = 0; i < var_num; i++){
            var_sign_ls[i] = random.nextInt(2);
        }

        // ageProblemTemplate() 실행 결과 class 변수 setting 됨
        ageProblemTemplate(prob_sentence_num, var_num_per_sentence, answer_inx, condition_inx);

        printTemplate();


        // 상수 var, name_var 설정
        name_var_min_value_ls = new int[name_var_num];
        name_var_max_value_ls = new int[name_var_num];
        name_ls = new String[name_var_num];
        for(int i = 0; i < name_var_num; i++){
            name_var_min_value_ls[i] = 10;
            name_var_max_value_ls[i] = 100;
            name_ls[i] = i+"사람"+i;
        }
        setVarMinMaxLs(prob_sentence_num, var_num_per_sentence);
        //System.out.println("var_min_ls = " + Arrays.toString(var_min_value_ls));
        //System.out.println("var_max_ls = " + Arrays.toString(var_max_value_ls));

        getRandomAgeValue(name_var_num, var_num_per_sentence);

        // template -> problem
        String[] real_prob = templateToProblem(name_ls, name_var_ls, var_ls,
                content_template, explanation_template, answer_template);

        System.out.println("\n\n-----------------------------------------------------");
        System.out.println(real_prob[0]);   // real_content
        System.out.println("\n\n-----------------------------------------------------");
        System.out.println(real_prob[1]);   // real_explanation
        System.out.println("\n\n-----------------------------------------------------");
        System.out.println(real_prob[2]);   // real_answer
        // return new String[] {real_content, real_explanation, real_answer};

    }

    public void ageProblemTemplate(int prob_sentence_num, int var_num_per_sentence,
                           int answer_inx, int condition_inx) {
        ////////////////////////////////////////////////////////////////////////////////////////////////
        // 문장 생성

        // condition 문장 생성
        String condition = "이때, {"+NAME_STR+condition_inx+"}의 나이는 {"+ NAME_VAR_STR +condition_inx+"}살 입니다.";

        // question 문장 생성
        String question = "그렇다면 {"+NAME_STR+answer_inx+"}의 나이는 몇 살입니까?";

        // answer 문장 생성
        String answer = "{"+ NAME_VAR_STR +answer_inx+"}살";

        // 상황 문장 생성 : {content, explanation}
        String[][] sentence_ls = new String[prob_sentence_num][2];
        int cond_inx_for_sentence = 1;  // age1, age2 중 age2가 given
        if(prob_sentence_num==1)    cond_inx_for_sentence = condition_inx;
        for(int i = 0; i < prob_sentence_num; i++){
            int var1_index = i * var_num_per_sentence;
            // create_age_sentence(유형id, 값 참조 시작하는 인덱스, answer_index, condition_index) ex) index = 2 -> name2, name3, age2, age3, var2, var3 사용
            sentence_ls[i] = create_age_sentence(sentence_category_id_ls[i], i , var_num_per_sentence, cond_inx_for_sentence,
                    useYear_ls[i], useMult_ls[i], useAddMinus_ls[i], var_sign_ls[var1_index+1], var_sign_ls[var1_index+2], var_sign_ls[var1_index+3]);  // sentence_ls[i] = {content, explanation}
        }


        /////////////////////////////////////////////////////////////////////////////////////////////////////
        // 상황 문장 연결

        // content : sentences + condition + question
        String content = "";
        for(int i = 0; i < sentence_ls.length; i++){ // 상황 문장 content - 순서 섞으면 난이도 올라감
            content += sentence_ls[i][0] + "\n";
        }
        content += condition + "\n" + question;

        // explanation : conditon_inx -> condition_inx + 1 -> ... -> 끝 index -> 1 -> 2 -> ... -> condition_inx - 1 순서로 연결
        String explanation = "";
        int start_index = (condition_inx + prob_sentence_num - 1) % prob_sentence_num;
        for(int i = start_index; i >= 0; i--){    // condition_inx - 1   ~   0
            explanation += sentence_ls[i][1] + "\n\n";   // 상황 문장 explanation
        }
        for(int i = sentence_ls.length - 1; i >= condition_inx; i--){                     // 끝   ~   condition_inx
            explanation += sentence_ls[i][1] + "\n\n";   // 상황 문장 exlanation
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////
        // 결과 : {문제, explanation, answer, prob_sentence_category_list, var_sign_ls}
        this.content_template = content;
        this.explanation_template = explanation;
        this.answer_template = answer;
        this.sentence_category_id_ls = sentence_category_id_ls;
        this.var_sign_ls = var_sign_ls;
        //String[] result_ls = new String[] {content, explanation, answer, Arrays.toString(sentence_category_id_ls), Arrays.toString(var_sign_ls)};

        /*
        int age_ls_length, int var_ls_length, int num_var_per_sentence,
                                    int year_min_value, int year_max_value,
                                    int[] sentence_category_ls, int[] var_sign_ls,
                                    int[] age_min_value_ls, int[] age_max_value_ls, int[] var_min_value_ls, int[] var_max_value_ls
         */
    }

    // 이은 색테이프 문제 알고리즘
    public void colorTapeProblem() {}

    // 어떤 수 문제 알고리즘
    public void anyNumberProblem() {

    }

    // 도형에서의 혼합 계산 응용 알고리즘
    public void geometryCalculation() {}



    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // 상황 문장 생성
    //

    // 나이 문제 상황 문장 생성
    // create_age_sentence(유형id, 값 참조 시작하는 인덱스) ex) index = 2 -> name2, name3, age2, age3, var2, var3 사용
    private String[] create_age_sentence(int category_id, int index, int var_num_per_sentence, int cond_inx,
                                         boolean useYear, boolean useMult, boolean useAddMinus,
                                         int var_sign, int year1_sign, int year2_sign){
        String name_category_token = age_category_token; // "의 나이"
        String name_unit_token = age_unit_token;
        String var34_unit_token = year_unit_token;
        String after_str_token = time_after_token;
        String before_str_token = time_before_token;

        if(category_id == AGE_CATEGORY_ID_YX){
            return create_sentence_yx(index, var_num_per_sentence, cond_inx,
                    useYear, useMult, useAddMinus,
                    var_sign, year1_sign, year2_sign,
                    name_category_token, name_unit_token, var34_unit_token, after_str_token, before_str_token);               // {content, explanation, sign}
        } else if (category_id == AGE_CATEGORY_ID_SUM_DIFFERENCE){
            return create_sentence_sum_diff(index, var_num_per_sentence, cond_inx,
                    var_sign,
                    name_category_token, name_unit_token);   // {content, explanation, sign}
        } else{
            return null;
        }
    }

    // (name_var1 +- var3) = (name_var2 +- var4) * var1 +- var2
    public String[] create_sentence_yx(int ls_index, int var_num_per_sentence, int cond_inx,
                                       boolean useYear, boolean useMult, boolean useAddMinus,
                                       int var2_sign, int year1_sign, int year2_sign,
                                       String name_category_token, String name_unit_token, String var34_unit_token, String after_str_token, String before_str_token){
        ArrayList<String> content_ls = new ArrayList<>(1);
        ArrayList<String> explanation_ls = new ArrayList<>(10);

        // index
        int index1 = ls_index;
        int index2 = index1 + 1;
        int var_index1 = ls_index * var_num_per_sentence;
        int var_index2 = var_index1 + 1;
        int year1_index = var_index1 + 2;
        int year2_index = var_index1 + 3;

        name_category_token = age_category_token; // "의 나이"
        name_unit_token = age_unit_token;
        var34_unit_token = year_unit_token;
        after_str_token = time_after_token;
        before_str_token = time_before_token;

        /*
        // string token used
        String blank_token = " ";
        String ui_token = "의";
        String gwa_token = "과";
        String wa_token = "와";
        String neun_token = "는";
        String eun_token = "은";
        String age_unit_token = "살";
        String bae_token = "배 한 것";
        String than_token = "보다";
        String same_token = "같습니다.";
        String more_amount_token = "많습니다.";
        String less_amount_token = "적습니다.";

        String name_to_age_token = "의 나이";
        String time_past_token = "년 후";
         */

        // token 조합
        String name1_token = VAR_START + NAME_STR + index1 + VAR_END;
        String name2_token = VAR_START + NAME_STR + index2 + VAR_END;
        String name1_with_category_token = name1_token + ui_token + blank_token +  name_category_token;
        String name2_with_category_token = name2_token + ui_token + blank_token +  name_category_token;
        String name_var1_token = VAR_START + NAME_VAR_STR + index1 + VAR_END;
        String name_var2_token = VAR_START + NAME_VAR_STR + index2 + VAR_END;
        String var1_token = VAR_START + VAR_STR + var_index1 + VAR_END;
        String var2_token = VAR_START + VAR_STR + var_index2 + VAR_END;
        String var3_token = VAR_START + VAR_STR + year1_index + VAR_END;    // year1
        String var4_token = VAR_START + VAR_STR + year2_index + VAR_END;   // year2
        //String var3_token = VAR_START + YEAR_STR + "1" + VAR_END;
        //String var4_token = VAR_START + YEAR_STR + "2" + VAR_END;

        String name1_add_str_token = "", name2_add_str_token = "";
        if(useYear){
            if(year1_sign == PLUS_SIGN){
                name1_add_str_token = var3_token + var34_unit_token + blank_token + after_str_token + blank_token; // "{year1}년 후 "
            } else if(year1_sign == MINUS_SIGN){
                name1_add_str_token = var3_token + var34_unit_token + blank_token + before_str_token + blank_token; // "{year1}년 전 "
            } else{
                System.out.println("ERROR:: invalid year sign");
            }
            if(year2_sign == PLUS_SIGN){
                name2_add_str_token = var4_token + var34_unit_token + blank_token + after_str_token + blank_token; // "{year2}년 후 "
            } else if(year2_sign == MINUS_SIGN){
                name2_add_str_token = var4_token + var34_unit_token + blank_token + before_str_token + blank_token; // "{year2}년 전 "
            } else{
                System.out.println("ERROR:: invalid year sign");
            }
        }


        // sign
        String var2_sign_token = "", var2_sign_blank_token = "";
        String var2_inv_sign_token = "", var2_inv_sign_blank_token = "";
        if(var2_sign == PLUS_SIGN){
            var2_sign_token = PLUS_SYM;  var2_sign_blank_token = PLUS_BLANK_STR;
            var2_inv_sign_token = MINUS_SYM; var2_inv_sign_blank_token = MINUS_BLANK_STR;
        } else if(var2_sign == MINUS_SIGN){
            var2_sign_token = MINUS_SYM; var2_sign_blank_token = MINUS_BLANK_STR;
            var2_inv_sign_token = PLUS_SYM;  var2_inv_sign_blank_token = PLUS_BLANK_STR;
        } else{
            System.out.println("ERROR:: SIGN VALUE ERROR");
            return null;
        }

        String var3_sign_token = PLUS_SYM, var3_inv_sign_token = MINUS_SYM;
        String var3_sign_blank_token = PLUS_BLANK_STR, var3_inv_sign_blank_token = MINUS_BLANK_STR;
        if(year1_sign == MINUS_SIGN) {
            var3_sign_token = MINUS_SYM;    var3_inv_sign_token = PLUS_SYM;
            var3_sign_blank_token = MINUS_BLANK_STR;    var3_inv_sign_blank_token = PLUS_BLANK_STR;
        }

        String var4_sign_token = PLUS_SYM, var4_inv_sign_token = MINUS_SYM;
        String var4_sign_blank_token = PLUS_BLANK_STR, var4_inv_sign_blank_token = MINUS_BLANK_STR;
        if(year2_sign == MINUS_SIGN) {
            var4_sign_token = MINUS_SYM;    var4_inv_sign_token = PLUS_SYM;
            var4_sign_blank_token = MINUS_BLANK_STR;    var4_inv_sign_blank_token = PLUS_BLANK_STR;
        }


        // content용 토큰
        String c_mult_token = ui_token + blank_token + var1_token + bae_token;  //"의 {var1}배 한 것"
        String c_mult_end_token = gwa_token + blank_token + same_token; //"과 같습니다.";
        String c_mult_end_wa_token = wa_token + blank_token + same_token; //"와 같습니다.";
        String c_pm_token = than_token + blank_token + var2_token + name_unit_token + blank_token;   //"보다 " + var2_token + "살 ";

        // content
        String content="";
        content = name1_add_str_token + name1_with_category_token + neun_token + blank_token + name2_add_str_token + name2_with_category_token;   // {name1}의 나이는 {name2}의 나이
        if(useMult){
            if(useAddMinus == false){   // 의 {var1}배 한 것과 같습니다.
                content += c_mult_token + c_mult_end_token;
            } else if(var2_sign == PLUS_SIGN){    // 의 {var1}배한 것보다 {var2}살 많습니다.
                content += c_mult_token + c_pm_token + more_amount_token;
            } else if(var2_sign == MINUS_SIGN){   // 의 {var1}배한 것보다 {var2}살 적습니다.
                content += c_mult_token + c_pm_token + less_amount_token;
            } else{
                content = "var2_sign value error\n";
            }
        } else{
            if(useAddMinus == false){   // 와 같습니다.
                content += c_mult_end_wa_token;
            } else if(var2_sign == PLUS_SIGN){   // 보다 {var2}살 많습니다.
                content += c_pm_token + more_amount_token;
            } else if(var2_sign == MINUS_SIGN){   // 보다 {var2}살 적습니다.
                content += c_pm_token + less_amount_token;
            } else{
                content = "var2_sign value error\n";
            }
        }
        content_ls.add(content);

        content = content_ls.get(0);
        for(int i = 1; i < content_ls.size(); i++){
            content += "\n" + content_ls.get(i);
        }


        // explanation token
        /*
        String ex_age_after_year_token = name1_add_str_token +"{"+NAME_STR+"%d}의 나이 = {"+AGE_STR+"%d} + {"+YEAR_STR+"} = [{"
                +AGE_STR+"%d}+{"+YEAR_STR+"}]";
        */
        String ex_expression_str_start_token = "\"" + content + "\"라는 문장을 식으로 바꾸면";
        String ex_expression_token = name1_add_str_token + name1_with_category_token
                + BLANK_SYM + EQUAL_SYM + BLANK_SYM + name2_add_str_token + name2_with_category_token;
        if(useMult) {
            ex_expression_token += MULT_BLANK_STR + var1_token;
        }
        if(useAddMinus) {
            ex_expression_token += var2_sign_blank_token + var2_token;
        }
        String ex_expression_str_end_token = "가 됩니다.";

        String ex_explain_start_token = "이제 이 식을 다음과 같은 순서로 풀 수 있습니다.";

        String ex_name_var1_after_year_token = name1_add_str_token + name1_with_category_token
                + EQUAL_BLANK_SYM + name_var1_token + var3_sign_blank_token + var3_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + name_var1_token + var3_sign_token + var3_token + EXPRESSION_END;
        String ex_name_var2_after_year_token = name2_add_str_token + name2_with_category_token
                + EQUAL_BLANK_SYM + name_var2_token + var4_sign_blank_token + var4_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + name_var2_token + var4_sign_token + var4_token + EXPRESSION_END;
        String ex_name_var1_after_year_same_age_token = EQUAL_BLANK_SYM + name2_add_str_token + name2_with_category_token;
        String ex_name_var2_after_year_same_age_token = EQUAL_BLANK_SYM + name1_add_str_token + name1_with_category_token;

        String ex_after_year_to_name_var1_token = name1_with_category_token
                + EQUAL_BLANK_SYM + name1_add_str_token + name1_with_category_token + var3_inv_sign_blank_token + var3_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + BRACKET_START + name_var2_token + var4_inv_sign_token + var4_token + BRACKET_END + MULT_SYM + var1_token + var2_sign_token + var2_token + EXPRESSION_END + var3_inv_sign_blank_token + var3_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + BRACKET_START + name_var2_token + var4_inv_sign_token + var4_token + BRACKET_END + MULT_SYM + var1_token + var2_sign_token + var2_token + var3_inv_sign_token + var3_token + EXPRESSION_END;
        String ex_after_year_to_name_var2_with_plus_sign_token = name2_with_category_token
                + EQUAL_BLANK_SYM + name2_add_str_token + name2_with_category_token + var4_inv_sign_blank_token + var4_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + BRACKET_START + name_var1_token + var3_inv_sign_token + var3_token + MINUS_SYM + var2_token + BRACKET_END + DIVIDE_SYM + var1_token + EXPRESSION_END + var4_inv_sign_blank_token + var4_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + BRACKET_START + name_var1_token + var3_inv_sign_token + var3_token + MINUS_SYM + var2_token + BRACKET_END + DIVIDE_SYM + var1_token + var4_inv_sign_token + var4_token + EXPRESSION_END;
        String ex_after_year_to_name_var2_with_minus_sign_token = name2_with_category_token
                + EQUAL_BLANK_SYM + name2_add_str_token + name2_with_category_token + var4_inv_sign_blank_token + var4_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + BRACKET_START + name_var1_token + var3_inv_sign_token + var3_token + PLUS_SYM + var2_token + BRACKET_END + DIVIDE_SYM + var1_token + EXPRESSION_END + var4_inv_sign_blank_token + var4_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + BRACKET_START + name_var1_token + var3_inv_sign_token + var3_token + PLUS_SYM + var2_token + BRACKET_END + DIVIDE_SYM + var1_token + var4_inv_sign_token + var4_token + EXPRESSION_END;

        // (%s%s의 나이) = (%s%s의 나이) * %d %s %d = %d * %d %s %d = %d
        // y년후 name1의 나이 = y년후 name2의나이 * var1 +- var2 = [age2+year] * var1 +- var2 = age1
        String ex_cond2_compute_with_plus_sign_token = name1_add_str_token + name1_with_category_token
                + EQUAL_BLANK_SYM + name2_add_str_token + name2_with_category_token + MULT_BLANK_STR + var1_token + var2_sign_blank_token + var2_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + name_var2_token + var4_sign_token + var4_token + EXPRESSION_END + MULT_BLANK_STR + var1_token + var2_sign_blank_token + var2_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + BRACKET_START + name_var2_token + var4_sign_token + var4_token + BRACKET_END + MULT_SYM + var1_token + var2_sign_token + var2_token + EXPRESSION_END;
        String ex_cond2_compute_with_minus_sign_token = name1_add_str_token + name1_with_category_token
                + EQUAL_BLANK_SYM + name2_add_str_token + name2_with_category_token + MULT_BLANK_STR + var1_token + var2_inv_sign_blank_token + var2_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + name_var2_token + var4_sign_token + var4_token + EXPRESSION_END + MULT_BLANK_STR + var1_token + var2_inv_sign_blank_token + var2_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + BRACKET_START + name_var2_token + var4_sign_token + var4_token + BRACKET_END + MULT_SYM + var1_token + var2_sign_token + var2_token + EXPRESSION_END;

        // if var2 != 0
        // (year년 후 name1의 나이) -+ var2 = age1 + year -+ var2
        String ex_cond1_compute_b4_divide_with_zero_var2_token = name2_add_str_token + name2_with_category_token + EQUAL_BLANK_SYM;
        String ex_cond1_compute_b4_divide_with_plus_sign_token = name1_add_str_token + name1_with_category_token + MINUS_BLANK_STR + var2_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + name_var1_token + var3_sign_token + var3_token + MINUS_SYM + var2_token + EXPRESSION_END;
        String ex_cond1_compute_b4_divide_with_minus_sign_token = name1_add_str_token + name1_with_category_token + PLUS_BLANK_STR + var2_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + name_var1_token + var3_sign_token + var3_token + PLUS_SYM + var2_token + EXPRESSION_END;
        if(useAddMinus==false){
            ex_cond1_compute_b4_divide_with_plus_sign_token="";
            ex_cond1_compute_b4_divide_with_minus_sign_token="";
        }

        // if var1 != 1
        //"(%s%s의 나이) = {age1 + year -+ var2} / var1 = %d\n"
        String ex_cond1_compute_divide_with_plus_sign_token = name2_add_str_token + name2_with_category_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + name_var1_token + var3_sign_token + var3_token + MINUS_SYM + var2_token + EXPRESSION_END + DIVIDE_BLANK_STR + var1_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + BRACKET_START + name_var1_token + var3_sign_token + var3_token + MINUS_SYM + var2_token + BRACKET_END + DIVIDE_SYM + var1_token + EXPRESSION_END;
        String ex_cond1_compute_divide_with_minus_sign_token = name2_add_str_token + name2_with_category_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + name_var1_token + var3_sign_token + var3_token + PLUS_SYM + var2_token + EXPRESSION_END + DIVIDE_BLANK_STR + var1_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + BRACKET_START + name_var1_token + var3_sign_token + var3_token + PLUS_SYM + var2_token + BRACKET_END + DIVIDE_SYM + var1_token + EXPRESSION_END;
        if(useMult==false) {
            ex_cond1_compute_divide_with_plus_sign_token="";
            ex_cond1_compute_divide_with_minus_sign_token="";
        }

        // explanation
        String explanation="";

        explanation_ls.add(ex_expression_str_start_token);
        explanation_ls.add(ex_expression_token);
        explanation_ls.add(ex_expression_str_end_token);

        explanation_ls.add(ex_explain_start_token);
        if(cond_inx == 0){  // age1 given, find age2
            explanation = ex_name_var1_after_year_token;
            if(useMult == false && useAddMinus == false)
                explanation += ex_name_var1_after_year_same_age_token;
            explanation_ls.add(explanation);

            explanation = "";
            if(useMult == false && useAddMinus == true)
                explanation += ex_cond1_compute_b4_divide_with_zero_var2_token;
            if(var2_sign == PLUS_SIGN){
                explanation += ex_cond1_compute_b4_divide_with_plus_sign_token;
                explanation_ls.add(explanation);
                explanation_ls.add(ex_cond1_compute_divide_with_plus_sign_token);
                explanation_ls.add(ex_after_year_to_name_var2_with_plus_sign_token);
            } else if(var2_sign == MINUS_SIGN){
                explanation += ex_cond1_compute_b4_divide_with_minus_sign_token;
                explanation_ls.add(explanation);
                explanation_ls.add(ex_cond1_compute_divide_with_minus_sign_token);
                explanation_ls.add(ex_after_year_to_name_var2_with_minus_sign_token);
            } else {
                explanation_ls.add("var2_sign value error");
            }
        } else{ // age2 given, find age1
            explanation = ex_name_var2_after_year_token;
            if(useMult == false && useAddMinus == false)
                explanation += ex_name_var2_after_year_same_age_token;
            explanation_ls.add(explanation);

            if(var2_sign == PLUS_SIGN){
                explanation_ls.add(ex_cond2_compute_with_plus_sign_token);
            } else if(var2_sign == MINUS_SIGN){
                explanation_ls.add(ex_cond2_compute_with_minus_sign_token);
            } else{
                explanation_ls.add("var2_sign value error");
            }

            explanation_ls.add(ex_after_year_to_name_var1_token);
        }

        explanation = explanation_ls.get(0);
        for(int i = 1; i < explanation_ls.size(); i++){
            explanation += "\n" + explanation_ls.get(i);
        }
        return new String[] {content, explanation};     // {content, explanation}
    }

    // name_var1 +- name_var2 = var1
    public String[] create_sentence_sum_diff(int ls_index, int var_num_per_sentence, int cond_inx, int sign,
                                             String name_category_token,
                                             String name_unit_token){
        name_category_token = age_category_token; // "의 나이"
        name_unit_token = age_unit_token;

        // index
        int index1 = ls_index;
        int index2 = ls_index + 1;
        int var_index = ls_index * var_num_per_sentence;

        /* string tokens
        String blank_token = " ";
        String ui_token = "의";
        String gwa_token = "과";
        String wa_token = "와";
        String neun_token = "는";
        String eun_token = "은";
        String eul_token = "을";
        String reul_token = "를";
        String eseo_token = "에서";

        String age_unit_token = "살";
        String bae_token = "배 한 것";
        String than_token = "보다";
        String same_token = "같습니다.";
        String more_amount_token = "많습니다.";
        String less_amount_token = "적습니다.";
        String sum_token = "합한";
        String difference_token = "뺀";
        String value_token = "값";
        String name_to_age_token = "의 나이";
        String time_past_token = "년 후";
         */

        String name1_token = VAR_START + NAME_STR + index1 + VAR_END;
        String name2_token = VAR_START + NAME_STR + index2 + VAR_END;
        String name_var1_token = VAR_START + NAME_VAR_STR + index1 + VAR_END;
        String name_var2_token = VAR_START + NAME_VAR_STR + index2 + VAR_END;
        String var1_token = VAR_START + VAR_STR + var_index + VAR_END;

        String name1_with_category_token = name1_token + ui_token + blank_token + name_category_token;    // "{name1}의 나이"
        String name2_with_category_token = name2_token + ui_token + blank_token + name_category_token;    // "{name2}의 나이"

        // content
        String content="";
        String sum_start_str_token = name1_with_category_token + wa_token + blank_token + name2_with_category_token + reul_token + blank_token;
        String diff_start_str_token = name1_with_category_token + eseo_token + blank_token + name2_with_category_token + reul_token + blank_token;
        String content_end_token = value_token + eun_token + blank_token + var1_token + wa_token + blank_token + same_token;
        if(sign == PLUS_SIGN){
            content =  sum_start_str_token + sum_token + blank_token + content_end_token;
        } else{
            content = diff_start_str_token + difference_token + blank_token + content_end_token;
        }

        // sign
        String sign_token = "", sign_blank_token="", inv_sign_token = "", inv_sign_blank_token="";
        if(sign== PLUS_SIGN) {
            sign_token = PLUS_SYM;  sign_blank_token = PLUS_BLANK_STR;
            inv_sign_token = MINUS_SYM; inv_sign_blank_token = MINUS_BLANK_STR;
        }
        else if(sign== MINUS_SIGN) {
            sign_token = MINUS_SYM; sign_blank_token = MINUS_BLANK_STR;
            inv_sign_token = PLUS_SYM;  inv_sign_blank_token = PLUS_BLANK_STR;
        }
        else sign_token = "!!!!!!!SIGN_ERROR!!!!";


        // explanation token 조합
        //(name1의 나이) +- (name2의 나이) = %d
        // (name1의 나이) = %d -+ (name2의 나이) = %d
        // (name2의 나이) = %d - name1의 나이 = %d
        String ex_expression_str_start_token = "\"" + content + "\"라는 문장을 식으로 바꾸면";
        String ex_expression_token = name1_with_category_token + sign_blank_token + name2_with_category_token
                + BLANK_SYM + EQUAL_SYM + BLANK_SYM + var1_token;
        String ex_expression_str_end_token = "가 됩니다.";
        String ex_explain_start_token = "이제 이 식을 다음과 같은 순서로 풀 수 있습니다.";
        String ex_name_var1_token = name1_with_category_token
                + EQUAL_BLANK_SYM + var1_token + inv_sign_blank_token + name2_with_category_token
                + EQUAL_BLANK_SYM + var1_token + inv_sign_blank_token + name_var2_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + var1_token + inv_sign_token + name_var2_token + EXPRESSION_END;
        String ex_name_var2_with_sum_token = name2_with_category_token
                + EQUAL_BLANK_SYM + var1_token + MINUS_BLANK_STR + name1_with_category_token
                + EQUAL_BLANK_SYM + var1_token + MINUS_BLANK_STR + name_var1_token
                + EQUAL_BLANK_SYM + EXPRESSION_START + var1_token + MINUS_SYM + name_var1_token + EXPRESSION_END;

        String explanation = "";
        ArrayList<String> explanation_ls = new ArrayList<>(10);
        explanation_ls.add(ex_expression_str_start_token);
        explanation_ls.add(ex_expression_token);
        explanation_ls.add(ex_expression_str_end_token);

        explanation_ls.add(ex_explain_start_token);
        if(cond_inx == 0) {  // age1 given, find age2
            if(sign == PLUS_SIGN) {
                explanation_ls.add(ex_name_var2_with_sum_token);
            } else if(sign == MINUS_SIGN) {
                explanation_ls.add("VALUE ERROR:: should do minus from bigger to smaller");
            } else{
                explanation_ls.add("SIGN VALUE ERROR");
            }
        } else if(cond_inx == 1){   // age2 given, find age1
            explanation_ls.add(ex_name_var1_token);
        }

        explanation = explanation_ls.get(0);
        for(int i = 1; i < explanation_ls.size(); i++){
            explanation += "\n" + explanation_ls.get(i);
        }

        return new String[] {content, explanation};     // {content, explanation}
    }


    /////////////////////////////////////////////////////////////////////////////////////////
    // 랜덤 숫자 뽑기

    // var_min_value_ls, var_max_value_ls 설정
    private void setVarMinMaxLs(int prob_sentence_num, int num_var_per_sentence){
        var_min_value_ls = new int[prob_sentence_num * num_var_per_sentence];
        var_max_value_ls = new int[prob_sentence_num * num_var_per_sentence];
        for(int i = 0; i < prob_sentence_num; i++){
            int var1_index = i * num_var_per_sentence;
            if(useMult_ls[i]) {    // var1
                var_min_value_ls[var1_index] = 2;
                var_max_value_ls[var1_index] = 5;
            } else{
                var_min_value_ls[var1_index] = 1;
                var_max_value_ls[var1_index] = 1;
            }
            if(useAddMinus_ls[i]){ // var2
                var_min_value_ls[var1_index+1] = 1;
                var_max_value_ls[var1_index+1] = 20;
            } else{
                var_min_value_ls[var1_index+1] = 0;
                var_max_value_ls[var1_index+1] = 0;
            }
            if(useYear_ls[i]){
                var_min_value_ls[var1_index+2] = 1;
                var_max_value_ls[var1_index+2] = 20;
                var_min_value_ls[var1_index+3] = 1;
                var_max_value_ls[var1_index+3] = 20;
            } else{
                var_min_value_ls[var1_index+2] = 0;
                var_max_value_ls[var1_index+2] = 0;
                var_min_value_ls[var1_index+3] = 0;
                var_max_value_ls[var1_index+3] = 0;
            }
        }
    }

    // random int 값 뽑기
    private int getRandomIntValue(int min_value, int max_value){
        if(max_value < min_value){
            System.out.println("ERROR:: getRandomIntValue() : min > max now... should be min <= max");
            return -1;
        } else if(max_value == min_value){
            return 0;
        } else{
            int ret = random.nextInt(max_value-min_value+1) + min_value;
            return ret;
        }
    }


    // 나이 문제 숫자 뽑기
    private void getRandomAgeValue(int age_ls_length, int num_var_per_sentence){
        name_var_ls = new int[age_ls_length];
        var_ls = new int[age_ls_length * num_var_per_sentence];

        int age0 = getRandomIntValue(name_var_min_value_ls[0], name_var_max_value_ls[0]);
        int given_age = age0;
        for(int i = sentence_category_id_ls.length - 1; i >= 0; i--){
            int age1_index = i;
            int age2_index = (i + 1) % name_var_ls.length;
            int var1_index = age1_index * num_var_per_sentence;
            int var2_index = var1_index + 1;
            int year1_index = var1_index + 2;
            int year2_index = var1_index + 3;
            if(sentence_category_id_ls[i] == AGE_CATEGORY_ID_YX){
                int[] ret_var = getRandomYXValue(given_age,
                        var_sign_ls[var2_index], var_sign_ls[year1_index], var_sign_ls[year2_index],
                        name_var_min_value_ls[age1_index], name_var_max_value_ls[age1_index],
                        var_min_value_ls[var1_index], var_max_value_ls[var1_index],
                        var_min_value_ls[var2_index], var_max_value_ls[var2_index],
                        var_min_value_ls[year1_index], var_max_value_ls[year1_index],
                        var_min_value_ls[year2_index], var_max_value_ls[year2_index]);
                name_var_ls[age1_index] = ret_var[0];
                name_var_ls[age2_index] = ret_var[1];
                var_ls[var1_index] = ret_var[2];
                var_ls[var2_index] = ret_var[3];
                var_ls[year1_index] = ret_var[4];
                var_ls[year2_index] = ret_var[5];

            } else if(sentence_category_id_ls[i] == AGE_CATEGORY_ID_SUM_DIFFERENCE){
                int[] ret_var = getRandomX1X2Value(given_age, var_sign_ls[i], name_var_min_value_ls[age1_index], name_var_max_value_ls[age1_index]);
                name_var_ls[age1_index] = ret_var[0];
                name_var_ls[age2_index] = ret_var[1];
                var_ls[var1_index] = ret_var[2];
            } else{
                System.out.println("ERROR:: invalid category id");
            }
        }
    }

    // (name_var1 +- var3) = (name_var2 +- var4) * var1 +- var2
    // name_var2 given
    // return new int[] {name_var1, name_var2, var1, var2, var3, var4}
    private int[] getRandomYXValue(int given_name_var2, int var_sign, int year1_sign, int year2_sign,
                                   int name_var1_min_value, int name_var1_max_value,
                                   int var1_min_value, int var1_max_value,
                                   int var2_min_value, int var2_max_value,
                                   int var3_min_value, int var3_max_value,
                                   int var4_min_value, int var4_max_value){ // name_var2, year given
        int var1=1, var2=0, var3=0, var4=0, name_var1=0, name_var2=given_name_var2;

        // name_var1 = name_var2 * var1 +- var2
        while(name_var1 < name_var1_min_value || name_var1 > name_var1_max_value) {
            var1 = getRandomIntValue(var1_min_value, var1_max_value);
            var2 = getRandomIntValue(var2_min_value, var2_max_value);
            var3 = getRandomIntValue(var3_min_value, var3_max_value);
            var4 = getRandomIntValue(var4_min_value, var4_max_value);
            name_var1 = 0;
            int name_var2_with_var4 = 0;
            int name_var1_with_var3 = 0;
            if(year2_sign == PLUS_SIGN){
                name_var2_with_var4 = name_var2 + var4;
            } else{
                name_var2_with_var4 = name_var2 - var4;
            }
            if(var_sign == PLUS_SIGN) {
                name_var1_with_var3 = name_var2_with_var4 * var1 + var2;
            } else{
                name_var1_with_var3 = name_var2_with_var4 * var1 - var2;
            }
            if(year1_sign == PLUS_SIGN){
                name_var1 = name_var1_with_var3 - var3;
            } else{
                name_var1 = name_var1_with_var3 + var3;
            }
        }

        return new int[] {name_var1, name_var2, var1, var2, var3, var4};
    }

    // name_var1 +- name_var2 = var1
    // name_var2 given
    // 단, name_var1 > name_var2 여야 (초등교육과정)
    // return new int[] {name_var1, name_var2, var1};
    private int[] getRandomX1X2Value(int given_name_var2, int sign,
                                     int name_var1_min_value, int name_var1_max_value){ // age2 given
        int age1 = getRandomIntValue(name_var1_min_value, name_var1_max_value);
        int age2 = given_name_var2;

        int var1 = -1;
        while (var1 < 0){
            if(sign == PLUS_SIGN){  // age1 + age2 = var1
                var1 = age1 + age2;
            } else { // age1 - age2 = var1
                var1 = age1 - age2;
            }
        }
        return new int[] {age1, age2, var1};
    }


    /////////////////////////////////////////////////////////////////////////////////////////////
    // 그 외 함수

    // return new String[] {real_content, real_explanation, real_answer};
    public String[] templateToProblem(String[] name_ls, int[] name_var_ls, int[] var_ls,
                                      String content_template, String explanation_template, String answer_template){
        // content, explanation, answer
        String real_content = content_template;
        String real_explanation = explanation_template;
        String real_answer = answer_template;
        for(int i = 0; i < name_ls.length; i++){
            String old_value = VAR_START + NAME_STR+i + VAR_END;
            String new_value = name_ls[i];
            real_content = real_content.replace(old_value, new_value);
            real_explanation = real_explanation.replace(old_value, new_value);
            real_answer = real_answer.replace(old_value, new_value);
        }
        for(int i = 0; i < name_var_ls.length;i++){
            String old_value = VAR_START + NAME_VAR_STR+i + VAR_END;
            String new_value = String.valueOf(name_var_ls[i]);
            real_content = real_content.replace(old_value, new_value);
            real_explanation = real_explanation.replace(old_value, new_value);
            real_answer = real_answer.replace(old_value, new_value);
        }
        for(int i = 0; i < var_ls.length; i++){
            String old_value = VAR_START + VAR_STR+i + VAR_END;
            String new_value = String.valueOf(var_ls[i]);
            real_content = real_content.replace(old_value, new_value);
            real_explanation = real_explanation.replace(old_value, new_value);
            real_answer = real_answer.replace(old_value, new_value);
        }

        // [] 속 식 계산
        /*
        ScriptEngineManager s = new ScriptEngineManager();
        ScriptEngine engine = s.getEngineByName("JavaScript");
        String str = "(10+20)*2";
        int num = 0;
        try {
            num = (int)engine.eval(str);
        } catch (ScriptException e) {
            throw new RuntimeException(e);
        }
        System.out.println(str + " = " + num);
         */

        return new String[] {real_content, real_explanation, real_answer};
    }

    public void printTemplate(){
        System.out.println("CONTENT ------------------------------------");
        System.out.println(content_template);
        System.out.println("EXPLANATION ------------------------------------");
        System.out.println(explanation_template);
        System.out.println("ANSWER ------------------------------------");
        System.out.println(answer_template);
        System.out.println("SENTENCE_CATEGORY_ID ------------------------------");
        System.out.println(Arrays.toString(sentence_category_id_ls));
        System.out.println("VAR_SIGN ------------------------------------");
        System.out.println(Arrays.toString(var_sign_ls));
        System.out.println("\n\n");
    }






}