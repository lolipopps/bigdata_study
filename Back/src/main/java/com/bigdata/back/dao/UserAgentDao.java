package com.bigdata.back.dao;

import com.bigdata.back.base.BaseDao;
import com.bigdata.back.entity.UserAgent;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAgentDao extends BaseDao<UserAgent, String> {
}
