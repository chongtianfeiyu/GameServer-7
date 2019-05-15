
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class test {
    ServerSocket server = null;
    Socket socket = null;
    final int port = 8080;
    ListenClient listenClient;
    // ArrayList<Socket> sockets = new ArrayList<>();
    ArrayList<MyThread> myThreads = new ArrayList<>();
    test(){
        // listentClient(port);
        listenClient = new ListenClient();
        listenClient.run();
    }
    // 路由配置
    // 配置login请求，转发数据请求，logout请求
    private boolean Router(String data){
        // data 数据格式 Type-Value
        String[] splits = data.split("-");
        String Type = splits[0];
        if (Type.equals(RequestType.LOGIN)){
            // 登陆请求
        }else if (Type.equals(RequestType.LOGOUT)){
            // 登出请求
        }else if (Type.equals(RequestType.GAMEOVER)){
            // 游戏结束请求
        }else if (Type.equals(RequestType.TRANSMIT)){
            // 转发数据请求
        }
        return true;
    }
    // handle login
    private void handleLogin(String data) {
        String[] splits = data.split("\\|");
        String usename = splits[0];
        String password = splits[1];
        // 查询数据库
        // 1 正确返回 status = 200

        // 2 错误返回 status = 502
        // 3 断开连接
    }

    // handle logout
    private void handleLogout(String data){

    }
    // handle transmit
    private void handleTransmit(String data){

    }
    // handle gameover
    private void  handleGameOver(String data){

    }
    class ListenClient implements Runnable {
        final int port = 8080;
        @Override
        public void run() {
            try {
                server = new ServerSocket(port);
                System.out.println("服务器监听"+ InetAddress.getLocalHost().getHostAddress().toString());
            }catch (IOException e){e.printStackTrace();}
            while (true) {
                try {
                    socket = server.accept();
                }catch (IOException e){e.printStackTrace();}
                    System.out.println("端口"+socket.getPort());
                    System.out.println("地址"+socket.getInetAddress());
                    //sockets.add(socket);
                    // 新建监听线程
                    MyThread thread = new MyThread(socket,Integer.toString((int)(1+Math.random()*1000)));
                    myThreads.add(thread);
                    thread.start();
                try {
                    Thread.sleep(30);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    public class MyThread extends Thread {
        Socket socket= null;
        String id = null;
        MyThread(Socket socket, String id){
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
                        String clientMessag = reader.readLine();
                        System.out.println("客户端:"+id+"发送"+clientMessag);
                        for (int i=0;i<myThreads.size();i++){
                            if (myThreads.get(i).id != id){
                                // 转发数据
                                myThreads.get(i).sendData(clientMessag);
                            }
                        }
                    }
                }
            }catch (Exception e){e.printStackTrace();}
        }

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
    private void transform(String id, String data){
        for(int i=0; i < myThreads.size();i++){
            if(myThreads.get(i).id.equals(id)) {
                myThreads.get(i).sendData(data);
            }
        }
    }
    public static void main(String[] argv){
        System.out.println("Hello world");
        new test();


    }
}
