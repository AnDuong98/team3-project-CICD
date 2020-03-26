package com.smartosc.training.export;

import com.smartosc.training.entity.AddressEntity;
import com.smartosc.training.repository.AddressRepository;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.springframework.util.ResourceUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AddressExportService {
    @Value("${report.path}")
    private String reportPath;
    @Autowired
    private AddressRepository addressRepository;

    public String generateReport() {
        List<AddressEntity> listAddress = new ArrayList<>();
        addressRepository.findAll().stream().forEach(a -> listAddress.add(a));
        try {
            File file = ResourceUtils.getFile("classpath:address.jrxml");
            InputStream input = new FileInputStream(file);
            // Compile the Jasper report from .jrxml to .japser
            JasperReport jasperReport = JasperCompileManager.compileReport(input);
            // Compile the Jasper report from .jrxml to .japser
            JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(listAddress);
            // Add parameters
            Map<String, Object> parameters = new HashMap<>();
//            parameters.put("Create By","HuuPhamDang");
            // Fill the report
            String exPath = reportPath + "\\Address.html";
            System.out.println(exPath);
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, source);
            JasperExportManager.exportReportToHtmlFile(jasperPrint, exPath);
            System.out.println("HTML Generate");
            xlsx(jasperPrint);
            System.out.println("Exel Generate");
            return "Report successfully generated @path= " + reportPath;
        } catch (Exception e) {
            return e.getMessage();
        }


    }

    private void xlsx(JasperPrint jasperPrint) throws JRException {
        JRXlsxExporter exporter = new JRXlsxExporter();
        exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
        exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(reportPath + "\\Address.xlsx"));
        SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
        configuration.setOnePagePerSheet(true);
        configuration.setRemoveEmptySpaceBetweenColumns(true);
        configuration.setDetectCellType(true);
        exporter.setConfiguration(configuration);
        exporter.exportReport();
    }


}
