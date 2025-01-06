package ru.itis.servlets.CRM;

import ru.itis.dto.CRM.Employee.EmployeeDetailDto;
import ru.itis.service.EmployeeService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/crm/employee/")
public class EmployeeDetailServlet extends HttpServlet {
    private EmployeeService employeeService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        employeeService = (EmployeeService) config.getServletContext().getAttribute("employeeService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String email = req.getParameter("id");

        Optional<EmployeeDetailDto> employee;
        try {
            employee = employeeService.findByEmail(email);
            if (employee.isPresent()) {
                req.setAttribute("employee", employee.get());
                req.getRequestDispatcher("/html/CRM/employee_detail.jsp").forward(req, resp);
            } else {
                resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String email = req.getParameter("id");

        if (email.equals("rootadmin@localhost.ru")) {
            resp.sendError(HttpServletResponse.SC_FORBIDDEN, "Действие запрещено");
            return;
        }

        try {
            employeeService.fire(email);
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка увольнения");
            return;
        }
        resp.sendRedirect("/crm/employee/list");
    }
}
