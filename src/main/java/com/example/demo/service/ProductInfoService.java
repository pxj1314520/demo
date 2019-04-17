package com.example.demo.service;

import com.example.demo.common.ResultResponse;
import com.example.demo.entity.ProductInfo;

public interface ProductInfoService {

    ResultResponse queryList();
    /*根据id查询商品*/
    ResultResponse<ProductInfo> queryById(String productId);
    /*修改商品库存的方法*/
    void updateProduct(ProductInfo productInfo);

}
