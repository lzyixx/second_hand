package com.example.secondhand.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.secondhand.entity.TOrder;
import com.example.secondhand.entity.TProduct;
import com.example.secondhand.entity.TType;
import com.example.secondhand.entity.TUser;
import com.example.secondhand.service.TOrderService;
import com.example.secondhand.service.TProductService;
import com.example.secondhand.service.TTypeService;
import com.example.secondhand.service.TUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

/**
 * 后台管理Controller
 */
@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private TUserService userService;

    @Autowired
    private TTypeService typeService;

    @Autowired
    private TProductService productService;

    @Autowired
    private TOrderService orderService;

    @GetMapping
    public String toAdminLogin() {
        return "admin/login";
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
            return "admin/index";
        } else {
            redirectAttributes.addFlashAttribute("message", "The user name or password is incorrect!");
            return "redirect:/admin";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.removeAttribute("user");
        return "redirect:/";
    }

    @GetMapping("/users")
    public String toAdminBlog(@RequestParam(value = "page", defaultValue = "1") Integer page,
                              @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                              Model model) {
        //分页
        PageHelper.startPage(page, pageSize);
        List<TUser> userList = userService.list(null);
        PageInfo pageInfo = new PageInfo(userList);
        model.addAttribute("userList", userList);
        model.addAttribute("page", page);
        model.addAttribute("total", pageInfo.getTotal());
        return "admin/users";
    }


    //新增user
    @PostMapping("/users")
    public String addUser(TUser user, HttpSession session, RedirectAttributes attributes) {
        if (user.getId() == null) {
            //判断用户名是否存在
            QueryWrapper queryWrapper = new QueryWrapper();
            queryWrapper.eq("username", user.getUsername());
            TUser tUser = userService.getOne(queryWrapper);
            if (tUser != null) {
                attributes.addFlashAttribute("message", "The user name already exists!");
            } else {
                //添加
                user.setPassword(user.getPassword());
                userService.save(user);
                attributes.addFlashAttribute("message", "Added successfully");
            }
        } else {
            //修改
            userService.updateById(user);
            attributes.addFlashAttribute("message", "Successfully modified");
        }
        return "redirect:/admin/users";
    }

    //查看修改user页面(回显)
    @GetMapping("/users/{id}")
    public String toEditBlog(@PathVariable Integer id, Model model) {
        model.addAttribute("user", userService.getById(id));
        return "admin/users-input";
    }

    //查看新增user页面
    @GetMapping("/users/input")
    public String toAddBlogs(Model model) {
        model.addAttribute("user", new TUser());
        return "admin/users-input";
    }

    //删除user
    @GetMapping("/users/delete/{id}")
    public String deleteBlog(@PathVariable Integer id, RedirectAttributes attributes) {
        boolean b = userService.removeById(id);
        if (b) {
            attributes.addFlashAttribute("message", "Successfully deleted");
        } else {
            attributes.addFlashAttribute("message", "Failed to delete");
        }
        return "redirect:/admin/users";
    }


    @GetMapping("/types")
    public String toAdminTypes(@RequestParam(value = "page", defaultValue = "1") Integer page,
                               @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                               Model model) {
        //分页
        PageHelper.startPage(page, pageSize);
        List<TType> typeList = typeService.list(null);
        PageInfo pageInfo = new PageInfo(typeList);
        model.addAttribute("typeList", typeList);
        model.addAttribute("page", page);
        model.addAttribute("total", pageInfo.getTotal());
        return "admin/types";
    }

    //新增type
    @PostMapping("/types")
    public String addType(TType type, RedirectAttributes attributes) {
        //根据name查找Type
        QueryWrapper<TType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", type.getName());
        TType tType = typeService.getOne(queryWrapper);
        if (tType != null) {
            //存在
            attributes.addFlashAttribute("message", "Duplicate categories cannot be added");
            return "redirect:/admin/types/input";
        } else {
            //添加
            typeService.save(type);
            attributes.addFlashAttribute("message", "Added successfully");
            return "redirect:/admin/types";
        }
    }

    //查看修改type页面(回显)
    @GetMapping("/types/{id}")
    public String toEditType(@PathVariable Long id, Model model) {
        model.addAttribute("type", typeService.getById(id));
        return "admin/types-input";
    }

    //修改type
    @PostMapping("/types/update")
    public String editType(TType type, RedirectAttributes attributes) {
        //根据name查找Type
        QueryWrapper<TType> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", type.getName());
        TType tType = typeService.getOne(queryWrapper);
        if (tType != null) {
            //存在
            attributes.addFlashAttribute("message", "The classification already exists");
            return "redirect:/admin/types/" + tType.getId();
        } else {
            //添加
            typeService.updateById(type);
            attributes.addFlashAttribute("message", "Modify successfully");
            return "redirect:/admin/types";
        }
    }

    //查看新增type页面
    @GetMapping("/types/input")
    public String toAddTypes(Model model) {
        model.addAttribute("type", new TType());
        return "admin/types-input";
    }

    //删除type
    @GetMapping("/types/delete/{id}")
    public String deleteType(@PathVariable Integer id, RedirectAttributes attributes) {
        boolean b = typeService.removeById(id);
        if (b) {
            attributes.addFlashAttribute("message", "Successfully deleted");
        } else {
            attributes.addFlashAttribute("message", "Failed to delete");
        }
        return "redirect:/admin/types";
    }


    @GetMapping("/products")
    public String toAdminProduct(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                 @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                 Model model) {
        //分页
        PageHelper.startPage(page, pageSize);
        QueryWrapper<TProduct> wrapper = new QueryWrapper<>();
        wrapper.ne("deleted", "1").or().isNull("deleted");
        List<TProduct> productList = productService.list(wrapper);
        PageInfo pageInfo = new PageInfo(productList);
        model.addAttribute("productList", productList);
        model.addAttribute("page", page);
        model.addAttribute("total", pageInfo.getTotal());
        return "admin/products";
    }


    //新增product
    @PostMapping("/products")
    public String addProduct(TProduct product, Model model, HttpSession session, RedirectAttributes attributes) {
        if (product.getId() == null) {
            //添加
            product.setTypeName(typeService.getById(product.getTypeId()).getName());
            product.setCreateTime(new Date());
            productService.save(product);
            attributes.addFlashAttribute("message", "Successfully added");
        } else {
            //修改
            product.setTypeName(typeService.getById(product.getTypeId()).getName());
            product.setCreateTime(new Date());
            productService.updateById(product);
            attributes.addFlashAttribute("message", "Successfully modified");
        }
        //获取userList
        model.addAttribute("userList", userService.list(null));
        //获取typeList
        model.addAttribute("typeList", typeService.list(null));
        return "redirect:/admin/products";
    }

    //查看修改product页面(回显)
    @GetMapping("/products/{id}")
    public String toEditProduct(@PathVariable Integer id, Model model) {
        model.addAttribute("product", productService.getById(id));
        //获取userList
        model.addAttribute("userList", userService.list(null));
        //获取typeList
        model.addAttribute("typeList", typeService.list(null));
        return "admin/products-input";
    }

    //查看新增product页面
    @GetMapping("/products/input")
    public String toAddProduct(Model model) {
        model.addAttribute("product", new TProduct());
        //获取userList
        model.addAttribute("userList", userService.list(null));
        //获取typeList
        model.addAttribute("typeList", typeService.list(null));
        return "admin/products-input";
    }

    //删除product
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Integer id, RedirectAttributes attributes) {
        TProduct product = new TProduct();
        product.setId(id);
        product.setDeleted(1);
        boolean b = productService.updateById(product);
        if (b) {
            attributes.addFlashAttribute("message", "Successfully deleted");
        } else {
            attributes.addFlashAttribute("message", "Failed to delete");
        }
        return "redirect:/admin/products";
    }


    @GetMapping("/orders")
    public String toAdminOrder(@RequestParam(value = "page", defaultValue = "1") Integer page,
                                 @RequestParam(value = "pageSize", defaultValue = "5") Integer pageSize,
                                 Model model) {
        //分页
        PageHelper.startPage(page, pageSize);
        List<TOrder> orderList = orderService.list(null);
        PageInfo pageInfo = new PageInfo(orderList);
        model.addAttribute("orderList", orderList);
        model.addAttribute("page", page);
        model.addAttribute("total", pageInfo.getTotal());
        return "admin/orders";
    }


    //修改order
    @PostMapping("/orders")
    public String addOrder(TOrder order, Model model, HttpSession session, RedirectAttributes attributes) {
        //修改
        order.setUpdateTime(new Date());
        orderService.updateById(order);
        attributes.addFlashAttribute("message", "Successfully modified");
        return "redirect:/admin/orders";
    }

    //查看修改order页面(回显)
    @GetMapping("/orders/{id}")
    public String toEditOrder(@PathVariable Long id, Model model) {
        model.addAttribute("order", orderService.getById(id));
        return "admin/orders-input";
    }

    //删除order
    @GetMapping("/orders/delete/{id}")
    public String deleteOrder(@PathVariable Long id, RedirectAttributes attributes) {
        boolean b = orderService.removeById(id);
        if (b) {
            attributes.addFlashAttribute("message", "Successfully deleted");
        } else {
            attributes.addFlashAttribute("message", "Failed to delete");
        }
        return "redirect:/admin/orders";
    }
}

