package com.example.demo.controller;

import com.example.demo.common.ResultResponse;
import com.example.demo.dto.OrderMasterDto;
import com.example.demo.dto.PageDto;
import com.example.demo.entity.OrderMaster;
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

     @RequestMapping("/list")
     @ApiOperation(value = "订单列表",httpMethod = "GET")
     public ResultResponse list(@Valid @ApiParam(name="订单列表",value = "传入json格式",required = true)PageDto pageDto, BindingResult bindingResult){
             //创建map
             HashMap<String, String> map = Maps.newHashMap();
             if (bindingResult.hasErrors()){
                 List<String> collect = bindingResult.getFieldErrors().stream().map(err -> err.getDefaultMessage()).collect(Collectors.toList());
                 map.put("参数校验异常", JsonUtil.object2string(collect));
                 return ResultResponse.fail();
             }
             return orderMasterService.selectMastlist(pageDto);

     }
    @RequestMapping("/detail")
    @ApiOperation(value = "订单详情",httpMethod = "GET")
    public ResultResponse list( String openId,String orederId){
        return orderMasterService.orderDetail(openId,orederId);

    }

    @RequestMapping("/cancel")
    @ApiOperation(value = "取消订单",httpMethod = "POST")
    public ResultResponse cancel( String openId,String orederId){
        return orderMasterService.deleteOrder(openId,orederId);

    }

}
