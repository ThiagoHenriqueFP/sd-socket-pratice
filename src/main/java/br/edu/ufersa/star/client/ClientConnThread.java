package br.edu.ufersa.star.client;

import br.edu.ufersa.protocol.MessageStructure;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Logger;

public class ClientConnThread implements Runnable {
    public static ObjectOutputStream out;
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final Integer id;
    private final Socket client;
    private Boolean isConnected;
    private final Scanner scanner;


    public ClientConnThread(Socket socket) {
        this.client = socket;
        this.id = socket.getLocalPort();
        this.isConnected = true;
        this.scanner = new Scanner(System.in);
    }


    @Override
    public void run() {
        try {
            out = new ObjectOutputStream(client.getOutputStream());

            logger.info("Client initializing");

            new Thread(new ClientInputThread(client)).start();
            String body, receiverId;

            while (isConnected) {
                System.out.println("send a message");
                body = scanner.nextLine();

                System.out.println("which client must receive?");
                MessageStructure getClients = new MessageStructure(
                        false,
                        "none",
                        this.id.toString(),
                        "getClients"
                );
                out.writeObject(getClients);
                receiverId = scanner.nextLine();

                MessageStructure message = new MessageStructure(
                        receiverId.equalsIgnoreCase("all"),
                        receiverId.equalsIgnoreCase("all") ? "" : receiverId,
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
