package com.ship.shipshop5.config.security;

import com.ship.shipshop5.config.security.SecurityUtils;
import org.springframework.security.web.savedrequest.HttpSessionRequestCache;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CustomRequestCache extends HttpSessionRequestCache {
    @Override
    public void saveRequest(HttpServletRequest request, HttpServletResponse response) {
        if(!SecurityUtils.isFrameworkInternalRequest(request)) {
            super.saveRequest(request, response);
        }
    }
}