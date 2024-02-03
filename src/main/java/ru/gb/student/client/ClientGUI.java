package ru.gb.student.client;

import ru.gb.student.server.ServerWindow;

import javax.swing.*;
import java.awt.*;

public class ClientGUI extends JFrame {
    ServerWindow serverWindow;
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;
    private final JTextArea log = new JTextArea();
    private final JPanel panelTop = new JPanel(new GridLayout(2,3));
    private final JTextField tfIPAddress = new JTextField("127.0.0.1");
    private final JTextField tfPort = new JTextField("8189");
    private final JTextField tfLogin = new JTextField("Ivan Ivanovich");
    private final JPasswordField passwordField = new JPasswordField("1234567");
    private final JButton btnLogin = new JButton("Login");
    private final JPanel panelBottom = new JPanel(new BorderLayout());
    private final JTextField tfMessage = new JTextField();
    private final JButton btnSend = new JButton("Send");
    private boolean loginStatus;

    public ClientGUI(ServerWindow serverWindow) throws HeadlessException {
        this.serverWindow = serverWindow;
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocation(serverWindow.getX() + ((int)(WIDTH * 1.05)) , serverWindow.getY());
        setTitle("Chat client");
        setBtnLogin();
        setBtnSend();
        setTfMessage();
        setLogListener();
        setPanelTop();
        setPanelBottom();
        add(panelTop, BorderLayout.NORTH);
        add(panelBottom, BorderLayout.SOUTH);
        log.setEditable(false);
        JScrollPane scrolllog = new JScrollPane(log);
        add(scrolllog);
        setAlwaysOnTop(true);
        setVisible(true);

    }

    private void setBtnLogin() {
        btnLogin.addActionListener(e -> {
            if (serverWindow.isServerWorking()) {
                serverWindow.saveMessage(tfLogin.getText() + " подключился к беседе.");
                loginStatus = true;
                tfMessage.setEditable(true);
                panelTop.setVisible(false);
                log.setText(serverWindow.getLogText());
                log.append("Вы успешно подключились!");
            } else {
                log.setText("Сервер недоступен!");
            }
        });
    }

    private void setBtnSend() {
        btnSend.addActionListener(e -> {
            if (serverWindow.isServerWorking()) {
                if (!tfMessage.getText().isEmpty()) {
                    serverWindow.saveMessage(tfLogin.getText() + ": " + tfMessage.getText());
                    tfMessage.setText("");
                    log.setText(serverWindow.getLogText());
                }
            }
        });
    }

    private void setLogListener(){
        this.serverWindow.getLogValue().addPropertyChangeListener(evt -> {
            if(serverWindow.isServerWorking() && loginStatus) {
                log.setText(serverWindow.getLogText());
            } else {
                loginStatus = false;
                panelTop.setVisible(true);
                log.setText("Сервер недоступен!");
                tfMessage.setEditable(false);
            }
        });
    }


    private void setPanelTop() {
        panelTop.add(tfIPAddress);
        panelTop.add(tfPort);
        panelTop.add(new JLabel(""));
        panelTop.add(tfLogin);
        panelTop.add(passwordField);
        panelTop.add(btnLogin);
    }

    private void setPanelBottom() {
        tfMessage.setEditable(loginStatus);
        panelBottom.add(tfMessage, BorderLayout.CENTER);
        panelBottom.add(btnSend, BorderLayout.EAST);
    }

    private void setTfMessage() {
        tfMessage.addActionListener(e -> {
            if (serverWindow.isServerWorking()) {
                if (!tfMessage.getText().isEmpty()) {
                    serverWindow.saveMessage(tfLogin.getText() + ": " + tfMessage.getText());
                    tfMessage.setText("");
                    log.setText(serverWindow.getLogText());
                }
            }
        });
    }
}


