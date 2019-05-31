
import Dao.Impl.UserDaoImpl;
import Dao.UserDao;
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

public class Server {
    ServerSocket server = null;
    Socket socket = null;
    final int port = 8080;
    ListenClient listenClient; // 客户端监听线程
    // ArrayList<Socket> sockets = new ArrayList<>();
    // 连接线程
    private ArrayList<ClientThread> clientThreads;
    //
    HashMap<String,ClientThread> verifiedClient; // 已经验证的客户端连接

    public Server(){
        init();
    }
    // 初始化
    private void init(){
        // 连接线程池
        clientThreads = new ArrayList<>();
        // 已经验证的线程
        verifiedClient = new HashMap<>();
        // 客户端监听线程
        listenClient = new ListenClient();
        listenClient.run();
    }

    // 路由配置
    // 配置login请求，转发数据请求，logout请求
    private void Router(String data, ClientThread clientThread ) {
        String type = Json.getType(data); // 获得类型
        if (type.equals(RequestType.LOGIN)){
            // 登陆请求
            handleLogin(data,clientThread);
        }else if (type.equals(RequestType.LOGOUT)){
            // 登出请求

        }else if (type.equals(RequestType.GAMEOVER)){
            // 游戏结束请求
            handleGameOver(data,clientThread);
        }else if (type.equals(RequestType.TRANSMIT)){
            // 转发数据请求
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
    // handle gameover
    private void  handleGameOver(String data,ClientThread clientThread){
        // 获得对手的id

        // 向对手发送游戏结束数据

    }

    // TODO 接收游戏的成绩
    private void handleGameGrade(String data,ClientThread clientThread){

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
        Socket socket= null;
        String id = null;
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
//                        System.out.println("客户端:"+id+"发送"+clientMessag);
//                        String type = Json.getType(clientMessag);
//                        if (type.equals(RequestType.LOGIN)){
//                            //
//                            System.out.println("登陆信息"+clientMessag);
//                            String success = "{\"type\":\"LOGINSUCCESS\",\"value\":\"45453232\"}";
////                            myThreads.get(0).sendData(success);
//                        }else if (type.equals(RequestType.MATCH)){
//                            System.out.println("匹配请求");
//                            String re = "" +
//                                    "{\"type\":\"MATCHSUCCESS\"," +
//                                    "\"value\":\"32323232\"}";
////                            myThreads.get(0).sendData(re);
//                        }else if (type.equals(RequestType.UPDATEGAMEBLOCK)){
//                            // 棋盘更新数据
////                            for (ClientThread myThread:myThreads){
////                                if (myThread.id != id){
////                                    myThread.sendData(clientMessag);
////                                }
////                            }
//                        }
//
//                        for (int i=0;i<myThreads.size();i++){
//                            if (myThreads.get(i).id != id){
//                                // 转发数据
//                                myThreads.get(i).sendData(clientMessag);
//                            }
//                        }
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

    // 将数据发送给id的线程
//    private void transform(String id, String data){
//        for(int i=0; i < myThreads.size();i++){
//            if(myThreads.get(i).id.equals(id)) {
//                myThreads.get(i).sendData(data);
//            }
//        }
//    }
    public static void main(String[] argv){
        System.out.println("Hello world");
        new Server();


    }
}
