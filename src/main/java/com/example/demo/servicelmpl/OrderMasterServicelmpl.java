package com.example.demo.servicelmpl;

import com.example.demo.common.OrderEnums;
import com.example.demo.common.PayEnums;
import com.example.demo.common.ResultEnums;
import com.example.demo.common.ResultResponse;
import com.example.demo.dto.MasterDetailDto;
import com.example.demo.dto.OrderDetailDto;
import com.example.demo.dto.OrderMasterDto;
import com.example.demo.dto.PageDto;
import com.example.demo.entity.OrderDetail;
import com.example.demo.entity.OrderMaster;
import com.example.demo.entity.ProductInfo;
import com.example.demo.exception.CustomException;
import com.example.demo.repository.OrderDetailRepository;
import com.example.demo.repository.OrderMasterRepository;
import com.example.demo.repository.ProductInfoRepository;
import com.example.demo.service.OrderDetailService;
import com.example.demo.service.OrderMasterService;
import com.example.demo.service.ProductInfoService;
import com.example.demo.util.BigDecimalUtil;
import com.example.demo.util.IDUtils;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderMasterServicelmpl implements OrderMasterService {
    @Autowired
    private ProductInfoService productInfoService;
    @Autowired
    private OrderDetailService orderDetailService;
    @Autowired
    private OrderMasterRepository orderMasterRepository;
    @Autowired
    private OrderDetailRepository orderDetailRepository;
    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Override
    @Transactional//开启事务
    public ResultResponse insertOreder(OrderMasterDto orderMasterDto) {
        //取出订单
        List<OrderDetailDto> items = orderMasterDto.getItems();
        System.out.println(items.toString());
        //创建订单detail集合，将符合的放入其中，最后批量插入
        List<OrderDetail> orderDetailList = Lists.newArrayList();
        //计算金额 高精度计算
        BigDecimal totalprice = new BigDecimal("0");
        for (OrderDetailDto item : items
        ) {
            ResultResponse<ProductInfo> resultResponse = productInfoService.queryById(item.getProductId());
            System.out.println(resultResponse.toString());
            //如果没有查到就生成订单失败
            if (resultResponse.getCode() == ResultEnums.FAIL.getCode()) {
                throw new CustomException(resultResponse.getMsg());
            }
            //获取查询的商品
            ProductInfo productInfo = resultResponse.getData();
            //判断库存是否足够
            if (productInfo.getProductStock() < item.getProductQuantity()) {
                throw new CustomException(ResultEnums.PRODUCT_NOT_ENOUGH.getMsg());
            }
            //将前台传入的订单项DTO与数据库查询到的 商品数据组装成OrderDetail 放入集合中  @builder
            OrderDetail orderDetail = OrderDetail.builder().detailId(IDUtils.createIdbyUUID()).productIcon(productInfo.getProductIcon())
                    .productId(item.getProductId()).productName(productInfo.getProductName())
                    .productPrice(productInfo.getProductPrice()).productQuantity(item.getProductQuantity())
                    .build();
            orderDetailList.add(orderDetail);
            //减少商品库存
            productInfo.setProductStock(productInfo.getProductStock() - item.getProductQuantity());
            productInfoService.updateProduct(productInfo);
            //计算价格
            totalprice = BigDecimalUtil.add(totalprice, BigDecimalUtil.multi(productInfo.getProductPrice(), item.getProductQuantity()));

        }
        //生成订单
        String orderId = IDUtils.createIdbyUUID();
        //构建订单信息 日期默认
        OrderMaster orderMaster = OrderMaster.builder().buyerAddress(orderMasterDto.getAddress()).buyerName(orderMasterDto.getName())
                .buyerOpenid(orderMasterDto.getOpenid()).orderStatus(OrderEnums.NEW.getCode())
                .payStatus(PayEnums.WAIT.getCode()).buyerPhone(orderMasterDto.getPhone())
                .orderId(orderId).orderAmount(totalprice).build();
        //生成的订单id 设置到订单项中
        List<OrderDetail> detailList = orderDetailList.stream().map(orderDetail -> {
            orderDetail.setOrderId(orderId);
            return orderDetail;
        }).collect(Collectors.toList());
        //插入订单项
        orderDetailService.batchInsert(detailList);
        //插入订单
        orderMasterRepository.save(orderMaster);
        HashMap<String, String> map = Maps.newHashMap();
        //按照前台要求的数据结构传入
        map.put("orderId", orderId);
        return ResultResponse.success(map);
    }
    /*订单列表*/
    @Override
    public ResultResponse selectMastlist(PageDto pageDto) {
        Pageable pageable =PageRequest.of(pageDto.getPage(), pageDto.getSize());
        List<OrderMaster> orderMasterList = orderMasterRepository.findAllByBuyerOpenid(pageDto.getOpenId(), pageable);
        return ResultResponse.success(orderMasterList);
    }
    /*订单详情*/
    @Override
    public ResultResponse orderDetail(String orderId, String openId) {
        if (openId==null){
            throw new CustomException(OrderEnums.OPENID_ERROR.getMsg());
        }
        OrderMaster mster = orderMasterRepository.findByBuyerOpenidAndOrderIdIn(orderId, openId);
        if (mster==null){
            throw new CustomException(OrderEnums.ORDER_NOT_EXITS.getMsg());
        }
        if (mster.getOrderStatus()==OrderEnums.CANCEL.getCode()){
            throw new CustomException(OrderEnums.CANCEL.getMsg());
        }
        System.out.println(mster);
        List<OrderDetail> detail= orderDetailRepository.findByOrderIdIn(mster.getOrderId());
        System.out.println(detail);
        MasterDetailDto masterDetailDto=MasterDetailDto.builder().address(mster.getBuyerAddress()).name(mster.getBuyerName())
                .openid(mster.getBuyerOpenid()).orderId(mster.getOrderId()).phone(mster.getBuyerPhone()).orderDetailList(detail).build();
        return ResultResponse.success(masterDetailDto);
    }
    /*取消订单*/
    @Override
    @Transactional//开启事务
    public ResultResponse deleteOrder(String orderId, String openId) {
        if (openId==null){
            throw new CustomException(OrderEnums.OPENID_ERROR.getMsg());
        }
        OrderMaster mster = orderMasterRepository.findByBuyerOpenidAndOrderIdIn(orderId, openId);
        if (mster==null){
            throw new CustomException(OrderEnums.ORDER_NOT_EXITS.getMsg());
        }
        if (mster.getOrderStatus()==OrderEnums.CANCEL.getCode()){
            throw new CustomException(OrderEnums.CANCEL.getMsg());
        }
        List<OrderDetail> detail= orderDetailRepository.findByOrderIdIn(mster.getOrderId());
        for (OrderDetail orderDetail :detail
                ) {
            ProductInfo byProductNameIn = productInfoRepository.findByProductNameIn(orderDetail.getProductName());
            byProductNameIn.setProductStock(byProductNameIn.getProductStock()+orderDetail.getProductQuantity());
            productInfoRepository.save(byProductNameIn);
        }
        mster.setOrderStatus(OrderEnums.CANCEL.getCode());
        orderMasterRepository.save(mster);
        return ResultResponse.success();
    }


}

