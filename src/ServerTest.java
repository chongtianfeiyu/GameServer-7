import javax.swing.*;
import java.awt.*;

public class ServerTest {
    Server server;
    JFrame jFrame;
    Container container;
    Dimension btnDimension ;
    public ServerTest(){

        init();
    }
    private void init(){
        server = new Server();
        jFrame = new JFrame("服务端测试");
        jFrame.setSize(100,300);
        container = jFrame.getContentPane();
        container.setLayout(new FlowLayout());
        btnDimension = new Dimension(100,40);

        // 按钮
    }

    public static void main(String[] args){
      new ServerTest();



    }

}
