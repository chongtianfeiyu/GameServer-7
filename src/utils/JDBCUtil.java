package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

// 连接数据库工具类
public class JDBCUtil {

    private static String driveClass = null;
    private static String url = null;
    private static String name = null;
    private static String password = null;

    static {
        try {
            // 1. 创建一个属性配置对象
            Properties properties = new Properties();
            InputStream inputStream = new FileInputStream("jdbc.properties");
            properties.load(inputStream);
            // 读取属性
            driveClass = properties.getProperty("driverClass");
            url = properties.getProperty("url");
            name = properties.getProperty("name");
            password = properties.getProperty("password");
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    // 获得连接对象
    public static Connection getConnection(){
        Connection connection = null;
        try {
            Class.forName(driveClass); // 加载驱动类
            connection = DriverManager.getConnection(url, name, password);
        }catch (ClassNotFoundException e){
            System.err.println("没有发现驱动类"+e.getMessage());
        }catch (SQLException e){
            System.err.println("SQL异常"+e.getMessage());
        }
        return connection;
    }

    // 释放连接
    public static void release(Connection connection, Statement statement, ResultSet resultSet){
        closeResultSet(resultSet);
        closeStatement(statement);
        closeConnection(connection);
    }

    //
    private static void closeResultSet(ResultSet resultSet){
        try{
            if (resultSet != null) resultSet.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    //
    private static void closeStatement(Statement statement){
        try{
            if (statement != null) statement.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
    private static void closeConnection(Connection connection){
        try {
            if (connection != null)
                connection.close();
        }catch (SQLException e){
            e.printStackTrace();
        }
    }
}
