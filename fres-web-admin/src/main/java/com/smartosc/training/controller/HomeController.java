package com.smartosc.training.controller;

import java.io.IOException;
import java.io.InputStreamReader;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.exceptions.CsvException;
import com.smartosc.training.dto.ProductDTO;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.smartosc.training.dto.UserDTO;
import com.smartosc.training.security.AppUserDetails;
import com.smartosc.training.service.impl.AppUserDetailsService;
import com.smartosc.training.service.impl.UserServiceImpl;
import com.smartosc.training.utils.SecurityUtil;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class HomeController {
    @Autowired
    private AppUserDetailsService userService;

    @Autowired
    private UserServiceImpl userServiceimpl;

    @RequestMapping(value = {"/login", "/"}, method = RequestMethod.GET)
    public String login(@RequestParam(name = "mess", defaultValue = "") String mess, Model model) {
        model.addAttribute("message", mess);
        return "login";
    }

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public void authenticate(@RequestParam("username") String username,
                             @RequestParam("password") String password, Model model) throws UsernameNotFoundException {
        AppUserDetails user = (AppUserDetails) userService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, user);
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String loginSuccessfulPage(Model model) {
        return "index";
    }

    @RequestMapping(value = "/user", method = RequestMethod.GET)
    public String userInfo(Model model, Principal principal, @RequestParam(name = "page", defaultValue = "1") int page) {
        model.addAttribute("userInfo", SecurityUtil.getAuthorities());
        int size = 10;
        model.addAttribute("totalpage", (int) Math.ceil((double) (userServiceimpl.countUser()) / size));
        List<UserDTO> users = userServiceimpl.showUser();
        model.addAttribute("listuser", users);
        model.addAttribute("currentpage", page);
        return "users/index";
    }

    @RequestMapping(value = "/edit/user/{id}", method = RequestMethod.GET)
    public String editUser(Model model, @ModelAttribute("model") UserDTO user, @PathVariable("id") Long id) {
        user = userServiceimpl.findById(id);
        model.addAttribute("model", user);
        return "users/update";
    }

    @GetMapping(value = "/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        System.out.println("logout:-----");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }
    }

    @RequestMapping(value = "/logoutSuccessful", method = RequestMethod.GET)
    public String logoutSuccessfulPage(Model model) {
        model.addAttribute("title", "Logout");
        return "logoutSuccessfulPage";
    }

    @GetMapping(value = "/user/report")
    public String exportXlsx() {
        userServiceimpl.exportExecl();
        return "users/index";
    }

    @PostMapping("/user/import")
    public String importFileExcel(@RequestParam("file") MultipartFile multipartFile,Model model) throws IOException {
        HttpHeaders header = new HttpHeaders();
        header.setBearerAuth(SecurityUtil.getJWTToken());

        List<UserDTO> list = userServiceimpl.showUser();
        List<UserDTO> dtoList = new ArrayList<>();
        boolean readEx = true;

        if (multipartFile.getOriginalFilename().endsWith(".xlsx")) {
            XSSFWorkbook workBook = new XSSFWorkbook(multipartFile.getInputStream());
            XSSFSheet workSheet = workBook.getSheetAt(0);

            for (int i = 0; i < workSheet.getPhysicalNumberOfRows(); i++) {
                UserDTO userDTO = new UserDTO();

                XSSFRow row = workSheet.getRow(i);

                String userName = row.getCell(0).getStringCellValue().trim();
                UserDTO userDTO1 = list.stream().filter(u -> userName.equalsIgnoreCase(u.getUsername())).findAny().orElse(null);
                if (userDTO1 == null) {
                    userDTO.setUsername(userName);
                    userDTO.setEmail(row.getCell(1).getStringCellValue().trim());
                    userDTO.setFullname(row.getCell(2).getStringCellValue().trim());
                    userDTO.setPassword(row.getCell(3).getStringCellValue().trim());
                    userDTO.setStatus((int) row.getCell(4).getNumericCellValue());
                    userDTO.setCreatedDate(row.getCell(5).getDateCellValue());
                    userDTO.setModifiedDate(row.getCell(6).getDateCellValue());
                } else {
                    readEx = false;
                    break;
                }
                dtoList.add(userDTO);
            }

            if (readEx) {
                userServiceimpl.importAll(dtoList);
            }
        } else if (multipartFile.getOriginalFilename().endsWith(".txt")) {
            try {
                InputStreamReader reader = new InputStreamReader(multipartFile.getInputStream());
                CSVParser csvParser = new CSVParserBuilder().withSeparator(';').build();
                CSVReader csvReader = new CSVReaderBuilder(reader).withCSVParser(csvParser).withSkipLines(1).build();
                List<String[]> rows = csvReader.readAll();
                for (int i = 0; i < rows.size(); i++) {
                    String[] row = rows.get(i);
                    String userName = row[0];
                    UserDTO dto = list.stream().filter(u -> userName.equalsIgnoreCase(u.getUsername())).findAny().orElse(null);

                    if (dto == null) {
                        UserDTO userDTO = new UserDTO();
                        userDTO.setCreatedDate(new SimpleDateFormat("MM/dd/yyyy HH:mm").parse(row[1]));
                        userDTO.setModifiedDate(new SimpleDateFormat("MM/dd/yyyy HH:mm").parse(row[2]));
                        userDTO.setStatus(Integer.parseInt(row[3]));
                        userDTO.setFullname(row[4]);
                        userDTO.setEmail(row[5]);
                        userDTO.setUsername(userName);
                        userDTO.setPassword(row[6]);
                        dtoList.add(userDTO);
                    } else {
                        readEx = false;
                        break;
                    }
                }
                if (readEx) {
                    userServiceimpl.importAll(dtoList);
                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }
        }
        else {
            return "redirect:/user?error";
        }


        return "redirect:/user";
    }
}
