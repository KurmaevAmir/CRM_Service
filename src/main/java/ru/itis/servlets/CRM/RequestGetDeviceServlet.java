package ru.itis.servlets.CRM;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.itis.dto.CRM.Device.DeviceAjaxDto;
import ru.itis.models.Device;
import ru.itis.service.RequestService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/crm/request/get/device")
public class RequestGetDeviceServlet extends HttpServlet {
    private ObjectMapper objectMapper = new ObjectMapper();
    private RequestService requestService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        requestService = (RequestService) config.getServletContext().getAttribute("requestService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String serialNumber = req.getParameter("serialNumber");
        Long specification = Long.parseLong(req.getParameter("specification"));

        DeviceAjaxDto deviceDto = new DeviceAjaxDto(serialNumber, specification);

        Device device;
        try {
            device = requestService.getDevice(deviceDto);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String deviceAsJson = objectMapper.writeValueAsString(device);
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.getWriter().println(deviceAsJson);
    }
}
