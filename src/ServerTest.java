import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ServerTest {
    Server server;
    JFrame jFrame;
    Container container;
    Dimension btnDimension ;
    public ServerTest(){

        init();
    }
    private void init(){
        jFrame = new JFrame("服务端测试");

//
        jFrame.setSize(200,300);
        jFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        container = jFrame.getContentPane();
        container.setLayout(new FlowLayout());
        btnDimension = new Dimension(200,40);

        // 按钮
        JButton insertGrade = new JButton("测试添加成绩");
        insertGrade.setPreferredSize(btnDimension);
        container.add(insertGrade);
        insertGrade.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                handleInsetGrade();
            }
        });
        //


        jFrame.setVisible(true);
        server = new Server();
    }
    private void handleInsetGrade(){
        String data = "" ;
//        server.Router();
    }

    public static void main(String[] args){
      new ServerTest();



    }

}
