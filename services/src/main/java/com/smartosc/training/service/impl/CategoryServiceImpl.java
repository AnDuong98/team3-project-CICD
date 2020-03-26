package com.smartosc.training.service.impl;

import java.io.*;
import java.nio.Buffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvException;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.*;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import com.smartosc.training.dto.CategoryDTO;
import com.smartosc.training.entity.CategoryEntity;
import com.smartosc.training.repository.CategoryRepository;
import com.smartosc.training.service.CategoryService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class CategoryServiceImpl implements CategoryService {

	@Autowired
	CategoryRepository repo;

	private ModelMapper map = new ModelMapper();

	@Override
	public void createCategory(CategoryDTO category) {
		CategoryEntity newproduct = map.map(category, CategoryEntity.class);
		newproduct.setStatus(1);
		repo.save(newproduct);
	}

	public void updateCategory(CategoryDTO categoryDTO) {
		Optional<CategoryEntity> cat = repo.findById(categoryDTO.getId());
		if (cat.isPresent()) {
			CategoryEntity categoryEntity = cat.get();
			map.map(categoryDTO, categoryEntity);
			repo.save(categoryEntity);
		}

	}

	@Override
	public void deleteCategory(Long id) {
		repo.deleteById(id);
	}

	@Override
	public List<CategoryDTO> getCategories() {
		List<CategoryEntity> categoryEntity = repo.findAll();
		if (!categoryEntity.isEmpty()) {
			List<CategoryDTO> list = new ArrayList<CategoryDTO>();
			categoryEntity.forEach(cate -> {
				CategoryDTO dto = new CategoryDTO();
				dto = map.map(cate, CategoryDTO.class);
				list.add(dto);
			});
			return list;
		}
		return null;
	}

	@Override
	public CategoryDTO show(Long id) {
		Optional<CategoryEntity> cat = repo.findById(id);
		CategoryDTO categoryDTO = new CategoryDTO();
		if (cat.isPresent()) {
			CategoryEntity categoryEntity = cat.get();
			categoryDTO = map.map(categoryEntity, CategoryDTO.class);
		}
		return categoryDTO;
	}

	private String reportPath;
	@Override
	public String generatedReport() {
		reportPath = "D:\\report";
		List<CategoryEntity> categoryDTOList = new ArrayList<>();
		repo.findAll().stream().forEach(c -> categoryDTOList.add(c));
		try {
			File file = ResourceUtils.getFile("classpath:reportCategory.jrxml");
			InputStream inputStream = new FileInputStream(file);
			// Compile the Jasper report from .jrxml to .japser
			JasperReport jasperReport = JasperCompileManager.compileReport(inputStream);
			// Get your data source
			JRBeanCollectionDataSource source = new JRBeanCollectionDataSource(categoryDTOList);
			// Add parameters
			Map<String, Object> parameters = new HashMap<>();
			parameters.put("createdBy", "JavaHelper.org");
			// Fill the report
			JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, source);
			// Export the report to a PDF file
			JasperExportManager.exportReportToPdfFile(jasperPrint, reportPath + "\\Category.pdf");
			System.out.println("PDF File Generated !!");
			JasperExportManager.exportReportToXmlFile(jasperPrint, reportPath + "\\Category.xml", true);
			System.out.println("XML File Generated !!");
			JasperExportManager.exportReportToHtmlFile(jasperPrint, reportPath + "\\Category.html");
			System.out.println("HTML Generated");
			xlsx(jasperPrint);
			csv(jasperPrint);
			return "Report successfully generated @path= " + reportPath;
		} catch (Exception e) {
			return e.getMessage();
		}
	}

	private void csv(JasperPrint jasperPrint) throws JRException {
		JRCsvExporter exporter = new JRCsvExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleWriterExporterOutput(reportPath + "\\Category.csv"));
		SimpleCsvExporterConfiguration configuration = new SimpleCsvExporterConfiguration();
		configuration.setFieldDelimiter(",");
		exporter.setConfiguration(configuration);
		exporter.exportReport();
	}

		// Ref: https://www.programcreek.com/java-api-examples/?class=net.sf.jasperreports.export.SimpleXlsxReportConfiguration&method=setOnePagePerSheet
	private void xlsx(JasperPrint jasperPrint) throws JRException {
		// Exports a JasperReports document to XLSX format. It has character output type and exports the document to a grid-based layout.
		JRXlsxExporter exporter = new JRXlsxExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(reportPath + "\\Category.xlsx"));
		SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
		configuration.setOnePagePerSheet(true);
		configuration.setRemoveEmptySpaceBetweenColumns(true);
		configuration.setDetectCellType(true);
		exporter.setConfiguration(configuration);
		exporter.exportReport();
	}

	@Override
	public Collection<CategoryDTO> searchByCat(String searchKey) {
		Collection<CategoryDTO> results = new ArrayList<>();
		Collection<CategoryEntity> entities = (List<CategoryEntity>) repo.findByName(searchKey);
		for (CategoryEntity item : entities) {
			CategoryDTO newDTO = map.map(item, CategoryDTO.class);
			results.add(newDTO);
		}
		return results;
	}

	@Transactional
	public Collection<CategoryDTO> importExcelFile(MultipartFile file) throws IOException {

		List<CategoryDTO> list = new ArrayList<>();
		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		XSSFSheet firstSheet = workbook.getSheetAt(0);

		for (int i=1; i<firstSheet.getPhysicalNumberOfRows()-1; i++) {
			CategoryDTO  categoryDTO = new CategoryDTO();
			XSSFRow row = firstSheet.getRow(i);
			categoryDTO.setName(row.getCell(6).getStringCellValue());
			categoryDTO.setDescription(row.getCell(4).getStringCellValue());
			categoryDTO.setImage(row.getCell(5).getStringCellValue());
			categoryDTO.setStatus((int)row.getCell(3).getNumericCellValue());
			categoryDTO.setCreatedDate(row.getCell(1).getDateCellValue());
			categoryDTO.setModifiedDate(row.getCell(2).getDateCellValue());
			list.add(categoryDTO);
		}
//		list.forEach(c->{
//			createCategory(c);
//		});
		list.forEach(this::createCategory);

        return list;
    }

    private void listName(String name){
		List nameList = new ArrayList();
		List<CategoryEntity> categories = repo.findAll();
		List<CategoryDTO> list = new ArrayList<CategoryDTO>();
		categories.forEach(cat -> {
			CategoryDTO dto = new CategoryDTO();
			dto = map.map(cat, CategoryDTO.class);
			nameList.add(dto.getName());
		});
	}

	@Override
	public Collection<CategoryDTO> importTXTFile(MultipartFile file) throws IOException {
		List<CategoryDTO> categoryDTOList = new ArrayList<>();
//		try {
//			BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()));
//			while ((line = reader.readLine()) != null) {
//				String[] lines = line.split("\\t");
//				elements.add(lines);
//			}
//		}catch (IOException e) {
//			e.printStackTrace();
//		}
//		for (int i=1; i< elements.size(); i++) {
//
//		}
		try {
			InputStreamReader reader = new InputStreamReader(file.getInputStream());
			CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build();
			CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(csvParser).withSkipLines(1).build();
			List<String[]> rows = csvReader.readAll();
			for (int i=0; i < rows.size(); i++){
				String [] row = rows.get(i);
				if (row.length>1){
					CategoryDTO categoryDTO =new CategoryDTO();
					categoryDTO.setModifiedDate(new SimpleDateFormat("MM/dd/yyyy HH:mm").parse(row[2]));
					categoryDTO.setCreatedDate(new SimpleDateFormat("MM/dd/yyyy HH:mm").parse(row[1]));
					categoryDTO.setDescription(row[4]);
					categoryDTO.setName(row[6]);
					categoryDTO.setImage(row[5]);
					categoryDTO.setStatus(Integer.parseInt(row[3].trim()));
					categoryDTOList.add(categoryDTO);
				}
			}
		} catch (IOException | CsvException | ParseException e) {
			e.printStackTrace();
		}
		categoryDTOList.forEach(this::createCategory);

		return categoryDTOList;

	}

}
