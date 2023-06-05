package com.onejo.seosuri.service;

import com.onejo.seosuri.controller.dto.template.TemplateDto;
import com.onejo.seosuri.domain.classification.Category;
import com.onejo.seosuri.domain.classification.CategoryRepository;
import com.onejo.seosuri.domain.classification.CategoryTitle;
import com.onejo.seosuri.domain.problem.ProblemTemplate;
import com.onejo.seosuri.domain.problem.ProblemTemplateRepository;
import com.onejo.seosuri.domain.testpaper.TestPaper;
import com.onejo.seosuri.exception.common.BusinessException;
import com.onejo.seosuri.exception.common.ErrorCode;
import com.onejo.seosuri.service.algorithm.problem.ProblemValueStruct;
import com.onejo.seosuri.service.algorithm.saveTemplate.SaveAllAgeTemplates;
import com.onejo.seosuri.service.algorithm.saveTemplate.SaveAllTemplates;
import com.onejo.seosuri.service.algorithm.saveTemplate.SaveAllUnknownNumTemplates;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SaveAllTemplateService {
    private final ProblemTemplateRepository problemTemplateRepository;
    private final CategoryRepository categoryRepository;

    @Transactional
    public ProblemTemplate saveOneTemplate(TemplateDto templateDto){
        System.out.println("\n\t\tSTARTED:: saveOneTemplate");
        ProblemTemplate problemTemplate = templateDto.toEntity();
        System.out.println("\t\t저장할 내용 :: ");
        templateDto.printTemplateDto();
        return problemTemplateRepository.save(problemTemplate);
    }

    @Transactional
    public List<ProblemTemplate> runSaveAllAgeTemplate(int start, int end){
        System.out.println("\n\t\tSTARTED:: runSaveAllAgeTemplate\n");

        Optional<Category> opt_category = categoryRepository.findByTitle(CategoryTitle.AGE);   // 여기서 오류 발생 : findByTitle을 콜 할 수 없다고 나옴 (InvocationTargetException)
        if(opt_category.isEmpty()){
            throw new BusinessException(ErrorCode.NO_EXIST_CATEGORY_TITLE);
        }
        Category category = opt_category.get();

        List<ProblemTemplate> res = new ArrayList<>();
        SaveAllAgeTemplates saveAllAgeTemplates = new SaveAllAgeTemplates();
        saveAllAgeTemplates.saveAllTemplates(start, end);
        for(TemplateDto templateDto: saveAllAgeTemplates.templateDtos){
            templateDto.setCategory(category);
            ProblemTemplate saved_problem_template = saveOneTemplate(templateDto);  // save in DB
            //TemplateDto saved_template_dto = new TemplateDto(saved_problem_template);
            res.add(saved_problem_template);
        }

        return res;
    }

    @Transactional
    public List<ProblemTemplate> runSaveAllUnknownNumTemplate(int start, int end) {
        System.out.println("\n\t\tSTARTED:: runSaveAllUnknownNumTemplate\n");

        Optional<Category> opt_category = categoryRepository.findByTitle(CategoryTitle.UNKNOWN_NUM);
        if(opt_category.isEmpty()){
            throw new BusinessException(ErrorCode.NO_EXIST_CATEGORY_TITLE);
        }
        Category category = opt_category.get();

        List<ProblemTemplate> res = new ArrayList<>();
        SaveAllUnknownNumTemplates saveAllUnknownNumTemplates = new SaveAllUnknownNumTemplates();
        saveAllUnknownNumTemplates.saveAllTemplates(start, end);
        for(TemplateDto templateDto: saveAllUnknownNumTemplates.templateDtos){
            templateDto.setCategory(category);
            ProblemTemplate saved_problem_template = saveOneTemplate(templateDto);  // save in DB
            //TemplateDto saved_template_dto = new TemplateDto(saved_problem_template);
            res.add(saved_problem_template);
        }

        return res;
    }

}
