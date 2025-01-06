package ru.itis.servlets;

import ru.itis.models.FileInfo;
import ru.itis.service.FileService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/uploaded/files")
public class FileServlet extends HttpServlet {
    private FileService fileService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        fileService = (FileService) config.getServletContext().getAttribute("fileService");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String fileId = req.getParameter("id");
        FileInfo fileInfo;
        try {
            fileInfo = fileService.getFileInfo(Long.parseLong(fileId));
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        resp.setContentType(fileInfo.getType());
        resp.setContentLength(fileInfo.getSize().intValue());
        resp.setHeader("Content-Disposition", "filename=\"" + fileInfo.getOriginalFileName() + "\"");
        fileService.writeFileFromStorage(Long.parseLong(fileId), resp.getOutputStream());
        resp.flushBuffer();
    }
}
