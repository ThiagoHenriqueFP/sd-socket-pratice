package br.edu.ufersa.server.topology;

import br.edu.ufersa.protocol.MessageStructure;
import br.edu.ufersa.server.exceptions.NodeFullException;

import java.io.Serializable;
import java.net.Socket;

public abstract class CustomTopology {

    public CustomTopology() {
    }

    public abstract void checkAndRegisterClient(Socket client) throws NodeFullException;
    public abstract void sendBroadcast(Serializable message);
}
