package ru.itis.servlets.CRM;

import ru.itis.dto.CRM.Specification.SpecificationListDto;
import ru.itis.models.TypeDevice;
import ru.itis.service.RequestService;
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

@WebServlet("/crm/specification/list")
public class SpecificationListServlet extends HttpServlet {
    private SpecificationService specificationService;
    private RequestService requestService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        specificationService = (SpecificationService) config.getServletContext().getAttribute("specificationService");
        requestService = (RequestService) config.getServletContext().getAttribute("requestService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String manufacturerIdString = req.getParameter("manufacturer");
        String typeDeviceIdString = req.getParameter("deviceType");

        try {
            List<SpecificationListDto> specifications;
            if (manufacturerIdString != null && !manufacturerIdString.isEmpty() && typeDeviceIdString != null && !typeDeviceIdString.isEmpty()) {
                Long manufacturerId = Long.parseLong(manufacturerIdString);
                Long typeDeviceId = Long.parseLong(typeDeviceIdString);
                specifications = specificationService.findByManufacturerAndTypeDevice(manufacturerId, typeDeviceId);
            } else {
                specifications = specificationService.findAll();
            }
            List<TypeDevice> typeDevices = requestService.getAllDevices();
            req.setAttribute("typeDevices", typeDevices);
            req.setAttribute("specifications", specifications);
            req.getRequestDispatcher("/html/CRM/specification_list.jsp").forward(req, resp);
        } catch (NullPointerException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String specificationIdString = req.getParameter("specificationId");
        try {
            Long specificationId = Long.parseLong(specificationIdString);
            specificationService.delete(specificationId);
            resp.sendRedirect("/crm/specification/list");
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (NullPointerException e) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
