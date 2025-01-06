package ru.itis.servlets;

import ru.itis.dto.HumanDto;
import ru.itis.repositories.UserRepository;
import ru.itis.service.AuthService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/login")
public class LoginServlet extends HttpServlet {
    private AuthService authService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/html/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String email = req.getParameter("email");
        String password = req.getParameter("password");

        Optional<HumanDto> humanOptional = authService.getHumanDto(email, password);

        if (humanOptional.isPresent()) {
            HumanDto human = humanOptional.get();
            HttpSession session = req.getSession(true);
            session.setMaxInactiveInterval(-1);
            session.setAttribute("userId", human.getId());
            session.setAttribute("userRole", human.getRole());
            session.setAttribute("email", human.getEmail());
            session.setAttribute("isAuthenticated", true);
            resp.sendRedirect("/");
        } else {
            req.setAttribute("error", "Неверный email или пароль");
            req.getRequestDispatcher("/html/login.jsp").forward(req, resp);
        }
    }

    @Override
    public void init() throws ServletException {
        authService = (AuthService) getServletContext().getAttribute("authService");
    }
}
