package com.smartosc.training.service.impl;

import com.smartosc.training.dto.OrderDTO;
import com.smartosc.training.dto.OrderDetailDTO;
import com.smartosc.training.dto.ProductDTO;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class HandleImportedFile {


    public List<OrderDetailDTO> readExcelData(String filename) throws IOException {

        List<OrderDetailDTO> data = new ArrayList<>();

//        String filename = "E:\\smartosc\\jasper-report\\import\\sample.xlsx";

        try {
            FileInputStream excelFile = new FileInputStream(new File(filename));
            Workbook workbook = new XSSFWorkbook(excelFile);

            Sheet sheet = workbook.getSheetAt(0);
            // skip the first line
            Iterator<Row> iterator = sheet.iterator();
            iterator.next();

            Iterator<Row> itr = sheet.iterator();    //iterating over excel file
            Map<String, Integer> headers = new HashMap<>();
            int line =0;
            while (itr.hasNext()) {
                Row row = itr.next();
                if (line==0){
                    line ++;
                    for (Cell headerCell:row){
                        headers.put(headerCell.getStringCellValue(),headerCell.getColumnIndex());
                    }
                }else {
//                populate value
                    OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
                    long id = (long) row.getCell(headers.get("id")).getNumericCellValue();
                    orderDetailDTO.setId(id);

                    SimpleDateFormat date = new SimpleDateFormat("E MMM dd HH:mm:ss z yyyy");
                    Date createddate = date.parse(String.valueOf(row.getCell(headers.get("createddate")).getDateCellValue()));
                    orderDetailDTO.setCreatedDate(createddate);

                    Date modifieddate = date.parse(String.valueOf(row.getCell(headers.get("modifieddate")).getDateCellValue()));
                    orderDetailDTO.setModifiedDate(modifieddate);

                    orderDetailDTO.setPrice(String.valueOf(row.getCell(headers.get("total")).getNumericCellValue()));
                    orderDetailDTO.setStatus(0);
                    orderDetailDTO.setQuantity(((int) row.getCell(headers.get("quantity")).getNumericCellValue()));

                    OrderDTO orderDTO = new OrderDTO();
                    orderDTO.setId((long) row.getCell(headers.get("orderid")).getNumericCellValue());
                    orderDetailDTO.setOrder(orderDTO);

                    ProductDTO productDTO = new ProductDTO();
                    productDTO.setId((long) row.getCell(headers.get("productid")).getNumericCellValue());
                    orderDetailDTO.setProduct(productDTO);

                    data.add(orderDetailDTO);

                }


            }

            return data;

        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;

    }


    public List<OrderDetailDTO> readTextData(String fileName){

        List<OrderDetailDTO> data = new ArrayList<>();
//        String fileName = "E:\\smartosc\\jasper-report\\import\\sample.txt";

        try {
            OrderDetailDTO orderDetailDTO = new OrderDetailDTO();

            File file = new File(fileName);
            BufferedReader bf = new BufferedReader(new FileReader(file));

            String record = "";

            while ((record = bf.readLine()) != null) {

                //populate
                String[] part = record.split(";");
                orderDetailDTO.setId(Long.parseLong(part[0]));

                SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss aa");
                orderDetailDTO.setCreatedDate(date.parse(part[1]));
                orderDetailDTO.setModifiedDate(date.parse(part[2]));

                OrderDTO orderDTO = new OrderDTO();
                orderDTO.setId(Long.parseLong(part[4]));
                orderDetailDTO.setOrder(orderDTO);

                ProductDTO productDTO = new ProductDTO();
                productDTO.setId(Long.parseLong(part[3]));
                orderDetailDTO.setProduct(productDTO);

                orderDetailDTO.setQuantity(Integer.parseInt(part[5]));

                orderDetailDTO.setPrice(part[6]);

                data.add(orderDetailDTO);
            }

            return data;

        } catch (Exception e) {
            e.printStackTrace();
        }


        return null;
    }

}
