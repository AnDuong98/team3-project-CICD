package com.smartosc.training.service;


import com.smartosc.training.dto.AddressDTO;

import java.util.List;

public interface IAddressService {
    public abstract void createAddress(AddressDTO addressDTO);
    public abstract List<AddressDTO> listAll();
}
