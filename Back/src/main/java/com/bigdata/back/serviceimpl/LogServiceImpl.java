package com.bigdata.back.serviceimpl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.bigdata.back.common.vo.SearchVo;
import com.bigdata.back.dao.LogDao;
import com.bigdata.back.entity.Log;
import com.bigdata.back.service.LogService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

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
