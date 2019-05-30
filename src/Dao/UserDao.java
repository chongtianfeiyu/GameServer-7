package Dao;

// 连接数据库的Dao
public interface UserDao {
    /*
    * 登陆方法
    * @param account 账号
    * @param password 密码
    * */
    boolean login(String account,String password);
}
