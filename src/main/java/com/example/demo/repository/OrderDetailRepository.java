package com.example.demo.repository;

import com.example.demo.entity.OrderDetail;
import org.springframework.data.jpa.repository.JpaRepository;
//订单项dao开发
public interface OrderDetailRepository extends JpaRepository<OrderDetail,String> {

}
