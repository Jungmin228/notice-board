package com.nhnacademy.board.servlet;

import com.nhnacademy.board.command.Command;
import com.nhnacademy.board.controller.LoginGetController;
import com.nhnacademy.board.controller.LoginPostController;
import com.nhnacademy.board.controller.BoardListController;
import java.io.IOException;
import java.util.Objects;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@WebServlet(name = "frontServlet", urlPatterns = "*.do")
public class FrontServlet extends HttpServlet {
    private static final String REDIRECT_PREFIX = "redirect:";

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp)
        throws ServletException, IOException {
        HttpSession session = req.getSession(false);
        // Todo: erase
        if (Objects.nonNull(session)) {
            log.error("session is not null");
            session.invalidate();
        } else {
            log.error("session is null");
        }

        resp.setContentType("text/html");
        resp.setCharacterEncoding("UTF-8");

        try {
            Command command = resolveCommand(req.getServletPath(), req.getMethod());

            // Todo: erase
            log.error("frontservlet: getServletPath - " + req.getServletPath());
            log.error("frontservlet: getMethod - " + req.getMethod());

            String view = command.execute(req, resp);
            // Todo: erase
            log.error("frontservlet: view - " + view);

            if (view.startsWith(REDIRECT_PREFIX)) {
                // `redirect:`로 시작하면 redirect 처리.
                resp.sendRedirect(view.substring(REDIRECT_PREFIX.length()));
            } else {
                // redirect가 아니면 JSP에게 view 처리를 위임하여 그 결과를 include시킴.
                RequestDispatcher rd = req.getRequestDispatcher(view);
                rd.include(req, resp);
            }
        } catch (Exception ex) {
            // 에러가 발생한 경우는 error page로 지정된 `/error.jsp`에게 view 처리를 위임.
            log.error("", ex);
            req.setAttribute("exception", ex);
            RequestDispatcher rd = req.getRequestDispatcher("/error.jsp");
            rd.forward(req, resp);
        }
    }

    private Command resolveCommand(String servletPath, String method) {
        Command command = null;

        if ("/main.do".equals(servletPath) && "GET".equalsIgnoreCase(method)) {
            command = new BoardListController();
        } else if ("/login.do".equals(servletPath) && "GET".equalsIgnoreCase(method)) {
            command = new LoginGetController();
        } else if ("/login.do".equals(servletPath) && "POST".equalsIgnoreCase(method)) {
            command = new LoginPostController();
        }

        return command;
    }
}