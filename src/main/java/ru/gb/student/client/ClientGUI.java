package ru.gb.student.client;

import ru.gb.student.server.ServerWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class ClientGUI extends JFrame implements ClientView {
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

    private void setting(ServerWindow serverWindow) {
        setSize(WIDTH, HEIGHT);
        setResizable(true);
        setTitle("Chat client");
        double x = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint().getX();
        double y = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint().getY();
        setLocation((int) (x + x * 0.25 * Math.random()), (int) ((y *0.25) + (y * Math.random())));
        setDefaultCloseOperation(HIDE_ON_CLOSE);
        client = new Client(this, serverWindow.getConnection());
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
        String name = "Иван-" + new Random().nextInt(20);
        tfLogin = new JTextField(name);
        setTitle(name);
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
        btnLogin.addActionListener(e -> login());
    }

    private void login() {
        if (client.connectToServer(tfLogin.getText())) {
            panelTop.setVisible(false);
        }
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
                    message();
                }
            }
        });
    }

    private void setBtnSend() {
        btnSend.addActionListener(e -> message());
    }

    private void message() {
        client.sendMessage(tfMessage.getText());
        tfMessage.setText("");
    }

    @Override
    public void sendMessage(String message) {
        log.append(message);
    }

    @Override
    public void disconnectedFromServer() {
        hidePanelTop(true);
    }

    @Override
    protected void processWindowEvent(WindowEvent e) {
        super.processWindowEvent(e);
        if (e.getID() == WindowEvent.WINDOW_CLOSING) {
            client.leaveChart();
        }
    }
}


