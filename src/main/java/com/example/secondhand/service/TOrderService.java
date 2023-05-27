package com.example.secondhand.service;

import com.example.secondhand.entity.TOrder;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

public interface TOrderService extends IService<TOrder> {

    boolean generateOrder(Integer pId,Integer userId,String receiverPhone, String receiverName, String receiverAddress);
}
