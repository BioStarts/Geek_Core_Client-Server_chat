package com.geekbrains.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class MainServer {

    Scanner inConsole = new Scanner(System.in);

    public static void main(String[] args) {
        MainServer zaz = new MainServer();
        zaz.server();
    }

    public void server() {
        try (ServerSocket serverSocket = new ServerSocket(8189)) {
            System.out.println("Сервер запущен на порту 8189");
            Socket socket = serverSocket.accept();
            System.out.println("Подключился клиент");
            DataInputStream sc = new DataInputStream(socket.getInputStream());
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            //Открываем чтение в потоке от клиента
            Thread r = new Thread(() -> {
                while (true) {
                    String msg = null;
                    try {
                        msg = sc.readUTF();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (msg.equals("/end")) {
                        System.out.println("От клиента получена команда на завершение работы");
                        break;
                    }
                    System.out.println(msg);
                }
                System.out.println("Сервер прекратил свою работу");
            });
            r.start();
            //Открываем запись в потоке для клиента
            Thread s = new Thread(() -> {
                while (true) {
                    String text = inConsole.nextLine();
                    System.out.println("Ввели со стороны сервера: " + text);
                    try {
                        out.writeUTF(text);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
            s.setDaemon(true);
            s.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
