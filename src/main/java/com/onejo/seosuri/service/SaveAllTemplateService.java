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
    public void runSaveAllAgeTemplate(int start, int end){
        SaveAllAgeTemplates saveAllAgeTemplates = new SaveAllAgeTemplates();
        saveAllAgeTemplates.saveAllTemplates(start, end);
        for(TemplateDto templateDto: saveAllAgeTemplates.templateDtos){
            ProblemTemplate saved_templateDto = saveOneTemplate(templateDto);
        }
    }

    @Transactional
    public void runSaveAllUnknownNumTemplate(int start, int end) {
        SaveAllUnknownNumTemplates saveAllUnknownNumTemplates = new SaveAllUnknownNumTemplates();
        saveAllUnknownNumTemplates.saveAllTemplates(start, end);
        for(TemplateDto templateDto: saveAllUnknownNumTemplates.templateDtos){
            ProblemTemplate saved_templateDto = saveOneTemplate(templateDto);
        }
    }

}
