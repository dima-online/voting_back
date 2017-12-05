package kz.bsbnb.security;

import kz.bsbnb.util.JsonUtil;
import kz.bsbnb.util.SimpleResponse;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Olzhas.Pazyldayev on 30.05.2017.
 */
public class CustomSessionManagementFilter extends GenericFilterBean {
    private static final String CONTENT_TYPE = "application/json";
    private static final String MESSAGE = "NOT AUTHORIZED";
    private CustomRequestMatcher matcher;

    public CustomSessionManagementFilter(String[] unfilteredUrl) {
        matcher = new CustomRequestMatcher(unfilteredUrl);
    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse res,
                         FilterChain chain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;

        if (!matcher.matches(request)) {
            if (hasSessionExpired(request)) {
                response.setContentType(CONTENT_TYPE);
                response.getWriter().write(JsonUtil.toJson(new SimpleResponse(MESSAGE).NOT_AUTHORIZED()));
                return;
            }
        }
        chain.doFilter(request, response);
    }

    private boolean hasSessionExpired(HttpServletRequest httpRequest) {
        if (httpRequest.getRequestedSessionId() != null
                && !httpRequest.isRequestedSessionIdValid()) {
            if (logger.isDebugEnabled()) {
                logger.debug("Requested session ID "
                        + httpRequest.getRequestedSessionId() + " is invalid.");
            }

            return true;
        }
        return false;
    }
}