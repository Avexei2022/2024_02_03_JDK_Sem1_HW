package ru.gb.student.server;

import javax.swing.*;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ServerWindow extends JFrame {

    private final File logFile = new File("log.txt");
    private static final int WIDTH = 400;
    private static final int HEIGHT = 300;
    private final JButton btnStart = new JButton("Start");
    private final JButton btnStop = new JButton("Stop");
    private final JTextArea log = new JTextArea();

    private boolean isServerWorking;

    public ServerWindow() throws HeadlessException {
        isServerWorking = false;
        setBtnStart();
        setBtnStop();
        setLog();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(WIDTH, HEIGHT);
        setLocationRelativeTo(null);
        setTitle("Chat server");
        setResizable(true);
        setAlwaysOnTop(true);
        JPanel panBottom = new JPanel(new GridLayout(1,2));
        panBottom.add(btnStart);
        panBottom.add(btnStop);
        add(panBottom, BorderLayout.SOUTH);
        JScrollPane scrolllog = new JScrollPane(log);
        add(scrolllog);
        setVisible(true);
    }

    public boolean isServerWorking() {
        return isServerWorking;
    }

    private void setBtnStart() {
        btnStart.addActionListener(e -> {
            if(!isServerWorking) {
                isServerWorking = true;
                log.setText("Сервер запущен!\n" + readFromLogFile());
                log.setEnabled(true);
            }
        });
    }

    private void setBtnStop() {
        btnStop.addActionListener(e -> {
            if(isServerWorking) {
                isServerWorking = false;
                log.setText("Сервер остановлен!\n");
                log.setEnabled(false);
            }
        });
    }

    private void setLog() {
        log.setEditable(false);
        log.setText("Сервер остановлен!");
        log.setEnabled(false);
    }

    public void saveMessage(String message){
        try {
            writeToLogFile(message);
        } catch (IOException e) {
            log.setText(e.getMessage());
        }
        setLogTextFromFile();
    }

    private void writeToLogFile(String newStringToLog) throws IOException {
        try (FileOutputStream fos = new FileOutputStream(logFile, true)) {
            fos.write(newStringToLog.getBytes());
            fos.write("\n".getBytes());
        } catch (IOException e) {
            throw (new IOException(e.getMessage()));
        }
    }

    private void setLogTextFromFile() {
        log.setText(readFromLogFile());
    }

    public String getLogText(){
        return log.getText();
    }

    public JTextComponent.AccessibleJTextComponent getLogValue() {
        return (JTextComponent.AccessibleJTextComponent)
                log.getAccessibleContext().getAccessibleText();
    }

    private String readFromLogFile(){
        String string;
        try (FileInputStream fis = new FileInputStream(logFile)) {
            string = new String(fis.readAllBytes(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            return e.getMessage();
        }
        return string;
    }

}
