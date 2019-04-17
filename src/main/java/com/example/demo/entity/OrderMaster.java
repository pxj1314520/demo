package com.example.demo.entity;

import com.example.demo.common.OrderEnums;
import com.example.demo.common.PayEnums;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
@Entity
@Data
@DynamicUpdate//如果有空值就不加入到sql当中
@Builder
public class OrderMaster implements Serializable {

    /** 订单id. */
    @Id
    private String orderId;

    /** 买家名字. */
    private String buyerName;

    /** 买家手机号. */
    private String buyerPhone;

    /** 买家地址. */
    private String buyerAddress;

    /** 买家微信Openid. */
    private String buyerOpenid;

    /** 订单总金额. */
    private BigDecimal orderAmount;

    /** 订单状态, 默认为0新下单. */
    private Integer orderStatus = OrderEnums.NEW.getCode();

    /** 支付状态, 默认为0未支付. */
    private Integer payStatus = PayEnums.WAIT.getCode();

    /** 创建时间. */
    private Date createTime;

    /** 更新时间. */
    private Date updateTime;


}