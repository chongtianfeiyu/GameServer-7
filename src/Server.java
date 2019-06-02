
import Dao.Impl.UserDaoImpl;
import Dao.UserDao;
import DataType.GameOverType;
import JsonData.LoginJson;
import utils.Config;
import utils.Json;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

public class Server {
    ServerSocket server = null;
    Socket socket = null;
    final int port = 8080;
    ListenClient listenClient; // 客户端监听线程
    // ArrayList<Socket> sockets = new ArrayList<>();
    // 连接线程
    private ArrayList<ClientThread> clientThreads;
    //
    private HashMap<String,ClientThread> verifiedClient; // 已经验证的客户端连接
    private Stack<String> matchPoor; // 待匹配池
    private HashMap<String,String> matchMap; // 匹配信息
    public Server(){
        init();
    }
    // 初始化
    private void init(){
        // 连接线程池
        clientThreads = new ArrayList<>();
        // 已经验证的线程
        verifiedClient = new HashMap<>();
        // 匹配池
        matchPoor = new Stack<>();
        matchMap = new HashMap<>();
        // 客户端监听线程
        listenClient = new ListenClient();
        listenClient.run();
    }

    // 路由配置
    // 配置login请求，转发数据请求，logout请求
    public void Router(String data, ClientThread clientThread ) {
        String type = Json.getType(data); // 获得类型
        if ("ERROR".equals(type)){
            // 错误的类型
            System.out.println("错误的数据格式:"+data);
        }else if (type.equals(RequestType.UPDATEGAMEBLOCK)){
            // 更新游戏方块数据,传输量最大，优先判断
            handleUpdateGameBlock(data,clientThread);
        }else if (type.equals(RequestType.LOGIN)){
            // 登陆请求
            handleLogin(data,clientThread);
        }else if (type.equals(RequestType.LOGOUT)){
            // 登出请求
        }else if (type.equals(RequestType.MATCH)) {
            // 匹配请求
            handleGameMatch(clientThread);
        }
        else if (type.equals(RequestType.GAMEOVER)){
            // 游戏结束请求
            handleGameOver(data,clientThread);
        }else if (type.equals(RequestType.GAMEGRADE)){
            // 处理游戏数据
            handleGameGrade(data,clientThread);
        }
    }
    // handle login
    private void handleLogin(String data,ClientThread clientThread) {
        try {
            LoginJson loginJson = Json.getLoginObject(data);
            // 查询数据库
            String account = loginJson.getAccount();
            String password = loginJson.getPassword();
            UserDao userDao = new UserDaoImpl();
            if (userDao.login(account,password)){
                // 登陆成功，返回成功的信息
                clientThread.setId(account);
                verifiedClient.put(account,clientThread); // 加入验证后的map
                String re =Json.getLoginSuccessResponse(account);
                clientThread.sendData(re);
            }else {
                // 登陆失败
                String status = "账号密码错误";
                String re =Json.getLoginFailureResponse(status);
                clientThread.sendData(re);
                // 返回失败的状态信息
            }
        }catch (Exception e){
            e.printStackTrace();
            clientThread.sendData(Json.getLoginFailureResponse("服务器出错"));
        }

    }

    // handle logout
    private void handleLogout(String data){

    }

    // handle transmit
    private void handleTransmit(String data){

    }
    // TODO 游戏结束请求
    private void  handleGameOver(String data,ClientThread clientThread){
        // 获得对手的id
        String ownID = clientThread.id;
        String opponentID = matchMap.get(ownID); // 从匹配信息中获得对手id
        // 将结束信息转义 win1 <-> defeat1 win2 <-> defeat2
        String gameOverType=null;
        ClientThread opponentClient=verifiedClient.get(opponentID);
        try{
            gameOverType = Json.getJsonMap(data).get("value").toString();
        }catch (IOException e){
            System.out.println("解析GameOverType错误");
            e.printStackTrace();
        }

        // 从客户端发送过来的游戏结束信息有两种
        // 1、扫完雷了 WIN1
        // 2、我方碰雷了 DEFEAT2
        if (gameOverType==null){
            System.out.println("游戏结束信息错误");
        }else if (gameOverType.equals(GameOverType.WIN1)){
            gameOverType = GameOverType.DEFEAT1; //转义
            sendGameOverResponse(gameOverType,opponentClient);
            removeMatch(ownID,opponentID);
        }else if (gameOverType.equals(GameOverType.DEFEAT2)){
            gameOverType = GameOverType.WIN2;
            sendGameOverResponse(gameOverType,opponentClient);
            removeMatch(ownID,opponentID);
        }
    }
    // 解除匹配
    private void removeMatch(String id1,String id2){
        matchMap.remove(id1);
        matchMap.remove(id2);
    }
    // 发送游戏结束请求
    private void sendGameOverResponse(String gameOverType,ClientThread clientThread){
         String re =Json.getGameOverResponse(gameOverType);
        // 向对手发送游戏结束数据
        System.out.println("向对方发送:"+re);
        clientThread.sendData(re);
    }
    // TODO handleUpdateGameBlock
    private void handleUpdateGameBlock(String data,ClientThread clientThread){
        // 获得对手的线程，向对手发送游戏数据
        String opponentId = matchMap.get(clientThread.id);
        verifiedClient.get(opponentId).sendData(data); // 转发数据
    }

    // TODO 匹配游戏请求
    private void handleGameMatch(ClientThread clientThread){
        // 判断匹配
        String ownID = clientThread.id;
        if (matchPoor.empty()){
            // 当前匹配池中没有待匹配，进栈
            matchPoor.push(ownID);
        }else{
            // 栈顶的待匹配线程出栈
            String opponentId = matchPoor.pop();
            // 设置匹配信息,双向关联
            matchMap.put(ownID,opponentId);
            matchMap.put(opponentId,ownID);
            // 返回匹配成功信息
            String re1 = Json.getMatchSuccessResponse(opponentId);
            clientThread.sendData(re1);
            String re2 = Json.getMatchSuccessResponse(ownID);
            verifiedClient.get(opponentId).sendData(re2);// 向对方发送匹配成功我的id
        }
        //
    }
    // TODO 接收游戏的成绩
    private void handleGameGrade(String data,ClientThread clientThread){
        try{
            Map gameGrade = Json.getJsonMap(data);
            String gameOverType = gameGrade.get("gameOverType").toString();
            String opponentId = gameGrade.get("opponentId").toString();
            String time = gameGrade.get("time").toString();
            String description = gameGrade.get("description").toString();
            // 游戏成绩
            String output =gameOverType+"|"+opponentId+"|"+time+"|"+description;
            System.out.println(output);

            // 插入数据
            UserDao userDao = new UserDaoImpl();
            userDao.insertGrade(clientThread.id,opponentId,Integer.valueOf(time),gameOverType,description);
        }catch (IOException e){
            e.printStackTrace();
        }

    }
    // 客户端监听类
    class ListenClient implements Runnable {
        final int port = Config.getPort();
        @Override
        public void run() {
            try {
                server = new ServerSocket(port);
                System.out.println("服务器监听"+ InetAddress.getLocalHost().getHostAddress().toString());
            }catch (IOException e){e.printStackTrace();}

            while (true) {
                try {
                    // 监听端口
                    socket = server.accept();
                }catch (IOException e)
                {
                    e.printStackTrace();
                }
                    System.out.println("端口"+socket.getPort());
                    System.out.println("地址"+socket.getInetAddress());
                    // sockets.add(socket);
                    // 新建监听线程
                    ClientThread thread = new ClientThread(socket,Integer.toString((int)(1+Math.random()*1000)));
                    clientThreads.add(thread); // 添加进连接线程中
                    thread.run();
                try {
                    Thread.sleep(60);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //  客户端线程
    class ClientThread implements Runnable {
        Socket socket;
        String id;
//        Server server;
        ClientThread(Socket socket, String id){
            this.socket = socket;
            this.id = id;
        }
        @Override
        public void run() {
            // super.run();
            try{
                while (true){
                    // 转发数据非常尽量快
                    Thread.sleep(5);
                    BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    if (reader.ready()){
                        String data = reader.readLine();
                        Router(data,this);
                    }
                }
            }catch (Exception e){e.printStackTrace();}
        }
        //
        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        // 发送数据函数
        public void sendData(String s){
            try {
                PrintWriter writer = new PrintWriter(socket.getOutputStream());
                if (writer!= null){
                    writer.println(s);
                    writer.flush();
                }
            }catch (Exception e){e.printStackTrace();}
        }
    }
//    public static void main(String[] argv){
//        System.out.println("Hello world");
//       Server server =  new Server();
//
//
//
//    }
}
