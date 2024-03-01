package br.edu.ufersa.client;

import br.edu.ufersa.protocol.MessageStructure;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ClientOutput implements Runnable {
    private final ObjectInputStream in;
    private final Socket client;
    public ClientOutput(Socket socket) {
        this.client = socket;
        try {
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                MessageStructure messageStructure = (MessageStructure) in.readObject();
                if (messageStructure.isBroadcast() ||
                        messageStructure.receiverId()
                                .equals(String.valueOf(client.getLocalPort())))
                    System.out.println(messageStructure);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
