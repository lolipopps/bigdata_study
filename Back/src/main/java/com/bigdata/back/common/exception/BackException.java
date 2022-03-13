package com.bigdata.back.common.exception;

import lombok.Data;

/**
 * @author hyt
 */
@Data
public class BackException extends RuntimeException {

    private String msg;

    public BackException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
