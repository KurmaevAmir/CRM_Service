package ru.itis.servlets.CRM;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.itis.models.TypeDevice;
import ru.itis.service.RequestService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/crm/specification/get/typesDevice")
public class SpecificationGetTypesDeviceServlet extends HttpServlet {
    private ObjectMapper objectMapper = new ObjectMapper();
    private RequestService requestService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        requestService = (RequestService) config.getServletContext().getAttribute("requestService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        try {
            List<TypeDevice> typeDevices = requestService.getAllDevices();
            String typesDeviceAsJson = objectMapper.writeValueAsString(typeDevices);
            resp.setCharacterEncoding("UTF-8");
            resp.setContentType("application/json");
            resp.getWriter().println(typesDeviceAsJson);
        }catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Не найдены типы работ");
        }
    }
}
