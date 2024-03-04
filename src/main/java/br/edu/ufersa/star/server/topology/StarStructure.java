package br.edu.ufersa.star.server.topology;

import br.edu.ufersa.protocol.MessageStructure;
import br.edu.ufersa.star.server.exceptions.NodeNotAccessibleException;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;
import java.util.HashMap;
import java.util.logging.Logger;

public class StarStructure {
    private final Logger logger = Logger.getLogger(this.getClass().getName());
    public final static HashMap<Integer, Socket> clients = new HashMap<>();
    public final static HashMap<Integer, ObjectOutputStream> outputStreams = new HashMap<>();
    private ObjectOutputStream out;

    public void checkAndRegisterClient(Socket node) throws IOException {
        Integer port = node.getPort();

        Socket alreadyConnected = clients.get(port);

        logger.info("trying to register client");

        if (alreadyConnected == null || alreadyConnected.isClosed()){
            clients.put(port, node);
            outputStreams.put(port, new ObjectOutputStream(node.getOutputStream()));
        }
        else logger.info("socket already connected");

        logger.info("client connected with port: " + port);
    }

    public void sendMessage(Integer port, Serializable message) {
        try {
            Socket node = clients.get(port);

            if (node == null || node.isClosed())
                throw new NodeNotAccessibleException();

            out = outputStreams.get(port);

            sendMessage(message);
        } catch (RuntimeException e) {
            logger.severe(e.getClass().getName() + ": " + e.getMessage());
            if (clients.get(port).isClosed())
                clients.remove(port);
        }
    }

    public void sendBroadCast(Integer sender, Serializable message) {
        clients.keySet().forEach(
                it -> {
                    if(!sender.equals(it)){
                        this.sendMessage(it, message);
                        logger.info("sending to: " + it);
                    }
                }
        );
    }

    private boolean checkClient(Integer port) {
        Socket client = clients.get(port);
        if (client.isClosed()) {
            clients.remove(port);
            return false;
        }
        return true;
    }

    public void getClientsList(Integer port) {
        StringBuilder list = new StringBuilder();
        list.append("listClients:\n");
        clients.keySet().forEach(
                it -> {
                    if (checkClient(it) && !it.equals(port))
                        list.append(" port: ").append(it).append(" | ");

                    list.deleteCharAt(list.length() - 1);
                }
        );

        MessageStructure message = new MessageStructure(
                false,
                port.toString(),
                "8080",
                list.toString()
        );

        this.sendMessage(port, message);
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
