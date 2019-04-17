package com.example.demo.service;

import com.example.demo.common.ResultResponse;
import com.example.demo.dto.OrderMasterDto;

public interface OrderMasterService {
    ResultResponse insertOreder(OrderMasterDto orderMasterDto);
}

