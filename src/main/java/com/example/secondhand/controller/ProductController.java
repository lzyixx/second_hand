package com.example.secondhand.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.secondhand.entity.TComment;
import com.example.secondhand.entity.TProduct;
import com.example.secondhand.entity.TUser;
import com.example.secondhand.service.TCommentService;
import com.example.secondhand.service.TProductService;
import com.example.secondhand.service.TTypeService;
import com.example.secondhand.service.TUserService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class ProductController {
    @Autowired
    private TProductService productService;

    @Autowired
    private TTypeService typeService;

    @Autowired
    private TUserService userService;

    @Autowired
    private TCommentService commentService;

    @GetMapping("/listProducts")
    public String listProducts(@RequestParam(value = "keyword", required = false) String keyword,
                               @RequestParam(value = "typeId", required = false) Integer typeId,
                               @RequestParam(value = "userId", required = false) Integer userId,
                               @RequestParam(value = "page", defaultValue = "1") Integer page,
                               @RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize,
                               @RequestParam(value = "minPrice",required = false) Double minPrice,
                               @RequestParam(value = "maxPrice", required = false) Double maxPrice,
                               Model model) {
        //分页
        PageHelper.startPage(page, pageSize);
        QueryWrapper<TProduct> queryWrapper = new QueryWrapper<>();
        queryWrapper.ne("deleted", "1").or().isNull("deleted");
        if(StringUtils.isNotBlank(keyword)){
            queryWrapper.like("name", keyword);
        }
        if(typeId!=null){
            queryWrapper.eq("type_id", typeId);
        }
        if(userId!=null){
            queryWrapper.eq("user_id", userId);
        }
        if(minPrice!=null){
            queryWrapper.ge("price", minPrice);
        }
        if(maxPrice!=null){
            queryWrapper.le("price", maxPrice);
        }
        List<TProduct> productList = productService.list(queryWrapper);
        PageInfo pageInfo = new PageInfo(productList);
        model.addAttribute("productList", productList);
        model.addAttribute("keyword", keyword);
        model.addAttribute("page", page);
        model.addAttribute("total", pageInfo.getTotal());
        model.addAttribute("pages", Math.max(pageInfo.getPages(), 1));
        return "products";
    }

    @GetMapping("/myListing")
    public String toMyProduct(@RequestParam(value = "page", defaultValue = "1") Integer page,
                              @RequestParam(value = "pageSize", defaultValue = "6") Integer pageSize,
                              Model model, HttpSession session) {
        //分页
        PageHelper.startPage(page, pageSize);
        TUser user = (TUser) session.getAttribute("user");
        if (user != null) {
            QueryWrapper<TProduct> queryWrapper = new QueryWrapper();
            queryWrapper.eq("user_id", user.getId());
            queryWrapper.ne("deleted", "1").or().isNull("deleted");
            List<TProduct> productList = productService.list(queryWrapper);
            PageInfo pageInfo = new PageInfo(productList);
            model.addAttribute("productList", productList);
            model.addAttribute("page", page);
            model.addAttribute("total", pageInfo.getTotal());
            model.addAttribute("pages", Math.max(pageInfo.getPages(), 1));
            return "myListing";
        } else {
            //登录
            return "login";
        }
    }


    //新增product
    @PostMapping("/products")
    @ResponseBody
    public String addMyProduct(TProduct product, HttpSession session) {
        TUser user = (TUser) session.getAttribute("user");
        if(user!=null){
            if (product.getId() == null) {
                //添加
                product.setTypeName(typeService.getById(product.getTypeId()).getName());
                product.setCreateTime(new Date());
                product.setUserId(user.getId());
                productService.save(product);
            } else {
                //修改
                product.setTypeName(typeService.getById(product.getTypeId()).getName());
                product.setCreateTime(new Date());
                productService.updateById(product);
            }
            return "success";
        }
        return "error";
    }

    //查看修改product页面(回显)
    @GetMapping("/products/{id}")
    public String toEditProduct(@PathVariable Integer id, Model model) {
        model.addAttribute("product", productService.getById(id));
        //获取typeList
        model.addAttribute("typeList", typeService.list(null));
        return "products-input";
    }

    //查看新增product页面
    @GetMapping("/products/input")
    public String toAddProduct(Model model) {
        model.addAttribute("product", new TProduct());
        //获取typeList
        model.addAttribute("typeList", typeService.list(null));
        return "products-input";
    }

    //删除product
    @GetMapping("/products/delete/{id}")
    public String deleteProduct(@PathVariable Integer id, RedirectAttributes attributes) {
        TProduct product = productService.getById(id);
        if(product!=null){
            product.setDeleted(1);
            productService.updateById(product);
            attributes.addFlashAttribute("message", "Successfully deleted");
        }else {
            attributes.addFlashAttribute("message", "Failed to delete");
        }
        return "redirect:/myListing";
    }

    //商品详情页
    @GetMapping("/productDetail")
    public String productDetail(@RequestParam(required = false) Integer id,
                                @RequestParam(required = false) Integer canBuy,
                                Model model) {
        TProduct tProduct = productService.getById(id);
        //补充卖家信息
        if (tProduct.getUserId() != null) {
            tProduct.setUser(userService.getById(tProduct.getUserId()));
        } else {
            tProduct.setUser(new TUser().setNickname("平台自营"));
        }
        model.addAttribute("product", tProduct);
        model.addAttribute("canBuy", canBuy);
        return "productDetail";
    }

    @RequestMapping("/comments")
    public String comments(Integer productId,Model model) {
        model.addAttribute("comments", commentService.listComments(productId));
        return "productDetail::commentList";
    }


    @PostMapping("/comments")
    @ResponseBody
    public String post(TComment comment, HttpSession session) {
        TUser user = (TUser) session.getAttribute("user");
        if (user != null) {
            comment.setUserId(user.getId());
            TProduct product = productService.getById(comment.getProductId());
            //判断是否是卖家
            if (product!=null&&comment.getUserId().equals(product.getUserId())) {
                comment.setAdminComment(true);
            }
            commentService.saveComment(comment);
            return "comment successfully";
        }
        return "error";
    }

    @PostMapping("/comment/delete")
    @ResponseBody
    public String deleteComment(String id) {
        if (StringUtils.isBlank(id)) {
            return "Failed to delete";
        }else {
            commentService.removeById(Long.parseLong(id));
            return "Successfully deleted";
        }
    }

}

