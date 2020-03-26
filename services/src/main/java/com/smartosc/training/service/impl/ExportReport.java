package com.smartosc.training.service.impl;

import com.smartosc.training.dto.ReportDTO;
import com.smartosc.training.entity.OrderDetailEntity;
import com.smartosc.training.repository.CategoryRepository;
import com.smartosc.training.repository.OrderDetailRepository;
import lombok.NoArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@NoArgsConstructor
public class ExportReport {

    private final Logger logger = LoggerFactory.getLogger(ExportReport.class);

    @Value("${report.path}")
    private String reportPath;

    @Autowired
    private OrderDetailRepository orderDetailRepository;

    public String generateReport() {
        List<OrderDetailEntity> orderDetailEntities = new ArrayList<>();

        orderDetailRepository.findByReportField().forEach(e -> orderDetailEntities.add(e));

        List<ReportDTO> reportDTOS = new ArrayList<>();
        orderDetailEntities.forEach(e -> {
            ReportDTO reportDTO = new ReportDTO();
            reportDTO.setId(e.getId());
            reportDTO.setCreatedDate(e.getCreateddate());
            reportDTO.setModifiedDate(e.getModifieddate());
            reportDTO.setName(e.getOrderid().getName());
            reportDTO.setProduct(e.getProduct_id().getName());
            reportDTO.setQuantity(e.getQuantity());
            reportDTO.setPrice(e.getProduct_id().getPrice());
            reportDTO.setTotal(e.getPrice());

            reportDTOS.add(reportDTO);
        });

        try {

            File file = ResourceUtils.getFile("classpath:order.jrxml");
            InputStream inputStream =  new FileInputStream(file);

            JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(reportDTOS);
            Map<String, Object> parameters = new HashMap<>();
            JasperPrint print = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

            logger.info("#Generate Xlsx Report --------------------------");
            exportXlsx(print);

            return reportPath;

        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }

    }

    private void exportXlsx(JasperPrint print) throws JRException {

        String path = generatePath(OrderDetailEntity.class);

        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(print));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(path));

        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        configuration.setOnePagePerSheet(true);
        configuration.setRemoveEmptySpaceBetweenColumns(true);
        configuration.setDetectCellType(true);
        exporter.setConfiguration(configuration);
        exporter.exportReport();

    }

    private String generatePath(Class clazz) {
        return String.format(reportPath + "\\'%s'.xlsx", clazz.getSimpleName());
    }

}
