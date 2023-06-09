package com.onejo.seosuri.service.algorithm.problem;

import com.onejo.seosuri.domain.problem.ProbWord;
import com.onejo.seosuri.domain.problem.Problem;
import com.onejo.seosuri.domain.problem.ProblemRepository;
import com.onejo.seosuri.domain.word.Word;
import com.onejo.seosuri.domain.word.WordRepository;
import com.onejo.seosuri.domain.word.WordType;
import com.onejo.seosuri.exception.common.BusinessException;
import com.onejo.seosuri.exception.common.ErrorCode;
import com.onejo.seosuri.service.algorithm.ProblemTokenStruct;
import com.onejo.seosuri.service.algorithm.ProblemValueStruct;
import com.onejo.seosuri.service.algorithm.exprCategory.ExprCategory;
import com.onejo.seosuri.service.algorithm.exprCategory.YXAgeExprCategory;
import lombok.RequiredArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeoutException;


public abstract class CreateProblem {

    ProblemValueStruct problemValueStruct = new ProblemValueStruct();

    public CreateProblem(ProblemValueStruct problemValueStruct){
        this.problemValueStruct = problemValueStruct;
    }


    abstract public void createProblem(int level) throws BusinessException;

    protected void createProblem(int prob_sentence_num, int constant_var_num, int variable_var_num, int var_num_per_sentence)
            throws BusinessException{
        //////////////////////////////////////////////////////////////////////////
        // 템플릿 DB값 가져오기 -> 가져옴

        // 템플릿, 템플릿 관련 값 -> 가져옴
        //setDBTemplateValues(prob_sentence_num, constant_var_num);

        // 변수 string, 변수 var 결정 - 변수 ex) 변수 string(어머니, 동생), 변수 var(어머니 나이가 51살일 경우, 바로 51이 변수 var)
        setVariantVarMinMaxLsAndStringLs(variable_var_num); // name_ls, name_var_min_value_ls, name_var_max_value_ls 설정

        // 변수 var 범위 미세 조정
        fixVariantVarRange();

        // 상수 var 범위 설정 -> 문제 숫자 값 랜덤 뽑기 시 이용됨 ex) 5를 더한다에서 5가 바로 상수 var
        setConstantVarMinMaxLs(prob_sentence_num, var_num_per_sentence);    // var_min_value_ls, var_max_value_ls 설정


        ////////////////////////////////////////////////////////////////////////////
        // 실제 문제에 채워넣을 값 뽑기 : 숫자 뽑기, 이름 뽑기
        // 상수 var, name_var 랜덤 뽑기 -> 숫자 변경 시 여기부터 다시 실행하면 됨!!!
        // ageProblem의 variable_var_num = 4, var_num_per_sentence = level
        // 인자 외에 내부에서 이용하는 값 : name_var_min_value_ls, name_value_max_value_ls, sentence_category_id_ls, var_sign_ls, var_min_value_ls, var_max_value_ls, useYear_ls, useMult_ls, useAddMinus_ls
        setVar(variable_var_num, constant_var_num, var_num_per_sentence);  // name_var_ls, var_ls 설정


        /////////////////////////////////////////////////////////////////////////////
        // template -> problem
        templateToProblem(problemValueStruct.variant_var_string_ls, problemValueStruct.variant_var_ls, problemValueStruct.constant_var_ls,
                problemValueStruct.content_template, problemValueStruct.explanation_template, problemValueStruct.answer_template);
    }


    ////////////////////////////////////////////////////////////////////////////
    // 템플릿 DB에서 값 가져오기
    protected void setDBTemplateValues(int prob_sentence_num, int constant_var_num){
        problemValueStruct.sentence_expr_category_id_ls = new int[prob_sentence_num];    // 각 상황문장이 어떤 유형의 문장인지를 저장한 배열
        problemValueStruct.expr_category_ls = new ExprCategory[prob_sentence_num];
        problemValueStruct.constant_var_sign_ls = new int[constant_var_num];    // DB에서 가져오기!!!
        problemValueStruct.useYear1_ls = new boolean[prob_sentence_num];   // DB에서 가져오기!!!
        problemValueStruct.useYear2_ls = new boolean[prob_sentence_num];   // DB에서 가져오기!!!
        problemValueStruct.useMult_ls = new boolean[prob_sentence_num];   // DB에서 가져오기!!!
        problemValueStruct.useAddMinus_ls = new boolean[prob_sentence_num];   // DB에서 가져오기!!! // entire addminus should be false

        // DB에서 값 가져오기
        for(int i = 0; i < prob_sentence_num; i++){
            problemValueStruct.sentence_expr_category_id_ls[i] = 0;     // DB에서 값 가져오기
            problemValueStruct.expr_category_ls[i] = new YXAgeExprCategory(); // DB에서 값 가져오기
            problemValueStruct.useYear1_ls[i] = true; // DB에서 값 가져오기
            problemValueStruct.useYear2_ls[i] = false; // DB에서 값 가져오기
            problemValueStruct.useMult_ls[i] = true; // DB에서 값 가져오기
            problemValueStruct.useAddMinus_ls[i] = true; // DB에서 값 가져오기
        }
        for(int i = 0; i < constant_var_num; i++){
            problemValueStruct.constant_var_sign_ls[i] = 0;  // DB에서 값 가져오기
        }

        // DB에서 템플릿 가져오기 - 난이도로 템플릿 고르고 그 중에서 랜덤 뽑기!!!
        problemValueStruct.content_template = "내용";
        problemValueStruct.explanation_template = "설명";
        problemValueStruct.answer_template = "답";
    }

    ////////////////////////////////////////////////////////////////////////////
    // 이름 DB에서 값 가져오기 - for문 안에만 수정하기
    protected void setVariantVarMinMaxLsAndStringLs(int name_var_num){
        problemValueStruct.variant_var_min_value_ls = new int[name_var_num];
        problemValueStruct.variant_var_max_value_ls = new int[name_var_num];
        problemValueStruct.variant_var_string_ls = new String[name_var_num];

        // 섞은 목록 각 필드에 저장
        for(int i=0; i<name_var_num; i++){
            problemValueStruct.variant_var_min_value_ls[i] = problemValueStruct.getWordList().get(i).getNumStart().intValue();
            problemValueStruct.variant_var_max_value_ls[i] = problemValueStruct.getWordList().get(i).getNumEnd().intValue();
            problemValueStruct.variant_var_string_ls[i] = problemValueStruct.getWordList().get(i).getContent();
        }
    }

    //////////////////////////////////////////////////////////////////////////
    // 값 설정 (DB에서 가져오는 것XX) - 수정할 필요 없음 (추후에 값 너무 크게 나오면 실험 통해서 바꿔야 할 수도...?)
    protected void setConstantVarMinMaxLs(int prob_sentence_num, int num_var_per_sentence){
        problemValueStruct.constant_var_min_value_ls = new int[prob_sentence_num * num_var_per_sentence];
        problemValueStruct.constant_var_max_value_ls = new int[prob_sentence_num * num_var_per_sentence];
        for(int i = 0; i < prob_sentence_num; i++){
            int var1_index = i * num_var_per_sentence;
            if(problemValueStruct.useMult_ls[i]) {    // var1
                problemValueStruct.constant_var_min_value_ls[var1_index] = 2;
                problemValueStruct.constant_var_max_value_ls[var1_index] = 5;
            } else{ // 0~100 =  0~100 * 2 - 100 // 99 *
                problemValueStruct.constant_var_min_value_ls[var1_index] = 1;
                problemValueStruct.constant_var_max_value_ls[var1_index] = 1;
            }
            if(problemValueStruct.useAddMinus_ls[i]){ // var2
                problemValueStruct.constant_var_min_value_ls[var1_index+1] = 1;
                problemValueStruct.constant_var_max_value_ls[var1_index+1] = 50;
            } else{
                problemValueStruct.constant_var_min_value_ls[var1_index+1] = 0;
                problemValueStruct.constant_var_max_value_ls[var1_index+1] = 0;
            }
            if(problemValueStruct.useYear1_ls[i]){
                problemValueStruct.constant_var_min_value_ls[var1_index+2] = 1;
                problemValueStruct.constant_var_max_value_ls[var1_index+2] = 20;
            } else{
                problemValueStruct.constant_var_min_value_ls[var1_index+2] = 0;
                problemValueStruct.constant_var_max_value_ls[var1_index+2] = 0;
            }
            if(problemValueStruct.useYear2_ls[i]){
                problemValueStruct.constant_var_min_value_ls[var1_index+3] = 1;
                problemValueStruct.constant_var_max_value_ls[var1_index+3] = 20;
            } else{
                problemValueStruct.constant_var_min_value_ls[var1_index+3] = 0;
                problemValueStruct.constant_var_max_value_ls[var1_index+3] = 0;
            }
        }
    }

    //////////////////////////////////////////////////////////////////////////////
    // 나이 문제 숫자 뽑기 - DB에서 가져왔던 값 이용
    // input : name_var_min_value_ls, name_value_max_value_ls, sentence_category_id_ls, var_sign_ls, var_min_value_ls, var_max_value_ls, useYear_ls, useMult_ls, useAddMinus_ls
    // output : name_var_ls, var_ls
    protected void setVar(int variable_var_num, int constant_var_num, int num_constant_var_per_sentence) {
        problemValueStruct.variant_var_ls = new int[variable_var_num];
        problemValueStruct.constant_var_ls = new int[constant_var_num];

        int ageN = ExprCategory.getRandomIntValue(problemValueStruct.variant_var_min_value_ls[variable_var_num - 1], problemValueStruct.variant_var_max_value_ls[variable_var_num - 1]);
        int given_age = ageN;
        int num_sentence = problemValueStruct.sentence_expr_category_id_ls.length;
        int start_index = num_sentence-1;   // 마지막 상황문장부터 숫자 뽑음 : 0, 1 / 1, 2 / 2, 3(given)


        //for(int i = start_index; i >= 0; i--){
        int i = start_index;    // 0, 1, ..., N 까지의 name_var 중 N-1에 해당하는 값: start_index    상황문장 N-1(마지막, start_index) : N-1, N name_var index

        long timeoutInMn = 1000;   // timeout 시간
        LocalDateTime totalStartTime = LocalDateTime.now();
        LocalDateTime startTime = LocalDateTime.now();

        while (i >= 0) {
            System.out.println("가장 밖이에유aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
            int age1_index = i;
            int age2_index = (i + 1) % problemValueStruct.variant_var_ls.length;
            int var1_index = age1_index * num_constant_var_per_sentence;
            int var2_index = var1_index + 1;
            int year1_index = var1_index + 2;
            int year2_index = var1_index + 3;

            try {
                int[] ret_var = problemValueStruct.expr_category_ls[i].getRandomValue(given_age,
                        problemValueStruct.constant_var_sign_ls[var2_index], problemValueStruct.constant_var_sign_ls[year1_index], problemValueStruct.constant_var_sign_ls[year2_index],
                        problemValueStruct.variant_var_min_value_ls[age1_index], problemValueStruct.variant_var_max_value_ls[age1_index],
                        problemValueStruct.constant_var_min_value_ls[var1_index], problemValueStruct.constant_var_max_value_ls[var1_index],
                        problemValueStruct.constant_var_min_value_ls[var2_index], problemValueStruct.constant_var_max_value_ls[var2_index],
                        problemValueStruct.constant_var_min_value_ls[year1_index], problemValueStruct.constant_var_max_value_ls[year1_index],
                        problemValueStruct.constant_var_min_value_ls[year2_index], problemValueStruct.constant_var_max_value_ls[year2_index],
                        problemValueStruct.useYear1_ls[i], problemValueStruct.useYear2_ls[i], problemValueStruct.useAddMinus_ls[i], problemValueStruct.useMult_ls[i]);
                problemValueStruct.variant_var_ls[age1_index] = ret_var[0];
                problemValueStruct.variant_var_ls[age2_index] = ret_var[1];
                problemValueStruct.constant_var_ls[var1_index] = ret_var[2];
                problemValueStruct.constant_var_ls[var2_index] = ret_var[3];
                problemValueStruct.constant_var_ls[year1_index] = ret_var[4];
                problemValueStruct.constant_var_ls[year2_index] = ret_var[5];

                i--;
            } catch (TimeoutException e) {
                i = start_index;
            }

            // timeout
            LocalDateTime now = LocalDateTime.now();
            long time_passed = ChronoUnit.MILLIS.between(startTime, now);
            long total_time_passed = ChronoUnit.MILLIS.between(totalStartTime, now);
            System.out.println("############# 문제 하나 뽑기 타임아웃 소요 시간 : " + time_passed);
            if(time_passed > timeoutInMn){
                System.out.println("문제 하나 뽑기에서 타임 아웃 발생 - age0 부터 다시 뽑읍시다~~~~~~~~~~~~~~~~~~~~~~``");
                ageN = ExprCategory.getRandomIntValue(problemValueStruct.variant_var_min_value_ls[variable_var_num - 1], problemValueStruct.variant_var_max_value_ls[variable_var_num - 1]);
                given_age = ageN;
                i = start_index;
                startTime = LocalDateTime.now();
            }
            if(total_time_passed > 10000){
                throw new BusinessException(ErrorCode.TIMEOUT_EXCEPTION);
            }
        }

    }



    /////////////////////////////////////////////////////////////////////////////////////////////
    // template -> problem : store in ProblemValueStruct
    public void templateToProblem(String[] name_ls, int[] name_var_ls, int[] var_ls,
                                  String content_template, String explanation_template, String answer_template)
            throws BusinessException{
        // content, explanation, answer
        String real_content = content_template;
        String real_explanation = explanation_template;
        String real_answer = answer_template;
        for(int i = 0; i < name_ls.length; i++){
            String old_value = ProblemTokenStruct.VAR_START + ProblemTokenStruct.NAME_STR+i + ProblemTokenStruct.VAR_END;
            String new_value = name_ls[i];
            real_content = real_content.replace(old_value, new_value);
            real_explanation = real_explanation.replace(old_value, new_value);
            real_answer = real_answer.replace(old_value, new_value);
        }
        for(int i = 0; i < name_var_ls.length;i++){
            String old_value = ProblemTokenStruct.VAR_START + ProblemTokenStruct.NAME_VAR_STR+i + ProblemTokenStruct.VAR_END;
            String new_value = String.valueOf(name_var_ls[i]);
            real_content = real_content.replace(old_value, new_value);
            real_explanation = real_explanation.replace(old_value, new_value);
            real_answer = real_answer.replace(old_value, new_value);
        }
        for(int i = 0; i < var_ls.length; i++){
            String old_value = ProblemTokenStruct.VAR_START + ProblemTokenStruct.VAR_STR+i + ProblemTokenStruct.VAR_END;
            String new_value = String.valueOf(var_ls[i]);
            real_content = real_content.replace(old_value, new_value);
            real_explanation = real_explanation.replace(old_value, new_value);
            real_answer = real_answer.replace(old_value, new_value);
        }

        problemValueStruct.real_content = calcExpr(real_content);
        problemValueStruct.real_explanation = calcExpr(real_explanation);
        problemValueStruct.real_answer = calcExpr(real_answer);
    }

    // templateToProblem에서 [식내용] -> 계산한 값
    public String calcExpr(String target) throws BusinessException{
        // [] 속 식 계산

        String res = "";
        ArrayList<Character> expression = new ArrayList<>();
        boolean inExpr = false;
        for(int i = 0; i < target.length(); i++){
            char char_i = target.charAt(i);
            if(char_i == '['){
                expression.clear();
                inExpr = true;
            } else if(char_i == ']'){
                inExpr = false;
                String expr = "";
                for(Character c: expression){
                    expr += c;
                }
                String calc_res = String.valueOf(CalculateExpr.calculate(expr));
                res += calc_res;
            } else {
                if (inExpr == true) {   // 식 속의 문자
                    expression.add(char_i);
                } else {    // 식 밖의 문자
                    res += char_i;
                }
            }
        }
        return res;
    }

    // variant var 범위 수정 - template에 따라
    private void fixVariantVarRange() {
        int num_sentence = problemValueStruct.sentence_expr_category_id_ls.length;
        // 마지막 상황문장부터 숫자 뽑음 : name0 name1(given) / name1 name2(given) / name2 name3(given)
        int name_var_num = num_sentence + 1;
        // name3 < name2 < name1 < name0 식이어야 템플릿 구조에 따른 무한 루프 방지 가능
        // 최소 2배 이상 씩 방지해야
        // name0 : max/(temp^(prob_num - name_index))
        for(int name_inx = 0; name_inx < name_var_num; name_inx++){ // 각 name_var 값에 대해서 max value 미세 조정
            for(int l = 0; l < name_inx; l++) {
                problemValueStruct.variant_var_max_value_ls[name_inx] /= 5;
            }
            problemValueStruct.variant_var_max_value_ls[name_inx] += 50;
            problemValueStruct.variant_var_min_value_ls[name_inx] = 30;
        }
        System.out.println(name_var_num);
    }

}
