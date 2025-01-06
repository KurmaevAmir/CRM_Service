package ru.itis.servlets.CRM;

import ru.itis.service.TypeWorkService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/crm/type/work/add")
public class TypeWorkCreateServlet extends HttpServlet {
    private TypeWorkService typeWorkService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        typeWorkService = (TypeWorkService) config.getServletContext().getAttribute("typeWorkService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/html/CRM/type_work_add.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String operation = req.getParameter("typeWork");
        try {
            if (operation != null && !operation.isEmpty()) {
                typeWorkService.save(operation);
                resp.sendRedirect("/crm/type/work/list");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
