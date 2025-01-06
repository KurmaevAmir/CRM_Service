package ru.itis.servlets.CRM;

import ru.itis.dto.CRM.Employee.EmployeeListDto;
import ru.itis.service.EmployeeService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/crm/employee/list")
public class EmployeeListServlet extends HttpServlet {
    private EmployeeService employeeService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        employeeService = (EmployeeService) config.getServletContext().getAttribute("employeeService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        String patronymic = req.getParameter("patronymic");

        List<EmployeeListDto> employees;
        if (name != null && !name.isEmpty() || surname != null && !surname.isEmpty()) {
            try {
                employees = employeeService.findByFullName(name, surname, patronymic);
                req.setAttribute("employees", employees);
            } catch (SQLException e) {
                req.setAttribute("error", "Некорректные данные");
            } finally {
                req.getRequestDispatcher("/html/CRM/employee_list.jsp").forward(req, resp);
            }
        } else if (name == null && surname == null && patronymic == null) {
            try {
                employees = employeeService.findAll();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            req.setAttribute("employees", employees);
            req.getRequestDispatcher("/html/CRM/employee_list.jsp").forward(req, resp);
        } else {
            req.setAttribute("error", "Не заполнены обязательные поля");
            req.getRequestDispatcher("/html/CRM/employee_list.jsp").forward(req, resp);
        }
    }
}
