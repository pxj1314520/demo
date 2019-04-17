package com.example.demo.controller;

import com.example.demo.common.ResultResponse;
import com.example.demo.dto.OrderMasterDto;
import com.example.demo.service.OrderMasterService;
import com.example.demo.util.JsonUtil;
import com.google.common.collect.Maps;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/buyer/order")
@Api(value = "订单相关接口",description = "完成订单的增删改查")
public class OrderMasterController {
     @Autowired
    private OrderMasterService orderMasterService;

     @RequestMapping("/create")
     @ApiOperation(value = "创建订单",httpMethod = "POST")
    public ResultResponse create(@Valid  @ApiParam(name="订单对象",value = "传入json格式",required = true) OrderMasterDto orderMasterDto, BindingResult bindingResult){
         //创建map
         HashMap<String, String> map = Maps.newHashMap();
         if (bindingResult.hasErrors()){
             List<String> collect = bindingResult.getFieldErrors().stream().map(err -> err.getDefaultMessage()).collect(Collectors.toList());
             map.put("参数校验异常", JsonUtil.object2string(collect));
             return ResultResponse.fail();
         }
         return orderMasterService.insertOreder(orderMasterDto);

     }
}
