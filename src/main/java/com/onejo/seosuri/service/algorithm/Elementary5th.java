// 이곳에 문제 생성 알고리즘을 넣어두고, ProblemSerice에서 필요한 method를 호출하면 될 듯 싶습니다..

package com.onejo.seosuri.service.algorithm;

import java.util.Random;

public class Elementary5th {
    Random random;
    static final int PLUS = 0;
    static final int MINUS = 1;
    //static final String PLUS = "+";
    //static final String MINUS = "-";

    // 상황 문장 id
    static final int AGE_CATEGORY_ID_YX = 0;
    static final int AGE_CATEGORY_ID_SUM_DIFFERENCE = 1;

    // 변수명 string 규칙
    static final String AGE_STR = "age";    // age 변수 : {age0}, {age1}, {age2}, ...
    static final String NAME_STR = "name";  // name 변수 : {name0}, {name1}, {name2}, ...
    static final String VAR_STR = "var";    // 상수 변수 : {var0}, {var1}, {var2}, ...

    // 나이 구하기 문제 알고리즘
    public void ageProblem() {
        ////////////////////////////////////////////////////////////////////////////////////////////////
        // 값 설정
        random = new Random(); //랜덤 객체 생성(디폴트 시드값 : 현재시간)
        random.setSeed(System.currentTimeMillis()); //시드값 설정을 따로 할수도 있음
        int prob_sentence_num = 5;  // 상황 문장 갯수 - 문제 난이도에 따라 값 달라짐
        int sentence_category_num = 2;  // 상황 문장 유형 갯수

        int var_num_per_sentence = 2;
        int[] age_ls = new int[prob_sentence_num + 1];  // 상황문장 1개일 때 age 변수는 2개, 상황 문장 하나 추가될 때마다 age 변수 1개씩 추가됨
        int[] var_ls = new int[prob_sentence_num * var_num_per_sentence];  // yx유형문장은 상수 변수 2개, 합차유형문장은 상수 변수 1개
        int[] sentence_category_id_ls = new int[prob_sentence_num];    // 각 상황문장이 어떤 유형의 문장인지를 저장한 배열
        for(int i = 0; i < sentence_category_id_ls.length; i++){
            sentence_category_id_ls[i] = random.nextInt(sentence_category_num);
        }

        int answer_inx = random.nextInt(age_ls.length);  // 구하는 나이의 인덱스
        int condition_inx = (random.nextInt(age_ls.length-1) + answer_inx) % age_ls.length;   // 조건으로 값이 주어진 나이의 인덱스, answer_inx와 다른 인덱스가 되도록 설정


        ////////////////////////////////////////////////////////////////////////////////////////////////
        // 문장 생성

        // condition 문장 생성
        String condition = "이때, {name"+condition_inx+"}의 나이는 {age"+condition_inx+"}살 입니다.";

        // question 문장 생성
        String question = "그렇다면 {name"+answer_inx+"}의 나이는 몇 살입니까?";

        // answer 문장 생성
        String answer = "{age"+answer_inx+"}살";

        // 상황 문장 생성 : {content, explanation}
        String[][] sentence_ls = new String[prob_sentence_num][2];
        for(int i = 0; i < sentence_ls.length; i++){
            // create_age_sentence(유형id, 값 참조 시작하는 인덱스, answer_index, condition_index) ex) index = 2 -> name2, name3, age2, age3, var2, var3 사용
            sentence_ls[i] = create_age_sentence(sentence_category_id_ls[i], i * var_num_per_sentence, answer_inx, condition_inx);  // sentence_ls[i] = {content, explanation}
        }


        /////////////////////////////////////////////////////////////////////////////////////////////////////
        // 상황 문장 연결

        // content : sentece 순서대로 + condition + question
        String content = "";
        for(int i = 0; i < sentence_ls.length; i++){
            content += sentence_ls[i][0];   // 상황 문장 content
        }
        content = content + condition + question;

        // explanation : conditon_inx -> condition_inx + 1 -> ... -> 끝 index -> 1 -> 2 -> ... -> condition_inx - 1 순서로 연결
        String explanation = "";
        for(int i = condition_inx; i < sentence_ls.length; i++){    // condition_inx   ~   끝 index
            explanation += sentence_ls[i][1];   // 상황 문장 explanation
        }
        for(int i = 0; i < condition_inx; i++){                     // 0   ~   condition_inx - 1
            explanation += sentence_ls[i][1];   // 상황 문장 exlanation
        }

        ////////////////////////////////////////////////////////////////////////////////////////////////////
        // 결과 : {문제, explanation, answer, prob_sentence_category_list}
        String[] result_ls = new String[] {content, explanation, answer, sentence_category_id_ls.toString()};
    }

    // create_age_sentence(유형id, 값 참조 시작하는 인덱스) ex) index = 2 -> name2, name3, age2, age3, var2, var3 사용
    private String[] create_age_sentence(int category_id, int index, int ans_index, int cond_index){
        if(index == AGE_CATEGORY_ID_YX){
            return create_age_sentence_yx(index, ans_index, cond_index);               // {content, explanation}
        } else if (index == AGE_CATEGORY_ID_SUM_DIFFERENCE){
            return create_age_sentence_sum_difference(index, ans_index, cond_index);   // {content, explanation}
        } else{
            return null;
        }
    }

    // age1 + year = (age2 + year) * var1 +- var2
    // {year}년 후 {name1}의 나이는 {year}년 후 {name2}의 나이의 {var1}배한 것과 같습니다.    year_token +
    // {year}년 후 {name1}의 나이는 {year}년 후 {name2}의 나이의 {var1}배한 것보다 {var2}살 많습니다(적습니다).
    // {year}년 후 {name1}의 나이는 {year}년 후 {name2}의 나이보다 {var2}살 많습니다(적습니다).
    // year_token + name1_token + year_token + name2_token
        // mult_token + mult_end_token          : 곱셈만
        // mult_token + pm_token + add_token    : 곱셈 + 덧셈
        // mult_token + pm_token + minus_token  : 곱셈 + 뺄셈
        // pm_token + add_token                 : 덧셈만
        // pm_token + minus_token               : 뺄셈만

    private String[] create_age_sentence_yx(int ls_index, int ans_index, int cond_index){
        int index1 = ls_index;
        int index2 = ls_index + 1;
        String year_token = "{year}년 후 ";
        String name1_token = "{"+NAME_STR+index1+"}의 나이는 ";
        String name2_token = "{"+NAME_STR+index2+"}의 나이";
        String mult_token = "의 {"+VAR_STR+index1+"}배 한 것";
        String mult_end_token = "과 같습니다.";
        String pm_token = "보다 {"+VAR_STR+index2+"}살 ";
        String add_token = "많습니다.";
        String minus_token = "적습니다.";




        return new String[] {};     // {content, explanation}
    }
    private String[] create_age_sentence_sum_difference(int index, int ans_index, int cond_index){


        return new String[] {};     // {content, explanation}
    }

    // 이은 색테이프 문제 알고리즘
    public void colorTapeProblem() {}

    // 어떤 수 문제 알고리즘
    public void anyNumberProblem() {}

    // 도형에서의 혼합 계산 응용 알고리즘
    public void geometryCalculation() {}


    //////////////////////////////////////////////////////////////////////////////////
    //
    // 문제 생성 - 추후 난이도 관련 업데이트 필요
    // return new String[] {content, explanation, answer, answer_inx_str, condition_inx_str};
    //
    //////////////////////////////////////////////////////////////////////////////////

    /*
        // 1. 상황 문장 갯수 설정 : prob_sentence_num

        // 2. 각 상황 문장 종류 결정
        //  유형 종류
        //      (age1 + year) = (age2 + year) * var1 +- var2
        //      age1 +- age2 = var1
        //  상황 문장 종류는 list에 저장 필요
        //      sentence_category_list = {prob_sentence_num, sentence_id_1, sentence_id_2, ...}

        // 3. answer_inx, condition_inx 설정

        // 4. 상황 문장 생성 : content, conditon, explanation, answer

        // 5. 상황 문장 연결
        // 문제 : content + conditon + answer_content
        //      content : 순서대로 연결
        //      condition : condition_inx에 해당하는 상황문장의 condition String
        //      answer_content : "이때, {ans_inx}의 값은 얼마입니까?"
        // explanation : conditon_inx -> condition_inx + 1 -> ... -> 끝 index -> 1 -> 2 -> ... -> condition_inx - 1 순서로 연결
        // answer : ans_inx에 해당하는 값

        // 6. return new String[] {문제, explanation, answer, prob_sentence_category_list}
        //      상황 문장 유형
        //      prob_sentence_category_list = "{sentence_num, sentence_id_1, sentence_id_2, ...}"
        //      "3 1 1 2"
    */

    /*
        ///////////////////////////////////////////////////////////////////////
        // 템플릿 이용해서 문제 생성하는 방법
        // 1. 값 생성 - 상황 문장 유형에 따라
        // 2. 명사(이름) 설정 - 값에 따라
        //      ex) 아버지의 나이 > 동생의 나이 와 같은 규칙이 지켜지도록
        //          이를 위해 이름 DB는 {이름 명사, 나이대, 최소값, 최대값}
        //              나이대 = 0 : 조카        <-- 초등 대상 문제이므로 제외하는 게 나을듯? (제외한다면, 나이대별로 최소값, 최대값 지정도 쉬워짐)
        //                      1 : 동생 < 8
        //                      2 : 나 = 초등학생 : 8~13
        //                      3 : 언니, 오빠, 누나, 형 13~25
        //                      4:  막내이모, 막내삼촌 : 25~35
        //                      4 : 어머니, 아버지, 이모, 고모, 삼촌 : 35~60
        //                      5 : 할아버지, 할머니   : 60~90
        //                      6 : 증조 할아버지, 증조 할머니 : 90~120
        //                      7 : 구미호, 도깨비...? :  120~
        //  "{name1}이 ~~ {name2}~~"
        //        row_id name1 fk_id1
        //        row_id name2 fk_id2
    */

    /* 유형1
        ex) 지수의 나이는 동생 보다 2배한 것 보다 3살 적습니다. 아버지의 나이가 동생 나이의 3배한 것 보다 5살 많을 때 지수의 나이가 15세 라면 아버지의 연세는 얼마일까요?
        {name1}의 나이는 {name2} 나이의 {var1}배한 것 보다 {var2}살 많(적)습니다.
        {name2(3)}의 나이는 {name3(2)}나이의 {var3}배한 것 보다 {var4}살 많(적)습니다.
        이때, name1(2, 3)의 나이가 var4살이라면 name2(1,3)의 나이는 얼마일까요?
        10년 후 지수의 나이는 10년 후 동생의 나이의 2배한 것보다 3살 적습니다.
        name1 = name2 * var1 +- var2
        name2(3) = name3(2) * var3 +- var4

        (y + year) = (x + year) * var1 +- var2

        age1, age2, age3
        age1, age2 문장
        age2, age3
        // 단어 대체목록
        나이 <-> 연세 (아버지, 어머니, 할머니, 할아버지, 삼촌 등등)
    */
    public String[] ageProb1(int level){
        String content="", explanation="", answer="", condition="";
        String[] name_ls = {"아버지", "어머니", "동생"};
        int[] age_ls = new int[3];
        int[] var_ls = new int[6];
        int sign1=PLUS, sign2=PLUS;

        while(true) {
            int[] value_ls;
            int[] value_ls2;
            int given_age = random.nextInt(20) + 5; // age3 given
            int year1 = random.nextInt(10) + 1;  // year given
            int year2 = 0;
            value_ls = getRandomAgeYXValue(given_age, year1);   // {sign, age2, age3, var3, var4}
            if(value_ls[1] <= 0) continue;
            while (true) {
                value_ls2 = getRandomAgeYXValue(value_ls[1], year2);   // {sign, age1, age2, var1, var2}
                if(value_ls2[1] > 0 ) break;
            }
            age_ls[0] = value_ls2[1];   // age1
            age_ls[1] = value_ls2[2];   // age2
            age_ls[2] = value_ls[2];    // age3
            var_ls[0] = value_ls2[3];   // var1
            var_ls[1] = value_ls2[4];   // var2
            var_ls[2] = value_ls[3];    // var3
            var_ls[3] = value_ls[4];    // var4
            var_ls[4] = year1;    // year1
            var_ls[5] = year2;   // year2
            sign1 = value_ls2[0];
            sign2 = value_ls[0];
            break;
        }

        // sentence_ls[i] = {content, condition, explanation, answer}
        String[][] sentence_ls = new String[2][3];
        for(int i = 0; i < level; i++){
            sentence_ls[i] = new String[3];
        }

        int rand = random.nextInt(3);
        int rand_inx = random.nextInt(1) + 1;
        int ans_inx = (rand + rand_inx) % 3;
        int cond_inx = (rand + rand_inx + 1) % 3;
        int rand1=1, rand2=1;
        if(cond_inx == 2){ // age3 given
            rand1 = 0;  // age3 -> age2 given, find age1
            rand2 = 0; // age3 given, find age2
        }
        else{   // age1 or age2 given
            if(cond_inx == 0){ // age1 given
                rand1 = 1; // age1 given, find age2
            }
            else if(cond_inx == 1){   // age2 given
                rand1 = 0; // age2 given, find age1
            }
            rand2 = 1;  // age 1 or age2 -> age2 given, find age3
        }

        sentence_ls[1] = ageSentence(name_ls[1], name_ls[2], age_ls[1], age_ls[2], var_ls[2], var_ls[3], var_ls[5], rand1, sign2);
        sentence_ls[0] = ageSentence(name_ls[0], name_ls[1], age_ls[0], age_ls[1], var_ls[0], var_ls[1], var_ls[4], rand2, sign1);
        // sentence_ls[i] = {content, condition, explanation, answer}

        content = sentence_ls[0][0] + sentence_ls[1][0];
        if(cond_inx == 2){
            content += sentence_ls[1][1];   // condition 추가
            explanation = sentence_ls[1][2] + sentence_ls[0][2];
        }
        else{
            content += sentence_ls[0][1];   // condition 추가
            explanation = sentence_ls[0][2] + sentence_ls[1][2];
        }

        content += String.format("이때, %s의 나이는 몇 살입니까?", name_ls[rand]);
        answer = String.valueOf(age_ls[ans_inx])+"살";

        System.out.printf("%s의 나이 = %d\n", name_ls[0], age_ls[0]);
        System.out.printf("%s의 나이 = %d\n", name_ls[1], age_ls[1]);
        System.out.printf("%s의 나이 = %d\n", name_ls[2], age_ls[2]);
        System.out.printf("var1 = %d,\t var2 = %d, \t var3 = %d, \t var4 = %d\n", var_ls[0], var_ls[1], var_ls[2], var_ls[3]);

        String answer_inx_str = String.valueOf(ans_inx);
        String condition_inx_str = String.valueOf(cond_inx);

        return new String[] {content, explanation, answer, answer_inx_str, condition_inx_str};
    }

    /* 유형 2 : 두 나이의 합 차만 주어진 경우
            x + y = var1
            x - y = var2
         */
    public String[] ageProb2(int level){
        int age1 = random.nextInt(100) + 5;
        int age2 = random.nextInt(100) + 5;
        if(age2 > age1){
            int temp = age1;
            age1 = age2;
            age2 = temp;
        }
        int ans_inx = random.nextInt(2);
        int cond_inx = (ans_inx + 1) % 2;
        int sign = random.nextInt(2);
        String[] name = {"아버지", "어머니"};
        String[] ret_str = ageSentence2(name[0], name[1], age1, age2, ans_inx, sign); // {content, condition, explanation, answer}
        String content = ret_str[0] + ret_str[1] + String.format("%s의 나이는 몇 살입니까?", name[ans_inx]);
        String explanation = ret_str[2];
        String answer = ret_str[3];
        String answer_inx_str = String.valueOf(ans_inx);
        String condition_inx_str = String.valueOf(cond_inx);

        return new String[] {content, explanation, answer, answer_inx_str, condition_inx_str};
    }


    //////////////////////////////////////////////////////////////////////////////////
    //
    // 랜덤 숫자 뽑기
    // return new int[] {sign, age1, age2, var1, var2};
    // return new int[] {sign, age1, age2, var1};
    //
    //////////////////////////////////////////////////////////////////////////////////

    // age1 + year = (age2 + year) * var1 +- var2
    private int[] getRandomAgeYXValue(int given_age, int year){ // age2, year given
        int var1=0, var2=0, age1=0, age2=given_age;

        int sign = random.nextInt(1);

        // age1 = age2 * var1 +- var2
        while(age1 <= 0) {
            var1 = random.nextInt(4) + 2;
            var2 = random.nextInt(20) + 1;
            if(sign == PLUS) {
                age1 = (age2 + year) * var1 + var2 - year;
            } else{
                age1 = (age2 + year) * var1 - var2 - year;
            }
        }

        return new int[] {sign, age1, age2, var1, var2};
    }

    // age1 +- age2 = var1
    private int[] getRandomAgeX1X2Value(int given_age, int given_inx){
        int age1, age2;
        /* 랜덤 값 범위 수정 필요
            세자리수 -> 높은 난이도
            두자리수 -> 낮은 난이도
         */
        if(given_inx == 0) {
            age1 = given_age;
            age2 = random.nextInt(50) + given_age; // age1 > age2 유지
        } else{
            age1 = random.nextInt(50) + given_age; // age1 > age2 유지
            age2 = given_age;
        }

        int sign = random.nextInt(2);
        int var1 = 0;
        if(sign == PLUS){  // age1 + age2 = var1
            var1 = age1 + age2;
        } else { // age1 - age2 = var1
            var1 = age1 - age2;
        }
        return new int[] {sign, age1, age2, var1};
    }


    //////////////////////////////////////////////////////////////////////////////////
    //
    // 문장 생성
    // return new String[] {content, condition, explanation, answer};
    //
    //////////////////////////////////////////////////////////////////////////////////

    public String[] ageSentence(String name1, String name2, int age1, int age2, int var1, int var2, int year, int ans_inx, int sign){
        // name1 = name2 * var1 +- var2
        // {year}년 후 {name1}의 나이는 {name2}의 나이의 {var1}배한 것과 같습니다.
        // {year}년 후 {name1}의 나이는 {name2}의 나이의 {var1}배한 것보다 {var2}살 많습니다(적습니다).
        String content="";
        String token0 = String.format("%d년 후 ", year);
        if(year==0){
            token0 = "";
        }
        String token1 = String.format("%s의 나이는 ", name1);
        String token1_1 = String.format("%s 나이의 %d배한 것", name2, var1);
        String token2_1 = "과 같습니다. ";
        String token2_2 = String.format("보다 %d살 많습니다. ", var2);
        String token2_3 = String.format("보다 %d살 적습니다. ", var2);

        content = token0 + token1 + token0 + token1_1;
        if (var2 == 0) {
            content += token2_1;
        } else {
            if (sign == PLUS) {
                content += token2_2;
            } else {
                content += token2_3;
            }
        }

        // age1 = age2 * var1 +- var2
        String explanation="";
        String answer="";
        String condition="";
        String sign_str = "+";
        String sign_inv_str = "-";
        if(sign == MINUS) {
            sign_str = "-";
            sign_inv_str = "+";
        }

        if(ans_inx==0){ // age2가 주어지고 age1을 답으로 구하는 경우
            // age1 = age2 * var1 +- var2
            condition = String.format("이때, %s의 나이는 %d살입니다. ", name2, age2);
            answer = String.valueOf(age1);
            if(year != 0){  // 나이 -> n년 후 나이 계산
                explanation += String.format("%s%s의 나이 = %d + %d = %d\n", token0, name2, age2, year, age2+year);
            }
            explanation += String.format("(%s%s의 나이) = (%s%s의 나이) * %d %s %d = %d * %d %s %d = %d\n", token0, name1, token0, name2, var1, sign_str, var2, age2+year, var1, sign_str, var2, age1+year);
            if(year != 0){  // n년 후 나이 -> 나이 계산
                explanation += String.format("%s의 나이 = %s%s의 나이 - %d = %d\n", name1, token0, name1, year, age1);
            }
        } else{ // age1이 주어지고 age2를 답으로 구하는 경우
            // age2 = (age1 -+ var2) / var1
            condition = String.format("이때, %s의 나이는 %d살입니다. ", name1, age1);
            answer = String.valueOf(age2);
            int temp1 = age1 + year - var2;
            if(sign == MINUS) temp1 = age1 + year + var2;
            if(year != 0){  // 나이 -> n년 후 나이 계산
                explanation += String.format("%s%s의 나이 = %d + %d = %d\n", token0, name1, age1, year, age1+year);
            }
            if(var2 != 0) {
                explanation += String.format("(%s%s의 나이) %s %d = %d\n", token0, name1, sign_inv_str, var2, temp1);
            }
            explanation += String.format("(%s%s의 나이) = %d / %d = %d\n", token0, name2, temp1, var1, temp1/var1);
            if(year != 0){  // n년 후 나이 -> 나이 계산
                explanation += String.format("%s의 나이 = %s%s의 나이 - %d = %d", name2, token0, name2, year, age2);
            }
        }

        return new String[] {content, condition, explanation, answer};
    }


    public String[] ageSentence2(String name1, String name2, int age1, int age2, int ans_inx, int sign){
        int var1 = age1 + age2;
        int var2 = age1 - age2;

        String content="";
        if(sign == PLUS){
            content =  String.format("%s의 나이와 %s의 나이를 합한 값은 %d와 같습니다. ", name1, name2, var1);
        } else{
            content = String.format("%s의 나이에서 %s의 나이를 뺀 값은 %d와 같습니다. ", name1, name2, var2);
        }

        String cond_name = "", ans_name="";
        int cond_age = 0, ans_age=0;
        if(ans_inx==0) {    // age2 given, find age1
            cond_name = name2;  ans_name = name1;
            cond_age = age2;    ans_age = age1;
        } else{ // age1 given, find age2
            cond_name = name1;  ans_name = name2;
            cond_age = age1;    ans_age = age2;
        }
        String condition = String.format("이때, %s의 나이는 %d입니다.\n", cond_name, cond_age);

        String explanation = "";
        if(sign == PLUS) {
            explanation = String.format("(%s의 나이) + (%s의 나이) = %d\n", name1, name2, var1);
            explanation += String.format("(%s의 나이) = %d - (%s의 나이) = %d\n", ans_name, var1, cond_name, var1 - cond_age);
        } else{
            explanation = String.format("(%s의 나이) - (%s의 나이) = %d\n", name1, name2, var1);
            explanation += String.format("(%s의 나이) = %d + (%s의 나이) = %d\n", name1, var1, name2, var1 + age2);
        }

        String answer="";
        if(ans_inx == 0){
            answer = String.valueOf(age1);
        } else{
            answer = String.valueOf(age2);
        }

        return new String[] {content, condition, explanation, answer};
    }



    /* 유형3 : 대입형 문제 -> 1부터 숫자 넣어가며 푸는 유형 -> 별개 유형으로 취급하기로 함
    ex) 남준이 형의 나이는 남준이 아버지의 나이에서 남준이의 나이를 나눈 것의 4배보다 2살 적습니다. 아버지의 연세가 46세 일 때, 남준이 형의 나이는 몇 살일까요?

    */

}