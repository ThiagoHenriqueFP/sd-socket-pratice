package br.edu.ufersa.star.client;

import br.edu.ufersa.protocol.MessageStructure;
import br.edu.ufersa.star.server.ServerConnThread;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.logging.Logger;

public class ClientInputThread implements Runnable{
    private final Logger logger = Logger.getLogger(this.getClass().getName());

    private final Socket socket;

    public ClientInputThread(Socket in) {
        this.socket = in;
    }

    @Override
    public void run() {
        logger.info("creating input thread");
        try {
            ObjectInputStream in = ClientConnThread.in;

            while (true) {
                MessageStructure message = (MessageStructure) in.readObject();

                System.out.println(message);
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }
}
