package ru.gb.student.client;

import ru.gb.student.server.ServerWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class ClientGUI extends JFrame implements View{
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;
    private Client client;



    private JTextArea log;
    JTextField tfIPAddress, tfPort, tfLogin, tfMessage;
    JPasswordField passwordField;
    JButton btnLogin, btnSend;
    JPanel panelTop, panelBottom;

    public ClientGUI(ServerWindow serverWindow) {
        setting(serverWindow);
        createPanel();
        setAlwaysOnTop(true);
        setVisible(true);
    }

    private void setting(ServerWindow server) {
        setSize(WIDTH, HEIGHT);
        setResizable(true);
        setTitle("Chat client");
        setLocation(server.getX() + ((int)(WIDTH * 1.05)) , server.getY());
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        client = new Client(this, server);
    }

    private void createPanel() {
        add(createPanelTop(), BorderLayout.NORTH);
        add(createLog());
        add(createPanelBottom(), BorderLayout.SOUTH);
    }

    private void hidePanelTop(boolean visible){
        panelTop.setVisible(visible);
    }

    private Component createPanelTop() {
        panelTop = new JPanel(new GridLayout(2,3));
        tfIPAddress = new JTextField("127.0.0.1");
        tfPort = new JTextField("8189");
        tfLogin = new JTextField("Ivan Ivanovich");
        passwordField = new JPasswordField("1234567");
        btnLogin = new JButton("Login");
        setBtnLogin();

        panelTop.add(tfIPAddress);
        panelTop.add(tfPort);
        panelTop.add(new JLabel(""));
        panelTop.add(tfLogin);
        panelTop.add(passwordField);
        panelTop.add(btnLogin);
        return panelTop;
    }

    private void setBtnLogin() {
        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                connectedToServer();
            }
        });
    }

    private Component createLog(){
        log = new JTextArea();
        log.setEditable(false);
        return new JScrollPane(log);
    }

    private Component createPanelBottom() {
        panelBottom = new JPanel(new BorderLayout());
        tfMessage = new JTextField();
        setTfMessage();

        btnSend = new JButton("Send");
        setBtnSend();
        panelBottom.add(tfMessage, BorderLayout.CENTER);
        panelBottom.add(btnSend, BorderLayout.EAST);
        return panelBottom;
    }

    private void setTfMessage() {
        tfMessage.addKeyListener(new KeyAdapter() {
            @Override
            public void keyTyped(KeyEvent e) {
                if (e.getKeyChar() == '\n'){
                    sendMessage();
                }
            }
        });
    }

    private void setBtnSend() {
        btnSend.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendMessage();
            }
        });
    }

//    @Override
//    protected void processWindowEvent(WindowEvent e) {
//        super.processWindowEvent(e);
//        if (e.getID() == WindowEvent.WINDOW_CLOSING){
//            disconnectedFromServer();
//        }
//    }

    public void sendMessage(){
        client.sendMessage(tfMessage.getText());
        tfMessage.setText("");
    }
    @Override
    public void sendMessage(String message) {
        log.append(message);
    }

    @Override
    public void connectedToServer() {
        if (client.connectToServer(tfLogin.getText())){
            hidePanelTop(false);
        }
    }

    @Override
    public void disconnectedFromServer() {
        hidePanelTop(true);
        client.disconnectFromServer();
    }
}


