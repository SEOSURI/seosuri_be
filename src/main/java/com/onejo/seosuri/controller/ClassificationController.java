package com.onejo.seosuri.controller;

import com.onejo.seosuri.controller.dto.classification.*;
import com.onejo.seosuri.controller.dto.problem.ProbRes;
import com.onejo.seosuri.domain.classification.Category;
import com.onejo.seosuri.domain.classification.CategoryTitle;
import com.onejo.seosuri.service.ClassificationService;
import com.onejo.seosuri.exception.common.BusinessException;
import com.onejo.seosuri.exception.common.ErrorCode;
import com.onejo.seosuri.response.BaseResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@RestController
@Tag(name = "문제 분류 API")
@RequestMapping("/api/classification")
@RequiredArgsConstructor
public class ClassificationController {

    private final ClassificationService classificationService;

    @Operation(summary = "문제 유형 분류", description = "사진을 넘겨주면 상위 유형 3개를 제시")
    @PostMapping(value = "", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<List<CategoryResDto>> classification(@RequestParam("file") MultipartFile multipartFile){
        try{
            String ocrRes = classificationService.ocrImage(multipartFile);
            List<CategoryResDto> categoryResDtos = classificationService.classification(ocrRes);
            return new BaseResponse<>(categoryResDtos);
        } catch(BusinessException e) {
            return new BaseResponse<>(e.getErrorCode());
        }
    }


    @Operation(summary = "문제 유형 분류", description = "사진을 넘겨주면 상위 유형 3개를 제시")
    @PostMapping(value = "/ocr", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public BaseResponse<String> imageToText(@RequestParam("file") MultipartFile multipartFile){
        try{
            String ocrRes = classificationService.ocrImage(multipartFile);
            //List<CategoryResDto> categoryResDtos = classificationService.classification(ocrRes);
            //return new BaseResponse<>(categoryResDtos);
            return new BaseResponse<>(ocrRes);
        } catch(BusinessException e) {
            return new BaseResponse<>(e.getErrorCode());
        }
    }



}
