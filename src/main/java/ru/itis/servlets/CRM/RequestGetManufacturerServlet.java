package ru.itis.servlets.CRM;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.itis.dto.CRM.TypeDevice.TypeDeviceAjaxDto;
import ru.itis.models.Manufacturer;
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

@WebServlet("/crm/request/get/manufacturer")
public class RequestGetManufacturerServlet extends HttpServlet {
    private ObjectMapper objectMapper = new ObjectMapper();
    private RequestService requestService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        requestService = (RequestService) config.getServletContext().getAttribute("requestService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        Long id = Long.parseLong(req.getParameter("id"));
        TypeDeviceAjaxDto typeDevice = new TypeDeviceAjaxDto(id);

        List<Manufacturer> manufacturers;
        try {
            manufacturers = requestService.getManufacturers(typeDevice.getId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String manufacturersAsJson = objectMapper.writeValueAsString(manufacturers);
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.getWriter().println(manufacturersAsJson);
    }
}
