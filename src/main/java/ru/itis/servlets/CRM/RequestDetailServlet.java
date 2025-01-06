package ru.itis.servlets.CRM;

import ru.itis.dto.CRM.Request.RequestDetailDto;
import ru.itis.dto.CRM.Work.WorkAddDto;
import ru.itis.dto.CRM.Work.WorkListDto;
import ru.itis.models.Status;
import ru.itis.service.RequestService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@WebServlet("/crm/request/")
public class RequestDetailServlet extends HttpServlet {
    private RequestService requestService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        requestService = (RequestService) config.getServletContext().getAttribute("requestService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String identifierStr = req.getParameter("id");
        Optional<UUID> identifier = requestService.validateDetailRequest(identifierStr);
        UUID identifierUUID = null;
        if (identifier.isPresent()) {
            identifierUUID = identifier.get();
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Optional<RequestDetailDto> requestData;
        try {
            requestData = requestService.findByIdentifier(identifierUUID);
            if (requestData.isPresent()) {
                req.setAttribute("request", requestData.get());
            } else {
                resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return;
            }
            List<WorkAddDto> newWorks = requestService.findWorkByModel(requestData.get().getModel());
            req.setAttribute("newWorks", newWorks);
            List<WorkListDto> works = requestService.findWorksByIdentifier(identifierUUID);
            req.setAttribute("works", works);
            List<Status> statuses = requestService.getAllStatuses();
            req.setAttribute("statuses", statuses);
            req.getRequestDispatcher("/html/CRM/request_detail.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String identifier = req.getParameter("identifier");
        String status = req.getParameter("status");

        String workIdString = req.getParameter("workId");

        String newWorkIdString = req.getParameter("newWork");

        try {
            if (status != null && !status.isEmpty()) {
                try {
                    UUID identifierUUID = UUID.fromString(identifier);
                    requestService.updateStatus(identifierUUID, status);
                    resp.sendRedirect("/crm/request/?id=" + identifier);
                } catch (SQLException e) {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
            } else if (workIdString != null && !workIdString.isEmpty()) {
                try {
                    requestService.deleteBind(identifier, workIdString);
                    resp.sendRedirect("/crm/request/?id=" + identifier);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IllegalArgumentException e) {
                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                    return;
                }
            } else if (newWorkIdString != null && !newWorkIdString.isEmpty()) {
                try {
                    HttpSession session = req.getSession();
                    String email = (String) session.getAttribute("email");
                    requestService.bindWork(identifier, newWorkIdString, email);
                    resp.sendRedirect("/crm/request/?id=" + identifier);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                } catch (IllegalArgumentException e) {
                    resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                }
            }
        } catch (NullPointerException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
    }
}
