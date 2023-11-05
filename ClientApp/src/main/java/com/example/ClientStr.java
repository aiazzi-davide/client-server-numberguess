package com.example;

import java.io.*;
import java.net.*;
import java.util.*;

public class ClientStr {

    Socket miosocket;
    Scanner input;
    String stringaUtente;
    String risposta;
    DataOutputStream outVersoServer;
    BufferedReader inDalServer;
    boolean win = false;

    public Socket connetti(String nomeServer, int portaServer) {
        System.out.println("CLIENT in esecuzione ...");
        try {

            input = new Scanner(System.in);
            miosocket = new Socket(nomeServer, portaServer);

            outVersoServer = new DataOutputStream(miosocket.getOutputStream());
            inDalServer = new BufferedReader(new InputStreamReader(miosocket.getInputStream()));

        } catch (UnknownHostException e) {
            System.err.println("Host sconosciuto");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("something went wrong, closing client...");
            System.exit(1);
        }
        return miosocket;
    }
    public void comunica() {
        try {
            System.out.println(inDalServer.readLine());

            while (!win) {
                stringaUtente = input.next();
                outVersoServer.writeBytes(stringaUtente + '\n');

                risposta = inDalServer.readLine();
                System.out.println("SERVER: " + risposta + '\n');
                if (risposta.contains("Hai indovinato!")) {
                    win = true;
                }  
            }
            System.out.println("CLIENT: termina elaborazione e chiude connessione");
            miosocket.close();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("Errore durante la comunicazione col server!");
            System.exit(1);
        }
    } 
}
