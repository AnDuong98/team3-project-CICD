package com.smartosc.training.controller;


import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.smartosc.training.config.ServicesConfig;
import com.smartosc.training.dto.APIResponse;
import com.smartosc.training.dto.AddressDTO;
import com.smartosc.training.service.RestTemplateService;
import com.smartosc.training.utils.SecurityUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Controller
@RequestMapping(value = "/address")
public class AddressController {

    @Autowired
    RestTemplateService restTemplateService;

    @Autowired
    private ServicesConfig servicesConfig;

    @GetMapping
    public String listAll(Model model) {
        String url = servicesConfig.getHotsName().concat(servicesConfig.getAddressAPI());
        ParameterizedTypeReference<APIResponse<List<AddressDTO>>> responseParameterizedTypeReference = new ParameterizedTypeReference<APIResponse<List<AddressDTO>>>() {
        };
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(SecurityUtil.getJWTToken());
        List<AddressDTO> list = restTemplateService.getSomething(url, HttpMethod.GET, headers, null, responseParameterizedTypeReference);
        model.addAttribute("list", list);
        return "/address/address";
    }

    @GetMapping(value = "/export")
    public String exportExcel() {
        String url = servicesConfig.getHotsName().concat(servicesConfig.getAddressAPI()).concat("/report");
        ParameterizedTypeReference<APIResponse<Object>> responseParameterizedTypeReference = new ParameterizedTypeReference<APIResponse<Object>>() {
        };
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(SecurityUtil.getJWTToken());
        ParameterizedTypeReference<APIResponse<Collection<AddressDTO>>> reference = new ParameterizedTypeReference<APIResponse<Collection<AddressDTO>>>() {
        };
        restTemplateService.getSomething(url, HttpMethod.GET, headers, null, responseParameterizedTypeReference);
        return "redirect:/address";
    }

    @GetMapping(value = "/importExcelFile")
    public String importExcelFileForm() {
        return null;
    }

    @PostMapping(value = "/importExcelFile")
    public String importExcelFile(MultipartFile file) throws IOException {
        String url = servicesConfig.getHotsName().concat(servicesConfig.getAddressAPI());
        String urlImport = servicesConfig.getHotsName().concat(servicesConfig.getAddressAPI()) + "/import";
        ParameterizedTypeReference<APIResponse<AddressDTO>> responseParameterizedTypeReference = new ParameterizedTypeReference<APIResponse<AddressDTO>>() {
        };
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(SecurityUtil.getJWTToken());
        ParameterizedTypeReference<APIResponse<Collection<AddressDTO>>> reference = new ParameterizedTypeReference<APIResponse<Collection<AddressDTO>>>() {
        };
        Collection<AddressDTO> addressDTOs = restTemplateService.getSomething(url, HttpMethod.GET, headers, null, reference);

        List<AddressDTO> list = new ArrayList<>();
        if (file.getOriginalFilename().endsWith(".xlsx")) {
            XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
            XSSFSheet firstSheet = workbook.getSheetAt(0);

            for (int i = 1; i < firstSheet.getPhysicalNumberOfRows() - 1; i++) {
                AddressDTO addressDTO = new AddressDTO();
                XSSFRow row = firstSheet.getRow(i);
                String addressName = row.getCell(1).getStringCellValue();
                AddressDTO addressDTO1 = addressDTOs.stream().filter(a -> addressName.equalsIgnoreCase(a.getAddress())).findAny().orElse(null);
                if (addressDTO1 == null) {
                    addressDTO.setAddress(addressName);
                    addressDTO.setPhone(String.valueOf(row.getCell(2).getNumericCellValue()));
                    addressDTO.setGender(row.getCell(3).getBooleanCellValue());
                    addressDTO.setStatus((int) row.getCell(4).getNumericCellValue());
                    addressDTO.setCreatedDate(row.getCell(5).getDateCellValue());
                    addressDTO.setModifiedDate(row.getCell(6).getDateCellValue());
                    restTemplateService.getSomething(urlImport, HttpMethod.POST, headers, addressDTO, responseParameterizedTypeReference);
                    System.out.println("");
                }
            }
        }
        return "redirect:/address";
    }

    @PostMapping(value = "/importTXT")
    public String importTXT(MultipartFile file) throws IOException {
        String url = servicesConfig.getHotsName().concat(servicesConfig.getAddressAPI()) + "/importTXT";
        ParameterizedTypeReference<APIResponse<AddressDTO>> responseParameterizedTypeReference = new ParameterizedTypeReference<APIResponse<AddressDTO>>() {
        };
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(SecurityUtil.getJWTToken());

        if (file.getOriginalFilename().endsWith(".txt")) {
            try {
                InputStreamReader reader = new InputStreamReader(file.getInputStream());
                CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build();
                CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(csvParser).withSkipLines(1).build();
                List<String[]> rows = csvReader.readAll();
                for (int i = 0; i < rows.size(); i++) {
                    String[] row = rows.get(i);
                    if (row.length > 1) {
                        AddressDTO addressDTO1 = new AddressDTO();
                        addressDTO1.setId(Long.parseLong(row[0]));
                        addressDTO1.setAddress(row[1]);
                        addressDTO1.setPhone(row[2]);
                        addressDTO1.setGender(Boolean.valueOf(row[3]));
                        addressDTO1.setStatus(Integer.valueOf(row[4]));
                        addressDTO1.setCreatedDate(new SimpleDateFormat("MM/dd/yyyy HH:mm").parse(row[5]));
                        addressDTO1.setModifiedDate(new SimpleDateFormat("MM/dd/yyyy HH:mm").parse(row[6]));
                        restTemplateService.getSomething(url, HttpMethod.POST, headers, addressDTO1, responseParameterizedTypeReference);
                    }
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();

            }

        }

        return "redirect:/address";
    }


}
