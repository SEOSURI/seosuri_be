package com.onejo.seosuri.service;

import com.onejo.seosuri.controller.dto.template.TemplateDto;
import com.onejo.seosuri.domain.classification.CategoryRepository;
import com.onejo.seosuri.domain.problem.ProbWordRepository;
import com.onejo.seosuri.domain.problem.ProblemTemplate;
import com.onejo.seosuri.domain.problem.ProblemTemplateRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TemplateService {
    private final ProblemTemplateRepository problemTemplateRepository;
    private final CategoryRepository categoryRepository;
    private final ProbWordRepository probWordRepository;

    public ProblemTemplate saveTemplate(TemplateDto templateDto){
        ProblemTemplate problemTemplate = templateDto.toEntity();
        return problemTemplateRepository.save(problemTemplate);
    }


}
