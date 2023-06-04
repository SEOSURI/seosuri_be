package com.onejo.seosuri.service;

import com.onejo.seosuri.controller.dto.template.TemplateDto;
import com.onejo.seosuri.domain.problem.ProblemTemplate;
import com.onejo.seosuri.domain.problem.ProblemTemplateRepository;
import com.onejo.seosuri.service.algorithm.problem.ProblemValueStruct;
import com.onejo.seosuri.service.algorithm.saveTemplate.SaveAllAgeTemplates;
import com.onejo.seosuri.service.algorithm.saveTemplate.SaveAllTemplates;
import com.onejo.seosuri.service.algorithm.saveTemplate.SaveAllUnknownNumTemplates;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class SaveAllTemplateService {
    private final ProblemTemplateRepository problemTemplateRepository;

    @Transactional
    public ProblemTemplate saveOneTemplate(TemplateDto templateDto){
        ProblemTemplate problemTemplate = templateDto.toEntity();
        return problemTemplateRepository.save(problemTemplate);
    }

    @Transactional
    public List<TemplateDto> runSaveAllAgeTemplate(int start, int end){
        List<TemplateDto> res = new ArrayList<>();
        SaveAllAgeTemplates saveAllAgeTemplates = new SaveAllAgeTemplates();
        saveAllAgeTemplates.saveAllTemplates(start, end);
        for(TemplateDto templateDto: saveAllAgeTemplates.templateDtos){
            ProblemTemplate saved_problem_template = saveOneTemplate(templateDto);
            TemplateDto saved_template_dto = new TemplateDto(saved_problem_template);
            res.add(saved_template_dto);
        }
        return res;
    }

    @Transactional
    public List<TemplateDto> runSaveAllUnknownNumTemplate(int start, int end) {
        List<TemplateDto> res = new ArrayList<>();
        SaveAllUnknownNumTemplates saveAllUnknownNumTemplates = new SaveAllUnknownNumTemplates();
        saveAllUnknownNumTemplates.saveAllTemplates(start, end);
        for(TemplateDto templateDto: saveAllUnknownNumTemplates.templateDtos){
            ProblemTemplate saved_problem_template = saveOneTemplate(templateDto);
            TemplateDto saved_template_dto = new TemplateDto(saved_problem_template);
            res.add(saved_template_dto);
        }
        return res;
    }

}
