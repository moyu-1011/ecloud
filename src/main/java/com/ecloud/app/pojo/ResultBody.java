package com.ecloud.app.pojo;

import com.ecloud.app.controller.advice.BaseErrorInterface;
import com.ecloud.app.enums.CommonEnum;

public class ResultBody {
    /**
     * 响应码
     **/
    private String resultCode;

    /**
     * 响应信息
     **/
    private String resultMsg;

    /**
     * 响应结果
     **/
    private Object result;

    public ResultBody() {
    }

    public ResultBody(BaseErrorInterface baseErrorInterface) {
        this.resultCode = baseErrorInterface.getResultCode();
        this.resultMsg = baseErrorInterface.getResultMsg();
    }

    public String getResultCode() {
        return resultCode;
    }

    public void setResultCode(String resultCode) {
        this.resultCode = resultCode;
    }

    public String getResultMsg() {
        return resultMsg;
    }

    public void setResultMsg(String resultMsg) {
        this.resultMsg = resultMsg;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }

    /**
     * 成功
     * @return
     */
    public static ResultBody success(){
        return success(null);
    }

    /**
     * 成功
     * @param data
     * @return
     */
    public static ResultBody success(Object data){
        ResultBody rb = new ResultBody();
        rb.setResultCode(CommonEnum.SUCCESS.getResultCode());
        rb.setResultMsg(CommonEnum.SUCCESS.getResultMsg());
        rb.setResult(data);
        return rb;
    }

    /**
     * 失败
     */
    public static ResultBody error(BaseErrorInterface error) {
        ResultBody rb = new ResultBody();
        rb.setResultCode(error.getResultCode());
        rb.setResultMsg(error.getResultMsg());
        rb.setResult(null);
        return rb;
    }

    /**
     * 失败
     */
    public static ResultBody error(String code, String message) {
        ResultBody rb = new ResultBody();
        rb.setResultCode(code);
        rb.setResultMsg(message);
        rb.setResult(null);
        return rb;
    }

    /**
     * 失败
     */
    public static ResultBody error(String message) {
        ResultBody rb = new ResultBody();
        rb.setResultCode("-1");
        rb.setResultMsg(message);
        rb.setResult(null);
        return rb;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
