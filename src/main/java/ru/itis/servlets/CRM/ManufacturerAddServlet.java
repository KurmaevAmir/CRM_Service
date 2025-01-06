package ru.itis.servlets.CRM;

import com.fasterxml.jackson.databind.ObjectMapper;
import ru.itis.dto.CRM.Manufacturer.ManufacturerAddAjaxDto;
import ru.itis.service.ManufacturerService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/crm/manufacturer/add")
public class ManufacturerAddServlet extends HttpServlet {
    private ManufacturerService manufacturerService;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void init(ServletConfig config) throws ServletException {
        manufacturerService = (ManufacturerService) config.getServletContext().getAttribute("manufacturerService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/html/CRM/add_specification.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        ManufacturerAddAjaxDto manufacturerAddAjaxDto = objectMapper.readValue(req.getReader(), ManufacturerAddAjaxDto.class);

        try {
            manufacturerService.saveManufacturer(manufacturerAddAjaxDto);
            resp.setStatus(HttpServletResponse.SC_OK);
            resp.setContentType("application/json");
            resp.getWriter().println("{\"status\": \"ok\"}");
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
