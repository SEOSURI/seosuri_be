package com.onejo.seosuri.util;

import com.onejo.seosuri.controller.dto.problem.ProbRes;
import com.onejo.seosuri.domain.problem.Problem;

import java.util.Comparator;

public class ProbNumComparatorTestPaper implements Comparator<Problem> {
    @Override
    public int compare(Problem prob1, Problem prob2) {
        if(prob1.getProbNum() > prob2.getProbNum()) {
            return 1;
        }
        else if(prob1.getProbNum()< prob2.getProbNum()){
            return -1;
        }
        else {
            return 0;
        }
    }

}

