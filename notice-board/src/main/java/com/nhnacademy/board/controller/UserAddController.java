package com.nhnacademy.board.controller;

import com.nhnacademy.board.command.Command;
import com.nhnacademy.board.domain.Users.General;
import com.nhnacademy.board.domain.Users.User;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class UserAddController implements Command {
    private Map<String, User> users;

    @Override
    public String execute(HttpServletRequest request, HttpServletResponse response) {
        users = (Map<String, User>) request.getServletContext().getAttribute("users");

        String recievedId = request.getParameter("id");
        String recievedPw = request.getParameter("pw");
        String recievedName = request.getParameter("name");
        String recievedProfile = request.getParameter("profile");

        if (users.containsKey(recievedId)) {
            throw new IllegalArgumentException("해당 아이디가 이미 존재합니다.");
        }

        users.put(recievedId, new General(recievedId, recievedPw, recievedName, recievedProfile));

        request.getServletContext().setAttribute("users", users);

        return "redirect:/userList.do";
    }
}
