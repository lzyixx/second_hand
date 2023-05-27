package com.example.secondhand.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.secondhand.entity.TProduct;
import com.example.secondhand.entity.TType;
import com.example.secondhand.entity.TUser;
import com.example.secondhand.service.TProductService;
import com.example.secondhand.service.TTypeService;
import com.example.secondhand.service.TUserService;
import com.github.pagehelper.PageHelper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class IndexController {

    @Autowired
    private TTypeService typeService;

    @Autowired
    private TUserService userService;

    @Autowired
    private TProductService productService;

    @RequestMapping({"/","index"})
    public String toIndex(Model model){
        QueryWrapper queryWrapper=new QueryWrapper();
        queryWrapper.orderByAsc("sort");
        List<TType> typeList = typeService.list(queryWrapper);
        for (TType tType : typeList) {
            QueryWrapper<TProduct> wrapper = new QueryWrapper<>();
            wrapper.ne("deleted", "1").or().isNull("deleted");
            wrapper.eq("type_id", tType.getId());
            List<TProduct> products = productService.list(wrapper);
            if(products.isEmpty()){
                tType.setProductNum(0);
            }else {
                tType.setProductNum(products.size());
            }
        }
        model.addAttribute("typeList",typeList);
        return "index";
    }

    @GetMapping("/login")
    public String toAdminLogin() {
        return "login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String username,
                        @RequestParam String password,
                        HttpSession session,
                        RedirectAttributes redirectAttributes) {
        TUser user = userService.checkUser(username, password);
        if (user != null) {
            user.setPassword(null);
            session.setAttribute("user", user);
            return "redirect:/index";
        } else {
            redirectAttributes.addFlashAttribute("message", "Incorrect username and password!");
            return "redirect:/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/index";
    }

    //register
    @GetMapping("/register")
    public String toRegister() {
        return "register";
    }

    @PostMapping("/register")
    @ResponseBody
    public String register(TUser user) {
        //username exist or not
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("username", user.getUsername());
        TUser tUser = userService.getOne(queryWrapper);
        if (tUser != null) {
            return "error";
        } else {
            //add
            userService.save(user);
            return "success";
        }
    }

    //个人信息管理
    @GetMapping("/myInfo")
    public String myInfo(HttpSession session, Model model) {
        TUser user = (TUser) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", userService.getById(user.getId()));
            return "myInfo";
        } else {
            return "login";
        }
    }

    //修改个人信息
    @PostMapping("/myInfo")
    public String updateMyInfo(TUser user, HttpSession session, RedirectAttributes attributes) {
        //修改
        if(StringUtils.isBlank(user.getPassword())){
            user.setPassword(null);
        }
        userService.updateById(user);
        attributes.addFlashAttribute("message", "Successfully modified");
        //更新session
        user.setPassword(null);
        session.setAttribute("user", user);
        return "redirect:/myInfo";
    }
}

