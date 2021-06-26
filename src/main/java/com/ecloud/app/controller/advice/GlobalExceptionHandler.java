package com.ecloud.app.controller.advice;

import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.ecloud.app.enums.CommonEnum;
import com.ecloud.app.pojo.ResultBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * 无效的bucket异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = AmazonS3Exception.class)
    public ResultBody exceptionHandler(AmazonS3Exception e) {
        logger.error("异常信息: ", e.getMessage());
        return ResultBody.error(CommonEnum.IMAGE_PATTERN_ERROR);
    }

    /**
     * 空指针异常
     *
     * @param e
     * @return
     */
    @ExceptionHandler(value = NullPointerException.class)
    public ResultBody exceptionHandler(NullPointerException e) {
        logger.error("异常信息: ", e);
        return ResultBody.error(CommonEnum.BODY_NOT_MATCH);
    }

}
