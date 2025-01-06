package ru.itis.filters;

import ru.itis.dto.HumanDto;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RoleFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HumanDto currentUser = (HumanDto) servletRequest.getAttribute("user");

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        if (request.getMethod().equals("POST") && request.getServletPath().equals("crm/")) {
            if (currentUser != null && currentUser.getRole().equals("employee")) {
                filterChain.doFilter(request, response);
            } else {
                response.setStatus(403);
            }
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }
    }

    @Override
    public void destroy() {

    }
}
