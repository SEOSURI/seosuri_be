package com.onejo.seosuri.util;

import com.onejo.seosuri.controller.dto.problem.ProbRes;

import java.util.Comparator;

public class ProbNumComparator implements Comparator<ProbRes> {
    @Override
    public int compare(ProbRes prob1, ProbRes prob2) {
        if(prob1.getNum() > prob2.getNum()) {
            return 1;
        }
        else if(prob1.getNum() < prob2.getNum()){
            return -1;
        }
        else {
            return 0;
        }
    }

}

