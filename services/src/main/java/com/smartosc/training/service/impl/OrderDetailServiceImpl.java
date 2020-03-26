package com.smartosc.training.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartosc.training.dto.OrderDetailDTO;
import com.smartosc.training.entity.OrderDetailEntity;
import com.smartosc.training.repository.OrderDetailRepository;
import com.smartosc.training.service.IOrderDetailService;

@Service
public class OrderDetailServiceImpl implements IOrderDetailService{

	@Autowired
	private OrderDetailRepository orderdetailRepo;
	
	private ModelMapper map = new ModelMapper();
	
	@Override
	public List<OrderDetailDTO> getOrderDetails() {
		List<OrderDetailEntity> orderDetailEntities = orderdetailRepo.findAll();
		List<OrderDetailDTO> list = new ArrayList<OrderDetailDTO>();
		orderDetailEntities.forEach(orderDetail->{
			OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
			orderDetail = map.map(orderDetail, OrderDetailEntity.class);
			list.add(orderDetailDTO);
		});
		return list;
	}
	
	@Override
	public void createOrderDetail(OrderDetailDTO orderDetailDTO) {
		
		 orderdetailRepo.save(map.map(orderDetailDTO, OrderDetailEntity.class));
	}
	
	

}
