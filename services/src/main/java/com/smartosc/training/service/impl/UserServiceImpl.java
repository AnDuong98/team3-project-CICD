package com.smartosc.training.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;

import com.smartosc.training.entity.AddressEntity;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import net.sf.jasperreports.export.SimpleXlsxReportConfiguration;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.smartosc.training.dto.AddressDTO;
import com.smartosc.training.dto.UserDTO;
import com.smartosc.training.entity.RoleEntity;
import com.smartosc.training.entity.UserEntity;
import com.smartosc.training.repository.AddressRepository;
import com.smartosc.training.repository.ControlUserRepository;
import com.smartosc.training.repository.RoleRepository;
import com.smartosc.training.security.utils.EncrytedPasswordUtil;
import com.smartosc.training.service.IUserService;
import org.springframework.util.ResourceUtils;

@Service
public class UserServiceImpl implements IUserService {

    private String reportPath;
    private ModelMapper map = new ModelMapper();

    @Autowired
    private ControlUserRepository user;

    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private AddressRepository addressRepository;


    private ModelMapper modelMapper = new ModelMapper();

    @Override
    public UserDTO save(UserDTO userDTO) {
        if (userDTO.getId() != null) {
            if (String.valueOf(userDTO.getStatus()) == null) {
                userDTO.setStatus(0);
            }
            UserEntity oldUser = user.findById(userDTO.getId()).get();
            oldUser.setStatus(userDTO.getStatus());
            UserEntity newUser = user.save(oldUser);
            return modelMapper.map(newUser, UserDTO.class);
        } else {
            userDTO.setPassword(EncrytedPasswordUtil.encrytePassword(userDTO.getPassword()));
            userDTO.setStatus(1);
            UserEntity newUser = modelMapper.map(userDTO, UserEntity.class);
            // RoleEntity usesRole = roleRepository.findByName("ROLE_ADMIN");
            RoleEntity adminRole = roleRepository.findByName("ROLE_USER");
            System.out.println(adminRole.getName());
            // newUser.getRoles().add(usesRole);
            newUser.getRoles().add(adminRole);
            newUser = user.save(newUser);
            return modelMapper.map(newUser, UserDTO.class);
        }
    }

    @Override
    public void delete(Long[] ids) {
        for (Long l : ids) {
            user.deleteById(l);
        }
    }

    @Override
    public List<UserDTO> findAll(Pageable pageable) {
        List<UserDTO> results = new ArrayList<>();
        List<UserEntity> entities = user.findAll(pageable).getContent();
        for (UserEntity item : entities) {
            UserDTO newDTO = modelMapper.map(item, UserDTO.class);
            results.add(newDTO);
        }
        return results;
    }

    @Override
    public UserDTO findById(long id) {
        return modelMapper.map(user.findById(id).get(), UserDTO.class);
    }

    @Override
    public int totalItem() {
        return (int) user.count();
    }

    @Override
    public List<UserDTO> findAll() {
        List<UserDTO> results = new ArrayList<>();
        List<UserEntity> entities = user.findAll();
        for (UserEntity item : entities) {
            UserDTO userDTO = modelMapper.map(item, UserDTO.class);
            AddressEntity addressEntity = item.getAddress();
            userDTO.setAddressId(addressEntity.getId());
            results.add(userDTO);
        }
        return results;
    }

    @Override
    public UserDTO findByUserame(String name) {
        UserEntity userEntity = user.findByUsername(name);
        UserDTO dto = new UserDTO();
        dto = modelMapper.map(userEntity, UserDTO.class);


        AddressDTO dto2 = modelMapper.map(userEntity.getAddress(), AddressDTO.class);
        dto.setAddress(dto2);


        return dto;
    }

    public String generateReport() {
        List<UserEntity> userEntities = user.findAll();
        List<UserDTO> userDTOS = new ArrayList<>();
        userEntities.forEach(u->{
            UserDTO dto = map.map(u,UserDTO.class);
            AddressEntity addressEntity = u.getAddress();
            dto.setAddressId(addressEntity.getId());
            userDTOS.add(dto);
        });


        reportPath = "D:\\";
        try {
            File file = ResourceUtils.getFile("D:\\Project\\Start-up Demo\\OSCFinal\\services\\src\\main\\resources\\user.jrxml");
            InputStream fileInputStream = new FileInputStream(file);

            JasperReport jasperReport = JasperCompileManager.compileReport(fileInputStream);

            JRBeanCollectionDataSource jrBeanCollectionDataSource = new JRBeanCollectionDataSource(userDTOS);
            HashMap<String,Object> parameter = new HashMap<>();
            parameter.put("CreatedBy","NamCx");

            JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameter, jrBeanCollectionDataSource);

            xlxs(jasperPrint);
            return "Report Successfully!! @path = " + reportPath;
        } catch (Exception e) {
            e.printStackTrace();
            return e.getMessage();
        }
    }


    private void xlxs(JasperPrint jasperPrint) throws JRException {
        JRXlsxExporter exporter = new JRXlsxExporter();
		exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
		exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(reportPath+"\\User.xlsx"));
		SimpleXlsxReportConfiguration configuration = new SimpleXlsxReportConfiguration();
		configuration.setOnePagePerSheet(true);
		configuration.setRemoveEmptySpaceBetweenRows(true);
		configuration.setRemoveEmptySpaceBetweenColumns(true);
		configuration.setDetectCellType(true);
		configuration.setWrapText(true);
		exporter.setConfiguration(configuration);
		exporter.exportReport();
    }

    public List<UserDTO> registerAlot(List<UserDTO> userDTOS) {
        for(UserDTO dto: userDTOS){
            UserEntity newUser = new UserEntity();
            newUser.setStatus(1);
            AddressEntity addressEntity = addressRepository.save(new AddressEntity());
            Optional<RoleEntity> role = roleRepository.findById(new Long(2));
            if(role.isPresent()) {
                RoleEntity roleEntities = role.get();
                List<RoleEntity> entities = new ArrayList<>();
                entities.add(roleEntities);
                newUser.setUsername(dto.getUsername());
                newUser.setEmail(dto.getEmail());
                newUser.setPassword(dto.getPassword());
                newUser.setFullName(dto.getFullname());
                newUser.setStatus(dto.getStatus());
                newUser.setCreatedDate(dto.getCreatedDate());
                newUser.setModifiedDate(dto.getModifiedDate());
                newUser.setRoles(entities);
                newUser.setAddress(addressEntity);
            }
            user.save(newUser);
        }
        return null;
    }
}