package com.bigdata.back.common.exception;

import lombok.Data;

/**
 * @author hyt
 */
@Data
public class LimitException extends RuntimeException {

    private String msg;

    public LimitException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
