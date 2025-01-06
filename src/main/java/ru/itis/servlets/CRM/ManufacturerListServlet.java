package ru.itis.servlets.CRM;

import ru.itis.models.Manufacturer;
import ru.itis.service.ManufacturerService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/crm/manufacturer/list")
public class ManufacturerListServlet extends HttpServlet {
    private ManufacturerService manufacturerService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        manufacturerService = (ManufacturerService) config.getServletContext().getAttribute("manufacturerService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            List<Manufacturer> manufacturers = manufacturerService.findAll();
            req.setAttribute("manufacturers", manufacturers);
            req.getRequestDispatcher("/html/CRM/manufacturer_list.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String manufacturerIdString = req.getParameter("manufacturerId");
        try {
            Long manufacturerId = Long.parseLong(manufacturerIdString);
            manufacturerService.delete(manufacturerId);
            resp.sendRedirect("/crm/manufacturer/list");
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
