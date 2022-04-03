package com.bigdata.back.serviceimpl;

import com.bigdata.back.common.vo.SearchVo;
import com.bigdata.back.dao.LogDao;
import com.bigdata.back.entity.Log;
import com.bigdata.back.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 日志接口实现
 * @author hyt
 */
@Slf4j
@Service
@Transactional
public class LogServiceImpl implements LogService {

    @Autowired
    private LogDao logDao;

    @Override
    public LogDao getRepository() {
        return logDao;
    }


    @Override
    public Page<Log> findByConfition(Integer type, String key, SearchVo searchVo, Pageable pageable) {
        return null;
    }

    @Override
    public void deleteAll() {

        logDao.deleteAll();
    }
}
