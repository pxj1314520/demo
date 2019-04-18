package com.example.demo.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ApiModel("分页参数")
public class PageDto {
    @NotBlank(message = "微信不能为空")
    String openId;
    Integer page=0;
    Integer size=10;
}
