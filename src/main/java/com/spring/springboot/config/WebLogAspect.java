package com.spring.springboot.config;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/** 
* @author   dfz
* 作者 E-mail:  dfz@jkinvest.cn
* @date 创建时间：2017年4月20日 下午2:39:24 
* @version 1.0 
* @parameter  
* @since  
* @return  
*/

/**
 *  web层的aspect管理日志
 * @author JK
 *
 */

@Aspect
@Component
public class WebLogAspect {
	  private Logger logger = Logger.getLogger(getClass());
	  
	  ThreadLocal<Long> startTime = new ThreadLocal<>();
	  
	 @Pointcut("execution(public * com.spring.springboot.controller..*.*(..)))")
	public void webLog(){}
	
	 @Before("webLog()")  
	 public void doBefore(JoinPoint joinPoint) throws Throwable {
		    startTime.set(System.currentTimeMillis());
		// 接收到请求，记录请求内容  
	        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();  
	        HttpServletRequest request = attributes.getRequest();
	     // 记录下请求内容
	        logger.info("URL====== : " + request.getRequestURL().toString());
	        logger.info("HTTP_METHOD====== : " + request.getMethod());
	        logger.info("IP====== : " + request.getRemoteAddr());
	        logger.info("CLASS_METHOD====== : " + joinPoint.getSignature().getDeclaringTypeName() + "." + joinPoint.getSignature().getName());
	        logger.info("ARGS====== : " + Arrays.toString(joinPoint.getArgs()));
		 
	 }
	 
	 @AfterReturning(returning = "ret", pointcut = "webLog()")
     public void doAfterReturning(Object ret) throws Throwable {
        // 处理完请求，返回内容
        logger.info("RESPONSE : " + ret);      
        logger.info("SPEND TIME : " + (System.currentTimeMillis() - startTime.get()));
     }
}
