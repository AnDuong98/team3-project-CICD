package com.smartosc.training.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.smartosc.training.entity.OrderDetailEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailRepository  extends JpaRepository<OrderDetailEntity, Long>{

    @Query(value = "select od.id, od.createddate, od.modifieddate, o.name, p.name, od.status, p.price, quantity, od.price, od.orderid, od.product_id " +
            "from orderdetail od " +
            "left join orderdb o on (o.id = od.id) " +
            "left join product p on (od.product_id = p.id)",nativeQuery = true)
    List<OrderDetailEntity> findByReportField();

}
