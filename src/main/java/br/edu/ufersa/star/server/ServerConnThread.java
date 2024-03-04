package br.edu.ufersa.star.server;

import br.edu.ufersa.protocol.MessageStructure;
import br.edu.ufersa.star.server.topology.StarStructure;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Logger;

public class ServerConnThread implements Runnable {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final StarStructure neighborhood = ServerInstance.clients;
    private final Integer id;
    private final Socket client;
    private boolean isConnected;

    public ServerConnThread(Socket client) {
        this.client = client;
        this.id = client.getLocalPort();
        this.isConnected = true;
    }

    @Override
    public void run() {
        logger.info("Creating thread to handle " + client.getPort());

        try {
            ObjectInputStream in = new ObjectInputStream(client.getInputStream());

            while (isConnected) {

                MessageStructure messageStructure = (MessageStructure) in.readObject();

                if (messageStructure.isBroadcast()) {
                    logger.info("sending broadcast");
                    neighborhood.sendBroadCast(Integer.parseInt(messageStructure.senderId()), messageStructure);
                } else if (messageStructure.body().equals("getClients")) {
                    neighborhood.getClientsList(Integer.parseInt(messageStructure.senderId()));
                } else if (messageStructure.receiverId().equals(String.valueOf(this.id))) {
                    System.out.println(messageStructure);
                } else {
                    logger.info("forwarding message to: " + messageStructure.receiverId());
                    try {
                        neighborhood.sendMessage(
                                Integer.parseInt(messageStructure.receiverId()),
                                messageStructure
                        );
                    } catch (NumberFormatException e) {
                        logger.severe("Entrada de dados invalida");
                    }
                }
            }

            in.close();
            client.close();
        } catch (IOException | ClassNotFoundException e) {
            logger.severe(e.getMessage() + ": " + e.getMessage());
        }

    }
}
