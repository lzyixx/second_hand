package com.example.secondhand.service.impl;

import com.example.secondhand.entity.TProduct;
import com.example.secondhand.mapper.TProductMapper;
import com.example.secondhand.service.TProductService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TProductServiceImpl extends ServiceImpl<TProductMapper, TProduct> implements TProductService {

    @Autowired
    private TProductMapper productMapper;

}
