package ru.itis.servlets.CRM;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.itis.models.Manufacturer;
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

@WebServlet("/crm/specification/get/manufacturers")
public class SpecificationGetManufacturers extends HttpServlet {
    private SpecificationService specificationService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init(ServletConfig config) throws ServletException {
        specificationService = (SpecificationService) config.getServletContext().getAttribute("specificationService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Manufacturer> manufacturers;
        try {
            manufacturers = specificationService.findAllManufacturers();
        } catch (SQLException e){
            throw new RuntimeException(e);
        }
        String manufacturersAsJson = objectMapper.writeValueAsString(manufacturers);
        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        resp.getWriter().println(manufacturersAsJson);
    }
}
