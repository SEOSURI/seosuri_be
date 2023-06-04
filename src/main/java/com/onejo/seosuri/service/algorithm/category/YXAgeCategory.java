package com.onejo.seosuri.service.algorithm.category;

import com.onejo.seosuri.service.algorithm.ProblemTokenStruct;

import java.util.ArrayList;

public class YXAgeCategory extends YXCategory {

    @Override
    public String createSentenceContent(boolean isCorrectNumSentence, int name_var_index1, int name_var_index2,
                                        int ls_index, int var_num_per_sentence,
                                        boolean useYear1, boolean useYear2, boolean useMult, boolean useAddMinus,
                                        int var2_sign, int year1_sign, int year2_sign,
                                        String name_category_token, String name_unit_token, String var34_unit_token,
                                        String after_str_token, String before_str_token) {
        String name_chosa_token = ProblemTokenStruct.ui_token;

        ArrayList<String> content_ls = new ArrayList<>(1);

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

        // content용 토큰
        String mult_str_token = ProblemTokenStruct.bae_token;  // "배 한 것" "를 곱한 것"
        String c_mult_token = name_chosa_token + ProblemTokenStruct.blank_token + var1_token + mult_str_token;  //"의 {var1}배 한 것" "에 {var1}를 곱한 것"
        String c_mult_end_token = ProblemTokenStruct.gwa_token + ProblemTokenStruct.blank_token + ProblemTokenStruct.same_token; //"과 같습니다.";
        String c_mult_end_wa_token = ProblemTokenStruct.wa_token + ProblemTokenStruct.blank_token + ProblemTokenStruct.same_token; //"와 같습니다.";
        String c_pm_token = ProblemTokenStruct.than_token + ProblemTokenStruct.blank_token + var2_token + name_unit_token + ProblemTokenStruct.blank_token;   //"보다 " + var2_token + "살 ";

        // content
        String content="";
        content = name1_add_str_token + name1_with_category_token + ProblemTokenStruct.neun_token + ProblemTokenStruct.blank_token + name2_add_str_token + name2_with_category_token;   // {name1}의 나이는 {name2}의 나이
        if(useMult){
            if(useAddMinus == false){   // 의 {var1}배 한 것과 같습니다.
                content += c_mult_token + c_mult_end_token;
            } else if(var2_sign == ProblemTokenStruct.PLUS_SIGN){    // 의 {var1}배한 것보다 {var2}살 많습니다.
                content += c_mult_token + c_pm_token + ProblemTokenStruct.more_amount_token;
            } else if(var2_sign == ProblemTokenStruct.MINUS_SIGN){   // 의 {var1}배한 것보다 {var2}살 적습니다.
                content += c_mult_token + c_pm_token + ProblemTokenStruct.less_amount_token;
            } else{
                content = "var2_sign value error\n";
            }
        } else{
            if(useAddMinus == false){   // 와 같습니다.
                content += c_mult_end_wa_token;
            } else if(var2_sign == ProblemTokenStruct.PLUS_SIGN){   // 보다 {var2}살 많습니다.
                content += c_pm_token + ProblemTokenStruct.more_amount_token;
            } else if(var2_sign == ProblemTokenStruct.MINUS_SIGN){   // 보다 {var2}살 적습니다.
                content += c_pm_token + ProblemTokenStruct.less_amount_token;
            } else{
                content = "var2_sign value error\n";
            }
        }
        content_ls.add(content);

        content = content_ls.get(0);
        for(int i = 1; i < content_ls.size(); i++){
            content += "\n" + content_ls.get(i);
        }

        return content;
    }

}
