package ru.itis.servlets.CRM;

import ru.itis.dto.CRM.Request.RequestListDto;
import ru.itis.dto.RequestDto;
import ru.itis.repositories.RequestRepository;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

//@WebServlet("/crm/request/list")
public class ChangeStatusServlet extends HttpServlet {
    private RequestRepository requestRepository;

    private static final String DB_URL = "jdbc:postgresql://localhost:5432/CRM+Service";
    private static final String DB_USER = "postgres";
    private static final String DB_PASSWORD = "Fvbh";

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String status = req.getParameter("status");

        if (status == null || status.isEmpty()) {
            status = "В обработке";
        }
        try {
            List<RequestListDto> requests = requestRepository.findByStatus(status);
            req.setAttribute("requests", requests);
            req.setAttribute("status", status);
            req.getRequestDispatcher("/html/CRM/change_status.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String requestId = req.getParameter("requestId");
        String newStatus = req.getParameter("newStatus");

        try {
            UUID identifierUUID = UUID.fromString(requestId);
            requestRepository.updateStatus(identifierUUID, newStatus);
            resp.sendRedirect("/crm/change_status");
        } catch (SQLException e) {
            throw new ServletException(e);
        }
    }

    @Override
    public void init(ServletConfig config) throws ServletException {
        requestRepository = (RequestRepository) config.getServletContext().getAttribute("requestRepository");
    }
}
