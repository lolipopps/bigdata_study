package com.bigdata.back.controller;

import cn.hutool.core.util.StrUtil;
import com.bigdata.back.common.exception.BackException;
import com.bigdata.back.common.utils.PageUtil;
import com.bigdata.back.common.utils.ResultUtil;
import com.bigdata.back.common.vo.PageVo;
import com.bigdata.back.common.vo.Result;
import com.bigdata.back.common.vo.SearchVo;
import com.bigdata.back.entity.User;
import com.bigdata.back.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.util.List;


/**
 * @author hyt
 */
@Slf4j
@RestController
@Api(tags = "用户接口")
@RequestMapping("/user")
@CacheConfig(cacheNames = "user")
@Transactional
public class UserController {

    public static final String USER = "user::";

    @Autowired
    private UserService userService;


    @PersistenceContext
    private EntityManager entityManager;

    @RequestMapping(value = "/regist", method = RequestMethod.POST)
    @ApiOperation(value = "注册用户")
    public Result<Object> regist(@Valid User u) {

        // 校验是否已存在
        checkUserInfo(u.getUsername(), u.getMobile(), u.getEmail());

        User user = userService.save(u);


        return ResultUtil.data(user);
    }

    @RequestMapping(value = "/resetPass", method = RequestMethod.POST)
    @ApiOperation(value = "重置密码")
    public Result<Object> resetPass(@RequestParam String[] ids) {

        for (String id : ids) {
            User u = userService.get(id);
            // 在线DEMO所需
            if ("test".equals(u.getUsername()) || "test2".equals(u.getUsername()) || "admin".equals(u.getUsername())) {
                throw new BackException("测试账号及管理员账号不得重置");
            }

            userService.update(u);

        }
        return ResultUtil.success("操作成功");
    }


    @RequestMapping(value = "/getByCondition", method = RequestMethod.GET)
    @ApiOperation(value = "多条件分页获取用户列表")
    public Result<Page<User>> getByCondition(User user,
                                             SearchVo searchVo,
                                             PageVo pageVo) {

        Page<User> page = userService.findByCondition(user, searchVo, PageUtil.initPage(pageVo));
        for (User u : page.getContent()) {
            // 关联角色


            // 游离态 避免后面语句导致持久化
            entityManager.detach(u);
            u.setPassword(null);
        }
        return new ResultUtil<Page<User>>().setData(page);
    }


    @RequestMapping(value = "/getAll", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部用户数据")
    public Result<List<User>> getAll() {

        List<User> list = userService.getAll();
        // 清除持久上下文环境 避免后面语句导致持久化
        entityManager.clear();
        for (User u : list) {
            u.setPassword(null);
        }
        return new ResultUtil<List<User>>().setData(list);
    }

    @RequestMapping(value = "/admin/add", method = RequestMethod.POST)
    @ApiOperation(value = "添加用户")
    public Result<Object> add(@Valid User u,
                              @RequestParam(required = false) String[] roleIds) {

        // 校验是否已存在
        checkUserInfo(u.getUsername(), u.getMobile(), u.getEmail());



        User user = userService.save(u);


        return ResultUtil.success("添加成功");
    }


    @RequestMapping(value = "/admin/disable/{userId}", method = RequestMethod.POST)
    @ApiOperation(value = "后台禁用用户")
    public Result<Object> disable(@ApiParam("用户唯一id标识") @PathVariable String userId) {

        User user = userService.get(userId);
        userService.update(user);
        // 手动更新缓存

        return ResultUtil.success("操作成功");
    }

    @RequestMapping(value = "/admin/enable/{userId}", method = RequestMethod.POST)
    @ApiOperation(value = "后台启用用户")
    public Result<Object> enable(@ApiParam("用户唯一id标识") @PathVariable String userId) {

        User user = userService.get(userId);
        userService.update(user);
        // 手动更新缓存

        return ResultUtil.success("操作成功");
    }

    @RequestMapping(value = "/delByIds", method = RequestMethod.POST)
    @ApiOperation(value = "批量通过ids删除")
    public Result<Object> delAllByIds(@RequestParam String[] ids) {

        for (String id : ids) {
            User u = userService.get(id);


            userService.delete(id);

        }
        return ResultUtil.success("批量通过id删除数据成功");
    }

    /**
     * 校验
     *
     * @param username 用户名 不校验传空字符或null 下同
     * @param mobile   手机号
     * @param email    邮箱
     */
    public void checkUserInfo(String username, String mobile, String email) {

        if (StrUtil.isNotBlank(username) && userService.findByUsername(username) != null) {
            throw new BackException("该登录账号已被注册");
        }
        if (StrUtil.isNotBlank(email) && userService.findByEmail(email) != null) {
            throw new BackException("该邮箱已被注册");
        }
        if (StrUtil.isNotBlank(mobile) && userService.findByMobile(mobile) != null) {
            throw new BackException("该手机号已被注册");
        }
    }
}

