package br.edu.ufersa.ring.server.topology;

import br.edu.ufersa.ring.client.ClientConnThread;
import br.edu.ufersa.ring.server.exceptions.NodeFullException;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class RingStructure {
    private Socket client;

    public RingStructure() {}

    public void checkAndRegisterClient(Socket client) {
        if (this.client == null || client.isClosed())
            this.client = client;
        else throw new NodeFullException("This node is Full!");
    }

    public void sendToNode(Socket node, Serializable message) {
        try {
            ObjectOutputStream out = ClientConnThread.out;

            out.reset();
            out.writeObject(message);
            out.flush();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMessage(Serializable message) {
        if (client != null && !client.isClosed()) sendToNode(client, message);
    }
}
