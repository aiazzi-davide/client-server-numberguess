package com.example;
import java.util.*;
import java.net.*;
import java.io.*;

public class ThreadServer extends Thread {

    ServerSocket server = null;
    Socket client = null;
    String stringaRicevuta = null;
    String risposta = null;
    BufferedReader inDalClient;
    DataOutputStream outVersoClient;
    int randomNum;
    boolean win = false;
    int tentativi = 1;
    int difficolta = 100;
    String ANSI_GREEN = "\u001B[32m";
    String ANSI_RED = "\u001B[31m";
    String ANSI_BLUE = "\u001B[34m";
    String ANSi_RESET = "\u001B[0m";

    

    public ThreadServer(Socket c) {
        this.client = c;
    }

    public void run() {
        try {
            inDalClient = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
            outVersoClient = new DataOutputStream(client.getOutputStream());

            outVersoClient.writeBytes("Benvenuto, inserisci la difficoltà (default: 100)" + '\n');

            stringaRicevuta = inDalClient.readLine();
            System.out.println("THREAD"+ this.threadId() + ":  Ricevuta la stringa dal client : " + stringaRicevuta);
            try {
                difficolta = Integer.parseInt(stringaRicevuta);
            } catch (Exception e) {
                System.out.println("THREAD"+ this.threadId() + ":  difficoltà default: 100");
                difficolta = 100;
            }
            
            randomNum = randomNumber(difficolta);
            System.out.println("THREAD"+ this.threadId() + ":\u001B[34m numero generato: " + randomNum);
            outVersoClient.writeBytes("inserisci un numero" + '\n');

            while (!win) {
            //attendo stringa dal client
            stringaRicevuta = inDalClient.readLine();

            System.out.println("THREAD"+ this.threadId() + ":  Ricevuta la stringa dal client " + client.getPort() + ":  "+ ANSI_GREEN + stringaRicevuta);

            //elaboro la stringa
            risposta = elaboraStringa(stringaRicevuta);

            if (risposta.equals("quit")) {
                System.out.println("THREAD"+ this.threadId()+ ANSI_RED + ":  Chiusura connessione");
                client.close();
                System.exit(1);
            }

            //rispondo al client
            System.out.println("THREAD"+ this.threadId() + ":  Invio la risposta al client" + client.getInetAddress());
            outVersoClient.writeBytes(risposta+ '\n');
        } 

        client.close();
        }catch (SocketException e) {
            System.out.println("THREAD"+ this.threadId()+ ANSI_RED + ":  Connessione chiusa dal client!");
            System.exit(1);

        }catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println("THREAD"+ this.threadId()+ ANSI_RED + ":  qualcosa è andato storto!");
            System.exit(1);
        }
    }

    public String elaboraStringa(String stringaRicevuta) {
        if (stringaRicevuta.equals("quit")) {
            return "quit";
        } else {
            try {
                int numero = Integer.parseInt(stringaRicevuta);
                if (numero == randomNum) {
                    win = true;
                    return "Hai indovinato! con " + tentativi + " tentativi";
                } else if (numero > randomNum) {
                    tentativi++;
                    return "Troppo alto!";
                } else {
                    tentativi++;
                    return "Troppo basso!";
                }
            } catch (Exception e) {
                return "Non hai inserito un numero!";
            }
        }
    }

    public int randomNumber( int difficolta) {
        Random rand = new Random();
        return rand.nextInt(difficolta);
    }
}
