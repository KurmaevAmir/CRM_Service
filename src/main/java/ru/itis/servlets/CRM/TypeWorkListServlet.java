package ru.itis.servlets.CRM;

import ru.itis.models.TypeWork;
import ru.itis.service.TypeWorkService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/crm/type/work/list")
public class TypeWorkListServlet extends HttpServlet {
    private TypeWorkService typeWorkService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        typeWorkService = (TypeWorkService) config.getServletContext().getAttribute("typeWorkService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        try {
            List<TypeWork> typesWork = typeWorkService.findAll();
            req.setAttribute("typesWork", typesWork);
            req.getRequestDispatcher("/html/CRM/types_work_list.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String typeWorkIdString = req.getParameter("typeWorkId");
        try {
            if (typeWorkIdString != null && !typeWorkIdString.isEmpty()) {
                typeWorkService.fire(typeWorkIdString);
                resp.sendRedirect("/crm/type/work/list");
            }
        } catch (NullPointerException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IllegalArgumentException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
