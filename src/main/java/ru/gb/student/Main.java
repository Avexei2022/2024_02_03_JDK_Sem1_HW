package ru.gb.student;

import ru.gb.student.client.ClientGUI;
import ru.gb.student.server.ServerWindow;

public class Main {
    public static void main(String[] args) {
        ServerWindow serverWindow = new ServerWindow();
        new ClientGUI(serverWindow);
        new ClientGUI(serverWindow);
        new ClientGUI(serverWindow);
    }
}
