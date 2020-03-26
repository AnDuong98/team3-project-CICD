package com.smartosc.training.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.smartosc.training.dto.OrderDTO;
import com.smartosc.training.dto.OrderDetailDTO;
import com.smartosc.training.dto.UserDTO;
import com.smartosc.training.entity.OrderDetailEntity;
import com.smartosc.training.entity.OrderEntity;
import com.smartosc.training.entity.ProductEntity;
import com.smartosc.training.entity.UserEntity;
import com.smartosc.training.repository.OrderDetailRepository;
import com.smartosc.training.repository.OrderRepository;
import com.smartosc.training.repository.ProductRepository;
import com.smartosc.training.security.repo.UserRepository;
import com.smartosc.training.service.IOrderService;

@Service
public class OrderServiceImpl implements IOrderService{

	@Autowired
	private OrderRepository orderRepo; 
	
	@Autowired
	private ProductRepository productRepository;
	
	@Autowired
	private OrderDetailRepository orderDetailRepository;
	
	@Autowired
	private UserRepository userRepo;
	
	private ModelMapper map = new ModelMapper();
	
	@Override
	public List<OrderDTO> getOrders() {
		List<OrderEntity> orderEntities = orderRepo.findAll();
		List<OrderDTO> list = new ArrayList<OrderDTO>();
		orderEntities.forEach(order->{
			OrderDTO dto = new OrderDTO();
			dto = map.map(order, OrderDTO.class);
			list.add(dto);
		});
		return list;
	}

	@Override
	public void createOrder(OrderDTO orderDTO) {
		
		OrderEntity order = new OrderEntity();
		order.setName(orderDTO.getName());
		order.setTotalPrice(orderDTO.getTotalPrice());
		order.setStatus(1);
		
		UserDTO userDTO = orderDTO.getUserDTOs();		
		UserEntity users = userRepo.findById(userDTO.getId()).get();
        order.setUser(users);
        
        List<OrderDetailEntity> detailEntities = new ArrayList<OrderDetailEntity>();
        List<OrderDetailDTO> detailDTOs = orderDTO.getDetails();
        
        
        order=orderRepo.save(order);
        
        for (OrderDetailDTO d : detailDTOs) {
        	OrderDetailEntity detailEntity = new OrderDetailEntity();
        	detailEntity.setOrder(order);
        	ProductEntity productEntity = productRepository.findById(d.getProduct().getId()).get();
        	detailEntity.setProduct(productEntity);
        	detailEntity.setPrice(d.getPrice());
        	detailEntity.setQuantity(d.getQuantity());
        	detailEntity.setStatus(1);
        	
        	detailEntity = orderDetailRepository.save(detailEntity);
        	
        	detailEntities.add(detailEntity);
		}
        
        order.setDetails(detailEntities);
        orderRepo.save(order);
	
	}
}

