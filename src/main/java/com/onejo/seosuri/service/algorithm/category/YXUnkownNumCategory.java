package com.onejo.seosuri.service.algorithm.category;

import com.onejo.seosuri.service.algorithm.ProblemTokenStruct;

import java.util.ArrayList;

public class YXUnkownNumCategory extends YXCategory{

    @Override
    public String createSentenceContent(boolean isCorrectNumSentence, int name_var_index1, int name_var_index2,
                                        int ls_index, int var_num_per_sentence,
                                        boolean useYear1, boolean useYear2, boolean useMult, boolean useAddMinus,
                                        int var2_sign, int year1_sign, int year2_sign,
                                        String name_category_token, String name_unit_token, String var34_unit_token,
                                        String after_str_token, String before_str_token){
        ArrayList<String> content_ls = new ArrayList<>(1);

        int var_index1 = ls_index * var_num_per_sentence;
        int var_index2 = var_index1 + 1;
        int year1_index = var_index1 + 2;
        int year2_index = var_index1 + 3;

        // token 조합
        String name1_token = ProblemTokenStruct.VAR_START + ProblemTokenStruct.NAME_STR + name_var_index1 + ProblemTokenStruct.VAR_END;
        String name2_token = ProblemTokenStruct.VAR_START + ProblemTokenStruct.NAME_STR + name_var_index2 + ProblemTokenStruct.VAR_END;
        String var1_token = ProblemTokenStruct.VAR_START + ProblemTokenStruct.VAR_STR + var_index1 + ProblemTokenStruct.VAR_END;
        String var2_token = ProblemTokenStruct.VAR_START + ProblemTokenStruct.VAR_STR + var_index2 + ProblemTokenStruct.VAR_END;
        String var3_token = ProblemTokenStruct.VAR_START + ProblemTokenStruct.VAR_STR + year1_index + ProblemTokenStruct.VAR_END;    // year1
        String var4_token = ProblemTokenStruct.VAR_START + ProblemTokenStruct.VAR_STR + year2_index + ProblemTokenStruct.VAR_END;   // year2

        String var4_addMin_token = "더한";
        if(year1_sign == ProblemTokenStruct.MINUS_SIGN){
            var4_addMin_token = "뺀";
        }

        String var2_addMin_token = "더해";
        if(var2_sign == ProblemTokenStruct.MINUS_SIGN){
            var2_addMin_token = "빼";
        }
        if(isCorrectNumSentence == false){
            var2_addMin_token = "더했";
            if(var2_sign == ProblemTokenStruct.MINUS_SIGN){
                var2_addMin_token = "뺐";
            }
        }

        String mult_token = "곱";
        String mult_to_am_token = "하고";
        String mult_to_end_token = "해";

        String end_correct_token = "야 할 것을";
        String end_wrong_token = "습니다.";
        String end_token = end_correct_token;
        if(isCorrectNumSentence == false){
            end_token = end_wrong_token;
        }
        /*
        {어떤 수}에 {var4}를 (더한/뺀) 후 {var1}을 곱하고 {var2}를 (더해/빼)야 할 것을
            잘못 계산한 수 = (어떤 수 +-var4) * var1 +- var2
        잘못해서 var8를 (더한/뺀) 후 var5을 곱하고 var6를 (더했/뺐)습니다.
            바르게 계산한 수 = (어떤 수 +- var8) * var5 +- var6
         */

        String content_pre_correct_token = name1_token + "에";
        String content_pre_wrong_token = "잘못해서";
        String addmin_var4_token = ProblemTokenStruct.blank_token + var4_token + ProblemTokenStruct.reul_token + ProblemTokenStruct.blank_token + var4_addMin_token + ProblemTokenStruct.blank_token + "후";
        String mult_var1_token = ProblemTokenStruct.blank_token + var1_token + ProblemTokenStruct.eul_token + ProblemTokenStruct.blank_token + mult_token;
        String addmin_var2_token = ProblemTokenStruct.blank_token + var2_token + ProblemTokenStruct.reul_token + ProblemTokenStruct.blank_token + var2_addMin_token;
        String content = "";
        if(isCorrectNumSentence){
            content += content_pre_correct_token;
        } else{
            content += content_pre_wrong_token;
        }
        if(useYear2){
            content += addmin_var4_token;
        }
        if(useMult){
            content += mult_var1_token;
        }
        if(useMult && useAddMinus){
            content += mult_to_am_token;
        }
        if(useAddMinus){
            content += addmin_var2_token;
        }
        content += end_token;

        return content;
    }


}
