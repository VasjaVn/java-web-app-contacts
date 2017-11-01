package com.contacts.dto.outer.response;

import java.io.Serializable;

public class Response implements Serializable {
    public static final String STATUS_FAILED = "failed";
    public static final String STATUS_SUCCESS = "success";

    protected String status;

    public String getStatus() {
        return status;
    }
    public void setStatus(String status) {
        this.status = status;
    }
}
