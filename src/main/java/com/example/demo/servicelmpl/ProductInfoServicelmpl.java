package com.example.demo.servicelmpl;

import com.example.demo.common.ProductEnums;
import com.example.demo.common.ResultEnums;
import com.example.demo.common.ResultResponse;
import com.example.demo.dto.ProductCategoryDto;
import com.example.demo.dto.ProductInfoDto;
import com.example.demo.entity.ProductCategory;
import com.example.demo.entity.ProductInfo;
import com.example.demo.repository.ProductCategoryRepository;
import com.example.demo.repository.ProductInfoRepository;
import com.example.demo.service.ProductInfoService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductInfoServicelmpl implements ProductInfoService {

    @Autowired
    private ProductCategoryRepository productCategoryRepository;
    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Override
    public ResultResponse queryList() {
        //查询所有分类
        List<ProductCategory> all = productCategoryRepository.findAll();
        System.out.println("all:"+all);
        List<ProductCategoryDto> productCategoryDtoList =
                all.stream().map(productCategory -> ProductCategoryDto.build(productCategory)).collect(Collectors.toList());
        //将all转换成dto
        System.out.println("category:"+productCategoryDtoList.toString());
        if (CollectionUtils.isEmpty(productCategoryDtoList)){
            return ResultResponse.fail();
        }
        //获取类目编号集合
        List<Integer> typeList =
                productCategoryDtoList.stream().map(productCategoryDto -> productCategoryDto.getCategoryType()).collect(Collectors.toList());
        //根据typeList  查询商品列表
        System.out.println("type:"+typeList.toString());
        List<ProductInfo> productInfoList
                = productInfoRepository.findByProductStatusAndCategoryTypeIn(ResultEnums.PRODUCT_UP.getCode(), typeList);
        //对typeList目标集合遍历 取出每个商品的类目编号 设置到对应的目录中
        List<ProductCategoryDto> productCategoryDtos = productCategoryDtoList.parallelStream().map(productCategoryDto -> {
            productCategoryDto.setProductInfoDtoList(productInfoList.stream()
            .filter(productInfo -> productInfo.getCategoryType()==productCategoryDto.getCategoryType())
                    .map(productInfo -> ProductInfoDto.build(productInfo)).collect(Collectors.toList()));
            return productCategoryDto;
        }).collect(Collectors.toList());
        System.out.println("product:"+productCategoryDtos.toString());
        return ResultResponse.success(productCategoryDtos);
    }
    /*根据id查询商品*/
    @Override
    public ResultResponse<ProductInfo> queryById(String productId) {
        if (StringUtils.isBlank(productId)){
            return ResultResponse.fail(ResultEnums.PARAM_ERROR.getMsg()+":"+productId);
        }
        Optional<ProductInfo> byId = productInfoRepository.findById(productId);
        if (!byId.isPresent()){
            return ResultResponse.fail(productId+":"+ResultEnums.NOT_EXITS.getMsg());
        }
        ProductInfo productInfo = byId.get();
        //判断商品状态看是否下架
        if (productInfo.getProductStatus()== ResultEnums.PRODUCT_DOWN.getCode()){
            return ResultResponse.fail(ResultEnums.PRODUCT_DOWN.getMsg());
        }
        return ResultResponse.success(productInfo);
    }
    /*修改商品库存的方法*/
    @Override
    @Transactional
    public void updateProduct(ProductInfo productInfo) {
      productInfoRepository.save(productInfo);
    }
}
