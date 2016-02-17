/**
 * 
 */
package com.platform.common.web;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

/**
 * 功能描述：Web应用工具类。</p>本类扩展自{@link org.springframework.web.util.WebUtils}.
 *
 * @author   andy.zheng
 * @version  0.1.0, 2015年5月21日 下午3:06:54
 * @since    Common Platform/Web 1.0.0
 */
public final class WebUtils extends org.springframework.web.util.WebUtils {
    
    private final static String CURRENT_USER = "currentUser";

    /**
     * 功能描述：获取当前Http请求对象.
     * 
     * @return  当前Http请求对象.
     */
    public static HttpServletRequest getHttpCurrentRequest() {
        ServletRequestAttributes currentRequestAttributes = (ServletRequestAttributes)RequestContextHolder.currentRequestAttributes();
        Assert.notNull(currentRequestAttributes);
        return currentRequestAttributes.getRequest();
    }
    
    /**
     * 功能描述：获取当前用户会话对象.
     * 
     * @return  Http会话对象.
     */
    public static HttpSession getCurrentSession() {
        return getHttpCurrentRequest().getSession(true);
    }
    
    
    /**
     * 功能描述：获取当前用户会话对象.
     * 
     * @return  Http会话对象.
     */
    public static HttpSession getCurrentSession(boolean create) {
        return getHttpCurrentRequest().getSession(create);
    }
    
    /**
     * 功能描述：保存当前会话用户。
     * 
     */
    public static void storeCurrentUser(Object currentUser) {
        getCurrentSession().setAttribute(CURRENT_USER, currentUser);
    }
    
    /**
     * 功能描述：获取当前会话用户。
     * 
     * @return  当前会话用户。
     */
    public static Object getCurrentUser() {
        return getCurrentSession().getAttribute(CURRENT_USER);
    }
}
