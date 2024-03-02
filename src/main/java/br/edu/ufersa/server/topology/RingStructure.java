package br.edu.ufersa.server.topology;

import br.edu.ufersa.client.ClientConnThread;
import br.edu.ufersa.server.exceptions.NodeFullException;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.net.Socket;

public class RingStructure extends CustomTopology {
    private Socket client;

    public RingStructure() {
        super();
    }

    @Override
    public void checkAndRegisterClient(Socket client) {
        if (this.client == null || client.isClosed())
            this.client = client;
        else throw new NodeFullException("This node is Full!");
    }

    @Override
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

    @Override
    public void sendMessage(Serializable message) {
        if (client != null && !client.isClosed()) sendToNode(client, message);
    }

//    @Override
//    public void getNodes() {
//        ObjectOutputStream out;
//        try {
//
//        }
//    }
}
