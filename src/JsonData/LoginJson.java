package JsonData;

// 登陆的json数据
public class LoginJson {
    private String type ; // 登陆数据类型
    private String account ;
    private String password ; // hash取值

    public String getAccount() {
        return account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    @Override
    public String toString() {
        return "登陆数据"+account+"|"+password;
    }
}
