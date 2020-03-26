package com.smartosc.training.service;


import java.util.List;

import com.smartosc.training.dto.OrderDetailDTO;


public interface IOrderDetailService {
	public abstract void createOrderDetail(OrderDetailDTO orderDetailDTO);
	
	public abstract List<OrderDetailDTO> getOrderDetails();
}
