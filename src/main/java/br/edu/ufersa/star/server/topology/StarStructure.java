package br.edu.ufersa.star.server.topology;

import br.edu.ufersa.protocol.MessageStructure;
import br.edu.ufersa.star.client.ClientConnThread;
import br.edu.ufersa.star.server.exceptions.NodeNotAccessibleException;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Logger;

public class StarStructure {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    private final HashMap<Integer, Socket> clients = new HashMap<>();
    private final ObjectOutputStream out = ClientConnThread.out;

    public void checkAndRegisterClient(Socket node) {
        Integer port = node.getPort();

        Socket alreadyConnected = clients.get(port);

        logger.info("trying to register client");

        if (alreadyConnected == null || alreadyConnected.isClosed())
            clients.put(port, node);
        else
            logger.info("socket already connected");

        logger.info("client connected with port: " + port);
    }

    public void sendMessage(Integer port, Serializable message) {
        try {
            Socket node = clients.get(port);

            if (node == null || node.isClosed())
                throw new NodeNotAccessibleException();

            sendMessage(message);
        } catch (RuntimeException e) {
            logger.severe(e.getClass().getName() + ": " + e.getMessage());
            if (clients.get(port).isClosed())
                clients.remove(port);
        }
    }

    public void sendBroadCast(Serializable message) {
        clients.keySet().forEach(
                it -> this.sendMessage(it, message)
        );
    }

    public void getClientsList(Integer port) {
            StringBuilder list = new StringBuilder();
            clients.keySet().forEach(
                    it -> list.append("port: ").append(it).append("\n")
            );

            MessageStructure message = new MessageStructure(
              false,
              port.toString(),
              "",
              list.toString()
            );

            this.sendMessage(message);
    }

    private void sendMessage(Serializable message) {
        try {
            out.writeObject(message);
            out.flush();
            out.reset();
        } catch (IOException e) {
            logger.severe(e.getClass().getName() + ": " + e.getMessage());
        }
    }
}
