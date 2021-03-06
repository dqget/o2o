package com.lovesickness.o2o.common.aop;

import com.lovesickness.o2o.util.ResultBean;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 处理和包装异常
 */
@Aspect
@Component
public class ControllerAOP {
    private static final Logger logger = LoggerFactory.getLogger(ControllerAOP.class);

    @Around("execution(public com.lovesickness.o2o.util.ResultBean *(..))")
    public Object handlerControllerMethod(ProceedingJoinPoint pjp) {
        long startTime = System.currentTimeMillis();
        ResultBean<?> result;
        try {
            result = (ResultBean<?>) pjp.proceed();
            logger.info(pjp.getSignature() + "use time:" + (System.currentTimeMillis() - startTime));
        } catch (Throwable e) {
            result = handlerException(pjp, e);
        }
        return result;
    }

    private ResultBean<?> handlerException(ProceedingJoinPoint pjp, Throwable e) {
        ResultBean<?> result = new ResultBean<>();
        // 已知异常
        if (e instanceof RuntimeException) {
            logger.error(pjp.getSignature() + " error ", e);
            result.setMsg(e.getLocalizedMessage() == null ? e.toString() : e.getLocalizedMessage());
            result.setCode(ResultBean.FAIL);
            result.setSuccess(false);
        } else {
            logger.error(pjp.getSignature() + " error ", e);
            e.printStackTrace();
            //TODO 未知的异常，应该格外注意，可以发送邮件通知等
            result.setMsg(e.toString());
            result.setCode(ResultBean.FAIL);
            result.setSuccess(false);
        }
        return result;
    }
}
