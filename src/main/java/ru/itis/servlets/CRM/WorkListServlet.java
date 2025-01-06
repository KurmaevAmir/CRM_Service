package ru.itis.servlets.CRM;

import ru.itis.dto.CRM.Work.WorkListDto;
import ru.itis.models.TypeDevice;
import ru.itis.service.RequestService;
import ru.itis.service.WorkService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet("/crm/work/list")
public class WorkListServlet extends HttpServlet {
    private WorkService workService;
    private RequestService requestService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        workService = (WorkService) config.getServletContext().getAttribute("workService");
        requestService = (RequestService) config.getServletContext().getAttribute("requestService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String specification = req.getParameter("specification");

        if (specification != null && !specification.isEmpty()) {
            Long specificationId = null;
            try {
                Optional<Long> specificationIdOptional = workService.validateSpecification(specification);
                if (specificationIdOptional.isPresent()) {
                    specificationId = specificationIdOptional.get();
                } else {
                    resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Несуществующая спецификация");
                    return;
                }
                List<WorkListDto> works = workService.findBySpecification(specificationId);
                req.setAttribute("works", works);
            } catch (SQLException e) {
                resp.sendError(404);
                return;
            }
        } else {
            try {
                List<WorkListDto> works = workService.findAllWorks();
                req.setAttribute("works", works);
            } catch (SQLException e) {
                resp.sendError(500, e.getMessage());
            }
        }
        try {
            List<TypeDevice> typeDevices = requestService.getAllDevices();
            req.setAttribute("typeDevices", typeDevices);
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Не найдены типы работ");
        }
        req.getRequestDispatcher("/html/CRM/work_list.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String workId = req.getParameter("workId");

        try {
            workService.fire(workId);
            resp.sendRedirect("/crm/work/list");
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ошибка удаления услуги");
        } catch (NumberFormatException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
