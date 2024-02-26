package br.edu.ufersa.client;

import br.edu.ufersa.protocol.MessageStructure;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Logger;

public class ClientConnThread implements Runnable {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final UUID id = UUID.randomUUID();
    private final Socket client;
    private Boolean isConnected;
    private final Scanner scanner;


    public ClientConnThread(Socket socket) {
        this.client = socket;
        this.isConnected = true;
        this.scanner = new Scanner(System.in);
    }


    @Override
    public void run() {
        try {
            logger.info("Client initializing");

            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
            String body;

            while(isConnected) {
                System.out.println("send a message");
                body = scanner.nextLine();

                MessageStructure message = new MessageStructure(
                        true,
                        "",
                        id.toString(),
                        body
                );

                if (body.equalsIgnoreCase("end") || body.equalsIgnoreCase("fim"))
                    isConnected = false;
                else out.writeObject(message);
            }

            out.close();
            client.close();
            scanner.close();
            logger.info("client disconnected");
        } catch (IOException e) {
            logger.severe("IOException: " + e.getMessage());
            scanner.close();
        }
    }
}
