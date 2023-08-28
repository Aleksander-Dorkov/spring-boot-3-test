package com.sasho.demo.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Component
public class LoggingFilter extends OncePerRequestFilter {
    private static final String REPEATED_DASH = "-".repeat(25);
    private static final String REQUEST = "Request";
    private static final String RESPONSE = "Response";
    private static final String REQUEST_HEADER = REPEATED_DASH.concat(REQUEST).concat(REPEATED_DASH);
    private static final String RESPONSE_HEADER = REPEATED_DASH.concat(RESPONSE).concat(REPEATED_DASH);

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Map<String, String> requestHeaders = buildRequestHeaders(request);
        logMessage(REQUEST, request.getMethod(), request.getRequestURI(), 0, requestHeaders);

        filterChain.doFilter(request, response);

        Map<String, String> responseHeaders = buildResponseHeaders(response);
        logMessage(RESPONSE, request.getMethod(), request.getRequestURI(), response.getStatus(), responseHeaders);
    }

    private void logMessage(String type, String method, String url, int statusCode, Map<String, String> headersMap) {
        String header = type.equals(REQUEST) ? REQUEST_HEADER : RESPONSE_HEADER;
        log.info("\n{}\nMethod: {}\nUrl: {}\nHeaders: {}\n{}",
                header, method, url, headersMap, (statusCode != 0 ? "Status Code: " + statusCode : "")
        );
    }

    private Map<String, String> buildRequestHeaders(HttpServletRequest request) {
        List<String> requestHeaders = Collections.list(request.getHeaderNames());
        return requestHeaders.stream()
                .collect(Collectors.toMap(
                        headerName -> headerName,
                        request::getHeader,
                        (value1, value2) -> value1 + ", " + value2
                ));
    }

    private Map<String, String> buildResponseHeaders(HttpServletResponse response) {
        List<String> responseHeaders = response.getHeaderNames().stream().toList();
        return responseHeaders.stream()
                .collect(Collectors.toMap(
                        headerName -> headerName,
                        response::getHeader,
                        (value1, value2) -> value1 + ", " + value2
                ));
    }
}
