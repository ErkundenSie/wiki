package net.siehe.wiki.controller;

import net.siehe.wiki.resp.CommonResp;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 统一异常处理、数据预处理等，eg前端返回1001 pagesize
 * 不需要手动调用springboot会自己扫描使用
 */
@ControllerAdvice
public class ControllerExceptionHandler {

    private static final Logger LOG = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    /**
     * 校验异常统一处理
     * 根据日志出现的XXXException来做统一异常处理
     * 统一处理BindException <=根据异常Resolved [org.springframework.validation.BindException
     * @param e
     * @return
     */
    @ExceptionHandler(value = BindException.class)
    @ResponseBody
    public CommonResp validExceptionHandler(BindException e) {
        CommonResp commonResp = new CommonResp();
        LOG.warn("参数校验失败：{}", e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        commonResp.setSuccess(false);
        commonResp.setMessage(e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
        return commonResp;
    }

//    /**
//     * 校验异常统一处理
//     * @param e
//     * @return
//     */
//    @ExceptionHandler(value = BusinessException.class)
//    @ResponseBody
//    public CommonResp validExceptionHandler(BusinessException e) {
//        CommonResp commonResp = new CommonResp();
//        LOG.warn("业务异常：{}", e.getCode().getDesc());
//        commonResp.setSuccess(false);
//        commonResp.setMessage(e.getCode().getDesc());
//        return commonResp;
//    }
//
//    /**
//     * 校验异常统一处理
//     * @param e
//     * @return
//     */
//    @ExceptionHandler(value = Exception.class)
//    @ResponseBody
//    public CommonResp validExceptionHandler(Exception e) {
//        CommonResp commonResp = new CommonResp();
//        LOG.error("系统异常：", e);
//        commonResp.setSuccess(false);
//        commonResp.setMessage("系统出现异常，请联系管理员");
//        return commonResp;
//    }
}
