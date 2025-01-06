package ru.itis.servlets.CRM;

import ru.itis.models.TypeDevice;
import ru.itis.service.RequestService;
import ru.itis.service.SpecificationService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/crm/specification/add")
public class SpecificationAddServlet extends HttpServlet {
    private RequestService requestService;
    private SpecificationService specificationService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        requestService = (RequestService) config.getServletContext().getAttribute("requestService");
        specificationService = (SpecificationService) config.getServletContext().getAttribute("specificationService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        try {
            List<TypeDevice> typeDevices = requestService.getAllDevices();
            req.setAttribute("typeDevices", typeDevices);
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Не найдены типы работ");
        }

        req.getRequestDispatcher("/html/CRM/add_specification.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String typeDeviceString = req.getParameter("deviceType");
        String manufacturerString = req.getParameter("manufacturer");
        String model = req.getParameter("model");
        String article = req.getParameter("article");

        if (article.length() > 12) {
            req.setAttribute("error", "Артикул слишком длинный");
            req.getRequestDispatcher("/html/CRM/add_specification.jsp").forward(req, resp);
            return;
        }

        try {
            specificationService.saveSpecification(model, article, typeDeviceString, manufacturerString);
            resp.sendRedirect("/crm/specification/list");
        } catch (NumberFormatException e) {
            resp.sendError(500, "Некорректные данные");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
