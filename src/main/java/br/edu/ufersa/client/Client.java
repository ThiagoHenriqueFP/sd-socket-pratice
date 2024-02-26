package br.edu.ufersa.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.UUID;
import java.util.logging.Logger;

public class Client {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final String id;
    private String ip;
    private Integer port;


    private void run() {
        try {
            logger.info("Starting client...");

            Socket socket = new Socket(this.ip, this.port);
            InetAddress inet = socket.getInetAddress();

            logger.info(
                    "Client connected to "
                            + inet.getHostAddress()
                            + "with id " + this.id
            );

            ClientConnThread client = new ClientConnThread(socket);

            Thread clientThread = new Thread(client);
            clientThread.start();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Client(String host, Integer port) {
        this.id = UUID.randomUUID().toString();
        this.ip = host;
        this.port = port;
        this.run();
    }

    public Client() {
        this.id = UUID.randomUUID().toString();
    }

    public String getId() {
        return id;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }
}
