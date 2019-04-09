package com.lovesickness.o2o.interceptor.shopadmin;

import com.lovesickness.o2o.entity.PersonInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * @author 懿
 */
public class ShopLoginInterceptor extends HandlerInterceptorAdapter {
    private static Logger log = LoggerFactory.getLogger(ShopLoginInterceptor.class);

    /**
     * 主要做事前拦截，即用户操作发生前，改写perHandle里的逻辑，进行拦截
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("请求地址:" + request.getServerName() + ":" + request.getServerPort() + request.getRequestURI());
        //从session中取出用户信息
        Object userObj = request.getSession().getAttribute("user");
        if (userObj != null) {
            //若用户信息不为空，将session中的用户信息转为PersonInfo实体类
            PersonInfo user = (PersonInfo) userObj;
            log.info("请求的用户信息：" + user);
            //空值判断 确保userId不为空  状态为1  用户类型为店家
            if (user.getUserId() != null && user.getUserId() > 0 && user.getEnableStatus() == 1 && user.getUserType() != null && user.getUserType() != 1) {
                return true;
            }
        }
        //不满足登录验证，则跳转到账号登录界面
        PrintWriter out = response.getWriter();
        out.println("<html>");
        out.println("<script>");
        out.println("window.open('" + request.getContextPath() + "/local/login?usertype=2','_self')");
        out.println("</script>");
        out.println("</html>");
        log.info("跳转回登录界面");
        return false;
    }
}
