package com.smartosc.training.service.impl;


import com.smartosc.training.dto.OrderDetailDTO;
import com.smartosc.training.entity.CategoryEntity;
import com.smartosc.training.exception.NotFoundException;
import com.smartosc.training.repository.OrderRepository;
import com.smartosc.training.repository.ProductRepository;
import net.bytebuddy.asm.Advice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.util.List;

import static com.smartosc.training.exception.NotFoundException.supplier;

@Service
public class BatchInsertService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    // Any failure causes the entire operation to roll back, none of the book will be added
    @Transactional
    public int[][] batchInsert(List<OrderDetailDTO> orderDetailDTOS, int batchSize) {

        return jdbcTemplate.batchUpdate(
                "insert into orderdetail (id,createddate,modifieddate,status, price, quantity,orderid,product_id) values(?,?,?,?,?,?,?,?)",
                orderDetailDTOS,
                batchSize,
                (ps, argument) -> {
                    ps.setLong(1, argument.getId());
                    ps.setDate(2, new Date(argument.getCreatedDate().getTime()));
                    ps.setDate(3, new Date(argument.getModifiedDate().getTime()));
                    ps.setInt(4, argument.getStatus());
                    ps.setString(5, argument.getPrice());
                    ps.setInt(6, argument.getQuantity());

                    try {

                        Long orderId = argument.getOrder().getId();
                        orderRepository.findById(orderId);
                        ps.setLong(7, orderId);

                        Long productId = argument.getProduct().getId();
                        productRepository.findById(productId);
                        ps.setLong(8, productId);

                    } catch (NullPointerException ex){
                        ex.getMessage();
                    }

                });
    }
}
