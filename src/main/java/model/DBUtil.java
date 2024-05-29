package model;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;

//这个类是用来封装数据库并建立连接
public class DBUtil {
     private volatile static DataSource dataSource = null;

    private DBUtil() {

    }

    public static DataSource getDataSource(){
         if(dataSource==null){
             synchronized (DBUtil.class){
                 if(dataSource==null){
                     dataSource = new MysqlDataSource();
                     ((MysqlDataSource)dataSource).setURL("jdbc:mysql://127.0.0.1:3306/blog_system?characterEncoding=utf8&useSSL=false");
                     ((MysqlDataSource)dataSource).setUser("root");
                     ((MysqlDataSource)dataSource).setPassword("15016246620");
                 }
             }
         }
         return dataSource;
     }

     public static Connection getConnection() throws SQLException {
         return (Connection) getDataSource().getConnection();
     }

     public static void close(Connection connection, PreparedStatement statement, ResultSet resultSet) throws SQLException {
         if(connection != null){
             connection.close();
         }
         if(statement!=null){
             statement.close();
         }
         if(resultSet!=null){
             resultSet.close();
         }
     }
}
