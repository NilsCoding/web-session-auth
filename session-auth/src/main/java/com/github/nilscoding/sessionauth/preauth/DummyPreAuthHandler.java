package com.github.nilscoding.sessionauth.preauth;

import javax.servlet.FilterConfig;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Dummy implementation (doing nothing) for apre auth handler
 * @author NilsCoding
 */
public class DummyPreAuthHandler implements IPreAuthHandler {

    public DummyPreAuthHandler() {
    }

    @Override
    public PreAuthState handlePreAuth(HttpServletRequest httpRequest, HttpServletResponse httpResponse, FilterConfig filterConfig, String sessionAttributeName) {
        // do nothing here as it is just a dummy
        return PreAuthState.CONTINUE_PROCESSING;
    }
    
}
