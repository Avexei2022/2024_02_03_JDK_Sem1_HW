package ru.gb.student.server;

import ru.gb.student.client.Client;
import ru.gb.student.client.ClientGUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class ServerWindow extends JFrame {

    private final File logFile = new File("log.txt");
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;

    private final ArrayList<Client> clientList;

    private JButton btnStart, btnStop;
    private JTextArea log;
    private boolean work;


    public ServerWindow() throws HeadlessException {
        clientList = new ArrayList<>();
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setResizable(true);
        setAlwaysOnTop(true);
        setTitle("Chat server");
        setLocationRelativeTo(null);
        createPanel();
        setVisible(true);

    }

    private void createPanel() {
        log = new JTextArea();
        add(log);
        add(createPanBottom(), BorderLayout.SOUTH);
        JScrollPane scrolllog = new JScrollPane(log);
        add(scrolllog);
    }

    public String getLog() {
        return readFromLogFile();
    }



    private void writeToLogFile(String newStringToLog) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(logFile, true)) {
            fos.write(newStringToLog.getBytes());
            fos.write("\n".getBytes());
        } catch (IOException e) {
            throw (new IOException(e.getMessage()));
        }
    }
//    private String readLog(){
//        StringBuilder stringBuilder = new StringBuilder();
//        try (FileReader reader = new FileReader(LOG_PATH);){
//            int c;
//            while ((c = reader.read()) != -1){
//                stringBuilder.append((char) c);
//            }
//            stringBuilder.delete(stringBuilder.length()-1, stringBuilder.length());
//            return stringBuilder.toString();
//        } catch (Exception e){
//            e.printStackTrace();
//            return null;
//        }
//    }

    private String readFromLogFile(){
        String string;
        try (FileInputStream fis = new FileInputStream(logFile)) {
            string = new String(fis.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return e.getMessage();
        }
        return string;
    }
    private void appendLog(String text){
        log.append(text + "\n");
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
        btnStart.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (work){
                    appendLog("Сервер уже был запущен");
                } else {
                    work = true;
                    appendLog("Сервер запущен!");
                }
            }
        });
    }

    private void setBtnStop() {
        btnStop.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (!work){
                    appendLog("Сервер уже был остановлен");
                } else {
                    work = false;
                    while (!clientList.isEmpty()){
                        disconnectUser(clientList.get(clientList.size()-1));
                    }
                    appendLog("Сервер остановлен!");
                }
            }
        });
    }


    public boolean connectUser(Client client){
        if (!work){
            return false;
        }
        clientList.add(client);
        return true;
    }
    public void disconnectUser(Client client){
        clientList.remove(client);
        if (client != null){
            client.disconnectFromServer();
        }
    }

    private void setLog() {
        log.setEditable(false);
        log.setText("Сервер остановлен!");
        log.setEnabled(false);
    }

    public void message(String text){
        if (!work){
            return;
        }
        appendLog(text);
        answerAll(text);
        try {
            writeToLogFile(text);
        } catch (IOException e) {
            log.setText(e.getMessage());
        }
    }


    private void answerAll(String text){
        for (Client client: clientList){
            client.showOnWindow(text);
        }
    }
}
