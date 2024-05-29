package Servlet;


import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/html/loginOut")
public class LoginOut extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //拿到会话信息
        HttpSession session = req.getSession(false);
        if(session != null){
            //删除会话信息
            session.removeAttribute("user");
        }
        //重定向到登录页面
        resp.sendRedirect("login.html");
    }
}
