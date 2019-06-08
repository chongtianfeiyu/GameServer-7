package View;

import javax.swing.*;
import java.awt.*;

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

    private boolean exit = false;
    public OperationFrame() {
        init();
        startGUIThread();
    }
    private void init(){
        instanceComponents();
        setComponentsLayoutStyle();
        setComponentsAction();
    }

    // TODO 实例化组件函数
    private void instanceComponents(){

    }

    // TODO 设置组件样式布局
    private void setComponentsLayoutStyle(){

    }

    // TODO 设置组件事件监听
    private void setComponentsAction(){}

    // TODO 开启GUI刷新线程
    private void startGUIThread(){

    }

    // TODO 关闭GUI刷新线程,一般到关闭服务器才会调用
    private void stopGuiThread(){
        exit = true; // 自然的结束线程
    }
}
