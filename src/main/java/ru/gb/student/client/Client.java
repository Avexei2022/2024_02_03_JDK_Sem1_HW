package ru.gb.student.client;

import ru.gb.student.server.Server;

public class Client {
    private final ClientView clientView;
    private String name;
    private final Server server;
    private boolean connected;

    public Client(ClientView clientView, Server server) {
        this.clientView = clientView;
        this.server = server;
    }

    public boolean connectToServer(String name){
        this.name = name;
        if (server.connectUser(this)){
            showOnWindow("Вы успешно подключились!\n");
            connected = true;
            String log = server.getLog();
            if (log != null){
                showOnWindow(log);
            }
            return true;
        } else {
            showOnWindow("Подключение не удалось");
            return false;
        }
    }

    public void disconnectFromServer(){
        if (connected) {
            connected = false;
            clientView.disconnectedFromServer();
            showOnWindow("Вы были отключены от сервера!");
        }
    }

    public void leaveChart() {
        server.disconnectUser(this);
    }

    public void sendMessage(String message){
        if (connected) {
            if (!message.isEmpty()) {
                server.message(name + ": " + message);
            }
        } else {
            showOnWindow("Нет подключения к серверу");
        }
    }

    public void showOnWindow(String text) {
        clientView.sendMessage(text + "\n");
    }

    public String getName() {
        return name;
    }
}
