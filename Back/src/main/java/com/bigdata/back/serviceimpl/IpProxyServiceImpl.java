package com.bigdata.back.serviceimpl;

import com.bigdata.back.dao.IpProxyDao;
import com.bigdata.back.entity.IpProxy;
import com.bigdata.back.service.IpProxyService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Slf4j
@Service
@Transactional
public class IpProxyServiceImpl implements IpProxyService {

    @Autowired
    private IpProxyDao ipProxyDao;

    @Override
    public IpProxyDao getRepository() {
        return ipProxyDao;
    }


}
