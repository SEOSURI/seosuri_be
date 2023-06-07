package com.onejo.seosuri.service.algorithm.problem;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculateExprTest {

    @Test
    void calculate() {
        String[] pm = new String[] {"+", "-"};
        String mult = "/";
        for(int var1 = 0; var1 < 100; var1++){
            for(int var2 = 0; var2 < 100; var2++){
                for(int var3 = 0; var3 < 4; var3++){
                    for(String pORm: pm) {
                        if(pORm == "+") {
                            assertEquals(CalculateExpr.calculate(
                                            "(" + String.valueOf(var1) + pORm + String.valueOf(var2) + ")" + "*" + String.valueOf(var3))
                                    , (var1 + var2) * var3);
                        } else if(pORm == "-"){
                            assertEquals(CalculateExpr.calculate(
                                            "(" + String.valueOf(var1) + pORm + String.valueOf(var2) + ")" + "*" + String.valueOf(var3))
                                    , (var1 - var2) * var3);
                        }
                    }
                }
            }
        }
    }

    @Test
    void calculateBracketTest() {
        String p = "+";
        String m = "-";
        String mult = "*";
        String div = "/";
        int var1 = 3;
        int var2 = 9;
        int var3 = 5;
        int var4 = 2;

        String exprStr = String.valueOf(var3)  + "*" + "(" + String.valueOf(var1) + p + String.valueOf(var2) + p + String.valueOf(var4) + ")" + "*" + String.valueOf(var3)  + "*" + String.valueOf(var3);
        int exprInt = var3 * (var1 + var2 + var4) * var3 * var3;
        for(int i = 0; i < 2; i++){
            exprStr = String.valueOf(var3)  + div + "(" + String.valueOf(var1) + m + exprStr + m + String.valueOf(var2) + ")" + "*" + String.valueOf(var3)  + "*" + String.valueOf(var3);
            exprInt = var3 / (var1 - exprInt - var2) * var3 * var3;
            assertEquals(CalculateExpr.calculate(exprStr), exprInt);
        }
    }
}