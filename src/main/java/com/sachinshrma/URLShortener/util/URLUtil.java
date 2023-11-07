package com.sachinshrma.URLShortener.util;

import jakarta.servlet.http.HttpServletRequest;

public class URLUtil {

    public static String getRequestHostUrl(HttpServletRequest request) {
        return request.getRequestURL().substring(0,request.getRequestURL().lastIndexOf(request.getRequestURI()));
    }
}
