package Dao.Impl;

import Dao.UserDao;
import utils.JDBCUtil;

import java.sql.*;

public class UserDaoImpl implements UserDao {


    @Override
    public boolean login(String account, String password) {
        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        boolean status = false;

        try {
            connection = JDBCUtil.getConnection();
            statement = connection.createStatement();

            String psSql = "select * from UserTb where account=? and password=?";
            // 语法校验
            PreparedStatement preparedStatement = connection.prepareStatement(psSql);
            // 设置?值
            preparedStatement.setString(1,account);
            preparedStatement.setString(2,password);

            System.out.println("查询语句" + psSql);
            resultSet = preparedStatement.executeQuery(); // 执行查询
            if (resultSet.next()) {
                // 登陆成功
                status = true;
                System.out.println("登陆成功");
            } else {
                System.out.println("登陆失败");
            }
        }catch (SQLException e){e.printStackTrace();}
        finally {
            JDBCUtil.release(connection,statement,resultSet); // 释放连接
        }
        return status;
    }
}
