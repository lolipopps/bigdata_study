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
 * 爬虫IP代理池实体对象
 */
@Data
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "t_ip_proxy")
@TableName("t_ip_proxy")
@ApiModel(value = "IP代理")
public class IpProxy extends BaseEntity {


    @ApiModelProperty(value = "ip地址")
    private String ip;

    @ApiModelProperty(value = "端口")
    private String port;

    @ApiModelProperty(value = "城市")
    private String city;

    @ApiModelProperty(value = "运营商")
    private String operator;


}
