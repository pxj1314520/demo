package com.example.demo.dto;

import com.example.demo.entity.OrderDetail;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.List;
@Data
@ApiModel("订单详情")//swagger 参数的描述信息
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MasterDetailDto {
    private String orderId;
    @NotBlank(message = "姓名不能为空")
    @ApiModelProperty(value = "买家姓名",dataType = "String")
    private String name;
    @NotBlank(message = "电话不能为空")
    @Length(min =11,max = 11,message = "请输入正确的手机号")
    @ApiModelProperty(value = "买家电话",dataType = "String")
    private String phone;
    @NotBlank(message = "地址不能为空")
    @ApiModelProperty(value = "买家地址",dataType = "String")
    private String address;
    @NotBlank(message = "必须输入用户的微信openid")
    @ApiModelProperty(value = "买家微信openid",dataType = "String")
    private String openid;
    @NotEmpty(message = "订单项不能为空")
    @Valid //表示需要嵌套验证
    @ApiModelProperty(value = "订单项集合",dataType = "List")
    private List<OrderDetail> orderDetailList;
}
