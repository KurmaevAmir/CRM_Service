package ru.itis.servlets.CRM;

import ru.itis.models.TypeDevice;
import ru.itis.models.TypeWork;
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

@WebServlet("/crm/work/add")
public class WorkAddServlet extends HttpServlet {
    private WorkService workService;
    private RequestService requestService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        workService = (WorkService) config.getServletContext().getAttribute("workService");
        requestService = (RequestService) config.getServletContext().getAttribute("requestService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        try {
            List<TypeWork> typesWork = workService.findAllTypesWork();
            req.setAttribute("typesWork", typesWork);
            List<TypeDevice> typeDevices = requestService.getAllDevices();
            req.setAttribute("typeDevices", typeDevices);
        } catch (SQLException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Не найдены типы работ");
        }

        req.getRequestDispatcher("/html/CRM/add_work.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String typeWorkOperation = req.getParameter("typeWork");
        String priceStr = req.getParameter("price");
        String warrantyStr = req.getParameter("warranty");
        String specificationIdStr = req.getParameter("specification");

        Long typeWorkId = null;
        Long specificationId = null;
        try {
            Optional<Long> specificationIdOptional = workService.validateSpecification(specificationIdStr);
            Optional<Long> idTypeWorkOptional = workService.validateTypeWorkOperation(typeWorkOperation);
            if (idTypeWorkOptional.isPresent() && specificationIdOptional.isPresent()) {
                specificationId = specificationIdOptional.get();
                typeWorkId = idTypeWorkOptional.get();
            } else {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Несуществующий тип работы");
                return;
            }
        } catch (SQLException e) {
            resp.sendError(404);
            return;
        }

        try {
            workService.saveWork(typeWorkId, priceStr, warrantyStr, specificationId);
            resp.sendRedirect("/crm/work/list");
        } catch (NumberFormatException e) {
            resp.sendError(404);
        } catch (SQLException e) {
            resp.sendError(500);
        }
    }
}
