package com.geekbrains.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

public class Start {

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    Scanner inConsole = new Scanner(System.in);

    public void initialize() {
        try {
            socket = new Socket("localhost", 8189);
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            Thread t = new Thread(() -> {
                try {
                    while (true) {
                        String msg = in.readUTF();
                        System.out.println(msg);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    closeConnection();
                }
            });
            t.setDaemon(true);
            t.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendMsg() {
        Thread s = new Thread(() -> {
            while (true) {
                String text = inConsole.nextLine();
                System.out.println("Ввели со стороны клиента: " + text);
                try {
                    out.writeUTF(text);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        s.start();
    }



    public void closeConnection() {
        try {
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        Start lol = new Start();
        lol.initialize();
        lol.sendMsg();
    }


}
