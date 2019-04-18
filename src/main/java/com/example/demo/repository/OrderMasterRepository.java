package com.example.demo.repository;

import com.example.demo.entity.OrderMaster;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderMasterRepository extends JpaRepository<OrderMaster,String> {
    List<OrderMaster> findAllByBuyerOpenid(String opendId, Pageable pageable);

    OrderMaster findByBuyerOpenidAndOrderIdIn(String opendId,String OrderId);
}
