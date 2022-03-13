package com.bigdata.back.service;



import com.bigdata.back.base.BaseService;
import com.bigdata.back.common.vo.SearchVo;

import com.bigdata.back.entity.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 日志接口
 * @author hyt
 */
public interface LogService extends BaseService<Log, String> {

    /**
     * 分页搜索获取日志
     * @param type
     * @param key
     * @param searchVo
     * @param pageable
     * @return
     */
    Page<Log> findByConfition(Integer type, String key, SearchVo searchVo, Pageable pageable);

    /**
     * 删除所有
     */
    void deleteAll();
}
