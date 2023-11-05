package com.example;
import java.net.*;
import java.io.*;

public class ServerStr
{
    Socket client = null;
    ServerSocket server = null;
    

    public void attendi(int port) {
        System.out.println("SERVER partito in esecuzione ...");
        try {
            server = new ServerSocket(port);

            while (true) {
                client = server.accept();
                System.out.println("connesso con il client " + client.getInetAddress() + " sulla porta " + client.getPort());
                Thread t = new ThreadServer(client);
                t.start();
            }
        } catch (IOException e) {
            System.out.println("Errore durante l'istanza del server !");
            e.printStackTrace();
            System.exit(1);
            }
        }
}
