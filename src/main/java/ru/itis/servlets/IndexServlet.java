package ru.itis.servlets;

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
import java.util.List;

@WebServlet("")
public class IndexServlet extends HttpServlet {
    private IndexService indexService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        indexService = (IndexService) config.getServletContext().getAttribute("indexService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        Long userId = (Long) session.getAttribute("userId");
        String role = (String) session.getAttribute("userRole");

        try {
            List<RequestListDto> requests = null;
            if (role.equals("client")) {
                requests = indexService.findByClient(userId);
                req.setAttribute("userRole", "client");
            } else if (role.equals("employee")) {
                requests = indexService.findByEmployee(userId);
                req.setAttribute("userRole", "employee");
            }
            req.setAttribute("requests", requests);
            req.getRequestDispatcher("/html/index.jsp").forward(req, resp);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
