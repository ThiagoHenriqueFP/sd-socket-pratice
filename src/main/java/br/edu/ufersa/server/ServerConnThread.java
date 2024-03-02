package br.edu.ufersa.server;

import br.edu.ufersa.protocol.MessageStructure;
import br.edu.ufersa.server.topology.CustomTopology;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Logger;

public class ServerConnThread<T extends CustomTopology> implements Runnable {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final Integer id;
    private final Socket client;
    private final T neighborhood;
    private boolean isConnected;

    public ServerConnThread(Socket client, T clients) {
        this.client = client;
        this.id = client.getLocalPort();
        this.isConnected = true;
        this.neighborhood = clients;
    }

    @Override
    public void run() {
        logger.info("Creating thread to handle " + client.getLocalPort());

        try {
            ObjectInputStream in = new ObjectInputStream(client.getInputStream());

            while (isConnected) {

                MessageStructure messageStructure = (MessageStructure) in.readObject();

                if (messageStructure.body().equalsIgnoreCase("end") || messageStructure.body().equalsIgnoreCase("fim"))
                    isConnected = false;
                else if (!messageStructure.receiverId().equals(String.valueOf(id))) {
                    logger.info("sending to next node");
                    neighborhood.sendMessage(messageStructure);
                }
                else {
                    System.out.println(messageStructure);
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
