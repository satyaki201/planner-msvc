package com.finance.planner.controller;

import com.finance.planner.service.PlannerService;
import org.apache.poi.ss.usermodel.*;
import com.finance.planner.api.PlannerApi;
import io.swagger.v3.oas.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/api")
public class PlannerController implements PlannerApi {

    @Autowired
    PlannerService plannerService;

    @Override
    public ResponseEntity<Resource> downloadExcel() {
        return null;
    }

    @Override
    public ResponseEntity<Void> uploadExcel(@Parameter(name = "file", description = "") @RequestPart(value = "file", required = false) MultipartFile file) {
        if (file == null || file.isEmpty()) {
            System.out.println("No file uploaded.");
            return ResponseEntity.badRequest().build();
        }
        try {
            InputStream inputStream = file.getInputStream();
            Workbook workbook = WorkbookFactory.create(inputStream);
            plannerService.validateExcelAndMakeJson(workbook);
            return ResponseEntity.ok().build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
