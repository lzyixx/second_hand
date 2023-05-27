package com.example.secondhand.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TComment implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private Integer userId;

    private String content;

    private Date createTime;

    private Long parentCommentId;

    private Boolean adminComment;

    private Long foreparentId;

    private Integer productId;

    @TableField(exist = false)
    private TUser user;

    @TableField(exist = false)
    private List<TComment> replyComments = new ArrayList<>();

    @TableField(exist = false)
    private TComment parentComment;
}
