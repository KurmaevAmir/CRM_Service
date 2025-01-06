package ru.itis.servlets.CRM;

import ru.itis.dto.CreateClientForm;
import ru.itis.dto.ValidateDto;
import ru.itis.service.CreateClientService;
import ru.itis.service.ValidationDataFormService;
import ru.itis.service.ValidationDataFormServiceImpl;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@WebServlet("/crm/client/new")
public class CreateClientServlet extends HttpServlet {
    private CreateClientService createClientService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        createClientService = (CreateClientService) config.getServletContext().getAttribute("createClientService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/html/CRM/create_client.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String name = req.getParameter("name");
        String surname = req.getParameter("surname");
        String patronymic = req.getParameter("patronymic");
        String dateOfBirthStr = req.getParameter("date_of_birth");
        String phoneNumber = req.getParameter("phoneNumber");
        String email = req.getParameter("email");
        String passportSeries = req.getParameter("passportSeries");
        String passportNumber = req.getParameter("passportNumber");
        String passportIssueDateStr = req.getParameter("passportIssueDate");
        String passportIssued = req.getParameter("passportIssued");
        String passportSubdivision = req.getParameter("passportSubdivision");
        String password = req.getParameter("password");
        String passwordConfirm = req.getParameter("confirmPassword");

        ValidateDto validateDto = ValidateDto.builder()
                .name(name)
                .surname(surname)
                .patronymic(patronymic)
                .date_of_birth(dateOfBirthStr)
                .phone_number(phoneNumber)
                .date_of_birth(dateOfBirthStr)
                .phone_number(phoneNumber)
                .email(email)
                .passportSeries(passportSeries)
                .passportNumber(passportNumber)
                .passportIssueDate(passportIssueDateStr)
                .passportIssued(passportIssued)
                .passportSubdivision(passportSubdivision)
                .password(password)
                .passwordConfirm(passwordConfirm)
                .build();

        ValidationDataFormService validationDataFormService = new ValidationDataFormServiceImpl(validateDto, false);

        validationDataFormService.validateForm();

        List<String> dateStr = Arrays.asList(dateOfBirthStr, passportIssueDateStr);
        List<Optional<Date>> dates = validationDataFormService.validateData(dateStr);
        Date dateOfBirth = dates.get(0).orElse(null);
        Date passportIssueDate = dates.get(1).orElse(null);

        List<String> errors = validationDataFormService.getErrors();

        if (!errors.isEmpty()) {
            req.setAttribute("errors", errors);
            req.getRequestDispatcher("/html/CRM/create_client.jsp").forward(req, resp);
            return;
        }

        CreateClientForm createClientForm = CreateClientForm.builder()
                .name(name)
                .surname(surname)
                .patronymic(patronymic)
                .date_of_birth(dateOfBirth)
                .phone_number(phoneNumber)
                .email(email)
                .passportSeries(passportSeries)
                .passportNumber(passportNumber)
                .passportIssueDate(passportIssueDate)
                .passportIssued(passportIssued)
                .passportSubdivision(passportSubdivision)
                .password(password)
                .build();
        try {
            String error = createClientService.createClient(createClientForm);
            if (error != null) {
                errors.add(error);
                req.setAttribute("errors", errors);
                req.getRequestDispatcher("/html/CRM/create_client.jsp").forward(req, resp);
                return;
            }
            resp.sendRedirect("/html/CRM/success.html");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
