package ru.itis.servlets;

import ru.itis.dto.CRM.Request.RequestDetailDto;
import ru.itis.dto.CRM.Request.RequestListDto;
import ru.itis.service.IndexService;
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
import java.util.Optional;
import java.util.UUID;

@WebServlet("/detail")
public class RequestDetailServlet extends HttpServlet {
    private IndexService indexService;
    private RequestService requestService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        indexService = (IndexService) config.getServletContext().getAttribute("indexService");
        requestService = (RequestService) config.getServletContext().getAttribute("requestService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        HttpSession session = req.getSession();

        String email = (String) session.getAttribute("email");

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
                RequestDetailDto dto = requestData.get();
                if (dto.getClientEmail().equals(email)) {
                    req.setAttribute("request", requestData.get());
                    req.getRequestDispatcher("/html/request_index_detail.jsp").forward(req, resp);
                    return;
                }
            }
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
