package ru.gb.student.server;

import ru.gb.student.client.Client;
import ru.gb.student.repo.RepoFile;
import ru.gb.student.repo.RepoInterface;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Server{
    private final ServerView serverView;
    private final List<Client> clientList;
    private final RepoInterface<String> repoInterface;
    boolean work;

    public Server(ServerView serverView, RepoInterface<String> repoInterface) {
        this.serverView = serverView;
        this.repoInterface = repoInterface;
        clientList = new ArrayList<>();
    }

    public void start() {
        if (work){
            showOnWindow("Сервер уже был запущен");
        } else {
            work = true;
            showOnWindow("Сервер запущен!");
        }
    }

    public void stop() {
        if (!work){
            showOnWindow("Сервер уже был остановлен");
        } else {
            for (Client client: clientList)
                client.disconnectFromServer();
            clientList.clear();
            showOnWindow("Сервер остановлен!");
        }
    }

    public boolean connectUser(Client client){
        if (!work){
            return false;
        }
        clientList.add(client);
        String message = client.getName() + " подключился к беседе";
        showOnWindow(message);
        answerAll(message);
        return true;
    }

    public void disconnectUser(Client client){
        clientList.remove(client);
        if (client != null){
            String message = client.getName() + " отключился от беседы";
            showOnWindow(message);
            answerAll(message);
        }
    }

    public String getLog() {
        return loadChartHistory();
    }

    private void saveChartHistory (String newStringToLog) throws IOException {
        repoInterface.save(newStringToLog);
    }

    private String loadChartHistory(){
        try {
            return repoInterface.load();
        } catch (IOException e) {
            return e.getMessage();
        }
    }

    public void message(String text){
        if (!work){
            return;
        }
        showOnWindow(text);
        answerAll(text);
        try {
            saveChartHistory(text);
        } catch (IOException e) {
            showOnWindow(e.getMessage());
        }
    }

    private void answerAll(String text){
        for (Client client: clientList){
            client.showOnWindow(text);
        }
    }

    private void showOnWindow(String text) {
        serverView.messageFromServer(text);
    }



}
