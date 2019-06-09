package View;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.util.ArrayList;

/*
* 服务器控制面板
* */
public class OperationFrame {
    private JFrame operationframe;
    private Container container;

    private JLabel ipLabel;
    private JLabel ipValueLabel;

    private JLabel portLabel;
    private JLabel portValueLabel;

    private JLabel connectionLabel;
    private JLabel connectionNumberLabel;

    private JLabel valifiedLabel;
    private JLabel valifiedNumberLabel;

    private JLabel matchLabel;
    private JLabel matchNumberLabel;

    private JButton restartBtn;
    private JButton freshBtn; // 刷新数据按钮

    private ArrayList<JLabel> labels; // 标签组
    private boolean exit = false;
    public OperationFrame() {
        init();
        startGUIThread();
    }

     // 获得居中的point
    public static Point getCenterPoint(double frameWidth,double frameHeight){
	    Toolkit toolkit = Toolkit.getDefaultToolkit();
	    Dimension screenSize = toolkit.getScreenSize();
	    double screenWidth = screenSize.getWidth();
	    double screenHeight = screenSize.getHeight();
	    return new Point((int)(screenWidth-frameWidth)/2,(int)(screenHeight-frameHeight)/2);
    }
     // 获得的大标签
    public static Font getLabelFont(){
	    return new Font("黑体",Font.BOLD,18);
    }
    // 控制面板的组件大小
    public static Dimension getOperateComponentSize(){
	    // 宽为100 高为40
	    return new Dimension(100,40);
    }
    private void init(){
        instanceComponents();
        setComponentsLayoutStyle();
        setComponentsAction();
    }

    // TODO 实例化组件函数
    private void instanceComponents(){
        labels = new ArrayList<>();

        operationframe = new JFrame("控制面板");
        container = operationframe.getContentPane();

        ipLabel = new JLabel("服务器ip");
        ipValueLabel = new JLabel("未启动");
        labels.add(ipLabel);
        labels.add(ipValueLabel);

        portLabel = new JLabel("端口:");
        portValueLabel = new JLabel("未启动");
        labels.add(portLabel);
        labels.add(portValueLabel);

        connectionLabel = new JLabel("连接人数:");
        connectionNumberLabel = new JLabel("0");
        labels.add(connectionLabel);
        labels.add(connectionNumberLabel);

        valifiedLabel = new JLabel("验证人数:");
        valifiedNumberLabel = new JLabel("0");
        labels.add(valifiedLabel);
        labels.add(valifiedNumberLabel);

        matchLabel = new JLabel("匹配人数:");
        matchNumberLabel = new JLabel("0");
        labels.add(matchLabel);
        labels.add(matchNumberLabel);

        restartBtn = new JButton("重启");
        freshBtn = new JButton("刷新");

    }

    // TODO 设置组件样式布局
    private void setComponentsLayoutStyle(){
        operationframe.setSize(new Dimension(500,300));
        // 设置居中
        operationframe.setLocation(getCenterPoint(operationframe.getWidth(),operationframe.getHeight()));
        container.setLayout(new FlowLayout());

        // 设置统一样式
        for(JLabel label:labels){
            label.setFont(getLabelFont());
            label.setPreferredSize(getOperateComponentSize());
        }

        // 设置按钮样式
        Color bgColor = new Color(64, 158, 255);
        Font btnFont = new Font("宋体",Font.BOLD,20);
        Border btnBorder = BorderFactory.createRaisedBevelBorder();

        restartBtn.setPreferredSize(getOperateComponentSize());
        restartBtn.setForeground(Color.white);
        restartBtn.setFont(btnFont);
        restartBtn.setBackground(bgColor);
        restartBtn.setBorder(btnBorder);

        freshBtn.setPreferredSize(getOperateComponentSize());
        freshBtn.setForeground(Color.white);
        freshBtn.setFont(btnFont);
        freshBtn.setBackground(bgColor);
        freshBtn.setBorder(btnBorder);

        // 添加标签组件
        for(JLabel label:labels){
            container.add(label);
        }

        // 添加按钮组件
        container.add(restartBtn);
        container.add(freshBtn);

    }

    // TODO 设置组件事件监听
    private void setComponentsAction(){
        operationframe.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        operationframe.setResizable(false);
        operationframe.setVisible(true);

    }

    // TODO 开启GUI刷新线程
    private void startGUIThread(){

    }

    // TODO 关闭GUI刷新线程,一般到关闭服务器才会调用
    private void stopGuiThread(){
        exit = true; // 自然的结束线程
    }
}
