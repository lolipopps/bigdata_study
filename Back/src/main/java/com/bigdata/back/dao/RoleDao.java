package com.bigdata.back.dao;



import com.bigdata.back.base.BaseDao;
import com.bigdata.back.entity.Role;

import java.util.List;

/**
 * 角色数据处理层
 * @author hyt
 */
public interface RoleDao extends BaseDao<Role, String> {

    /**
     * 获取默认角色
     * @param defaultRole
     * @return
     */
    List<Role> findByDefaultRole(Boolean defaultRole);
}
