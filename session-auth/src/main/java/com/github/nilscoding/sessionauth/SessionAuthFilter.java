package com.github.nilscoding.sessionauth;

import com.github.nilscoding.sessionauth.preauth.IPreAuthHandler;
import com.github.nilscoding.sessionauth.preauth.PreAuthState;
import java.io.IOException;
import java.util.regex.Pattern;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Session-based authentication filter
 * @author NilsCoding
 */
public class SessionAuthFilter implements Filter {

    public static final String DEFAULT_SESSION_PARAM_NAME = "authGranted";
    public static final String DEFAULT_DENY_ACTION = "httpReturn=403";
    
    protected FilterConfig filterConfig;
    protected IPreAuthHandler preAuthHandler;
    protected Pattern cfgWhitelistUrlRegex;
    protected String sessionParamName = null;
    protected String denyAction = null;
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        String whitelistRegexStr = filterConfig.getInitParameter("whitelistUrlRegex");
        try {
            this.cfgWhitelistUrlRegex = Pattern.compile(whitelistRegexStr);
        } catch (Exception ex) {
            this.cfgWhitelistUrlRegex = null;
        }
        this.sessionParamName = Utils.notEmptyOrDefault(filterConfig.getInitParameter("sessionParam"), DEFAULT_SESSION_PARAM_NAME);
        this.denyAction = Utils.notEmptyOrDefault(filterConfig.getInitParameter("denyAction"), DEFAULT_DENY_ACTION);
        String preAuthHandlerClassname = filterConfig.getInitParameter("preAuthHandler");
        if (Utils.isEmpty(preAuthHandlerClassname) == false) {
            this.preAuthHandler = Utils.createByName(preAuthHandlerClassname, IPreAuthHandler.class);
        }
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        if (((request instanceof HttpServletRequest) == false) || ((response instanceof HttpServletResponse) == false)) {
            // non-http requests should not be handled here
            chain.doFilter(request, response);
            return;
        }
        HttpServletRequest httpRequest = (HttpServletRequest)request;
        HttpServletResponse httpResponse = (HttpServletResponse)response;
        
        PreAuthState preAuthState = PreAuthState.CONTINUE_PROCESSING;
        if (this.preAuthHandler != null) {
            try {
                preAuthState = this.preAuthHandler.handlePreAuth(httpRequest, httpResponse, filterConfig, this.sessionParamName);
            } catch (Exception preAuthEx) {
                // kindly catch all exceptions
            }
        }
        
        if (preAuthState == PreAuthState.CANCEL_PROCESSING) {
            return;
        }
        
        boolean needCheckAccess = true;
        boolean canAccess = false;
        
        if (this.cfgWhitelistUrlRegex != null) {
            String url = httpRequest.getRequestURI();
            if (this.cfgWhitelistUrlRegex.matcher(url).matches() == true) {
                needCheckAccess = false;
            }
        }
        
        if (needCheckAccess == true) {
            // get session but do not create any itself
            HttpSession httpSession = httpRequest.getSession(false);
            if (httpSession == null) {
                // no previous session exists, so deny access
                canAccess = false;
            } else {
                // get session attribute
                Object sessionParamValue = httpSession.getAttribute(this.sessionParamName);
                // validate session value to be true or false
                canAccess = Utils.isTrue(sessionParamValue);
            }
        }
        
        if (canAccess == false) {
            // deny access
            if (Utils.isEmpty(this.denyAction) == true) {
                httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            // return http code?
            int sendHttpCode = Utils.getRegexInt(this.denyAction, "^httpReturn=(\\d+)$", 1, -1000);
            if (sendHttpCode > 0) {
                String sendHttpMessage = Utils.getRegexStr(this.denyAction, "^httpReturn=(\\d+),message=(.*)$", 2, null);
                if (Utils.isEmpty(sendHttpMessage)) {
                    httpResponse.sendError(sendHttpCode);
                } else {
                    httpResponse.sendError(sendHttpCode, sendHttpMessage);
                }
                return;
            }
            // redirect?
            if (this.denyAction.startsWith("redirect=")) {
                String redirLocation = this.denyAction.substring(9).trim();
                httpResponse.sendRedirect(redirLocation);
                return;
            }
            // invalid option, so send forbidden
            httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        
        // process requested resource
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // no nothing here
    }
    
}
