package Servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Blog;
import model.BlogDao;
import model.User;
import model.UserDao;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/html/getAuthorInfo")
public class AuthorInfo extends HttpServlet {
    ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
       //1.拿到blogId
        String blogId = req.getParameter("blogId");
        if(blogId==null){
            resp.setContentType("text/html; charset=utf8");
            resp.getWriter().write("blogId没有找到");
            return;
        }
        //2.查找blog表中的对象
        Blog blog = null;
        BlogDao blogDao = new BlogDao();
        try {
            blog = blogDao.getBlog(Integer.parseInt(blogId));
            if (blog==null){
                resp.setContentType("text/html; charset=utf8");
                resp.getWriter().write("blogId没有找到");
                return;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //3.拿到user对象
        User user = null;
        UserDao userDao = new UserDao();
        try {
            user = userDao.getUserById(blog.getUserId());
            if(user==null){
                resp.setContentType("text/html; charset=utf8");
                resp.getWriter().write("userId没有找到");
                return;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        //4.把user传回浏览器
        user.setPassword("");
        resp.setContentType("application/json; charset=utf8");
        String respJson = objectMapper.writeValueAsString(user);
        resp.getWriter().write(respJson);
    }
}
