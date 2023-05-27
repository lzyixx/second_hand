package com.example.secondhand.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.secondhand.entity.TComment;
import com.example.secondhand.mapper.TCommentMapper;
import com.example.secondhand.mapper.TUserMapper;
import com.example.secondhand.service.TCommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class TCommentServiceImpl extends ServiceImpl<TCommentMapper, TComment> implements TCommentService {
    @Autowired
    private TCommentMapper commentMapper;

    @Autowired
    private TUserMapper userMapper;

    @Override
    public List<TComment> listComments(Integer productId) {
        //找到所有评论
        QueryWrapper<TComment> queryWrapper = new QueryWrapper<>();
        queryWrapper.isNull("parent_comment_id");
        queryWrapper.eq("product_id", productId);
        queryWrapper.orderByDesc("create_time");
        List<TComment> comments = commentMapper.selectList(queryWrapper);
        for (TComment comment : comments) {
            //查找所有子评论（只分为两级）
            List<TComment> childs = commentMapper.selectList(new QueryWrapper<TComment>().eq("foreparent_id", comment.getId()));
            for (TComment child : childs) {
                TComment parentComment = commentMapper.selectById(child.getParentCommentId());
                parentComment.setUser(userMapper.selectById(parentComment.getUserId()));
                child.setParentComment(parentComment);
                child.setUser(userMapper.selectById(child.getUserId()));
            }
            comment.setReplyComments(childs);
            comment.setUser(userMapper.selectById(comment.getUserId()));
        }
        return comments;
    }

    @Override
    public void saveComment(TComment comment) {
        if (comment.getParentCommentId() == -1) {
            comment.setParentCommentId(null);
        }
        if(comment.getForeparentId()==-1){
            comment.setForeparentId(null);
        }
        comment.setCreateTime(new Date());
        commentMapper.insert(comment);
    }
}
