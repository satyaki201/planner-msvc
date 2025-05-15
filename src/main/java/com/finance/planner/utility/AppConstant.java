package com.finance.planner.utility;

import org.apache.poi.ss.usermodel.CellType;

import java.util.HashMap;

public class AppConstant {

    public static CellType columnTypes (String columnName)
    {
        HashMap <String, CellType>allTypes=new HashMap<>();
        allTypes.put("Current Amount",CellType.NUMERIC);
        allTypes.put("Growth Yearly (%)",CellType.NUMERIC);
        allTypes.put("Jump Growth (%)",CellType.NUMERIC);
        allTypes.put("Frequency Of Jump",CellType.NUMERIC);
        allTypes.put("Monthly Payment",CellType.NUMERIC);
        allTypes.put("Coverage Amount",CellType.NUMERIC);
        allTypes.put("Loan Amount ",CellType.NUMERIC);
        allTypes.put("Interest Rate (%)",CellType.NUMERIC);

        return allTypes.getOrDefault(columnName, CellType._NONE);
    }



}
