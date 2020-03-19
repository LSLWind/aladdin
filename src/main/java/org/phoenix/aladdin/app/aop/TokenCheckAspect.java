package org.phoenix.aladdin.app.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.phoenix.aladdin.app.constant.AppConstant;
import org.phoenix.aladdin.error.BusinessException;
import org.phoenix.aladdin.error.EmBusinessError;
import org.phoenix.aladdin.response.CommonReturnType;
import org.phoenix.aladdin.util.JSONUtil;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * 定义一个切面处理token验证操作
 */
//TODO 校验切面，未启用
@Aspect
@Component
public class TokenCheckAspect {
    public TokenCheckAspect(){

    }

    @Around(value = "execution(public * org.phoenix.aladdin.app.client.controller.*.*(..))")
    public CommonReturnType tokenCheck(ProceedingJoinPoint pjp) throws BusinessException{
        //校验token是否正确
        try {
            Object[] args=pjp.getArgs();
            HttpServletRequest request=null;
            HttpSession session=null;
            for(Object o:args){
                if(o instanceof HttpServletRequest)request=(HttpServletRequest) o;
                else if(o instanceof HttpSession) session=(HttpSession)o;
            }
             //拦截请求不到请求则判定为非法
            if(request==null||session==null){
                throw new BusinessException(EmBusinessError.REQUEST_ILLEGAL_ERROR);
            }
            //TODO 测试
            session.setAttribute("openid","oAqzI5WVdw_NxibU-uZWsIMcaKSk");
            session.setAttribute("session_key","x/oiB8xnE3yrUdmJnTPD7Q==");


            /** TODO 暂时关闭token校验
            String token = request.getHeader("token");
            String sessionKey = (String) session.getAttribute("session_key");
            if (token == null || !token.equals(sessionKey)) {
                return CommonReturnType.create(JSONUtil.oneErrorData(EmBusinessError.REQUEST_ILLEGAL_ERROR.getErrMsg()),
                        EmBusinessError.REQUEST_ILLEGAL_ERROR.getErrCode());
            }
             */
            //重新设置session时间120分钟
            session.setMaxInactiveInterval(120*60);
            System.out.println("AOP校验拦截token");
            return (CommonReturnType) pjp.proceed();
        }catch (Throwable e){
            e.printStackTrace();
        }
        return CommonReturnType.create(EmBusinessError.UNKNOWN_ERROR);
    }
}
