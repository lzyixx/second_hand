package com.example.secondhand.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class TOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ID_WORKER_STR)
    private Long id;

    private Integer userId;

    //0 未支付，1已支付
    private Integer payStatus;

    private Date createTime;

    private Date updateTime;

    private Integer productId;

    private BigDecimal totalPrice;

    //物流公司(配送方式)
    private String deliveryCompany;

    //物流单号
    private String deliverySn;

    //收货人姓名
    private String receiverName;

    //收货人电话
    private String receiverPhone;

    //收货人地址
    private String receiverAddress;

    //订单备注
    private String note;

    //确认收货状态：0->未确认；1->已确认
    private Integer confirmStatus;

    @TableField(exist = false)
    private TProduct product;


}
