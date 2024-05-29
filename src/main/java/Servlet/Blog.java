package Servlet;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.BlogDao;
import model.User;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/html/blog")
public class Blog extends HttpServlet {
    ObjectMapper objectMapper = new ObjectMapper();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        BlogDao blogDao = new BlogDao();
        String respJson = "";

        String blogId = req.getParameter("blogId");
        if(blogId==null){
            try {
                List<model.Blog> blogList = blogDao.getBlogs();
                respJson = objectMapper.writeValueAsString(blogList);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }else{
            try {
                model.Blog blog = blogDao.getBlog(Integer.parseInt(blogId));
                System.out.println("getBlog没问题！");
                respJson = objectMapper.writeValueAsString(blog);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        resp.setContentType("application/json; charset=utf8");
        resp.getWriter().write(respJson);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //1.读取请求中的参数
        req.setCharacterEncoding("utf8");
        String title = req.getParameter("title");
        String content = req.getParameter("content");

        //2.判断是否有空的
        if (title == null || title.length() == 0 || content == null || content.length() == 0) {
            resp.setContentType("text/html; charset=utf8");
            resp.getWriter().write("你的题目或者内容为空");
            return;
        }

        //3.判断登录状态
        HttpSession session = req.getSession();
        if(session==null){
            resp.sendRedirect("/login");
            return;
        }
        User user = (User) session.getAttribute("user");
        if(user==null){
            resp.sendRedirect("/login");
            return;
        }

        //4.构造blog对象
        model.Blog blog = new model.Blog();
        blog.setTitle(title);
        blog.setContent(content);
        blog.setUserId(user.getUserId());
        BlogDao blogDao = new BlogDao();
        try {
            blogDao.insert(blog);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        resp.sendRedirect("blog_list.html");
    }
}
