package br.edu.ufersa.star.client;

import br.edu.ufersa.protocol.MessageStructure;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Logger;

public class ClientInputThread implements Runnable {
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final Socket socket;

    public ClientInputThread(Socket in) {
        this.socket = in;
    }

    @Override
    public void run() {
        logger.info("creating input thread");
        try {
            ObjectInputStream in = new ObjectInputStream(socket.getInputStream());

            while (true) {
                MessageStructure message = (MessageStructure) in.readObject();
                if (message.body().startsWith("listClients:")) {
                    System.out.println(
                            "----- clients alive -----\n"
                            + message.body()
                            + "\nto send a broadcast, enter \"all\"");
                } else if (message.isBroadcast()) {
                    System.out.println("--- broadcast message ---\n" + message.body());
                } else {
                    System.out.println(
                            "Message from " + message.senderId()
                                    + "\n->" + message.body()
                    );
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            logger.severe(e.getClass().getName() + " : " + e.getMessage());
        }

    }
}
