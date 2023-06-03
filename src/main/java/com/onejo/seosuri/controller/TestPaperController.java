package com.onejo.seosuri.controller;

import com.itextpdf.text.DocumentException;
import com.onejo.seosuri.controller.dto.testpaper.*;
import com.onejo.seosuri.domain.testpaper.TestPaperRepository;
import com.onejo.seosuri.service.TestPaperService;
import com.onejo.seosuri.exception.common.BusinessException;
import com.onejo.seosuri.response.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@Slf4j
@RestController
@Tag(name = "문제지 생성 및 발송 API")
@RequestMapping("/api/testpaper")
@RequiredArgsConstructor
public class TestPaperController {

    private final TestPaperService testPaperService;

    @Operation(summary = "시험지 이메일 발송", description = "입력한 이메일로 시험지 전송")
    @GetMapping("/email")
    public BaseResponse<String> sendTestPaperEmail(@RequestBody EmailDto emailDto){
        try{
            testPaperService.sendEmail(emailDto.getEmail(), emailDto.getTestPaperId());
            return new BaseResponse<>("Email sent successfully");
        } catch(BusinessException e) {
            return new BaseResponse<>(e.getErrorCode());
        }
    }


}
