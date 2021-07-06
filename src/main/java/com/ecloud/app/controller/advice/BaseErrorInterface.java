package com.ecloud.app.controller.advice;

public interface BaseErrorInterface {
    /** 错误码 **/
    String getResultCode();

    /** 错误描述 **/
    String getResultMsg();
}
