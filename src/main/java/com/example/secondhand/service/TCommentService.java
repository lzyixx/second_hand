package com.example.secondhand.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.example.secondhand.entity.TComment;

import java.util.List;

public interface TCommentService extends IService<TComment> {

    List<TComment> listComments(Integer productId);

    void saveComment(TComment comment);
}
