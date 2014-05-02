/*
 * Copyright 2013 JBoss Inc
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.overlord.jetty.filter;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * Welcome File Filter.
 *
 * @author kconner@redhat.com
 */
public class WelcomeFileFilter implements Filter {
    private static final String WELCOME_FILES = "welcomeFiles";
    private String[] welcomeFiles;

    @Override
    public void init(final FilterConfig filterConfig)
        throws ServletException {
        final String initParam = filterConfig.getInitParameter(WELCOME_FILES);
        if (initParam == null) {
            throw new ServletException("Missing '" + WELCOME_FILES + "' initialisation parameter");
        }
        final String[] files = initParam.split(",");
        final List<String> fileList = new ArrayList<String>();
        for(String file: files) {
            final String trimmed = file.trim();
            if (trimmed.length() > 0) {
                if (trimmed.contains("/")) {
                    throw new ServletException("Invalid '" + WELCOME_FILES + "' initialisation parameter, must not contain '/'");
                }
                fileList.add(trimmed);
            }
        }
        if (fileList.size() == 0) {
            throw new ServletException("Invalid '" + WELCOME_FILES + "' initialisation parameter");
        }
        
        welcomeFiles = fileList.toArray(new String[0]);
    }

    @Override
    public void doFilter(final ServletRequest request, final ServletResponse response,
        final FilterChain chain)
        throws IOException, ServletException {
        final HttpServletRequest httpServletRequest = (HttpServletRequest)request;
        final HttpServletResponse httpServletResponse = (HttpServletResponse)response;
        
        final String requestServletPath = httpServletRequest.getServletPath();
        final String requestPathInfo = httpServletRequest.getPathInfo();
        final String basePath = normalise(requestServletPath) + normalise(requestPathInfo);
        
        if ((basePath.length() == 0) || basePath.endsWith("/")) {
            final String path = (basePath.length() == 0) ? "/" : basePath;
            
            final ServletContext servletContext = request.getServletContext();
            for (String welcomeFile: welcomeFiles) {
                final String location = path + welcomeFile;
                final URL welcomeFileURL = servletContext.getResource(location);
                if (welcomeFileURL != null) {
                    httpServletResponse.sendRedirect(servletContext.getContextPath() + location);
                    return;
                }
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void destroy() {
    }
    
    private static String normalise(final String path) {
        return (path == null ? "" : path);
    }
}
