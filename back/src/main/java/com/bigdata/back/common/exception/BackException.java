package com.bigdata.back.common.exception;

import lombok.Data;

/**
 * @author Exrickx
 */
@Data
public class BackException extends RuntimeException {

    private String msg;

    public BackException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
