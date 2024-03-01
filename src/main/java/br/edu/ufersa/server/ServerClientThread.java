package br.edu.ufersa.server;

import br.edu.ufersa.client.Client;

import java.util.logging.Logger;

public class ServerClientThread implements Runnable {

    private final Integer port;
    private final Logger logger;

    public ServerClientThread(Integer port, Logger logger) {
        this.port = port;
        this.logger = logger;
    }

    @Override
    public void run() {
        boolean trying = true;
        while (trying) {
            try {
                int newPort = port;
                new Client("127.0.0.1", ++newPort > 8083 ? 8080 : newPort);
                trying = false;
            } catch (Exception e) {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                logger.warning("connection refused, retrying in 5 seconds");
            }
        }


    }
}
