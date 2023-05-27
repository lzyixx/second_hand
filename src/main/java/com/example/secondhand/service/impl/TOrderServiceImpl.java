package com.example.secondhand.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.secondhand.entity.TOrder;
import com.example.secondhand.entity.TProduct;
import com.example.secondhand.mapper.TOrderMapper;
import com.example.secondhand.mapper.TProductMapper;
import com.example.secondhand.service.TOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@Service
@Transactional
public class TOrderServiceImpl extends ServiceImpl<TOrderMapper, TOrder> implements TOrderService {

    @Autowired
    private TOrderMapper orderMapper;

    @Autowired
    private TProductMapper productMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean generateOrder(Integer pId,Integer userId,String receiverPhone, String receiverName, String receiverAddress) {
        TOrder order=new TOrder();
        order.setId(Long.parseLong(getOrderIdByTime()));
        order.setCreateTime(new Date());
        order.setUpdateTime(new Date());
        order.setPayStatus(1);
        order.setConfirmStatus(0);
        order.setUserId(userId);
        order.setProductId(pId);
        order.setReceiverName(receiverName);
        order.setReceiverPhone(receiverPhone);
        order.setReceiverAddress(receiverAddress);
        TProduct product = productMapper.selectById(pId);
        if(product==null){
            return false;
        }
        order.setTotalPrice(product.getPrice());
        orderMapper.insert(order);
        //下架商品
        product.setDeleted(1);
        productMapper.updateById(product);
        return true;
    }

    private static String getOrderIdByTime() {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
        String newDate=sdf.format(new Date());
        String result="";
        Random random=new Random();
        for(int i=0;i<3;i++){
            result+=random.nextInt(10);
        }
        return newDate+result;
    }
}
