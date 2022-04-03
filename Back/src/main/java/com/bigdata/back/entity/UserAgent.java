package com.bigdata.back.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import com.bigdata.back.base.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 爬虫User-Agent池实体对象
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "t_user_agent")
@TableName("t_user_agent")
@ApiModel(value = "Agent池实体对象")
public class UserAgent extends BaseEntity {

    @ApiModelProperty(value = "User Agent")
    private String userAgent;
}