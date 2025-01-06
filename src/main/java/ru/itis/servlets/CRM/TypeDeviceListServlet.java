package ru.itis.servlets.CRM;

import ru.itis.models.TypeDevice;
import ru.itis.service.TypeDeviceService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/crm/type/device/list")
public class TypeDeviceListServlet extends HttpServlet {
    private TypeDeviceService typeDeviceService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        typeDeviceService = (TypeDeviceService) config.getServletContext().getAttribute("typeDeviceService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        try {
            List<TypeDevice> typesDevice = typeDeviceService.findAll();
            req.setAttribute("typesDevice", typesDevice);
            req.getRequestDispatcher("/html/CRM/type_device_list.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String typeDeviceIdString = req.getParameter("typeDeviceId");
        try {
            if (typeDeviceIdString != null && !typeDeviceIdString.isEmpty()) {
                typeDeviceService.delete(typeDeviceIdString);
                resp.sendRedirect("/crm/type/device/list");
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
