import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

// 客户端连接线程类
public class ClientThread implements Runnable {
    private String clientId = null; // 线程id
    private String opponentId = null ; // 对手id
    private Socket socket = null; // 该客户端线程的socket

    public ClientThread(Socket socket, String id) {
        this.socket = socket;
        this.clientId = id;
    }
    // 设置对手的id
    public void setOpponentId(String opponentId) {
        this.opponentId = opponentId;
    }
    // 获得对手id
    public String getOpponentId() {
        return opponentId;
    }
    // 获得该线程的id
    public String getClientId() {return this.clientId;}

    // 向该线程发送数据
    public void sendData(String data) {
        try {
            if (socket != null){
                PrintWriter writer = new PrintWriter(socket.getOutputStream());
                writer.println(data); // 发送数据
                writer.flush();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    @Override
    public void run() {
        // 转发数据
        while (true) {
            try {
                Thread.sleep(5);
                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                if (reader.ready()) {
                    // 有数据过来
                    String clientMessage = reader.readLine();
                    // 转发数据
                }
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
