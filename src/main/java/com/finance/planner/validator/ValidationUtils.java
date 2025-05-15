package com.finance.planner.validator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.ss.usermodel.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import static com.finance.planner.utility.AppConstant.columnTypes;

// Validation Rules:
// 1. Max 15 columns and 50 rows allowed.
// 2. Hyperlinks are strictly disallowed.
// 3. No cell should contain more than 20 characters.
// 4. Interest values cannot be negative.
// 5. Coverage period cannot exceed 100 years.
// 6. Disallowed special characters: {}, <>, and unnecessary usage of () unless part of basic allowed formulas.  {To be implemented}
// 7. Only the following formulas are allowed: SUM, *, /, -, and copy-paste of static values.  {To be Implemented}
// 8. Strict type validation as per AppUtils definitions.

public class ValidationUtils {
    private static final Log log = LogFactory.getLog(ValidationUtils.class);

    public boolean validateTypeAndCharacter(Sheet sheet)
    {
        Row rowOne = sheet.getRow(0);
        Map<Integer, String>columnList=new HashMap<>();
        for(int i=0;i<10;i++)
        {
            if(rowOne.getCell(i)!=null)
                columnList.put(i,rowOne.getCell(i).toString().trim());
        }
        for(int i=1;i<50;i++) {
            if(i>sheet.getLastRowNum()) break;
            Row row=sheet.getRow(i);
            if(row!=null) {
                for (var j : columnList.entrySet()) {
                    CellType a= columnTypes(j.getValue());
                    Cell b = row.getCell(j.getKey());
                    if(b!=null) {
                        if (b.getHyperlink() != null)
                            validateHyperLink(b);
                        if (b.getCellType() == CellType.FORMULA)
                            validateFormula(b);
                        if (a != b.getCellType())
                            return false;
                    }

                }
            }
        }
        return true;
    }

    public void validateFormula(Cell cell)
    {
        log.error("Formula Received Not allowed"+cell.getCellFormula());
        throw new IllegalArgumentException("DO NOT USE Formula");
    }

    public boolean validateSize(MultipartFile multipartFile)
    {
        return multipartFile.getSize() <= 1024 * 1024;
    }

    public void validateHyperLink(Cell cell)
    {
        log.error("Hyperlink Received Not allowed"+cell.getCellFormula());
        throw new IllegalArgumentException("DO NOT INSERT Hyperlink");
    }

}
