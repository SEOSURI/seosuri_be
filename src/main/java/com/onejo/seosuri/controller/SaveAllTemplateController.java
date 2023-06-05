package com.onejo.seosuri.controller;

import com.onejo.seosuri.controller.dto.problem.CreateProbReq;
import com.onejo.seosuri.controller.dto.problem.ProbRes;
import com.onejo.seosuri.controller.dto.template.SaveTemplateReq;
import com.onejo.seosuri.controller.dto.template.TemplateDto;
import com.onejo.seosuri.exception.common.BusinessException;
import com.onejo.seosuri.response.BaseResponse;
import com.onejo.seosuri.service.SaveAllTemplateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.*;

import java.util.List;


@Slf4j
@RestController
@Tag(name = "템플릿 DB 저장 API")
@RequestMapping("/api/template")
@RequiredArgsConstructor
public class SaveAllTemplateController {

    private final SaveAllTemplateService saveAllTemplateService;

    @Operation(summary = "나이 문제 템플릿 DB 저장", description = "최초 템플릿 DB 데이터.\n나이 유형 문제 템플릿 정보 저장.")
    @PatchMapping("/save/age")
    public BaseResponse<List<TemplateDto>> saveAllAgeTemplates(@RequestBody SaveTemplateReq saveTemplateReq){
        try{
            List<TemplateDto> problemList = saveAllTemplateService.runSaveAllAgeTemplate(saveTemplateReq.getStart_template_id(), saveTemplateReq.getEnd_template_id());
            return new BaseResponse<>(problemList);
        } catch(BusinessException e) {
            return new BaseResponse<>(e.getErrorCode());
        }
    }

    @Operation(summary = "어떤 수 문제 템플릿 DB 저장", description = "최초 템플릿 DB 데이터.\n어떤 수 유형 문제 템플릿 정보 저장.")
    @PatchMapping("/save/unknown_num")
    public BaseResponse<List<TemplateDto>> saveAllUnknownNumTemplates(@RequestBody SaveTemplateReq saveTemplateReq){
        try{
            List<TemplateDto> problemList = saveAllTemplateService.runSaveAllUnknownNumTemplate(saveTemplateReq.getStart_template_id(), saveTemplateReq.getEnd_template_id());
            return new BaseResponse<>(problemList);
        } catch(BusinessException e) {
            return new BaseResponse<>(e.getErrorCode());
        }
    }

}
