package com.ysh.rfid.entity;


import com.ysh.rfid.annotation.Column;
import com.ysh.rfid.annotation.Pk;
import com.ysh.rfid.annotation.Table;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * <p>
 * 用户实体类
 * </p>
 *
 */
@Data
@Table(name = "ysh_rfid")
public class User implements Serializable {
    /**
     * 主键
     */
    @Pk
    private String uid;

    /**
     * 芯片名字
     */
    private String name;

    private String kind;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 上次更新时间
     */
    @Column(name = "last_update_time")
    private Date lastUpdateTime;
}
