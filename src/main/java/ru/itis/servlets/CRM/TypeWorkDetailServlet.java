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
import java.util.Optional;

@WebServlet("/crm/type/work/")
public class TypeWorkDetailServlet extends HttpServlet {
    private TypeWorkService typeWorkService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        typeWorkService = (TypeWorkService) config.getServletContext().getAttribute("typeWorkService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String typeWorkIdString = req.getParameter("id");

        Long typeWork;
        try {
            typeWork = Long.parseLong(typeWorkIdString);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            Optional<TypeWork> typeWorkOptional = typeWorkService.findById(typeWork);
            if (typeWorkOptional.isPresent()) {
                req.setAttribute("typeWork", typeWorkOptional.get());
                req.getRequestDispatcher("/html/CRM/type_work_detail.jsp").forward(req, resp);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String typeWorkOperation = req.getParameter("typeWorkOperation");
        String typeWorkIdString = req.getParameter("id");

        Long typeWork;
        try {
            typeWork = Long.parseLong(typeWorkIdString);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        try {
            typeWorkService.update(typeWork, typeWorkOperation);
            resp.sendRedirect("/crm/type/work/list");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
