package com.onejo.seosuri.service.algorithm;

import org.junit.jupiter.api.Test;

class Elementary5thTest {

    @Test
    void saveUnknownNumProblemTemplate(){
        Elementary5th elementary5th = new Elementary5th();
        elementary5th.saveUnknownNumProblemTemplates();
        //elementary5th.anyNumberProblem(3);
    }

    @Test
    void saveAgeProblemTemplate(){
        Elementary5th elementary5th = new Elementary5th();
        elementary5th.saveAgeProblemTemplates();
    }

    @Test
    void permutation(){
        int prob_sentence_num = 3;
        int var_num_per_sentence = 4;
        int var_num = prob_sentence_num * var_num_per_sentence;
        Elementary5th elementary5th = new Elementary5th();
        //elementary5th.set_useBoolean_ls_ls(prob_sentence_num);
        elementary5th.setSentence_category_id_ls_ls(prob_sentence_num);
        //elementary5th.setVar_sign_ls_ls(var_num);
        for(int i = 0; i < elementary5th.sentence_category_id_ls_ls.length; i++){
            System.out.println(elementary5th.sentence_category_id_ls_ls[i].toString());
        }
    }

    @Test
    void ageProblem() {
        Elementary5th elementary5th = new Elementary5th();
        elementary5th.ageProblem(3);
        elementary5th.printTemplate();
    }

    @Test
    void templateToProblem() {
        Elementary5th elementary5th = new Elementary5th();
        String[] name_ls = new String[] {"어머니", "동생"};
        int[] name_var_ls = new int[] {50, 10};
        int[] var_ls = new int[] {1, 2, 3, 4};
        String content_template = "{var2}년 후 {name0}의 나이는 {var3}년 후 {name1}의 나이의 {var0}배 한 것보다 {var1}살 많습니다.";
        String explanation_template = "\"{var2}년 후 {name0}의 나이는 {var3}년 후 {name1}의 나이의 {var0}배 한 것보다 {var1}살 많습니다.\"라는 문장을 식으로 바꾸면\n" +
                "{var2}년 후 {name0}의 나이 = {var3}년 후 {name1}의 나이 * {var0} + {var1}\n" +
                "가 됩니다.\n" +
                "이제 이 식을 다음과 같은 순서로 풀 수 있습니다.\n" +
                "{var2}년 후 {name0}의 나이\n" +
                "= {name_var0} + {var2}\n" +
                "= [{name_var0}+{var2}]\n" +
                "{var2}년 후 {name0}의 나이 - {var1}\n" +
                "= [{name_var0}+{var2}-{var1}]\n" +
                "{var3}년 후 {name1}의 나이\n" +
                "= [{name_var0}+{var2}-{var1}] / {var0}\n" +
                "= [({name_var0}+{var2}-{var1})/{var0}]\n" +
                "{name1}의 나이\n" +
                "= {var3}년 후 {name1}의 나이 - {var4}\n" +
                "= [({name_var0}-{var2}-{var1})/{var0}] - {var3}\n" +
                "= [({name_var0}-{var2}-{var1})/{var0}-{var3}]";
        String answer_template = "{name_var1}살";
        String[] real_prob = elementary5th.templateToProblem(name_ls, name_var_ls, var_ls, content_template, explanation_template, answer_template);
        System.out.println(real_prob[0]);   // content
        System.out.println(real_prob[1]);   // explanation
        System.out.println(real_prob[2]);   // answer
    }

    String age_category_token = "나이";
    String age_unit_token = "살";
    String year_unit_token = "년";
    String time_after_token = "후";
    String time_before_token = "전";

    @Test
    void create_age_sentence_yx() {
        Elementary5th elementary5th = new Elementary5th();
        int ls_index = 1;
        int var_num_per_sentence = 1;

        String name_category_token = age_category_token; // "의 나이"
        String name_unit_token = age_unit_token;
        String var34_unit_token = year_unit_token;
        String after_str_token = time_after_token;
        String before_str_token = time_before_token;

        boolean[] tf_set = new boolean[] {true, false};
        int case_id = 0;
        // cond_inx
        for(int cond_inx: new int[] {0, 1})
            for(boolean useYear1: tf_set)
                for(boolean useYear2: tf_set)
                    for(boolean useMult: tf_set)
                        for(boolean useAddMinus: tf_set)/*
                            for(int year1_sign: new int[] {0, 1})
                                for(int year2_sign: new int[] {0, 1})
                                    for(int var_sign: new int[] {0, 1})*/ {
                                        case_id++;
                                        int year1_sign=0, year2_sign=0, var_sign=0;
                                        System.out.println("\n\nCASE" + case_id + "\t@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
                                        System.out.println("cond="+cond_inx+" y1="+useYear1+" y2="+useYear2+" m="+useMult+" am="+useAddMinus+" yrs="+year1_sign+year2_sign+" vars="+var_sign);
                                        /*
                                        System.out.println("cond_inx = " + cond_inx);
                                        System.out.println("useYear1 = " + useYear1);
                                        System.out.println("useMult = " + useMult);
                                        System.out.println("useAddMinus = " + useAddMinus);
                                        System.out.println("year1_sign = " + year1_sign);
                                        System.out.println("year2_sign = " + year2_sign);
                                        System.out.println("var_sign = " + var_sign);
                                         */
                                        String content = elementary5th.create_ageProb_content_yx(0, 1, ls_index, var_num_per_sentence, cond_inx,
                                                useYear1, useYear2, useMult, useAddMinus,
                                                var_sign, year1_sign, year2_sign,
                                                name_category_token, name_unit_token, var34_unit_token, after_str_token, before_str_token);
                                        String explanation = elementary5th.create_explanation_yx(content, 0, 1, ls_index, var_num_per_sentence, cond_inx,
                                                useYear1, useYear2, useMult, useAddMinus, true,
                                                var_sign, year1_sign, year2_sign,
                                                name_category_token, name_unit_token, var34_unit_token, after_str_token, before_str_token);
                                        System.out.println(content);
                                        System.out.println(explanation);

                                        /*
                                        String[] sentence_ls = elementary5th.create_sentence_yx(0, 1, ls_index, var_num_per_sentence, cond_inx,
                                                useYear1, useMult, useAddMinus,
                                                var_sign, year1_sign, year2_sign,
                                                name_category_token, name_unit_token, var34_unit_token, after_str_token, before_str_token);*/
                                        /*
                                        System.out.println("content-------------------------");
                                        System.out.println(sentence_ls[0]);
                                        System.out.println("explanation-------------------------------------");*/
                                        //System.out.println(sentence_ls[1]);

                                    }
    }

    @Test
    void create_age_sentence_sum_difference() {
        String name_category_token = age_category_token; // "의 나이"
        String name_unit_token = age_unit_token;

        Elementary5th elementary5th = new Elementary5th();
        int ls_index = 0;
        int var_num_per_sentence = 1;
        for(int cond_inx: new int[] {0, 1})
            for(int sign: new int[] {0, 1}) {
                String[] sentence_ls = elementary5th.create_sentence_sum_diff(ls_index, var_num_per_sentence, cond_inx, sign,
                        name_category_token, name_unit_token);
                System.out.println("content-------------------------");
                System.out.println(sentence_ls[0]);
                System.out.println("explanation-------------------------------------");
                System.out.println(sentence_ls[1]);
                System.out.println("주어진 값 인덱스 = " + cond_inx + "\n\n");
            }
    }
}