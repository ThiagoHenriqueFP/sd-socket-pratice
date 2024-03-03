package br.edu.ufersa.star.server;

import br.edu.ufersa.protocol.MessageStructure;
import br.edu.ufersa.star.server.topology.StarStructure;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

public class ServerConnThread implements Runnable {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final Integer id;
    private final Socket client;
    private final StarStructure neighborhood;
    private boolean isConnected;

    public ServerConnThread(Socket client, StarStructure clients) {
        this.client = client;
        this.id = client.getLocalPort();
        this.isConnected = true;
        this.neighborhood = clients;
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
                    neighborhood.sendBroadCast(messageStructure);
                } else if (messageStructure.body().equals("getClients")) {
                    neighborhood.getClientsList(Integer.parseInt(messageStructure.senderId()));
                } else if (messageStructure.receiverId().equals(String.valueOf(this.id))) {
                    System.out.println(messageStructure);
                } else {
                    logger.info("forwarding message to: " + messageStructure.receiverId());
                    neighborhood.sendMessage(
                            Integer.parseInt(messageStructure.receiverId()),
                            messageStructure
                    );
                }
            }

            in.close();
            client.close();
        } catch (IOException e) {
            logger.severe("IOException: " + e.getMessage());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

    }
}
