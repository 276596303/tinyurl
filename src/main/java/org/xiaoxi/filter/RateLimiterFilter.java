package org.xiaoxi.filter;

import org.omg.CORBA.PRIVATE_MEMBER;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.xiaoxi.rateLimiter.dao.RateHandle;
import org.xiaoxi.rateLimiter.dao.impl.RateHandleImpl;
import org.xiaoxi.rateLimiter.enums.Rate;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by YanYang on 2016/6/24.
 */
@WebFilter(filterName = "rateLimiterFilter", urlPatterns = {"/user/", "/short/"})
public class RateLimiterFilter extends HttpServlet implements Filter {
    private static final Logger LOGGER = LoggerFactory.getLogger(RateLimiterFilter.class);

    private RateHandle rateHandle = new RateHandleImpl();

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        String feature = ((HttpServletRequest) request).getParameter("username");
        String action = ((HttpServletRequest) request).getMethod().toLowerCase();
        long current_time = System.currentTimeMillis();
        try {
            if ((action.equals("post") || action.equals("put") || action.equals("delete")) &&
                    rateHandle.isLimit(action, feature, current_time, 2, Rate.SECOND)) {

                ((HttpServletResponse) response).sendError(HttpServletResponse.SC_FORBIDDEN, "请求次数过于频繁");
            } else {
                chain.doFilter(request, response);
            }
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    @Override
    public void destroy() {

    }
}
