package com.smartosc.training.service.impl;

import com.smartosc.training.dto.AddressDTO;
import com.smartosc.training.dto.CategoryDTO;
import com.smartosc.training.entity.AddressEntity;
import com.smartosc.training.entity.CategoryEntity;
import com.smartosc.training.repository.AddressRepository;
import com.smartosc.training.service.IAddressService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class AddressServiceImpl implements IAddressService {

    @Autowired
    private AddressRepository addressRepository;

    private ModelMapper map = new ModelMapper();

    @Override
    public void createAddress(AddressDTO addressDTO) {
        AddressEntity newAddress = map.map(addressDTO, AddressEntity.class);
        addressRepository.save(newAddress);
    }

    @Override
    public List<AddressDTO> listAll() {
        List<AddressEntity> addressEntities = addressRepository.findAll();
        if (!addressEntities.isEmpty()) {
            List<AddressDTO> list = new ArrayList<AddressDTO>();
            addressEntities.forEach(cate -> {
                AddressDTO dto = new AddressDTO();
                dto = map.map(cate, AddressDTO.class);
                list.add(dto);
            });
            return list;
        }
        return null;
    }


}
