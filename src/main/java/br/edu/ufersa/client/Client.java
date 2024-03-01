package br.edu.ufersa.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Logger;

public class Client implements Runnable {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final Integer id;
    private String ip;
    private Integer port;


    @Override
    public void run() {
        try {
            logger.info("Starting client...");

            Socket socket = new Socket(this.ip, this.port);
            InetAddress inet = socket.getInetAddress();

            logger.info(
                    "Client connected to "
                            + inet.getHostAddress()
                            + " with id " + this.id
            );

            ClientConnThread client = new ClientConnThread(socket);
            new Thread(client).start();

            ClientOutput output = new ClientOutput(socket);
            Thread out = new Thread(output);
            out.setDaemon(true);
            out.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Client(String host, Integer port) {
        this.id = port;
        this.ip = host;
        this.port = port;
        this.run();
    }

    public Integer getId() {
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
