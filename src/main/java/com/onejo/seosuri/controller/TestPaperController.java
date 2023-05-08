package com.onejo.seosuri.controller;

import com.onejo.seosuri.controller.dto.testpaper.*;
import com.onejo.seosuri.service.TestPaperService;
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

@Slf4j
@RestController
@Tag(name = "문제지 생성 및 발송 API")
@RequestMapping("/api/testpaper")
@RequiredArgsConstructor
public class TestPaperController {

    private final TestPaperService testPaperService;

    @Operation(summary = "시험지 생성", description = "시험문제 -> html -> pdf 후 DB에 저장")
    @PostMapping("/create")
    public BaseResponse<String> createTestPaper(@RequestBody CreateTestPaperReq createTestPaperReq){
        try{
            return new BaseResponse<>("result");
        } catch(BusinessException e) {
            return new BaseResponse<>(e.getErrorCode());
        }
    }

    @Operation(summary = "시험지 이메일 발송", description = "입력한 이메일로 시험지 전송")
    @GetMapping("/email/{email}")
    public BaseResponse<String> sendTestPaperEmail(@Parameter(description = "이메일")@PathVariable String email){
        try{
            return new BaseResponse<>("result");
        } catch(BusinessException e) {
            return new BaseResponse<>(e.getErrorCode());
        }
    }


}
