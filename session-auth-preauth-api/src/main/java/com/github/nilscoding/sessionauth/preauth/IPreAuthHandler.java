package com.github.nilscoding.sessionauth.preauth;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Interface for pre-auth handlers, e.g. to do some custom authorization
 * @author NilsCoding
 */
public interface IPreAuthHandler {
    
    /**
     * Handle the pre-auth, returning whether (or not) to continue with the filter
     * @param httpRequest   http request
     * @param httpResponse  http response
     * @param filterConfig  filter config
     * @param sessionAttributeName  name of session attribute containing auth state (maybe set a value of true or false)
     * @return  state to either continue filter processing or skip it
     */
    PreAuthState handlePreAuth(HttpServletRequest httpRequest, HttpServletResponse httpResponse, FilterConfig filterConfig, String sessionAttributeName);
    
}
