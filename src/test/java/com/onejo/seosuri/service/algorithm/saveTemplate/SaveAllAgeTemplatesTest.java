package com.onejo.seosuri.service.algorithm.saveTemplate;

import com.onejo.seosuri.service.algorithm.problem.ProblemValueStruct;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SaveAllAgeTemplatesTest {

    @Test
    void saveAllTemplates() {
        int[] temp = {0, 0};
        ProblemValueStruct problemValueStruct = new ProblemValueStruct();
        //SaveAllAgeTemplates saveAllAgeTemplates = new SaveAllAgeTemplates(temp, problemValueStruct);
        //saveAllAgeTemplates.saveAllTemplates();
        SaveAllUnknownNumTemplates saveAllUnknownNumTemplates = new SaveAllUnknownNumTemplates(temp, problemValueStruct);
        saveAllUnknownNumTemplates.saveAllTemplates();
    }

}