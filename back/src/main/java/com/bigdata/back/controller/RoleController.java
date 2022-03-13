package com.bigdata.back.controller;

import com.bigdata.back.common.utils.PageUtil;
import com.bigdata.back.common.utils.ResultUtil;
import com.bigdata.back.common.vo.PageVo;
import com.bigdata.back.common.vo.Result;
import com.bigdata.back.entity.Role;
import com.bigdata.back.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author Exrickx
 */
@Slf4j
@RestController
@Api(tags = "角色管理接口")
@RequestMapping("/role")
@Transactional
public class RoleController {

    @Autowired
    private RoleService roleService;




    @RequestMapping(value = "/getAllList", method = RequestMethod.GET)
    @ApiOperation(value = "获取全部角色")
    public Result<Object> roleGetAll() {

        List<Role> list = roleService.getAll();
        return ResultUtil.data(list);
    }

    @RequestMapping(value = "/getAllByPage", method = RequestMethod.GET)
    @ApiOperation(value = "分页获取角色")
    public Result<Page<Role>> getRoleByPage(String key, PageVo page) {

        Page<Role> list = roleService.findByCondition(key, PageUtil.initPage(page));

        return new ResultUtil<Page<Role>>().setData(list);
    }

    @RequestMapping(value = "/setDefault", method = RequestMethod.POST)
    @ApiOperation(value = "设置或取消默认角色")
    public Result<Object> setDefault(@RequestParam String id,
                                     @RequestParam Boolean isDefault) {

        Role role = roleService.get(id);
        if (role == null) {
            return ResultUtil.error("角色不存在");
        }
        role.setDefaultRole(isDefault);
        roleService.update(role);
        return ResultUtil.success("设置成功");
    }

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    @ApiOperation(value = "保存数据")
    public Result<Role> save(Role role) {

        Role r = roleService.save(role);
        return new ResultUtil<Role>().setData(r);
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    @ApiOperation(value = "更新数据")
    public Result<Role> edit(Role entity) {

        Role r = roleService.update(entity);

        return new ResultUtil<Role>().setData(r);
    }


}
