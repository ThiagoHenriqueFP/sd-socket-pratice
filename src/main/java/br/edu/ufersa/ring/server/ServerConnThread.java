package br.edu.ufersa.ring.server;

import br.edu.ufersa.protocol.MessageStructure;
import br.edu.ufersa.protocol.PortMapping;
import br.edu.ufersa.ring.server.topology.RingStructure;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Logger;

public class ServerConnThread implements Runnable {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final Integer id;
    private final Socket client;
    private final RingStructure neighborhood;
    private boolean isConnected;

    public ServerConnThread(Socket client, RingStructure clients) {
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
                String previousPort = String.valueOf(PortMapping.getPreviousPort(messageStructure.senderId()));

                if (messageStructure.body().equalsIgnoreCase("end") || messageStructure.body().equalsIgnoreCase("fim")) {
                    isConnected = false;
                } else if (messageStructure.isBroadcast() && !previousPort.equals(this.id.toString())) {
                    System.out.println("--- broadcast message ---\n" + messageStructure.body());
                    neighborhood.sendMessage(messageStructure);
                } else if (!messageStructure.receiverId().equals(String.valueOf(id)) && !messageStructure.receiverId().isBlank()) {
                    logger.info("sending to next node");
                    neighborhood.sendMessage(messageStructure);
                } else {
                    System.out.println(
                            "Message from " + messageStructure.senderId()
                                    + "\n->" + messageStructure.body()
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
