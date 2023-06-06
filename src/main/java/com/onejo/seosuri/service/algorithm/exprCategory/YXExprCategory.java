package com.onejo.seosuri.service.algorithm.exprCategory;

import com.onejo.seosuri.service.algorithm.ProblemTokenStruct;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public abstract class YXExprCategory extends ExprCategory {

    @Override
    public String toString(){
        return "YX 유형";
    }

    abstract public String createTemplateSentenceContent(boolean isCorrectNumSentence, int name_var_index1, int name_var_index2,
                                                         int ls_index, int var_num_per_sentence,
                                                         boolean useYear1, boolean useYear2, boolean useMult, boolean useAddMinus,
                                                         int var2_sign, int year1_sign, int year2_sign,
                                                         String name_category_token, String name_unit_token, String var34_unit_token,
                                                         String after_str_token, String before_str_token);

    @Override
    public String createTemplateSentenceExplanation(String content, int name_var_index1, int name_var_index2,
                                                    int ls_index, int var_num_per_sentence, int cond_inx,
                                                    boolean useYear1, boolean useYear2, boolean useMult, boolean useAddMinus,
                                                    int var2_sign, int year1_sign, int year2_sign,
                                                    String name_category_token, String name_unit_token, String var34_unit_token,
                                                    String after_str_token, String before_str_token) {
        String name_chosa_token = ProblemTokenStruct.ui_token;

        ArrayList<String> explanation_ls = new ArrayList<>(10);

        // index
        //int name_var_index1 = ls_index;
        //int name_var_index2 = name_var_index1 + 1;

        int var_index1 = ls_index * var_num_per_sentence;
        int var_index2 = var_index1 + 1;
        int year1_index = var_index1 + 2;
        int year2_index = var_index1 + 3;

        // token 조합
        String name1_token = ProblemTokenStruct.VAR_START + ProblemTokenStruct.NAME_STR + name_var_index1 + ProblemTokenStruct.VAR_END;
        String name2_token = ProblemTokenStruct.VAR_START + ProblemTokenStruct.NAME_STR + name_var_index2 + ProblemTokenStruct.VAR_END;
        String name1_with_category_token = name1_token + ProblemTokenStruct.ui_token + ProblemTokenStruct.blank_token +  name_category_token;
        String name2_with_category_token = name2_token + ProblemTokenStruct.ui_token + ProblemTokenStruct.blank_token +  name_category_token;
        String name_var1_token = ProblemTokenStruct.VAR_START + ProblemTokenStruct.NAME_VAR_STR + name_var_index1 + ProblemTokenStruct.VAR_END;
        String name_var2_token = ProblemTokenStruct.VAR_START + ProblemTokenStruct.NAME_VAR_STR + name_var_index2 + ProblemTokenStruct.VAR_END;
        String var1_token = ProblemTokenStruct.VAR_START + ProblemTokenStruct.VAR_STR + var_index1 + ProblemTokenStruct.VAR_END;
        String var2_token = ProblemTokenStruct.VAR_START + ProblemTokenStruct.VAR_STR + var_index2 + ProblemTokenStruct.VAR_END;
        String var3_token = ProblemTokenStruct.VAR_START + ProblemTokenStruct.VAR_STR + year1_index + ProblemTokenStruct.VAR_END;    // year1
        String var4_token = ProblemTokenStruct.VAR_START + ProblemTokenStruct.VAR_STR + year2_index + ProblemTokenStruct.VAR_END;   // year2

        String name1_add_str_token = "", name2_add_str_token = "";
        if(useYear1) {
            if (year1_sign == ProblemTokenStruct.PLUS_SIGN) {
                name1_add_str_token = var3_token + var34_unit_token + ProblemTokenStruct.blank_token + after_str_token + ProblemTokenStruct.blank_token; // "{year1}년 후 "
            } else if (year1_sign == ProblemTokenStruct.MINUS_SIGN) {
                name1_add_str_token = var3_token + var34_unit_token + ProblemTokenStruct.blank_token + before_str_token + ProblemTokenStruct.blank_token; // "{year1}년 전 "
            } else {
                System.out.println("ERROR:: invalid year sign");
            }
        }
        if(useYear2) {
            if(year2_sign == ProblemTokenStruct.PLUS_SIGN){
                name2_add_str_token = var4_token + var34_unit_token + ProblemTokenStruct.blank_token + after_str_token + ProblemTokenStruct.blank_token; // "{year2}년 후 "
            } else if(year2_sign == ProblemTokenStruct.MINUS_SIGN){
                name2_add_str_token = var4_token + var34_unit_token + ProblemTokenStruct.blank_token + before_str_token + ProblemTokenStruct.blank_token; // "{year2}년 전 "
            } else{
                System.out.println("ERROR:: invalid year sign");
            }
        }



        // sign
        String var2_sign_token = "", var2_sign_blank_token = "";
        String var2_inv_sign_token = "", var2_inv_sign_blank_token = "";
        if(var2_sign == ProblemTokenStruct.PLUS_SIGN){
            var2_sign_token = ProblemTokenStruct.PLUS_SYM;  var2_sign_blank_token = ProblemTokenStruct.PLUS_BLANK_STR;
            var2_inv_sign_token = ProblemTokenStruct.MINUS_SYM; var2_inv_sign_blank_token = ProblemTokenStruct.MINUS_BLANK_STR;
        } else if(var2_sign == ProblemTokenStruct.MINUS_SIGN){
            var2_sign_token = ProblemTokenStruct.MINUS_SYM; var2_sign_blank_token = ProblemTokenStruct.MINUS_BLANK_STR;
            var2_inv_sign_token = ProblemTokenStruct.PLUS_SYM;  var2_inv_sign_blank_token = ProblemTokenStruct.PLUS_BLANK_STR;
        } else{
            System.out.println("ERROR:: SIGN VALUE ERROR");
            return null;
        }

        String var3_sign_token = ProblemTokenStruct.PLUS_SYM, var3_inv_sign_token = ProblemTokenStruct.MINUS_SYM;
        String var3_sign_blank_token = ProblemTokenStruct.PLUS_BLANK_STR, var3_inv_sign_blank_token = ProblemTokenStruct.MINUS_BLANK_STR;
        if(year1_sign == ProblemTokenStruct.MINUS_SIGN) {
            var3_sign_token = ProblemTokenStruct.MINUS_SYM;    var3_inv_sign_token = ProblemTokenStruct.PLUS_SYM;
            var3_sign_blank_token = ProblemTokenStruct.MINUS_BLANK_STR;    var3_inv_sign_blank_token = ProblemTokenStruct.PLUS_BLANK_STR;
        }

        String var4_sign_token = ProblemTokenStruct.PLUS_SYM, var4_inv_sign_token = ProblemTokenStruct.MINUS_SYM;
        String var4_sign_blank_token = ProblemTokenStruct.PLUS_BLANK_STR, var4_inv_sign_blank_token = ProblemTokenStruct.MINUS_BLANK_STR;
        if(year2_sign == ProblemTokenStruct.MINUS_SIGN) {
            var4_sign_token = ProblemTokenStruct.MINUS_SYM;    var4_inv_sign_token = ProblemTokenStruct.PLUS_SYM;
            var4_sign_blank_token = ProblemTokenStruct.MINUS_BLANK_STR;    var4_inv_sign_blank_token = ProblemTokenStruct.PLUS_BLANK_STR;
        }

        String mult_str_token = ProblemTokenStruct.bae_token;  // "배 한 것" "를 곱한 것"
        String c_mult_token = name_chosa_token + ProblemTokenStruct.blank_token + var1_token + mult_str_token;  //"의 {var1}배 한 것" "에 {var1}를 곱한 것"
        String c_mult_end_token = ProblemTokenStruct.gwa_token + ProblemTokenStruct.blank_token + ProblemTokenStruct.same_token; //"과 같습니다.";
        String c_mult_end_wa_token = ProblemTokenStruct.wa_token + ProblemTokenStruct.blank_token + ProblemTokenStruct.same_token; //"와 같습니다.";
        String c_pm_token = ProblemTokenStruct.than_token + ProblemTokenStruct.blank_token + var2_token + name_unit_token + ProblemTokenStruct.blank_token;   //"보다 " + var2_token + "살 ";


        // explanation token
        String mult_blank_var1_token = ProblemTokenStruct.MULT_BLANK_STR + var1_token;
        String mult_var1_token = ProblemTokenStruct.MULT_SYM + var1_token;
        if(useMult == false) {
            mult_blank_var1_token = "";
            mult_var1_token = "";
        }
        String inv_add_minus_blank_var2_token = var2_inv_sign_blank_token + var2_token;
        String inv_add_minus_var2_token = var2_inv_sign_token + var2_token;
        String add_minus_blank_var2_token = var2_sign_blank_token + var2_token;
        String add_minus_var2_token = var2_sign_token + var2_token;
        if(useAddMinus == false){
            add_minus_blank_var2_token = "";
            add_minus_var2_token = "";
            inv_add_minus_blank_var2_token = "";
            inv_add_minus_var2_token = "";
        }
        String expr_add_minus_blank_var4_token = var4_sign_blank_token + var4_token;
        String expr_add_minus_blank_var3_token = var3_sign_blank_token + var3_token;
        String expr_add_minus_var4_token = var4_sign_token + var4_token;
        String expr_add_minus_var3_token = var3_sign_token + var3_token;
        if(useYear1 == false){
            expr_add_minus_var3_token = "";
            expr_add_minus_blank_var3_token = "";
        }
        if(useYear2 == false){
            expr_add_minus_var4_token = "";
            expr_add_minus_blank_var4_token = "";
        }


        String ex_expression_str_start_token = "\"" + content + "\"라는 문장을 식으로 바꾸면";
        String ex_expression_token = name1_add_str_token + name1_with_category_token
                + ProblemTokenStruct.EQUAL_NL_BLANK_SYM + name2_add_str_token + name2_with_category_token + mult_blank_var1_token + add_minus_blank_var2_token;
        String ex_expression_str_end_token = "가 됩니다.";

        String ex_explain_start_token = "이제 이 식을 다음과 같은 순서로 풀 수 있습니다.";

        // name_var -> name_var +- year   또는   name_var 값 선언
        String ex_name_var1_after_year_token = name1_add_str_token + name1_with_category_token
                + ProblemTokenStruct.EQUAL_NL_BLANK_SYM + name_var1_token + expr_add_minus_blank_var3_token;
        String ex_name_var2_after_year_token = name2_add_str_token + name2_with_category_token
                + ProblemTokenStruct.EQUAL_NL_BLANK_SYM + name_var2_token + expr_add_minus_blank_var4_token;
        if(useYear1){
            ex_name_var1_after_year_token += ProblemTokenStruct.EQUAL_NL_BLANK_SYM + ProblemTokenStruct.EXPRESSION_START + name_var1_token + expr_add_minus_var3_token + ProblemTokenStruct.EXPRESSION_END;
        }
        if(useYear2){
            ex_name_var2_after_year_token += ProblemTokenStruct.EQUAL_NL_BLANK_SYM + ProblemTokenStruct.EXPRESSION_START + name_var2_token + expr_add_minus_var4_token + ProblemTokenStruct.EXPRESSION_END;
        }


        String expr_name_var1_token = ProblemTokenStruct.BRACKET_START + name_var2_token + expr_add_minus_var4_token + ProblemTokenStruct.BRACKET_END + mult_var1_token + add_minus_var2_token;
        String expr_name_var2_token = ProblemTokenStruct.BRACKET_START + name_var1_token + expr_add_minus_var3_token + inv_add_minus_var2_token + ProblemTokenStruct.BRACKET_END + ProblemTokenStruct.DIVIDE_SYM + var1_token;


        // if var2 != 0
        // (year년 후 name1의 나이) -+ var2 = age1 + year -+ var2
        String ex_cond1_compute_b4_divide_token = name1_add_str_token + name1_with_category_token + inv_add_minus_blank_var2_token
                + ProblemTokenStruct.EQUAL_NL_BLANK_SYM + ProblemTokenStruct.EXPRESSION_START + name_var1_token + expr_add_minus_var3_token + inv_add_minus_var2_token + ProblemTokenStruct.EXPRESSION_END;
        String ex_eq_name_var2_token = ProblemTokenStruct.EQUAL_NL_BLANK_SYM + name2_add_str_token + name2_with_category_token;
        String expr_cond1_computed_b4_divide_token = name_var1_token + expr_add_minus_var3_token + inv_add_minus_var2_token;
        // if var1 != 1
        //"(%s%s의 나이) = {age1 + year -+ var2} / var1 = %d\n"
        // age1 given, find age2
        String ex_cond1_compute_divide_token = name2_add_str_token + name2_with_category_token
                + ProblemTokenStruct.EQUAL_NL_BLANK_SYM + ProblemTokenStruct.EXPRESSION_START + expr_cond1_computed_b4_divide_token + ProblemTokenStruct.EXPRESSION_END + ProblemTokenStruct.DIVIDE_BLANK_STR + var1_token
                + ProblemTokenStruct.EQUAL_NL_BLANK_SYM + ProblemTokenStruct.EXPRESSION_START + ProblemTokenStruct.BRACKET_START + expr_cond1_computed_b4_divide_token + ProblemTokenStruct.BRACKET_END + ProblemTokenStruct.DIVIDE_SYM + var1_token + ProblemTokenStruct.EXPRESSION_END;

        // (%s%s의 나이) = (%s%s의 나이) * %d %s %d = %d * %d %s %d = %d
        // y년후 name1의 나이 = y년후 name2의나이 * var1 +- var2 = [age2+year] * var1 +- var2 = age1
        // age2 given, find age1
        String ex_cond2_compute_token = name1_add_str_token + name1_with_category_token
                + ProblemTokenStruct.EQUAL_NL_BLANK_SYM + name2_add_str_token + name2_with_category_token + mult_blank_var1_token + add_minus_blank_var2_token
                + ProblemTokenStruct.EQUAL_NL_BLANK_SYM + ProblemTokenStruct.EXPRESSION_START + name_var2_token + expr_add_minus_var4_token + ProblemTokenStruct.EXPRESSION_END + mult_blank_var1_token + add_minus_blank_var2_token;
        if(!(useMult == false && useAddMinus == false)){
            ex_cond2_compute_token += ProblemTokenStruct.EQUAL_NL_BLANK_SYM + ProblemTokenStruct.EXPRESSION_START + ProblemTokenStruct.BRACKET_START + name_var2_token + expr_add_minus_var4_token + ProblemTokenStruct.BRACKET_END + mult_var1_token + add_minus_var2_token + ProblemTokenStruct.EXPRESSION_END;
        }

        // name_var +- year -> name_var
        String ex_after_year_to_name_var1_token = name1_with_category_token
                + ProblemTokenStruct.EQUAL_NL_BLANK_SYM + name1_add_str_token + name1_with_category_token + var3_inv_sign_blank_token + var3_token
                + ProblemTokenStruct.EQUAL_NL_BLANK_SYM + ProblemTokenStruct.EXPRESSION_START + expr_name_var1_token + ProblemTokenStruct.EXPRESSION_END + var3_inv_sign_blank_token + var3_token
                + ProblemTokenStruct.EQUAL_NL_BLANK_SYM + ProblemTokenStruct.EXPRESSION_START + expr_name_var1_token + var3_inv_sign_token + var3_token + ProblemTokenStruct.EXPRESSION_END;
        String ex_after_year_to_name_var2_token = name2_with_category_token
                + ProblemTokenStruct.EQUAL_NL_BLANK_SYM + name2_add_str_token + name2_with_category_token + var4_inv_sign_blank_token + var4_token
                + ProblemTokenStruct.EQUAL_NL_BLANK_SYM + ProblemTokenStruct.EXPRESSION_START + expr_name_var2_token + ProblemTokenStruct.EXPRESSION_END + var4_inv_sign_blank_token + var4_token
                + ProblemTokenStruct.EQUAL_NL_BLANK_SYM + ProblemTokenStruct.EXPRESSION_START + expr_name_var2_token + var4_inv_sign_token + var4_token + ProblemTokenStruct.EXPRESSION_END;



        // explanation
        String explanation="";

        // ~라는 문장을 식으로 바꾸면 ~~가 됩니다.
        explanation_ls.add(ex_expression_str_start_token);
        explanation_ls.add(ex_expression_token);
        explanation_ls.add(ex_expression_str_end_token);

        // 이제 이 식을 다음과 같은 순서로 풀 수 있습니다.
        explanation_ls.add(ex_explain_start_token);

        // 풀이 과정 설명
        if(cond_inx == 0){  // age1 given, find age2
            if(useMult==false && useAddMinus == false){
                explanation_ls.add(ex_name_var1_after_year_token + ex_eq_name_var2_token);
            } else {
                explanation_ls.add(ex_name_var1_after_year_token);
            }

            if(useAddMinus) {
                if(useMult==false){
                    explanation_ls.add(ex_cond1_compute_b4_divide_token + ex_eq_name_var2_token);
                } else{
                    explanation_ls.add(ex_cond1_compute_b4_divide_token);
                }
            }

            if(useMult) {
                explanation_ls.add(ex_cond1_compute_divide_token);
            }

            if(useYear2) {
                explanation_ls.add(ex_after_year_to_name_var2_token);
            }

        } else{ // age2 given, find age1
            explanation_ls.add(ex_name_var2_after_year_token);
            explanation_ls.add(ex_cond2_compute_token);
            if(useYear1) {
                explanation_ls.add(ex_after_year_to_name_var1_token);
            }
        }


        explanation = explanation_ls.get(0);
        for(int i = 1; i < explanation_ls.size(); i++){
            explanation += "\n\n" + explanation_ls.get(i);
        }
        return explanation;     // {content, explanation}
    }


    @Override
    public int[] getRandomValue (int given_name_var2, int var_sign, int year1_sign, int year2_sign, int name_var1_min_value, int name_var1_max_value, int var1_min_value, int var1_max_value, int var2_min_value, int var2_max_value, int var3_min_value, int var3_max_value, int var4_min_value, int var4_max_value, boolean useYear1, boolean useYear2, boolean useAddMinus, boolean useMult) throws TimeoutException { // name_var2, year given
        int var1=1, var2=0, var3=0, var4=0, name_var1=0, name_var2=given_name_var2;

        long timeoutInMn = 3;   // timeout 시간
        LocalDateTime startTime = LocalDateTime.now();

        // name_var1 = name_var2 * var1 +- var2
        while(name_var1 < name_var1_min_value || name_var1 > name_var1_max_value) {
            var1 = getRandomIntValue(var1_min_value, var1_max_value);
            var2 = getRandomIntValue(var2_min_value, var2_max_value);
            var3 = getRandomIntValue(var3_min_value, var3_max_value);
            var4 = getRandomIntValue(var4_min_value, var4_max_value);
            if(useMult == false)  {
                var1 = 1;
            }
            if(useAddMinus == false){
                var2 = 0;
            }
            if(useYear1 == false) {
                var3 = 0;
            }
            if(useYear2 == false) {
                var4 = 0;
            }

            int name_var2_with_var4 = 0;
            int name_var1_with_var3 = 0;
            if(year2_sign == ProblemTokenStruct.PLUS_SIGN){
                name_var2_with_var4 = name_var2 + var4;
            } else{
                name_var2_with_var4 = name_var2 - var4;
            }
            if(var_sign == ProblemTokenStruct.PLUS_SIGN) {
                name_var1_with_var3 = name_var2_with_var4 * var1 + var2;
            } else{
                name_var1_with_var3 = name_var2_with_var4 * var1 - var2;
            }
            if(year1_sign == ProblemTokenStruct.PLUS_SIGN){
                name_var1 = name_var1_with_var3 - var3;
            } else{
                name_var1 = name_var1_with_var3 + var3;
            }

            // timeout
            if(ChronoUnit.MINUTES.between(startTime, LocalDateTime.now()) > timeoutInMn){
                throw new TimeoutException();
            }
        }

        return new int[] {name_var1, name_var2, var1, var2, var3, var4};
    }


}

