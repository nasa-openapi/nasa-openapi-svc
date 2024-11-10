package com.nasa.interceptor;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.HandlerInterceptor;

import com.nasa.util.TimeZoneContext;

public class TimeZoneInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // Retrieve the timezone from the request header (you can also use parameters or cookies)
        String timezone = request.getHeader("X-Timezone");
        System.out.println("Time zone is :"+ timezone);
        if (timezone != null && !timezone.isEmpty()) {
            // Set the timezone in the ThreadLocal
            TimeZoneContext.setTimezone(timezone);
        } else {
            // Set default timezone or handle accordingly
        	TimeZoneContext.setTimezone("UTC");
        }
        
        return true; // Continue the request
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // Clear the ThreadLocal after request completion to avoid memory leaks
    	TimeZoneContext.clear();
    }
}

