package ru.itis.filters;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@WebFilter("/crm/*")
public class AdminFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();

        Boolean isAuthenticated = false;
        Boolean sessionExist = session != null;
        Boolean isCRMPage = false;
        List<String> CRMPages = Arrays.asList("/crm/request/list", "/crm/client/new", "/crm/request/create",
                "/crm/request/get/clients", "/crm/request/get/manufacturer", "/crm/request/get/specification",
                "/crm/request/get/device", "/crm/device/create", "/crm/request/", "/crm/employee/list", "/crm/employee/",
                "/crm/employee/register", "/crm/work/add", "/crm/work/list", "/crm/specification/add",
                "/crm/manufacturer/add", "/crm/specification/get/typesDevice", "/crm/specification/get/manufacturers",
                "/crm/specification/binding/manufacturer", "/crm/type/work/list", "/crm/type/work/",
                "/crm/manufacturer/list", "/crm/specification/list", "/crm/type/work/add", "/crm/type/device/add",
                "/crm/type/device/list");
        for (String page : CRMPages) {
            if (request.getRequestURI().equals(page)) {
                isCRMPage = true;
                break;
            }
        }
        Boolean access = false;

        if (sessionExist) {
            isAuthenticated = (Boolean) session.getAttribute("isAuthenticated");
            if (isAuthenticated == null) {
                isAuthenticated = false;
            } else {
                access = session.getAttribute("userRole").equals("employee");
            }
        }

        if (isAuthenticated && isCRMPage && access) {
            filterChain.doFilter(request, response);
        } else {
            response.sendError(404);
        }
    }
}
