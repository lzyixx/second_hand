package com.example.secondhand.service;

import com.example.secondhand.entity.TUser;
import com.baomidou.mybatisplus.extension.service.IService;

public interface TUserService extends IService<TUser> {

    TUser checkUser(String username, String code);
}
