package com.p2pdinner.common;

import java.io.Serializable;

/**
 * Created by rajaniy on 7/21/15.
 */
public class ErrorResponse implements Serializable {
    private String message;
    private int status;
    private String code;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
