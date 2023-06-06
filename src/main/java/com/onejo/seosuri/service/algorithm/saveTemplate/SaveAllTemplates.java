package com.onejo.seosuri.service.algorithm.saveTemplate;

import com.onejo.seosuri.controller.dto.template.TemplateDto;
import com.onejo.seosuri.service.algorithm.ProblemTokenStruct;
import com.onejo.seosuri.service.algorithm.exprCategory.ExprCategory;
import com.onejo.seosuri.service.algorithm.createTemplate.CreateTemplate;
import com.onejo.seosuri.service.algorithm.ProblemValueStruct;

import java.util.ArrayList;

public abstract class SaveAllTemplates {

    // DB에 저장할 값 모아둔 ArrayList
    public ArrayList<TemplateDto> templateDtos = new ArrayList<>();

    ExprCategory[] possible_Expr_category_ls;    // should be set in the lower classes
    protected int[] category_id_ls = new int[] {};
    protected ProblemValueStruct problemValueStruct = new ProblemValueStruct();
    protected CreateTemplate createTemplate;

    ArrayList<ExprCategory>[] category_ls_ls;

    ArrayList<Integer>[] sentence_category_id_ls_ls;
<<<<<<< HEAD
    int[] target_sentence_category_ls = new int[] { ProblemTokenStruct.CATEGORY_ID_YX, ProblemTokenStruct.CATEGORY_ID_SUM_DIFFERENCE};
=======
    int[] target_sentence_category_ls = new int[] {ProblemTokenStruct.EXPR_CATEGORY_ID_YX, ProblemTokenStruct.EXPR_CATEGORY_ID_SUM_DIFFERENCE};
>>>>>>> feat/template
    ArrayList<Integer>[] var_sign_ls_ls;
    public boolean[][] useBoolean_ls_ls;
    private static final boolean[] target_boolean = new boolean[] {true, false};


    // constructor
    protected SaveAllTemplates(int[] category_id_ls){
        this.category_id_ls = category_id_ls;
    }

    abstract public void saveAllTemplates(int start_template_id, int end_template_id);

    protected void saveInList() {
        //problemValueStruct.printTemplate();   // for debugging
        TemplateDto templateDto = new TemplateDto(problemValueStruct.template_level, problemValueStruct.category,
                problemValueStruct.content_template, problemValueStruct.answer_template, problemValueStruct.explanation_template,
                problemValueStruct.sentence_expr_category_id_ls, problemValueStruct.expr_category_ls, problemValueStruct.constant_var_sign_ls,
                problemValueStruct.useYear1_ls, problemValueStruct.useYear2_ls, problemValueStruct.useMult_ls, problemValueStruct.useAddMinus_ls);
        templateDtos.add(templateDto);
        //templateDto.printTemplateDto();
        System.out.println(templateDto.getContent()+"\n");
    }



    // 순열 메서드(cnt는 선택 횟수)
    public void set_useBoolean_ls_ls(int prob_sentence_num){
        int row_num = (int)Math.pow(2.0f, prob_sentence_num);
        useBoolean_ls_ls = new boolean[row_num][prob_sentence_num];
        for(int i = 0; i < row_num; i++){
            useBoolean_ls_ls[i] = new boolean[prob_sentence_num];
        }
        bool_permutation(0);
    }

    // starts with permutation(0)
    public void bool_permutation(int cnt) {
        int N = useBoolean_ls_ls.length;
        int prob_sentence_num = useBoolean_ls_ls[0].length;
        if (cnt == useBoolean_ls_ls[0].length) {
            return;
        }
        // 대상 집합을 순회하며 숫자를 하나 선택한다.
        for (int i = 0; i < target_boolean.length; i++) {
            // ex) 8개 종류
            // 2개로 나눠 -> 0 ~ N/2-1 : true, N/2 ~ 2N/2-1 : false
            //      cnt = 0     -> 2^1
            // 4개로 나눠 -> 0 ~ N/4-1 : true, N/4 ~ 2N/4-1 : false, 2N/4 ~ 3N/4-1 : true, 3N/4 ~ 4N/4-1 : false
            //      cnt = 1     -> 2^2
            // 8개로 나눠 -> ...
            int offset = N/(int)Math.pow(2, cnt+1);
            for(int j = i * offset; j < N; j += 2 * offset){
                for(int row = j; row < j+offset; row++){
                    useBoolean_ls_ls[row][cnt] = target_boolean[i];
                }
            }
            bool_permutation(cnt + 1);
        }
    }

    public void setSentence_category_id_ls_ls(int prob_sentence_num){
        int row_num = (int)Math.pow(ProblemTokenStruct.SENTENCE_CATEGORY_NUM, prob_sentence_num);
        sentence_category_id_ls_ls = new ArrayList[row_num];   // 모든 순열 리스트
        for(int i = 0; i < row_num; i++){
            sentence_category_id_ls_ls[i] = new ArrayList<Integer>();
        }
        int_permutation(0, sentence_category_id_ls_ls, target_sentence_category_ls, prob_sentence_num);
    }

    protected void setVar_sign_ls_ls(int var_num){
        int row_num = (int)Math.pow(2, var_num);
        var_sign_ls_ls = new ArrayList[row_num];
        for(int i = 0; i < row_num; i++){
            var_sign_ls_ls[i] = new ArrayList<Integer>();
        }
        int_permutation(0, var_sign_ls_ls, new int[] {ProblemTokenStruct.PLUS_SIGN, ProblemTokenStruct.MINUS_SIGN}, var_num);
    }

    protected void setCategory_ls_ls(int prob_sentence_num){
        int row_num = (int)Math.pow(ProblemTokenStruct.SENTENCE_CATEGORY_NUM, prob_sentence_num);
        category_ls_ls = new ArrayList[row_num];   // 모든 순열 리스트
        for(int i = 0; i < row_num; i++){
            category_ls_ls[i] = new ArrayList<ExprCategory>();
        }
        category_permutation(0, category_ls_ls, possible_Expr_category_ls, prob_sentence_num);
    }

    protected ExprCategory[] arrayListToCategoryArray(ArrayList<ExprCategory> exprCategory_ls){
        ExprCategory[] result = new ExprCategory[exprCategory_ls.size()];
        for(int i = 0; i < exprCategory_ls.size(); i++){
            result[i] = exprCategory_ls.get(i);
        }
        return result;
    }

    // n^r개 배열 나옴
    // n = target.length
    public void int_permutation(int cnt, ArrayList<Integer>[] dest, int[] target, int r) {
        // target에서 숫자 골라 중복순열 만들기
        // cnt는 현재 탐색 깊이 (depth)
        int n = target.length;
        int N = (int)Math.pow(n, r);
        if (cnt == r) {
            return;
        }
        // 대상 집합을 순회하며 숫자를 하나 선택한다.
        for (int i = 0; i < target.length; i++) {
            // ex) 8개 종류
            // 2개로 나눠 -> 0 ~ N/2-1 : true, N/2 ~ 2N/2-1 : false
            //      cnt = 0     -> 2^1
            // 4개로 나눠 -> 0 ~ N/4-1 : true, N/4 ~ 2N/4-1 : false, 2N/4 ~ 3N/4-1 : true, 3N/4 ~ 4N/4-1 : false
            //      cnt = 1     -> 2^2
            // 8개로 나눠 -> ...
            int offset = N/(int)Math.pow(n, cnt+1);
            for(int j = i * offset; j < N; j += 2 * offset) {
                for (int row = j; row < j + offset; row++) {
                    dest[row].add(target[i]);
                }
            }
        }
        int_permutation(cnt + 1, dest, target, r);
    }

    public void category_permutation(int cnt, ArrayList<ExprCategory>[] dest, ExprCategory[] target, int r) {
        // target에서 숫자 골라 중복순열 만들기
        // cnt는 현재 탐색 깊이 (depth)
        int n = target.length;
        int N = (int)Math.pow(n, r);
        if (cnt == r) {
            return;
        }
        // 대상 집합을 순회하며 숫자를 하나 선택한다.
        for (int i = 0; i < target.length; i++) {
            // ex) 8개 종류
            // 2개로 나눠 -> 0 ~ N/2-1 : true, N/2 ~ 2N/2-1 : false
            //      cnt = 0     -> 2^1
            // 4개로 나눠 -> 0 ~ N/4-1 : true, N/4 ~ 2N/4-1 : false, 2N/4 ~ 3N/4-1 : true, 3N/4 ~ 4N/4-1 : false
            //      cnt = 1     -> 2^2
            // 8개로 나눠 -> ...
            int offset = N/(int)Math.pow(n, cnt+1);
            for(int j = i * offset; j < N; j += 2 * offset) {
                for (int row = j; row < j + offset; row++) {
                    dest[row].add(target[i]);
                }
            }
        }
        category_permutation(cnt + 1, dest, target, r);
    }

}
