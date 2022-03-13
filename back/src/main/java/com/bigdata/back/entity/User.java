package com.bigdata.back.entity;

import com.bigdata.back.base.BaseEntity;
import com.bigdata.back.common.utils.NameUtil;
import com.bigdata.back.common.vo.PermissionDTO;
import com.bigdata.back.common.vo.RoleDTO;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.List;

/**
 * @author Exrickx
 */
@Data
@Accessors(chain = true)
@Entity
@DynamicInsert
@DynamicUpdate
@Table(name = "t_user")
@TableName("t_user")
@ApiModel(value = "用户")
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "登录名")
    @Column(unique = true, nullable = false)
    @Pattern(regexp = NameUtil.regUsername, message = "登录账号不能包含特殊字符且长度不能>16")
    private String username;

    @ApiModelProperty(value = "密码")
    @NotNull(message = "不能为空")
    private String password;

    @ApiModelProperty(value = "用户名/昵称/姓名")
    @NotBlank(message = "不能为空")
    @Size(max = 20, message = "昵称长度不能超过20")
    private String nickname;

    @ApiModelProperty(value = "手机")
    @Pattern(regexp = NameUtil.regMobile, message = "11位手机号格式不正确")
    private String mobile;

    @ApiModelProperty(value = "邮箱")
    @Pattern(regexp = NameUtil.regEmail, message = "邮箱格式不正确")
    private String email;

    @ApiModelProperty(value = "街道地址")
    private String street;

    @ApiModelProperty(value = "性别")
    private String sex;

    @ApiModelProperty(value = "描述/详情/备注")
    private String description;

    @JsonFormat(timezone = "GMT+8", pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "生日")
    private Date birth;

}
