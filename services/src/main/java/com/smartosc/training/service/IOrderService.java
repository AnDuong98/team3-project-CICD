package com.smartosc.training.service;



import java.util.List;

import com.smartosc.training.dto.OrderDTO;


public interface IOrderService {
	public abstract List<OrderDTO> getOrders();
	public abstract void createOrder(OrderDTO orderDTO);
	
}
