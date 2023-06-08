package com.onejo.seosuri.service;

import com.onejo.seosuri.ai.orc.ImageToText;
import com.onejo.seosuri.controller.dto.classification.CategoryResDto;
import com.onejo.seosuri.domain.classification.Category;
import com.onejo.seosuri.domain.classification.CategoryTitle;
import com.onejo.seosuri.exception.common.BusinessException;
import com.onejo.seosuri.exception.common.ErrorCode;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;

@Service
public class ClassificationService {

    @Transactional
    public String ocrImage(MultipartFile multipartFile) throws BusinessException {
        try {
            File file = convert(multipartFile);
            ImageToText imageToText = new ImageToText();
            String ocr_res_str = imageToText.detectText(file);
            return ocr_res_str;
        } catch(IllegalStateException e){
            throw new BusinessException(ErrorCode.MULTIPARTFILE_CONVERT_ERROR);
        }
    }

    public File convert(MultipartFile file)
    {
        File convFile = new File(file.getOriginalFilename());
        try {
            convFile.createNewFile();
            FileOutputStream fos = new FileOutputStream(convFile);
            fos.write(file.getBytes());
            fos.close();
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.FILE_IO_ERROR);
        }
        return convFile;
    }

    @Transactional
    public List<CategoryResDto> classification(String ocr_res_str){
        List<CategoryResDto> res = new ArrayList<>();

        // bert로 분류
        CategoryResDto ageCategoryResDto = new CategoryResDto(CategoryTitle.AGE.getCategoryName());
        CategoryResDto unknownNumCategoryResDto = new CategoryResDto(CategoryTitle.UNKNOWN_NUM.getCategoryName());
        CategoryResDto colorTapeCategoryResDto = new CategoryResDto(CategoryTitle.COLOR_TAPE.getCategoryName());
        CategoryResDto geometryCategoryResDto = new CategoryResDto(CategoryTitle.GEOMETRY_APP_CALC.getCategoryName());

        String[] age_str_ls = new String[] {"나이", "연세", "어머니", "살", "아버지", "나", "동생"};
        String[] unknown_str_ls = new String[] {"어떤 수", "바르게 계산한", "잘못 계산한", "잘못하여", "잘못 계산", "제대로 계산"};
        String[] geometry_str_ls = new String[] {"삼각형", "사각형", "오각형", "직사각형", "정사각형", "둘레", "길이"};
        String[] colortape_str_ls = new String[] {"색테이프", "색 테이프", "테이프의 길이", "길이", "테이프"};

        int age_count = calcCount(age_str_ls, ocr_res_str);
        int unknown_count = calcCount(unknown_str_ls, ocr_res_str);
        int geometry_count = calcCount(geometry_str_ls, ocr_res_str);
        int colortape_count = calcCount(colortape_str_ls, ocr_res_str);

        CategoryCount ageObj = new CategoryCount(age_count, true, ageCategoryResDto);
        CategoryCount unknownObj = new CategoryCount(unknown_count, false, unknownNumCategoryResDto);
        CategoryCount geometryObj = new CategoryCount(geometry_count, false, geometryCategoryResDto);
        CategoryCount colortapeObj = new CategoryCount(colortape_count, false, colorTapeCategoryResDto);

        CategoryCount[] categoryCounts = new CategoryCount[]{ageObj, unknownObj, geometryObj, colortapeObj};
        Arrays.sort(categoryCounts);

        for(CategoryCount item: categoryCounts){
            res.add(item.getCategoryResDto());
        }
        return res;
    }

    private int calcCount(String[] target, String str){
        int count = 0;
        for(String item: target){
            if(str.contains(item)){
                count++;
            }
        }
        return count;
    }


}
