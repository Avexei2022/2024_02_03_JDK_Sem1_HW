package ru.gb.student.client;

public interface ClientView {
    void sendMessage(String message);
    void disconnectedFromServer();
}
