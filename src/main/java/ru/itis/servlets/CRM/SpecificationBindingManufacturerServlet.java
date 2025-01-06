package ru.itis.servlets.CRM;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.itis.dto.CRM.Manufacturer.ManufacturerBindingDto;
import ru.itis.service.SpecificationService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/crm/specification/binding/manufacturer")
public class SpecificationBindingManufacturerServlet extends HttpServlet {
    private SpecificationService specificationService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init(ServletConfig config) throws ServletException {
        specificationService = (SpecificationService) config.getServletContext().getAttribute("specificationService");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        ManufacturerBindingDto manufacturerBindingDto = objectMapper.readValue(req.getReader(), ManufacturerBindingDto.class);

        try {
            specificationService.bindingManufacturer(manufacturerBindingDto);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().println("{\"status\": \"ok\"}");
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
