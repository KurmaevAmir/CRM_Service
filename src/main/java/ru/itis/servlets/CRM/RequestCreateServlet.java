package ru.itis.servlets.CRM;

import ru.itis.dto.CRM.Request.CreateRequestDto;
import ru.itis.models.TypeDevice;
import ru.itis.service.RequestService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/crm/request/create")
@MultipartConfig
public class RequestCreateServlet extends HttpServlet {
    private RequestService requestService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        requestService = (RequestService) config.getServletContext().getAttribute("requestService");
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
        req.getRequestDispatcher("/html/CRM/new_request.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        Long client = null;
        Long specification = null;
        try {
            client = Long.parseLong(req.getParameter("client"));
            specification = Long.parseLong(req.getParameter("specification"));
        } catch (NumberFormatException e) {
            resp.sendError(404, "Invalid request");
        }
        String description = req.getParameter("description");
        String serialNumber = req.getParameter("serialNumber");
        Part part = req.getPart("file");

        CreateRequestDto requestDto = new CreateRequestDto();
        requestDto.setDescription(description);
        requestDto.setSerialNumber(serialNumber);
        requestDto.setClientId(client);
        requestDto.setSpecificationId(specification);
        requestDto.setFile(part.getInputStream());
        requestDto.setOriginalFileName(part.getSubmittedFileName());
        requestDto.setContentType(part.getContentType());
        requestDto.setSize(part.getSize());
        try {
            requestService.saveRequest(requestDto);
            resp.sendRedirect("/crm/request/list");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
}
