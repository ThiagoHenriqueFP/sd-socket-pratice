package br.edu.ufersa.server.topology;

import br.edu.ufersa.protocol.MessageStructure;
import br.edu.ufersa.server.exceptions.NodeFullException;

import java.io.IOException;
import java.io.PrintStream;
import java.io.Serializable;
import java.net.Socket;

public class RingStructure extends CustomTopology {
    private Socket left;
    private Socket right;

    public RingStructure() {
        super();
    }

    @Override
    public void checkAndRegisterClient(Socket client) {
        if (left == null)
            left = client;
        else if (right == null)
            right = client;
        else throw new NodeFullException("This node is Empty!");
    }

    private void sendToNode(Socket node, Serializable message) {
        PrintStream out;
        try {

            out = new PrintStream(node.getOutputStream());
            out.println(message);
            out.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendBroadcast(Serializable message) {
        if (left != null) sendToNode(left, message);
        if (right != null) sendToNode(right, message);
    }
}
