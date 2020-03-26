package com.smartosc.training.controller;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.annotation.security.PermitAll;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smartosc.training.config.ServicesConfig;
import com.smartosc.training.dto.APIResponse;
import com.smartosc.training.dto.CategoryDTO;
import com.smartosc.training.dto.Pagination;
import com.smartosc.training.dto.ProductDTO;
import com.smartosc.training.service.RestTemplateService;
import com.smartosc.training.utils.SecurityUtil;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
@RequestMapping("/Product")
public class ProductController {

    @Autowired
    RestTemplateService productRestTemplate;
    @Autowired
    private ServicesConfig servicesConfig;

    @GetMapping("/report")
    public String empReport() {
        String url = servicesConfig.getHotsName().concat(servicesConfig.getProductAPI());
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(SecurityUtil.getJWTToken());
        productRestTemplate.getSomething("http://localhost:8004/products/report", HttpMethod.GET, headers, "", new ParameterizedTypeReference<APIResponse<String>>() {
        });
        return "redirect:/Product";
    }

    @PostMapping("/upload")
    public ModelAndView uploadFile(@RequestParam("file") MultipartFile file){
        String fileName=this.storeFile(file);
        ModelAndView mav = new ModelAndView("redirect:/Product");
        try{

            if(file.getOriginalFilename().endsWith(".xlsx")){
                this.saveFileImport(file);
            }
            if(file.getOriginalFilename().endsWith(".txt")){
                this.saveFileTXTImport(file);
            }else{
                mav.addObject("mess","file cant read");
            }

        }catch(Exception e){
            e.printStackTrace();
        }

        String fileDownloadUri=ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/downloadFile/")
                .path(fileName)
                .toUriString();

        return mav;
    }

    public String saveFileTXTImport(MultipartFile file) throws Exception {
        BufferedReader br;
        String url=servicesConfig.getHotsName().concat(servicesConfig.getProductAPI());
        ParameterizedTypeReference<APIResponse<ProductDTO>>reponseType=new ParameterizedTypeReference<APIResponse<ProductDTO>>(){
        };
        HttpHeaders headers=new HttpHeaders();
        headers.setBearerAuth(SecurityUtil.getJWTToken());
        List<String> result = new ArrayList<>();
        try {

            String line;
            InputStream is = file.getInputStream();
            br = new BufferedReader(new InputStreamReader(is));
            while ((line = br.readLine()) != null) {
                result.add(line);
            }

        } catch (IOException e) {
            System.err.println(e.getMessage());
        }
        if(!result.isEmpty()) {
            for (int i = 1; i < result.size(); i++) {
                ProductDTO item=new ProductDTO();
                String[] data = result.get(i).split(";");
                item.setDescription(data[3]);
                item.setImage(data[4]);
                item.setName(data[5]);
                item.setPrice(data[6]);
                item.setCreatedDate(new SimpleDateFormat("dd/MM/yyyy").parse(data[1]));
                item.setModifiedDate(new SimpleDateFormat("dd/MM/yyyy").parse(data[2]));
                productRestTemplate.getSomething(url,HttpMethod.POST,headers,item,reponseType);
            }
        }

        return"hahahah";
    }

    public String saveFileImport(MultipartFile file)throws Exception{
        Path resourceDirectory=Paths.get("fres-web-admin","src","main","resources","fileimport");
        String absolutePath=resourceDirectory.toFile().getAbsolutePath()+"\\Employee.xlsx";
        FileInputStream Stream=(FileInputStream)file.getInputStream();
        XSSFWorkbook wb=new XSSFWorkbook(Stream);
        XSSFSheet sheet=wb.getSheetAt(0);

        String url=servicesConfig.getHotsName().concat(servicesConfig.getProductAPI());
        ParameterizedTypeReference<APIResponse<ProductDTO>>reponseType=new ParameterizedTypeReference<APIResponse<ProductDTO>>(){};
        HttpHeaders headers=new HttpHeaders();
        headers.setBearerAuth(SecurityUtil.getJWTToken());

        //XSSFRow row;
        int numbrow=sheet.getPhysicalNumberOfRows();

        FormulaEvaluator evaluator=wb.getCreationHelper().createFormulaEvaluator();

        for(int i=1;i<numbrow; i++){
            ProductDTO item=new ProductDTO();
            item.setStatus(1);
            for(int j=0;j<sheet.getRow(i).getPhysicalNumberOfCells();j++){
                XSSFCell cell=sheet.getRow(i).getCell(j);
                if(cell!=null){
                    switch(j){
                        case 3:
                            item.setDescription(sheet.getRow(i).getCell(j).getStringCellValue());
                            break;
                        case 4:
                            item.setImage(sheet.getRow(i).getCell(j).getStringCellValue());
                            break;
                        case 5:
                            item.setName(sheet.getRow(i).getCell(j).getStringCellValue());
                            break;
                        case 6:
                            item.setPrice(String.valueOf(sheet.getRow(i).getCell(j).getNumericCellValue()));
                            break;
                        case 1:
                            item.setCreatedDate(sheet.getRow(i).getCell(j).getDateCellValue());
                            break;
                        case 2:
                            item.setModifiedDate(sheet.getRow(i).getCell(j).getDateCellValue());
                            break;
                    }
                }
        }
        productRestTemplate.getSomething(url,HttpMethod.POST,headers,item,reponseType);
        }
        Stream.close();
        return"redirect:/Product";
        }

public String storeFile(MultipartFile file){
        Path resourceDirectory=Paths.get("fres-web-admin","src","main","resources","fileimport");
        String absolutePath=resourceDirectory.toFile().getAbsolutePath();


        Path fileStorageLocation=Paths.get(absolutePath).toAbsolutePath().normalize();

        // Normalize file name
        String fileName=StringUtils.cleanPath(file.getOriginalFilename());

        try{
        // Check if the file's name contains invalid characters
        if(fileName.contains("..")){
        System.out.println("Sorry! Filename contains invalid path sequence "+fileName);
        }

        // Copy file to the target location (Replacing existing file with the same name)
        Path targetLocation=fileStorageLocation.resolve(fileName);
        Files.copy(file.getInputStream(),targetLocation,StandardCopyOption.REPLACE_EXISTING);

        return fileName;
        }catch(IOException ex){
        return null;
        }
        }

@GetMapping
@PermitAll
public String listAll(Model modelMap){
        String url=servicesConfig.getHotsName().concat(servicesConfig.getProductAPI());
        ParameterizedTypeReference<APIResponse<List<ProductDTO>>>reponseType=new ParameterizedTypeReference<APIResponse<List<ProductDTO>>>(){
        };
        HttpHeaders headers=new HttpHeaders();
        headers.setBearerAuth(SecurityUtil.getJWTToken());
        List<ProductDTO> list=productRestTemplate.getSomething(url,HttpMethod.GET,headers,null,reponseType);
        modelMap.addAttribute("listproduct",list);
        return"./products/index";
        }

@GetMapping(value = "/view/{id}")
public String show(Model modelMap,@PathVariable("id") String id){
        String url=servicesConfig.getHotsName().concat(servicesConfig.getProductAPI()).concat("/").concat(id);
        ParameterizedTypeReference<APIResponse<ProductDTO>>reponseType=new ParameterizedTypeReference<APIResponse<ProductDTO>>(){
        };
        HttpHeaders headers=new HttpHeaders();
        headers.setBearerAuth(SecurityUtil.getJWTToken());
        ProductDTO product=productRestTemplate.getSomething(url,HttpMethod.GET,headers,null,reponseType);
        modelMap.addAttribute("product",product);
        return"./products/product-view";
        }

@GetMapping("/create")
public String createProduct(@ModelAttribute("product") ProductDTO product,ModelMap modelMap){
        String url=servicesConfig.getHotsName().concat(servicesConfig.getCategoryAPI());
        ParameterizedTypeReference<APIResponse<List<CategoryDTO>>>reponseType=new ParameterizedTypeReference<APIResponse<List<CategoryDTO>>>(){
        };
        HttpHeaders headers=new HttpHeaders();
        headers.setBearerAuth(SecurityUtil.getJWTToken());
        List<CategoryDTO> list=productRestTemplate.getSomething(url,HttpMethod.GET,headers,null,reponseType);
        modelMap.addAttribute("listCategory",list);
        modelMap.addAttribute("product",product);

        return"./products/product-add";

        }

@PostMapping("/save")
public String save(@ModelAttribute("product") ProductDTO product,@RequestParam("listCategory") List<Long> list){
        String url=servicesConfig.getHotsName().concat(servicesConfig.getProductAPI());
        ParameterizedTypeReference<APIResponse<ProductDTO>>reponseType=new ParameterizedTypeReference<APIResponse<ProductDTO>>(){
        };
        HttpHeaders headers=new HttpHeaders();
        headers.setBearerAuth(SecurityUtil.getJWTToken());

        List<CategoryDTO> dtos=new ArrayList<>();
        list.forEach(d->{
        CategoryDTO dto=new CategoryDTO();
        dto.setId(d);
        dtos.add(dto);
        product.setCategoryDTOs(dtos);

        });
        productRestTemplate.getSomething(url,HttpMethod.POST,headers,product,reponseType);

        return"redirect:/Product/";
        }

@GetMapping(value = "/update/{id}")
public String updateProduct(Model modelMap,@PathVariable("id") Long id){
        String url=servicesConfig.getHotsName().concat(servicesConfig.getProductAPI()).concat("/")+id;
        ParameterizedTypeReference<APIResponse<ProductDTO>>reponseType=new ParameterizedTypeReference<APIResponse<ProductDTO>>(){
        };
        String urlCat=servicesConfig.getHotsName().concat(servicesConfig.getCategoryAPI());
        ParameterizedTypeReference<APIResponse<List<CategoryDTO>>>reponseCat=new ParameterizedTypeReference<APIResponse<List<CategoryDTO>>>(){
        };
        HttpHeaders headers=new HttpHeaders();
        headers.setBearerAuth(SecurityUtil.getJWTToken());

        ProductDTO product=productRestTemplate.getSomething(url,HttpMethod.GET,headers,null,reponseType);
        List<CategoryDTO> listCat=productRestTemplate.getSomething(urlCat,HttpMethod.GET,headers,null,reponseCat);

        List<CategoryDTO> selectedCat=new ArrayList<>();
        selectedCat=product.getCategoryDTOs();
        Set<Long> selectedPartsLongSet=selectedCat.stream().map(CategoryDTO::getId).collect(Collectors.toSet());
        modelMap.addAttribute("selectedPartsLongSet",selectedPartsLongSet);
        modelMap.addAttribute("listCat",listCat);
        modelMap.addAttribute("selectedCat",selectedCat);
        modelMap.addAttribute("product",product);
        return"./products/product-edit";
        }

@PostMapping(value = "/update/{id}")
public String saveUpdate(Model modelMap,@PathVariable("id") Long id,@ModelAttribute("product") ProductDTO product,
@RequestParam("listCat") List<Long> list){

        String url=servicesConfig.getHotsName().concat(servicesConfig.getProductAPI()).concat("/")+id;
        ParameterizedTypeReference<APIResponse<ProductDTO>>reponseType=new ParameterizedTypeReference<APIResponse<ProductDTO>>(){
        };
        HttpHeaders headers=new HttpHeaders();
        headers.setBearerAuth(SecurityUtil.getJWTToken());

        product.setId(id);
        List<CategoryDTO> dtos=new ArrayList<>();
        list.forEach(d->{
        CategoryDTO dto=new CategoryDTO();
        dto.setId(d);
        dtos.add(dto);
        product.setCategoryDTOs(dtos);
        });

        productRestTemplate.getSomething(url,HttpMethod.PUT,headers,product,reponseType);
        return"redirect:/Product/";
        }

@GetMapping(value = "/delete/{id}")
public String deleteProduct(@PathVariable("id") Long id){
        String url=servicesConfig.getHotsName().concat(servicesConfig.getProductAPI()).concat("/")+id;
        ParameterizedTypeReference<APIResponse<Object>>reponseType=new ParameterizedTypeReference<APIResponse<Object>>(){
        };
        HttpHeaders headers=new HttpHeaders();
        headers.setBearerAuth(SecurityUtil.getJWTToken());
        productRestTemplate.getSomething(url,HttpMethod.DELETE,headers,null,reponseType);
        return"redirect:/Product/";
        }

// Add new Category by modal
@PostMapping(value = "/category")
public String handleCategorySubmission(@ModelAttribute("newCat") CategoryDTO categoryDTO){
        String url=servicesConfig.getHotsName().concat(servicesConfig.getCategoryAPI());
        ParameterizedTypeReference<APIResponse<CategoryDTO>>reponseType=new ParameterizedTypeReference<APIResponse<CategoryDTO>>(){
        };
        HttpHeaders headers=new HttpHeaders();
        headers.setBearerAuth(SecurityUtil.getJWTToken());
        productRestTemplate.getSomething(url,HttpMethod.POST,headers,categoryDTO,reponseType);
        return"redirect:/Product/create";
        }

@GetMapping(value = "/list")
public String listAllProductsWithPAgination(Model modelMap,
@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
@RequestParam(name = "size", required = false, defaultValue = "2") Integer size){

        String url="http://localhost:8000/products/list/"+page+"/"+size;
//		String url = "http://localhost:8000/products/list?page=1&size=2";
        ParameterizedTypeReference<APIResponse<ProductDTO>>reponseType=new ParameterizedTypeReference<APIResponse<ProductDTO>>(){
        };
        HttpHeaders headers=new HttpHeaders();
        headers.setBearerAuth(SecurityUtil.getJWTToken());
        APIResponse<List<ProductDTO>>list=productRestTemplate.exchangePaging(url,HttpMethod.GET,headers,
        reponseType);
        Pagination pagination=list.getPagination();
        modelMap.addAttribute("page",pagination);
        modelMap.addAttribute("listproduct",list.getData());

        return"products";

        }
        }
