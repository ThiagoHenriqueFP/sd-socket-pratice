package br.edu.ufersa.server;

import br.edu.ufersa.protocol.MessageStructure;
import br.edu.ufersa.server.topology.CustomTopology;
import br.edu.ufersa.server.topology.RingStructure;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.UUID;
import java.util.logging.Logger;

public class ServerConnThread<T extends CustomTopology> implements Runnable {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final UUID id = UUID.randomUUID();
    private final Socket client;
    private final T neighborhood;
    private boolean isConnected;
    private ObjectInputStream in;

    public ServerConnThread(Socket client, T clients) {
        this.client = client;
        this.isConnected = true;
        this.neighborhood = clients;
    }

    @Override
    public void run() {
        logger.info("Creating thread to handle " + client.getInetAddress());

        try {
            in = new ObjectInputStream(client.getInputStream());

            while (isConnected) {

                MessageStructure messageStructure = (MessageStructure) in.readObject();

                if (messageStructure.body().equalsIgnoreCase("end") || messageStructure.body().equalsIgnoreCase("fim"))
                    isConnected = false;
                else {
                    MessageStructure message = new MessageStructure(
                            messageStructure.isBroadcast(),
                            messageStructure.receiverId(),
                            this.id.toString(),
                            messageStructure.body()
                    );
                    neighborhood.sendBroadcast(message);
                    System.out.println(message);
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
