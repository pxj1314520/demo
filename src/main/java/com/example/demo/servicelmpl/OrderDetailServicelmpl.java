package com.example.demo.servicelmpl;

import com.example.demo.dao.AbstractBatchDao;
import com.example.demo.entity.OrderDetail;
import com.example.demo.service.OrderDetailService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
public class OrderDetailServicelmpl extends AbstractBatchDao<OrderDetail> implements OrderDetailService {
    @Override
    @Transactional
    public void batchInsert(List<OrderDetail> orderDetailList) {
        super.batchInsert(orderDetailList);
    }
}
