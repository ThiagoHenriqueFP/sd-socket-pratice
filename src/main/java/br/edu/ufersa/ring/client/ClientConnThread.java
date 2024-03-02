package br.edu.ufersa.ring.client;

import br.edu.ufersa.ring.protocol.MessageStructure;
import br.edu.ufersa.ring.server.topology.RingStructure;

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


    public ClientConnThread(Socket socket, RingStructure clients) {
        this.client = socket;
        this.id = socket.getPort();
        this.isConnected = true;
        this.scanner = new Scanner(System.in);
        clients.checkAndRegisterClient(socket);
    }


    @Override
    public void run() {
        try {
            logger.info("Client initializing");

            out = new ObjectOutputStream(client.getOutputStream());
            String body, receiverId;

            while(isConnected) {
                System.out.println("send a message");
                body = scanner.nextLine();

                System.out.println("which client must receive? [8080, 8081, 8082, 8083, all]");
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
