package br.edu.ufersa.star.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Logger;

public class Client {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final String ip;
    private final Integer port;


    public void run() {
        try {
            logger.info("Starting client...");

            Socket socket = new Socket(this.ip, this.port);
            int id = socket.getPort();
            InetAddress inet = socket.getInetAddress();

            logger.info(
                    "Client connected to "
                            + inet.getHostAddress()
                            + " with id " + id
            );

            ClientConnThread client = new ClientConnThread(socket);
            new Thread(client).start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Client(String host, Integer port) {
        this.ip = host;
        this.port = port;
        this.run();
    }
}