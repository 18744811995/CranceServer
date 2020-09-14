package com.hegu.tsurutani.handler;

import com.alibaba.fastjson.JSON;
import com.hegu.tsurutani.service.SysLogService;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

@Aspect
@Component
public class WebLogAspect {
    private Logger logger= LoggerFactory.getLogger(WebLogAspect.class);
    private SysLogEntity logEntity;
    @Autowired
    private SysLogService sysLogService;

    @Pointcut("execution(public * com.hegu.tsurutani.app.controller.*.*(..))")
    public void appReq(){
        logger.info("=================>app请求");
    }
    @Pointcut("execution(public * com.hegu.tsurutani.controller.*.*(..))")
    public void adminReq(){
        System.out.println("================>后台管理请求");

    }
    //==============================APP接口拦截器================================================//
    @Before("appReq()")
    public void appDoBefore(JoinPoint joinPoint){
        logger.info("WebLogAspect.appDoBefore()");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        logEntity=new SysLogEntity();
        logEntity.setReqUrl(request.getRequestURL().toString());
        logEntity.setReqMethod(request.getMethod());
        logEntity.setIp(request.getRemoteAddr());
        logEntity.setClassMethodName(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logEntity.setReqTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        logEntity.setSouType("APP");
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            logger.info("================>第" + (i+1) + "个参数为:" + args[i]);
            if(String.valueOf(args[i]).contains("com.hegu.tsurutani.entity")){
                logger.info("==============>存在实体类");
                logEntity.setReqParams(JSON.toJSONString(args[i]));
                logger.info("=============>对象值："+logEntity.getReqParams());
            }
        }
    }
    @AfterReturning(returning = "ret",pointcut ="appReq()")
    public void appDoAfterReturning(Object ret){
        // 处理完请求，返回内容
        logger.info("WebLogAspect.doAfterReturning()");
        logEntity.setStatus("success");
        logEntity.setResult(JSON.toJSONString(ret));
        sysLogService.saveSysLog(logEntity);
        logger.info("==================>result:"+logEntity.getResult());
    }

    @AfterThrowing(pointcut ="appReq()",throwing = "error")
    public void afterThrowing(Throwable error){
        logger.info("【注解：AfterThrowing】方法异常时执行.....");
        logEntity.setStatus("error");
        logEntity.setResult(error.getMessage());
        sysLogService.saveSysLog(logEntity);
        logger.info("==================>result:"+logEntity.getResult());
    }
//==============================APP接口拦截器================================================//
//==============================后台管理拦截器================================================//
    @Before("adminReq()")
    public void adminDoBefore(JoinPoint joinPoint){
        System.out.println(joinPoint);
        logger.info("WebLogAspect.adminDoBefore()");
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        logEntity=new SysLogEntity();
        logEntity.setReqUrl(request.getRequestURL().toString());
        logEntity.setReqMethod(request.getMethod());
        logEntity.setIp(request.getRemoteAddr());
        logEntity.setClassMethodName(joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
        logEntity.setReqTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        logEntity.setSouType("Admin");
        Object[] args = joinPoint.getArgs();
        for (int i = 0; i < args.length; i++) {
            logger.info("================>第" + (i+1) + "个参数为:" + args[i]);
            if(String.valueOf(args[i]).contains("com.hegu.tsurutani.entity")){
                logger.info("==============>存在实体类");
                logEntity.setReqParams(JSON.toJSONString(args[i]));
                logger.info("=============>对象值："+logEntity.getReqParams());
            }
        }
    }
    @AfterReturning(returning = "ret",pointcut ="adminReq()")
    public void adminDoAfterReturning(Object ret){
        // 处理完请求，返回内容
        logger.info("WebLogAspect.adminDoAfterReturning()");
        logEntity.setStatus("success");
        logEntity.setResult(JSON.toJSONString(ret));
        sysLogService.saveSysLog(logEntity);
        logger.info("==================>result:"+logEntity.getResult());
    }

    @AfterThrowing(pointcut ="adminReq()",throwing = "error")
    public void adminAfterThrowing(Throwable error){
        logger.info("【注解：AfterThrowing】方法异常时执行.....");
        logEntity.setStatus("error");
        logEntity.setResult(error.getMessage());
        sysLogService.saveSysLog(logEntity);
        logger.info("==================>result:"+logEntity.getResult());
    }
//==============================后台管理拦截器================================================//
}
