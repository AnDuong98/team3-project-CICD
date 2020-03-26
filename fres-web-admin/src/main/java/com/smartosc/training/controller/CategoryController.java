package com.smartosc.training.controller;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import javax.annotation.security.PermitAll;
import com.smartosc.training.service.impl.HandleImportedFile;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.smartosc.training.config.ServicesConfig;
import com.smartosc.training.dto.APIResponse;
import com.smartosc.training.dto.CategoryDTO;
import com.smartosc.training.service.RestTemplateService;
import com.smartosc.training.utils.SecurityUtil;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@RestController
@RequestMapping("/Categories")
public class CategoryController {

	@Autowired
	RestTemplateService categoryRestTemplate;
	
	@Autowired
	private ServicesConfig servicesConfig;
	
	@GetMapping
	@PermitAll
	public String listAll(Model modelMap) {
		String url = servicesConfig.getHotsName().concat(servicesConfig.getCategoryAPI());
		ParameterizedTypeReference<APIResponse<Collection<CategoryDTO>>> reference = new ParameterizedTypeReference<APIResponse<Collection<CategoryDTO>>>() {
		};
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(SecurityUtil.getJWTToken());
		Collection<CategoryDTO> list = categoryRestTemplate.getSomething(url, HttpMethod.GET, headers, null, reference);
		modelMap.addAttribute("listCategory", list);
		return "./categories/index";
	}
	
	@GetMapping(value = "/view/{id}")
	public String updateCategory(Model modelMap, @PathVariable("id") Long id) {
		String url = servicesConfig.getHotsName().concat(servicesConfig.getCategoryAPI()).concat("/") + id;
		ParameterizedTypeReference<APIResponse<CategoryDTO>> reponseType = new ParameterizedTypeReference<APIResponse<CategoryDTO>>() {};
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(SecurityUtil.getJWTToken());
		CategoryDTO categoryDTO = categoryRestTemplate.getSomething(url, HttpMethod.GET, headers, null, reponseType);
		modelMap.addAttribute("category", categoryDTO);
		return "./categories/view";
	}
	
	@PostMapping(value = "/update/{id}")
	public String saveUpdate(Model modelMap, @PathVariable("id") Long id, @ModelAttribute("category") CategoryDTO categoryDTO) {
		categoryDTO.setId(id);
		String url = servicesConfig.getHotsName().concat(servicesConfig.getCategoryAPI()).concat("/") + id;
		ParameterizedTypeReference<APIResponse<CategoryDTO>> reponseType = new ParameterizedTypeReference<APIResponse<CategoryDTO>>() {};
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(SecurityUtil.getJWTToken());
		categoryDTO = categoryRestTemplate.getSomething(url, HttpMethod.PUT, headers, categoryDTO, reponseType);
		modelMap.addAttribute("category", categoryDTO);
		return "redirect:/Categories/";
	}

	@GetMapping("/create")
	public String createProduct() {

		return "./categories/add";
	}
	
	@PostMapping("/save")
	public String save(@ModelAttribute("category") CategoryDTO categoryDTO) {
		String url = servicesConfig.getHotsName().concat(servicesConfig.getCategoryAPI());
		ParameterizedTypeReference<APIResponse<CategoryDTO>> reponseType = new ParameterizedTypeReference<APIResponse<CategoryDTO>>() {
		};
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(SecurityUtil.getJWTToken());
		categoryRestTemplate.getSomething(url, HttpMethod.POST, headers, categoryDTO, reponseType);
		return "redirect:/Categories/";
	}
	
	@GetMapping(value = "/delete/{id}")
	public String delete(@PathVariable("id") Long id) {
		String url = servicesConfig.getHotsName().concat(servicesConfig.getCategoryAPI()).concat("/") + id;
		ParameterizedTypeReference<APIResponse<Object>> reponseType = new ParameterizedTypeReference<APIResponse<Object>>() {};
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(SecurityUtil.getJWTToken());
		categoryRestTemplate.getSomething(url, HttpMethod.DELETE, headers, null, reponseType);
		return "redirect:/Categories/";
	}	

	@GetMapping(value = "/export")
	public String exportExcelFile() throws FileNotFoundException {
		String url = servicesConfig.getHotsName().concat(servicesConfig.getCategoryAPI()).concat("/report");
		ParameterizedTypeReference<APIResponse<Object>> reponseType = new ParameterizedTypeReference<APIResponse<Object>>() {};
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(SecurityUtil.getJWTToken());
		categoryRestTemplate.getSomething(url, HttpMethod.GET, headers, null, reponseType);

		return "redirect:/Categories/";
	}

	@PostMapping(value = "/import")
	public String importExcelFile(MultipartFile file) throws IOException {

		String url = servicesConfig.getHotsName().concat(servicesConfig.getCategoryAPI());
		ParameterizedTypeReference<APIResponse<CategoryDTO>> reponseType = new ParameterizedTypeReference<APIResponse<CategoryDTO>>() {
		};
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(SecurityUtil.getJWTToken());
		ParameterizedTypeReference<APIResponse<Collection<CategoryDTO>>> reference = new ParameterizedTypeReference<APIResponse<Collection<CategoryDTO>>>() {
		};
		Collection<CategoryDTO> categoryDTOS = categoryRestTemplate.getSomething(url, HttpMethod.GET, headers, null, reference);
		List<CategoryDTO> list = new ArrayList<>();
		if (file.getOriginalFilename().endsWith(".xlsx")) {
			XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
			XSSFSheet firstSheet = workbook.getSheetAt(0);

			for (int i = 1; i < firstSheet.getPhysicalNumberOfRows() - 1; i++) {
				CategoryDTO categoryDTO = new CategoryDTO();
				XSSFRow row = firstSheet.getRow(i);
				String categotyName = row.getCell(6).getStringCellValue();
				CategoryDTO categoryDTOcompare = categoryDTOS.stream().filter(c -> categotyName.equalsIgnoreCase(c.getName())).findAny().orElse(null);
				if (categoryDTOcompare == null) {
					categoryDTO.setName(categotyName);
					categoryDTO.setDescription(row.getCell(4).getStringCellValue());
					categoryDTO.setImage(row.getCell(5).getStringCellValue());
					categoryDTO.setStatus((int) row.getCell(3).getNumericCellValue());
					categoryDTO.setCreatedDate(row.getCell(1).getDateCellValue());
					categoryDTO.setModifiedDate(row.getCell(2).getDateCellValue());
					categoryRestTemplate.getSomething(url, HttpMethod.POST, headers, categoryDTO, reponseType);
				}
			}
		}else{
			return "redirect:/Categories?errorE";
		}
		return "redirect:/Categories/";
	}

	@PostMapping(value = "/importTXT")
	public String importTXTFile(MultipartFile file, final RedirectAttributes redirectAttributes) throws IOException {
		String url = servicesConfig.getHotsName().concat(servicesConfig.getCategoryAPI());
		ParameterizedTypeReference<APIResponse<CategoryDTO>> reponseType = new ParameterizedTypeReference<APIResponse<CategoryDTO>>() {
		};
		HttpHeaders headers = new HttpHeaders();
		headers.setBearerAuth(SecurityUtil.getJWTToken());
		ParameterizedTypeReference<APIResponse<Collection<CategoryDTO>>> reference = new ParameterizedTypeReference<APIResponse<Collection<CategoryDTO>>>() {
		};
		Collection<CategoryDTO> categoryDTOS = categoryRestTemplate.getSomething(url, HttpMethod.GET, headers, null, reference);
		if (file.getOriginalFilename().endsWith(".txt")) {
			try {
				InputStreamReader reader = new InputStreamReader(file.getInputStream());
				CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build();
				CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(csvParser).withSkipLines(1).build();
				List<String[]> rows = csvReader.readAll();
				for (int i = 0; i < rows.size(); i++) {
					String[] row = rows.get(i);
					if (row.length > 1) {
						String categotyName = row[6];
						CategoryDTO categoryDTOcompare = categoryDTOS.stream().filter(c -> categotyName.equalsIgnoreCase(c.getName())).findAny().orElse(null);
						if (categoryDTOcompare == null) {
							CategoryDTO categoryDTO = new CategoryDTO();
							categoryDTO.setModifiedDate(new SimpleDateFormat("MM/dd/yyyy HH:mm").parse(row[2]));
							categoryDTO.setCreatedDate(new SimpleDateFormat("MM/dd/yyyy HH:mm").parse(row[1]));
							categoryDTO.setDescription(row[4]);
							categoryDTO.setName(categotyName);
							categoryDTO.setImage(row[5]);
							categoryDTO.setStatus(Integer.parseInt(row[3].trim()));
							categoryRestTemplate.getSomething(url, HttpMethod.POST, headers, categoryDTO, reponseType);
						}
					}
				}
			} catch (IOException  | ParseException e) {
				e.printStackTrace();
			}
		}else{
			return "redirect:/Categories?error";
		}
		return "redirect:/Categories";
	}
}
