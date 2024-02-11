package ru.gb.student.server;

import ru.gb.student.repo.RepoFile;

import javax.swing.*;
import java.awt.*;

public class ServerWindow extends JFrame implements ServerView {

    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private JButton btnStart, btnStop;
    private JTextArea log;
    private final Server server;

    public ServerWindow() {
        settings();
        server = new Server(this, new RepoFile());
    }

    private void settings() {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setResizable(true);
        setAlwaysOnTop(true);
        setTitle("Chat server");
        double x = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint().getX();
        double y = GraphicsEnvironment.getLocalGraphicsEnvironment().getCenterPoint().getY();
        setLocation((int) (x * 0.5), (int) y);
        createPanel();
        setVisible(true);
    }

    private void createPanel() {
        log = new JTextArea();
        log.setEditable(false);
        add(log);
        add(createPanBottom(), BorderLayout.SOUTH);
        JScrollPane scroll = new JScrollPane(log);
        add(scroll);
    }

    public Server getConnection() {
        return server;
    }

    private Component createPanBottom() {
        JPanel panBottom = new JPanel(new GridLayout(1, 2));
        btnStart = new JButton("Start");
        setBtnStart();
        btnStop = new JButton("Stop");
        setBtnStop();
        panBottom.add(btnStart);
        panBottom.add(btnStop);
        return panBottom;
    }

    private void setBtnStart() {
        btnStart.addActionListener(e -> server.start());
    }

    private void setBtnStop() {
        btnStop.addActionListener(e -> server.stop());
    }

    @Override
    public void messageFromServer(String s_message) {
        appendLog(s_message);
    }

    private void appendLog(String text){
        log.append(text + "\n");
    }

}
