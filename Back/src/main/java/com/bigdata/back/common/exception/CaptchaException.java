package com.bigdata.back.common.exception;

import lombok.Data;

/**
 * @author hyt
 */
@Data
public class CaptchaException extends RuntimeException {

    private String msg;

    public CaptchaException(String msg) {
        super(msg);
        this.msg = msg;
    }
}
