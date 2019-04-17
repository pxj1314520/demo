package com.example.demo.service;

import com.example.demo.entity.OrderDetail;

import java.util.List;

public interface OrderDetailService {
    //批量插入
    void batchInsert(List<OrderDetail> orderDetailList);
}
