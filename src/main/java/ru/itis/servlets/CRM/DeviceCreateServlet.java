package ru.itis.servlets.CRM;

import ru.itis.dto.CRM.Device.DeviceDto;
import ru.itis.models.TypeDevice;
import ru.itis.service.DeviceService;
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

@WebServlet("/crm/device/create")
public class DeviceCreateServlet extends HttpServlet {
    private RequestService requestService;
    private DeviceService deviceService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        requestService = (RequestService) config.getServletContext().getAttribute("requestService");
        deviceService = (DeviceService) config.getServletContext().getAttribute("createDeviceService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<TypeDevice> typeDevices;
        try {
            typeDevices = requestService.getAllDevices();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        req.setAttribute("typeDevices", typeDevices);
        req.getRequestDispatcher("/html/CRM/new_device.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        Long typeDeviceId = null;
        Long manufacturerId = null;
        Long specificationId = null;
        try {
            typeDeviceId = Long.parseLong(req.getParameter("deviceType"));
            manufacturerId = Long.parseLong(req.getParameter("manufacturer"));
            specificationId = Long.parseLong(req.getParameter("specification"));
        } catch (NumberFormatException e) {
            resp.sendError(404, "Invalid request");
        }
        String serialNumber = req.getParameter("serialNumber");
        String color = req.getParameter("color");
        DeviceDto deviceDto = new DeviceDto();
        deviceDto.setTypeDeviceId(typeDeviceId);
        deviceDto.setManufacturerId(manufacturerId);
        deviceDto.setSpecificationId(specificationId);
        deviceDto.setSerialNumber(serialNumber);
        deviceDto.setColor(color);
        try {
            deviceService.saveDevice(deviceDto);
            resp.sendRedirect("/html/CRM/success.html");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
