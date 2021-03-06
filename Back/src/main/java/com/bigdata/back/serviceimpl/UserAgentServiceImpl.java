package com.bigdata.back.serviceimpl;

import com.bigdata.back.dao.UserAgentDao;
import com.bigdata.back.service.UserAgentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@Transactional
public class UserAgentServiceImpl implements UserAgentService {

    @Autowired
    private UserAgentDao userAgentDao;

    @Override
    public UserAgentDao getRepository() {
        return userAgentDao;
    }


}
