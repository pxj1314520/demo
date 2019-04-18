package com.example.demo.service;

import com.example.demo.common.ResultResponse;
import com.example.demo.dto.OrderMasterDto;
import com.example.demo.dto.PageDto;

public interface OrderMasterService {
    ResultResponse insertOreder(OrderMasterDto orderMasterDto);

    ResultResponse selectMastlist(PageDto pageDto);

    ResultResponse orderDetail(String orderId, String openId);

    ResultResponse deleteOrder(String orderId, String openId);
}

