package com.example.secondhand.mapper;

import com.example.secondhand.entity.TProduct;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

public interface TProductMapper extends BaseMapper<TProduct> {

    List<TProduct> selectSomeNewProduct(int countNun);
}
