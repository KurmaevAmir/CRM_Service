package ru.itis.servlets.CRM;

import ru.itis.service.TypeDeviceService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/crm/type/device/add")
public class TypeDeviceAddServlet extends HttpServlet {
    private TypeDeviceService typeDeviceService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        typeDeviceService = (TypeDeviceService) config.getServletContext().getAttribute("typeDeviceService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/html/CRM/add_type_device.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String typeDevice = req.getParameter("typeDevice");

        try {
            if (typeDevice != null && !typeDevice.isEmpty()) {
                typeDeviceService.save(typeDevice);
                resp.sendRedirect("/crm/type/device/list");
            }
        } catch (NullPointerException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
