package com.onejo.seosuri.service.algorithm.exprCategory;

import java.util.Random;
import java.util.concurrent.TimeoutException;

public abstract class ExprCategory {
    static Random random = new Random();

    @Override
    abstract public String toString();

    abstract public String createTemplateSentenceContent(boolean isCorrectNumSentence, int name_var_index1, int name_var_index2,
                                                         int ls_index, int var_num_per_sentence,
                                                         boolean useYear1, boolean useYear2, boolean useMult, boolean useAddMinus,
                                                         int var2_sign, int year1_sign, int year2_sign,
                                                         String name_category_token, String name_unit_token, String var34_unit_token,
                                                         String after_str_token, String before_str_token);

    abstract public String createTemplateSentenceExplanation(String content, int name_var_index1, int name_var_index2,
                                                             int ls_index, int var_num_per_sentence, int cond_inx,
                                                             boolean useYear1, boolean useYear2, boolean useMult, boolean useAddMinus,
                                                             int var2_sign, int year1_sign, int year2_sign,
                                                             String name_category_token, String name_unit_token, String var34_unit_token, String after_str_token, String before_str_token);

    // 숫자 값 뽑기
    abstract public int[] getRandomValue(int given_name_var2, int var_sign, int year1_sign, int year2_sign,
                                         int name_var1_min_value, int name_var1_max_value,
                                         int var1_min_value, int var1_max_value,
                                         int var2_min_value, int var2_max_value,
                                         int var3_min_value, int var3_max_value,
                                         int var4_min_value, int var4_max_value,
                                         boolean useYear1, boolean useYear2, boolean useAddMinus, boolean useMult)
            throws TimeoutException;

    // random int 값 뽑기 - getRandomValue()에서 호출해서 사용
    public static int getRandomIntValue(int min_value, int max_value){

        if(max_value < min_value){
            System.out.println("ERROR:: getRandomIntValue() : min > max now... should be min <= max");
            return -1;
        } else if(max_value == min_value){
            System.out.println("그거 아니..? 최소 최대 같더라 ");
            return min_value;
        } else{
            int ret = random.nextInt(max_value-min_value+1) + min_value;
            System.out.println("알려줘 나의 명예 랜덤값: " + ret);
            return ret;
        }
    }


}
