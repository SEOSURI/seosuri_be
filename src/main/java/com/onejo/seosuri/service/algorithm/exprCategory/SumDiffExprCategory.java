package com.onejo.seosuri.service.algorithm.exprCategory;

import com.onejo.seosuri.service.algorithm.ProblemTokenStruct;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class SumDiffExprCategory extends ExprCategory {

    @Override
    public String toString() {
        return "합차 유형";
    }

    @Override
    public String createSentenceContent(boolean isCorrectNumSentence, int name_var_index1, int name_var_index2,
                                        int ls_index, int var_num_per_sentence,
                                        boolean useYear1, boolean useYear2, boolean useMult, boolean useAddMinus,
                                        int var2_sign, int year1_sign, int year2_sign,
                                        String name_category_token, String name_unit_token, String var34_unit_token,
                                        String after_str_token, String before_str_token) {
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

        String name1_token = ProblemTokenStruct.VAR_START + ProblemTokenStruct.NAME_STR + index1 + ProblemTokenStruct.VAR_END;
        String name2_token = ProblemTokenStruct.VAR_START + ProblemTokenStruct.NAME_STR + index2 + ProblemTokenStruct.VAR_END;
        String name_var1_token = ProblemTokenStruct.VAR_START + ProblemTokenStruct.NAME_VAR_STR + index1 + ProblemTokenStruct.VAR_END;
        String name_var2_token = ProblemTokenStruct.VAR_START + ProblemTokenStruct.NAME_VAR_STR + index2 + ProblemTokenStruct.VAR_END;
        String var1_token = ProblemTokenStruct.VAR_START + ProblemTokenStruct.VAR_STR + var_index + ProblemTokenStruct.VAR_END;

        String name1_with_category_token = name1_token + ProblemTokenStruct.ui_token + ProblemTokenStruct.blank_token + name_category_token;    // "{name1}의 나이"
        String name2_with_category_token = name2_token + ProblemTokenStruct.ui_token + ProblemTokenStruct.blank_token + name_category_token;    // "{name2}의 나이"

        // content
        String content="";
        String sum_start_str_token = name1_with_category_token + ProblemTokenStruct.wa_token + ProblemTokenStruct.blank_token + name2_with_category_token + ProblemTokenStruct.reul_token + ProblemTokenStruct.blank_token;
        String diff_start_str_token = name1_with_category_token + ProblemTokenStruct.eseo_token + ProblemTokenStruct.blank_token + name2_with_category_token + ProblemTokenStruct.reul_token + ProblemTokenStruct.blank_token;
        String content_end_token = ProblemTokenStruct.value_token + ProblemTokenStruct.eun_token + ProblemTokenStruct.blank_token + var1_token + ProblemTokenStruct.wa_token + ProblemTokenStruct.blank_token + ProblemTokenStruct.same_token;
        if(var2_sign == ProblemTokenStruct.PLUS_SIGN){
            content =  sum_start_str_token + ProblemTokenStruct.sum_token + ProblemTokenStruct.blank_token + content_end_token;
        } else{
            content = diff_start_str_token + ProblemTokenStruct.difference_token + ProblemTokenStruct.blank_token + content_end_token;
        }

        return content;
    }


    @Override
    public String createSentenceExplanation(String content, int name_var_index1, int name_var_index2,
                                            int ls_index, int var_num_per_sentence, int cond_inx,
                                            boolean useYear1, boolean useYear2, boolean useMult, boolean useAddMinus,
                                            int var2_sign, int year1_sign, int year2_sign,
                                            String name_category_token, String name_unit_token, String var34_unit_token,
                                            String after_str_token, String before_str_token) {
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

        String name1_token = ProblemTokenStruct.VAR_START + ProblemTokenStruct.NAME_STR + index1 + ProblemTokenStruct.VAR_END;
        String name2_token = ProblemTokenStruct.VAR_START + ProblemTokenStruct.NAME_STR + index2 + ProblemTokenStruct.VAR_END;
        String name_var1_token = ProblemTokenStruct.VAR_START + ProblemTokenStruct.NAME_VAR_STR + index1 + ProblemTokenStruct.VAR_END;
        String name_var2_token = ProblemTokenStruct.VAR_START + ProblemTokenStruct.NAME_VAR_STR + index2 + ProblemTokenStruct.VAR_END;
        String var1_token = ProblemTokenStruct.VAR_START + ProblemTokenStruct.VAR_STR + var_index + ProblemTokenStruct.VAR_END;

        String name1_with_category_token = name1_token + ProblemTokenStruct.ui_token + ProblemTokenStruct.blank_token + name_category_token;    // "{name1}의 나이"
        String name2_with_category_token = name2_token + ProblemTokenStruct.ui_token + ProblemTokenStruct.blank_token + name_category_token;    // "{name2}의 나이"

        // sign
        String sign_token = "", sign_blank_token="", inv_sign_token = "", inv_sign_blank_token="";
        if(var2_sign== ProblemTokenStruct.PLUS_SIGN) {
            sign_token = ProblemTokenStruct.PLUS_SYM;  sign_blank_token = ProblemTokenStruct.PLUS_BLANK_STR;
            inv_sign_token = ProblemTokenStruct.MINUS_SYM; inv_sign_blank_token = ProblemTokenStruct.MINUS_BLANK_STR;
        }
        else if(var2_sign== ProblemTokenStruct.MINUS_SIGN) {
            sign_token = ProblemTokenStruct.MINUS_SYM; sign_blank_token = ProblemTokenStruct.MINUS_BLANK_STR;
            inv_sign_token = ProblemTokenStruct.PLUS_SYM;  inv_sign_blank_token = ProblemTokenStruct.PLUS_BLANK_STR;
        }
        else sign_token = "!!!!!!!SIGN_ERROR!!!!";


        // 기본식 : (name1의 나이) +- (name2의 나이) = %d
        // explanation token 조합
        // (name1의 나이) = %d -+ (name2의 나이) = %d // name1 구하기, +/-
        // (name2의 나이) = %d - name1의 나이 = %d // name2 구하기, +
        // (name2의 나이) = name1의 나이 - %d = %d // name2 구하기, -
        String ex_expression_str_start_token = "\"" + content + "\"라는 문장을 식으로 바꾸면";
        String ex_expression_token = name1_with_category_token + sign_blank_token + name2_with_category_token
                + ProblemTokenStruct.BLANK_SYM + ProblemTokenStruct.EQUAL_SYM + ProblemTokenStruct.BLANK_SYM + var1_token;
        String ex_expression_str_end_token = "가 됩니다.";
        String ex_explain_start_token = "이제 이 식을 다음과 같은 순서로 풀 수 있습니다.";
        String ex_name_var1_token = name1_with_category_token
                + ProblemTokenStruct.EQUAL_NL_BLANK_SYM + var1_token + inv_sign_blank_token + name2_with_category_token
                + ProblemTokenStruct.EQUAL_NL_BLANK_SYM + var1_token + inv_sign_blank_token + name_var2_token
                + ProblemTokenStruct.EQUAL_NL_BLANK_SYM + ProblemTokenStruct.EXPRESSION_START + var1_token + inv_sign_token + name_var2_token + ProblemTokenStruct.EXPRESSION_END;
        String ex_name_var2_with_sum_token = name2_with_category_token
                + ProblemTokenStruct.EQUAL_NL_BLANK_SYM + var1_token + ProblemTokenStruct.MINUS_BLANK_STR + name1_with_category_token
                + ProblemTokenStruct.EQUAL_NL_BLANK_SYM + var1_token + ProblemTokenStruct.MINUS_BLANK_STR + name_var1_token
                + ProblemTokenStruct.EQUAL_NL_BLANK_SYM + ProblemTokenStruct.EXPRESSION_START + var1_token + ProblemTokenStruct.MINUS_SYM + name_var1_token + ProblemTokenStruct.EXPRESSION_END;
        String ex_name_var2_with_diff_token = name2_with_category_token
                + ProblemTokenStruct.EQUAL_NL_BLANK_SYM + name1_with_category_token + ProblemTokenStruct.MINUS_BLANK_STR + var1_token
                + ProblemTokenStruct.EQUAL_NL_BLANK_SYM + name_var1_token + ProblemTokenStruct.MINUS_BLANK_STR + var1_token
                + ProblemTokenStruct.EQUAL_NL_BLANK_SYM + ProblemTokenStruct.EXPRESSION_START + name_var1_token + ProblemTokenStruct.MINUS_SYM + var1_token + ProblemTokenStruct.EXPRESSION_END;

        String explanation = "";
        ArrayList<String> explanation_ls = new ArrayList<>(10);
        explanation_ls.add(ex_expression_str_start_token);
        explanation_ls.add(ex_expression_token);
        explanation_ls.add(ex_expression_str_end_token);

        explanation_ls.add(ex_explain_start_token);
        if(cond_inx == 0) {  // age1 given, find age2
            if(var2_sign == ProblemTokenStruct.PLUS_SIGN) {
                explanation_ls.add(ex_name_var2_with_sum_token);
            } else if(var2_sign == ProblemTokenStruct.MINUS_SIGN) {
                explanation_ls.add(ex_name_var2_with_diff_token);
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

        return explanation;
    }

    @Override
    public int[] getRandomValue(int given_name_var2, int var_sign, int year1_sign, int year2_sign, int name_var1_min_value, int name_var1_max_value, int var1_min_value, int var1_max_value, int var2_min_value, int var2_max_value, int var3_min_value, int var3_max_value, int var4_min_value, int var4_max_value, boolean useYear1, boolean useYear2, boolean useAddMinus, boolean useMult) throws TimeoutException {
        int age1 = 0;
        int age2 = given_name_var2;

        long timeoutInMn = 3;   // timeout 시간
        LocalDateTime startTime = LocalDateTime.now();

        int var1 = -1;
        while (var1 < 0){
            age1 = getRandomIntValue(name_var1_min_value, name_var1_max_value);
            if(var_sign == ProblemTokenStruct.PLUS_SIGN){  // age1 + age2 = var1
                var1 = age1 + age2;
            } else { // age1 - age2 = var1
                var1 = age1 - age2;
            }

            // timeout
            if(ChronoUnit.MINUTES.between(startTime, LocalDateTime.now()) > timeoutInMn){
                throw new TimeoutException();
            }
        }

        return new int[] {age1, age2, var1, 0, 0, 0};
    }


}
