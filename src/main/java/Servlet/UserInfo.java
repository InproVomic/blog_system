package Servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/html/userInfo")
public class UserInfo extends HttpServlet {
    ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if(session==null){
            resp.sendRedirect("login.html");
            return;
        }
        User user = (User) session.getAttribute("user");
        if(user==null){
            resp.sendRedirect("login.html");
        }

        resp.setContentType("application/json; charset=utf8");
        user.setPassword("");
        String respJSON = objectMapper.writeValueAsString(user);
        resp.getWriter().write(respJSON);
    }
}
