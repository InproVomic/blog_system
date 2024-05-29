package Servlet;

import model.User;
import model.UserDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/html/login")
public class Login extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("utf8");
        String username = req.getParameter("username");
        String password = req.getParameter("password");
        System.out.println("username:"+username);
        System.out.println("password:"+password);
        //1.验证username和password是否合理
        if(username == null ||username.length() == 0 || password == null || password.length() == 0){
            resp.setContentType("text/html; charset=utf8");
            resp.getWriter().write("你输入的用户名或密码为空！");
            return;
        }

        //2.密码错误或者用户名不存在的情况
        UserDao userDao = new UserDao();
        User user = userDao.getUserByName(username);
        if(user==null || !user.getPassword().equals(password)){
            resp.setContentType("text/html; charset=utf8");
            resp.getWriter().write("你输入的用户名或者密码错误！");
            return;
        }

        //3.创建会话
        HttpSession session = req.getSession(true);//这里设置成true，表示如果不存在就创建
        session.setAttribute("user",user);//创建user对象

        //4.重定向到主页
        resp.sendRedirect("blog_list.html");
        System.out.println("已重定向到主页");
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //通过这个来反应用户的登陆状态
        //如果返回的是403，表明用户是未登录状态

        //1.如果会话不存在就说明用户未登录
        HttpSession session = req.getSession(false);//如果会话不存在就不创建新的会话
        if(session==null){
            System.out.println("会话不存在！");
            resp.setStatus(403);
            return;
        }

        //2.如果用户对象不存在就说明用户未登录
        User user = (User) session.getAttribute("user");
        if(user==null){
            System.out.println("user对象不存在！");
            resp.setStatus(403);
            return;
        }

        resp.setStatus(200);
    }

}
