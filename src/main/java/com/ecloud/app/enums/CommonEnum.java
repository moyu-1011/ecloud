package com.ecloud.app.enums;

import com.ecloud.app.controller.advice.BaseErrorInterface;

public enum CommonEnum implements BaseErrorInterface {
    SUCCESS("200", "成功!"),
    IMAGE_PATTERN_ERROR("400", "图片格式仅支持JPG/PNG/BMP/JEPG/WBMP!"),
    BODY_NOT_MATCH("400", "空指针异常!");

    private String resultCode;
    private String resultMsg;

    CommonEnum(String resultCode, String resultMsg) {
        this.resultCode = resultCode;
        this.resultMsg = resultMsg;
    }


    @Override
    public String getResultCode() {
        return resultCode;
    }

    @Override
    public String getResultMsg() {
        return resultMsg;
    }
}
