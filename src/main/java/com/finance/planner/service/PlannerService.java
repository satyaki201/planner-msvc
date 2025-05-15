package com.finance.planner.service;

//import jakarta.transaction.Transactional;
import com.finance.planner.mapper.ExcelToJSON;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

//@Transactional
@Service
public class PlannerService {

    public void validateExcelAndMakeJson(Workbook workbook) throws Exception {
        List<CompletableFuture<Boolean>> getValidationOfEachSheet=IntStream.range(1,workbook.getNumberOfSheets())
            .mapToObj(i->CompletableFuture.supplyAsync(()->excelValidation(workbook.getSheetAt(i))))
            .toList();

        CompletableFuture<Void> getAllDone = CompletableFuture.allOf(
                getValidationOfEachSheet.toArray(new CompletableFuture[0]));
        getAllDone.join();
        List<Boolean> a=getValidationOfEachSheet.stream().map(CompletableFuture::join).toList();
        for(Boolean i:a) {
            if (!i)
                throw new Exception("Sorry Wrong Excel");
        }
        sendToTopic("my-topic","Hello");
    }

    private boolean excelValidation(Sheet sheet)
    {
        System.out.println(sheet.getSheetName());
        return false;
    }

    private List<Map<String,Map<String, Map<String, Double>>>> excelToEventConversion(Workbook workbook)
    {
        ExcelToJSON excelToJSON=new ExcelToJSON();
        List<CompletableFuture<Map<String,Map<String, Map<String, Double>>>>> getJSONList=IntStream.range(1,workbook.getNumberOfSheets())
                .mapToObj(i->CompletableFuture.supplyAsync(()->excelToJSON.convertSheetToJSON(workbook.getSheetAt(i))))
                .toList();
        CompletableFuture<Void> getAllDone = CompletableFuture.allOf(
                getJSONList.toArray(new CompletableFuture[0]));
        getAllDone.join();
        return getJSONList.stream().map(CompletableFuture::join).toList();
    }

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    public void sendToTopic(String topic, String message) {
        // Send message to the specified topic
        kafkaTemplate.send(topic, message);
        System.out.println("Sent message to topic: " + topic);
    }

}
