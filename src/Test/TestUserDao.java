package Test;

import Dao.Impl.UserDaoImpl;
import Dao.UserDao;

public class TestUserDao {
    public static void main(String[] args){
        UserDao userDao = new UserDaoImpl();
        String account ="123456";
        String password = "123456";
        userDao.login(account,password);
    }
}
