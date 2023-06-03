package com.onejo.seosuri.service.algorithm.problem;

import com.onejo.seosuri.service.algorithm.ProblemTokenStruct;
import com.onejo.seosuri.service.algorithm.category.Category;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public abstract class CreateProblem {

    ProblemValueStruct problemValueStruct = new ProblemValueStruct();

    CreateProblem(ProblemValueStruct problemValueStruct){
        this.problemValueStruct = problemValueStruct;
    }

    abstract public void createProblem(int level);

    // DB 연결
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
                problemValueStruct.constant_var_max_value_ls[var1_index+1] = 20;
            } else{
                problemValueStruct.constant_var_min_value_ls[var1_index+1] = 0;
                problemValueStruct.constant_var_max_value_ls[var1_index+1] = 0;
            }
            if(problemValueStruct.useYear1_ls[i]){
                problemValueStruct.constant_var_min_value_ls[var1_index+2] = 1;
                problemValueStruct.constant_var_max_value_ls[var1_index+2] = 100;
            } else{
                problemValueStruct.constant_var_min_value_ls[var1_index+2] = 0;
                problemValueStruct.constant_var_max_value_ls[var1_index+2] = 0;
            }
            if(problemValueStruct.useYear2_ls[i]){
                problemValueStruct.constant_var_min_value_ls[var1_index+3] = 1;
                problemValueStruct.constant_var_max_value_ls[var1_index+3] = 100;
            } else{
                problemValueStruct.constant_var_min_value_ls[var1_index+3] = 0;
                problemValueStruct.constant_var_max_value_ls[var1_index+3] = 0;
            }
        }
    }

    // DB 연결
    protected void setVariantVarMinMaxLsAndStringLs(int name_var_num){
        problemValueStruct.variant_var_min_value_ls = new int[name_var_num];
        problemValueStruct.variant_var_max_value_ls = new int[name_var_num];
        problemValueStruct.variant_var_string_ls = new String[name_var_num];
        for(int i = 0; i < name_var_num; i++){      // DB 연결 -> DB에서 값 받아와야
            problemValueStruct.variant_var_min_value_ls[i] = 10;
            problemValueStruct.variant_var_max_value_ls[i] = 100;
            problemValueStruct.variant_var_string_ls[i] = i+"사람"+i;
        }
    }

    // 나이 문제 숫자 뽑기
    // input : name_var_min_value_ls, name_value_max_value_ls, sentence_category_id_ls, var_sign_ls, var_min_value_ls, var_max_value_ls, useYear_ls, useMult_ls, useAddMinus_ls
    // output : name_var_ls, var_ls
    protected void setVar(int variable_var_num, int constant_var_num, int num_constant_var_per_sentence){
        problemValueStruct.variant_var_ls = new int[variable_var_num];
        problemValueStruct.constant_var_ls = new int[constant_var_num];

        int age0 = Category.getRandomIntValue(problemValueStruct.variant_var_min_value_ls[0], problemValueStruct.variant_var_max_value_ls[0]);
        int given_age = age0;
        int num_sentence = problemValueStruct.sentence_category_id_ls.length;
        int start_index = num_sentence - 1;   // 마지막 상황문장부터 숫자 뽑음
        for(int i = start_index; i >= 0; i--){
            int age1_index = i;
            int age2_index = (i + 1) % problemValueStruct.variant_var_ls.length;
            int var1_index = age1_index * num_constant_var_per_sentence;
            int var2_index = var1_index + 1;
            int year1_index = var1_index + 2;
            int year2_index = var1_index + 3;

            try {
                int[] ret_var = problemValueStruct.category_ls[i].getRandomValue(given_age,
                        problemValueStruct.var_sign_ls[var2_index], problemValueStruct.var_sign_ls[year1_index], problemValueStruct.var_sign_ls[year2_index],
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
            } catch (TimeoutException e){
                i = start_index;
                continue;
            }
        }
    }



    /////////////////////////////////////////////////////////////////////////////////////////////
    // template -> problem

    // return new String[] {real_content, real_explanation, real_answer};
    public String[] templateToProblem(String[] name_ls, int[] name_var_ls, int[] var_ls,
                                      String content_template, String explanation_template, String answer_template){
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

        real_content = calcExpr(real_content);
        real_explanation = calcExpr(real_explanation);
        real_answer = calcExpr(real_answer);

        return new String[] {real_content, real_explanation, real_answer};
    }

    // templateToProblem에서 [식내용] -> 계산한 값
    public String calcExpr(String target){
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
                String calc_res = "DEFAULT_EXPR_STR:: ";

                // calculate

                // 임시 dummy 값
                for(int j = 0; j < expression.size(); j++){
                    calc_res += expression.get(j);
                }

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

}
