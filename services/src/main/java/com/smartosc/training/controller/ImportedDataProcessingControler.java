package com.smartosc.training.controller;

import com.smartosc.training.dto.APIResponse;
import com.smartosc.training.dto.ImportWrapper;
import com.smartosc.training.dto.OrderDetailDTO;
import com.smartosc.training.service.impl.BatchInsertService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@Controller
public class ImportedDataProcessingControler {

    private final int BATCH_SIZE = 100;

    @Autowired
    private BatchInsertService batchInsertService;

    @PostMapping(value = "/insertBatch")
    public ResponseEntity<Object> insertOrderDetailBatch(@RequestBody ImportWrapper importWrapper) {

        try {
            batchInsertService.batchInsert(importWrapper.getOrderDetailDTOS(),BATCH_SIZE);
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }

        return new ResponseEntity<>(new APIResponse<OrderDetailDTO>(HttpStatus.OK.value(), "Insert Batch Order successfully"),
                HttpStatus.OK);
    }
}
