package com.biu.wifi.campus.dao.model;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * biu_travel_expense_notice
 * @author 
 */
@Data
public class BiuTravelExpenseNotice implements Serializable {
    /**
     * 主键
     */
    private Integer id;

    /**
     * 申请ID
     */
    private Integer expenseId;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 备注
     */
    private String remark;

    /**
     * 通知接收人ID
     */
    private Integer toUserId;

    /**
     * 是否已读(1是，2否)
     */
    private Short isRead;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    /**
     * 是否删除(1是，2否)
     */
    private Short isDelete;

    /**
     * 删除时间
     */
    private Date deleteTime;

    /**
     * 是否收到(1是，2否)
     */
    private Short isConfirm;

    /**
     * 收到确认时间
     */
    private Date confirmTime;

    private static final long serialVersionUID = 1L;
}