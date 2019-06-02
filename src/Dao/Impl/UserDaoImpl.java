package Dao.Impl;

import Dao.UserDao;
import utils.JDBCUtil;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

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

    // insertGrade 加入成绩
    @Override
    public boolean insertGrade(String account, String opponentId, int costTime, String status, String description) {
        System.out.println("插入成绩");
        Connection connection = null;
        PreparedStatement preparedStatement = null;

        try{
            connection = JDBCUtil.getConnection();
            String sql = "insert into gradeTb values (?,?,?,?,?,?,?)";
            preparedStatement = connection.prepareStatement(sql);

            // 填充字符
            String id =Integer.toString((int)(1+Math.random()*100000));
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd hh:mm:ss");

            Date date = new Date();
            String createTime = format.format(date);
            System.out.println("插入时间"+createTime);

            preparedStatement.setString(1,id);
            preparedStatement.setString(2,account);
            preparedStatement.setString(3,opponentId);
            preparedStatement.setString(4,createTime);
            preparedStatement.setString(5,String.valueOf(costTime));
            preparedStatement.setString(6,status);
            preparedStatement.setString(7,description);

            // 运行语句
            int result = preparedStatement.executeUpdate();
            if (result>0){
                System.out.println("添加成绩成功");
            }else
            {
                System.out.println("添加成绩失败");
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return false;
    }
}
