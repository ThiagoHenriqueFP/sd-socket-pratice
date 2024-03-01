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
    private final Integer id;
    private final Socket client;
    private Boolean isConnected;
    private final Scanner scanner;


    public ClientConnThread(Socket socket) {
        this.client = socket;
        this.id = socket.getPort();
        this.isConnected = true;
        this.scanner = new Scanner(System.in);
    }


    @Override
    public void run() {
        try {
            logger.info("Client initializing");

            ObjectOutputStream out = new ObjectOutputStream(client.getOutputStream());
            String body, receiverId, direction;

            while(isConnected) {
                System.out.println("send a message");
                body = scanner.nextLine();

                System.out.println("which client must receive? [8080, 8081, 8082, 8083, none]");
                receiverId = scanner.nextLine();

                System.out.println("which direction? [left, right]");
                direction = scanner.nextLine();

                if(!direction.equals("left") && !direction.equals("right"))
                    direction = "left";

                MessageStructure message = new MessageStructure(
                        receiverId.equalsIgnoreCase("none"),
                        receiverId.equalsIgnoreCase("none") ? "" : receiverId,
                        direction,
                        id.toString(),
                        body
                );

                if (body.equalsIgnoreCase("end") || body.equalsIgnoreCase("fim")){
                    isConnected = false;
                    // todo remove from server
                }
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
