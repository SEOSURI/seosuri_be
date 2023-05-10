package com.onejo.seosuri.controller;

import com.onejo.seosuri.controller.dto.problem.*;
import com.onejo.seosuri.service.ProblemService;
import com.onejo.seosuri.exception.common.BusinessException;
import com.onejo.seosuri.exception.common.ErrorCode;
import com.onejo.seosuri.response.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Tag(name = "문제 생성 및 수정 API")
@RequestMapping("/api/problem")
@RequiredArgsConstructor
public class ProblemController {

    private final ProblemService problemService;

    @Operation(summary = "문제 생성", description = "최초 문제 리스트(10문제).\nDB에 생성된 문제 정보 저장.")
    @PostMapping("/create")
    public BaseResponse<List<CreateProbRes>> createProblems(@RequestBody CreateProbReq createProbReq){
        try{
            List<CreateProbRes> problemList = problemService.createProblem(createProbReq.getCategoryTitle(), createProbReq.getLevel());
            return new BaseResponse<>(problemList);
        } catch(BusinessException e) {
            return new BaseResponse<>(e.getErrorCode());
        }
    }

    @Operation(summary = "문제 삭제", description = "DB에서 문제 삭제")
    @DeleteMapping("/delete")
    public BaseResponse<String> deleteProblem(@RequestBody DeleteProbReq deleteProbReq){
        try{
            return new BaseResponse<>("tmp");
        } catch(BusinessException e) {
            return new BaseResponse<>(e.getErrorCode());
        }
    }

    @Operation(summary = "문제 변경", description = "새로운 문제로 변경")
    @PutMapping("/change")
    public BaseResponse<String> change(@RequestBody ChangeProbReq changeProbReq){
        try{
            return new BaseResponse<>("tmp");
        } catch(BusinessException e) {
            return new BaseResponse<>(e.getErrorCode());
        }
    }

    @Operation(summary = "숫자 변경", description = "문제 1개에 대한 정보를 알려주면 랜덤으로 숫자 변경하여 반환")
    @PatchMapping("/change/number")
    public BaseResponse<String> changeNumber(@RequestBody ChangeNumReq changeNumReq){
        try{
            return new BaseResponse<>("tmp");
        } catch(BusinessException e) {
            return new BaseResponse<>(e.getErrorCode());
        }
    }
}
