package com.example.secondhand.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.secondhand.entity.TOrder;
import com.example.secondhand.entity.TProduct;
import com.example.secondhand.entity.TUser;
import com.example.secondhand.service.TOrderService;
import com.example.secondhand.service.TProductService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class OrderController {
    @Autowired
    private TOrderService orderService;

    @Autowired
    private TProductService productService;

    @PostMapping("/payOrder")
    @ResponseBody
    public String payOrder(HttpSession session,Integer pId,String receiverPhone, String receiverName, String receiverAddress) {
        TUser user = (TUser) session.getAttribute("user");
        if (user != null&&pId!=null) {
            //生成订单
           boolean flag = orderService.generateOrder(pId,user.getId(),receiverPhone,  receiverName,  receiverAddress);
           if(flag){
               return "success";
           }
        }
        return "false";
    }

    @GetMapping("/myOrder")
    public String myOrder(@RequestParam(value = "page", defaultValue = "1") Integer page,
                              @RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize,
                              Model model, HttpSession session) {
        //分页
        PageHelper.startPage(page, pageSize);
        TUser user = (TUser) session.getAttribute("user");
        if (user != null) {
            QueryWrapper<TOrder> queryWrapper = new QueryWrapper();
            queryWrapper.eq("user_id", user.getId());
            List<TOrder> orderList = orderService.list(queryWrapper);
            //填充product
            for (TOrder tOrder : orderList) {
                tOrder.setProduct(productService.getById(tOrder.getProductId()));
            }
            PageInfo pageInfo = new PageInfo(orderList);
            model.addAttribute("orderList", orderList);
            model.addAttribute("page", page);
            model.addAttribute("total", pageInfo.getTotal());
            model.addAttribute("pages", Math.max(pageInfo.getPages(), 1));
            return "myOrder";
        } else {
            //登录
            return "login";
        }
    }

    //确认收货
    @GetMapping("/receiveProduct")
    public String receiveProduct(String id){
        TOrder order = orderService.getById(id);
        order.setConfirmStatus(1);
        orderService.updateById(order);
        return "redirect:/myOrder";
    }
}

