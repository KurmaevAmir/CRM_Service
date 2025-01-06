package ru.itis.servlets.CRM;

import ru.itis.dto.CRM.Request.RequestListDto;
import ru.itis.models.Status;
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
import java.util.UUID;

@WebServlet("/crm/request/list")
public class RequestListServlet extends HttpServlet {
    private RequestService requestService;
    @Override
    public void init(ServletConfig config) throws ServletException {
        requestService = (RequestService) config.getServletContext().getAttribute("requestService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String status = req.getParameter("status");
        String identifier = req.getParameter("search");

        List<RequestListDto> requests;
        try {
            List<Status> statuses = requestService.getAllStatuses();
            req.setAttribute("statuses", statuses);
        } catch (SQLException e) {
            throw new ServletException(e);
        }

        if (identifier != null && !identifier.isEmpty()) {
            try {
                requests = requestService.findAllByIdentifier(identifier);
                req.setAttribute("requests", requests);
                req.setAttribute("status", status);
            } catch (SQLException e) {
                req.setAttribute("error", "Некорректный идентификатор");
            } finally {
                req.getRequestDispatcher("/html/CRM/request_list.jsp").forward(req, resp);
            }
        } else {
            try {
                requests = requestService.findAllByStatus(status);
                req.setAttribute("requests", requests);
                req.setAttribute("status", status);
                req.getRequestDispatcher("/html/CRM/request_list.jsp").forward(req, resp);
            } catch (SQLException e) {
                throw new ServletException(e);
            }
        }
    }
}
