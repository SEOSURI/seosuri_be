package com.onejo.seosuri.service.algorithm;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class Elementary5thTest {

    @Test
    void ageProblem() {
        Elementary5th elementary5th = new Elementary5th();
        elementary5th.ageProblem();
    }


    @Test
    void create_age_sentence_yx() {
        Elementary5th elementary5th = new Elementary5th();
        int ls_index = 0;
        int var_num_per_sentence = 1;

        boolean[] tf_set = new boolean[] {true, false};
        for(int cond_inx: new int[] {0, 1})
            for(boolean useYear: tf_set)
                for(boolean useMult: tf_set)
                    for(boolean useAddMinus: tf_set)
                        for(int sign: new int[] {0, 1}) {
                            String[] sentence_ls = elementary5th.create_age_sentence_yx(ls_index, var_num_per_sentence, cond_inx, useYear, useMult, useAddMinus, sign);
                            System.out.println("content-------------------------");
                            System.out.println(sentence_ls[0]);
                            System.out.println("explanation-------------------------------------");
                            System.out.println(sentence_ls[1]);
                            System.out.println("주어진 값 인덱스 = " + cond_inx + "\n\n");
                        }
    }

    @Test
    void create_age_sentence_sum_difference() {
        Elementary5th elementary5th = new Elementary5th();
        int ls_index = 0;
        int var_num_per_sentence = 1;
        for(int cond_inx: new int[] {0, 1})
            for(int sign: new int[] {0, 1}) {
                String[] sentence_ls = elementary5th.create_age_sentence_sum_difference(ls_index, var_num_per_sentence, cond_inx, sign);
                System.out.println("content-------------------------");
                System.out.println(sentence_ls[0]);
                System.out.println("explanation-------------------------------------");
                System.out.println(sentence_ls[1]);
                System.out.println("주어진 값 인덱스 = " + cond_inx + "\n\n");
            }
    }
}