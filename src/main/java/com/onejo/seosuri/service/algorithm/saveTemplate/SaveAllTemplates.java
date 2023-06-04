package com.onejo.seosuri.service.algorithm.saveTemplate;

import com.onejo.seosuri.domain.problem.ProblemTemplate;
import com.onejo.seosuri.service.algorithm.ProblemTokenStruct;
import com.onejo.seosuri.service.algorithm.exprCategory.ExprCategory;
import com.onejo.seosuri.service.algorithm.createTemplate.CreateTemplate;
import com.onejo.seosuri.service.algorithm.problem.ProblemValueStruct;

import java.util.ArrayList;

public abstract class SaveAllTemplates {
    ExprCategory[] possible_Expr_category_ls;    // should be set in the lower classes
    protected int[] category_id_ls = new int[] {};
    protected ProblemValueStruct problemValueStruct = new ProblemValueStruct();
    protected CreateTemplate createTemplate;

    ArrayList<ExprCategory>[] category_ls_ls;

    ArrayList<Integer>[] sentence_category_id_ls_ls;
    int[] target_sentence_category_ls = new int[] {ProblemTokenStruct.CATEGORY_ID_YX, ProblemTokenStruct.CATEGORY_ID_SUM_DIFFERENCE};
    ArrayList<Integer>[] var_sign_ls_ls;
    public boolean[][] useBoolean_ls_ls;
    private static final boolean[] target_boolean = new boolean[] {true, false};

    protected SaveAllTemplates(int[] category_id_ls, ProblemValueStruct problemValueStruct, CreateTemplate createTemplate){
        this.category_id_ls = category_id_ls;
        this.problemValueStruct = problemValueStruct;
        this.createTemplate = createTemplate;
    }

    abstract public void saveAllTemplates();

    protected void saveInDB() {
        problemValueStruct.printTemplate();
        //DB에 저장 - 다음 값들은 위에서 저장됨 -> 이제 DB에 저장해보자!!!
        /*
        problemValueStruct.content_template = "내용";
        problemValueStruct.explanation_template = "설명";
        problemValueStruct.answer_template = "답";

        problemValueStruct.sentence_category_id_ls = new int[prob_sentence_num];    // 각 상황문장이 어떤 유형의 문장인지를 저장한 배열
        problemValueStruct.category_ls = new Category[prob_sentence_num];
        problemValueStruct.var_sign_ls = new int[constant_var_num];    // DB에서 가져오기!!!
        problemValueStruct.useYear1_ls = new boolean[prob_sentence_num];   // DB에서 가져오기!!!
        problemValueStruct.useYear2_ls = new boolean[prob_sentence_num];   // DB에서 가져오기!!!
        problemValueStruct.useMult_ls = new boolean[prob_sentence_num];   // DB에서 가져오기!!!
        problemValueStruct.useAddMinus_ls = new boolean[prob_sentence_num];   // DB에서 가져오기!!! // entire addminus should be false

        problemValueStruct.variant_var_min_value_ls = new int[name_var_num];
        problemValueStruct.variant_var_max_value_ls = new int[name_var_num];
        problemValueStruct.variant_var_string_ls = new String[name_var_num];

        */

        ProblemTemplate problemTemplate = new ProblemTemplate();

        //Long id = template_id
        //Category category = ???
        //Long category_id
        //SmallUnit smallUnit
        //CategoryTitle title
        //List<TestPaper> testPapers
        //List<ProblemTemplate> problemTemplates
        //String content = varElementary5th.content_template;
        //String level = prob_sentence_num -> '상', '중', '하'
        //String explanation = varElementary5th.explanation_template;
        //String answer = varElementary5th.answer_template;
        //String sentenceCategoryList = varElementary5th.sentence_category_id_ls;
        //List<Problem> problems = ????

        // 다음은 DB에 column 생성한 후 저장해야
        //varElementary5th.var_sign_ls;
        //varElementary5th.useYear1_ls;
        //varElementary5th.useMult_ls;
        //varElementary5th.useAddMinus_ls;

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
