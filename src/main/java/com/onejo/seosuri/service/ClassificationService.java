package com.onejo.seosuri.service;

import com.onejo.seosuri.ai.orc.ImageToText;
import com.onejo.seosuri.controller.dto.classification.CategoryResDto;
import com.onejo.seosuri.domain.classification.CategoryTitle;
import com.onejo.seosuri.exception.common.BusinessException;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ClassificationService {

    @Transactional
    public String ocrImage(String image_filename) throws BusinessException {
        ImageToText imageToText = new ImageToText();
        String ocr_res_str = null;
        String filePath = image_filename;
        /*
        try {
            InputStream tempInputStream = getClass().getResourceAsStream("/image/"+image_filename);
            StringWriter writer = new StringWriter();
            IOUtils.copy(tempInputStream, writer, "UTF-8");
            //filePath = writer.toString();
            //filePath = IOUtils.toString(getClass().getResourceAsStream("/image/"+image_filename), "UTF-8");
            System.out.println("\nFILEPATH:: "+filePath+"\n");
        } catch (IOException e) {
            throw new BusinessException(ErrorCode.NO_EXIST_FILE);
        }

         */
        ocr_res_str = imageToText.detectText(filePath);
        return ocr_res_str;
    }

    @Transactional
    public List<CategoryResDto> classification(String ocr_res_str){
        List<CategoryResDto> res = new ArrayList<>();

        // bert로 분류
        CategoryResDto ageCategoryResDto = new CategoryResDto(CategoryTitle.AGE);
        CategoryResDto unknownNumCategoryResDto = new CategoryResDto(CategoryTitle.UNKNOWN_NUM);
        CategoryResDto colorTapeCategoryResDto = new CategoryResDto(CategoryTitle.COLOR_TAPE);
        CategoryResDto geometryCategoryResDto = new CategoryResDto(CategoryTitle.GEOMETRY_APP_CALC);


        List<CategoryResDto> elseRes = new ArrayList<>();
        elseRes.add(unknownNumCategoryResDto);
        elseRes.add(colorTapeCategoryResDto);
        elseRes.add(geometryCategoryResDto);
        Random random = new Random();
        random.setSeed(System.currentTimeMillis());
        int first = random.nextInt(3);  // 0~2
        int second = (first + random.nextInt(2) + 1) % 3;  // 0~1
        int third = 3 - first - second;


        res.add(ageCategoryResDto);
        res.add(elseRes.get(first));
        res.add(elseRes.get(second));
        res.add(elseRes.get(third));

        return res;
    }

}
