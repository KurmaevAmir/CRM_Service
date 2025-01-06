package ru.itis.servlets.CRM;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.itis.dto.CRM.Specification.SpecificationAjaxDto;
import ru.itis.models.Specification;
import ru.itis.service.RequestService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/crm/request/get/specification")
public class RequestGetSpecificationServlet extends HttpServlet {
    private ObjectMapper objectMapper = new ObjectMapper();
    private RequestService requestService;

    @Override
    public void init() throws ServletException {
        requestService = (RequestService) getServletContext().getAttribute("requestService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        Long manufacturerId = Long.parseLong(req.getParameter("manufacturerId"));
        Long typeDeviceId = Long.parseLong(req.getParameter("typeDeviceId"));
        SpecificationAjaxDto specification = new SpecificationAjaxDto(manufacturerId, typeDeviceId);

        List<Specification> specifications;
        try {
            specifications = requestService.getSpecifications(specification.getManufacturerId(), specification.getTypeDeviceId());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        String specificationsAsJson = objectMapper.writeValueAsString(specifications);
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.getWriter().println(specificationsAsJson);
    }
}
