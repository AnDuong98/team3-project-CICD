package com.smartosc.training.controller;

import com.smartosc.training.config.ServicesConfig;
import com.smartosc.training.dto.APIResponse;
import com.smartosc.training.dto.ImportWrapper;
import com.smartosc.training.dto.OrderDetailDTO;
import com.smartosc.training.service.RestTemplateService;
import com.smartosc.training.service.impl.HandleImportedFile;
import com.smartosc.training.utils.SecurityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@RestController
public class ImportOrderController {

    @Autowired
    private ServicesConfig servicesConfig;

    @Autowired
    private RestTemplateService restTemplate;

    @Autowired
    private HandleImportedFile handleImportedFile;


    @PostMapping(value = "/insert")
    public String insertBatchOrderXLSX(/*String filename*/) throws IOException {

//        String fileName = "E:\\smartosc\\jasper-report\\import\\sample.txt";
        String fileName = "E:\\smartosc\\jasper-report\\import\\sample.xlsx";

        String extension = fileName.substring(fileName.lastIndexOf("."));
        List<OrderDetailDTO> orderDetailDTOS = new ArrayList<>();

        if (extension.equals("txt")){
            orderDetailDTOS = handleImportedFile.readExcelData(fileName);
        } else {
            orderDetailDTOS = handleImportedFile.readTextData(fileName);
        }

        ImportWrapper importWrapper = new ImportWrapper();
        importWrapper.setOrderDetailDTOS(orderDetailDTOS);

        String url = servicesConfig.getHotsName().concat(servicesConfig.getInsertBatchAPI());
        ParameterizedTypeReference<APIResponse<ImportWrapper>> reponseType = new ParameterizedTypeReference<APIResponse<ImportWrapper>>() {};
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(SecurityUtil.getJWTToken());
        headers.setContentType(MediaType.APPLICATION_JSON);
        restTemplate.getSomething(url, HttpMethod.POST, headers, importWrapper, reponseType);
        return "inserted";
    }




}
