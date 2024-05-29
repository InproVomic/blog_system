package model;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

//完成对博客表的操作
public class BlogDao {
    //1. 新增操作
    public void insert(Blog blog) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        try {
            //1.建立连接
            connection = DBUtil.getConnection();
            //2.构建sql语句
            String sql = "insert into blog values (null,?,?,now(),?)";
            statement = (PreparedStatement) connection.prepareStatement(sql);
            statement.setString(1,blog.getTitle());
            statement.setString(2,blog.getContent());
            statement.setInt(3,blog.getUserId());
            //3.执行sql
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            DBUtil.close(connection,statement,null);
        }
    }
    //2. 查询博客列表
    public List<Blog> getBlogs() throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        List<Blog> blogList = new ArrayList<>();

        try {
            connection = DBUtil.getConnection();
            String sql = "select * from blog order by postTime desc";//降序排序，保证查看的时候最上面的都是最新的博客
            statement = (PreparedStatement) connection.prepareStatement(sql);
            resultSet = statement.executeQuery();
            while(resultSet.next()){
                Blog blog = new Blog();
                blog.setBlogId(resultSet.getInt("blogId"));
                blog.setTitle(resultSet.getString("title"));
                String content = resultSet.getString("content");
                //如果content的内容过长对其进行截断
                if(content.length() > 100){
                    content = content.substring(0,100) + "...";
                }
                blog.setContent(content);
                blog.setPostTime(resultSet.getTimestamp("postTime"));
                blog.setUserId(resultSet.getInt("userId"));
                blogList.add(blog);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            DBUtil.close(connection,statement,resultSet);
        }
        return blogList;
    }

    //3.根据blogId查找博客
    public Blog getBlog(int blogId) throws SQLException {
        System.out.println("开始查询数据库,blogId为："+blogId);
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DBUtil.getConnection();
            String sql = "select * from blog where blogId = ?";
            statement = (PreparedStatement) connection.prepareStatement(sql);
            statement.setInt(1,blogId);
            resultSet = statement.executeQuery();
            if(resultSet.next()){
                System.out.println("执行后无大碍！");
                Blog blog = new Blog();
                blog.setTitle(resultSet.getString("title"));
                blog.setUserId(resultSet.getInt("userId"));
                System.out.println("到userId也没问题");
                blog.setPostTime(resultSet.getTimestamp("postTime"));
                blog.setBlogId(resultSet.getInt("blogId"));
                System.out.println("到blogId也没问题");
                blog.setContent(resultSet.getString("content"));
                System.out.println("到查询数据库也没啥问题！");
                return blog;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            DBUtil.close(connection,statement,resultSet);
        }
        return null;
    }
    //4.根据blogId删除博客
    public void delete(int blogId) throws SQLException {
        Connection connection = null;
        PreparedStatement statement = null;

        try {
            connection = DBUtil.getConnection();
            String sql = "delete from blog where blogId = ?";
            statement = (PreparedStatement) connection.prepareStatement(sql);
            statement.setInt(1,blogId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            DBUtil.close(connection,statement,null);
        }
    }
}
