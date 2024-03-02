package br.edu.ufersa.server;

import br.edu.ufersa.client.Client;
import br.edu.ufersa.server.topology.CustomTopology;

import java.util.logging.Logger;

public class ServerClientThread <T extends CustomTopology> implements Runnable {

    private final Integer port;
    private final Logger logger;
    private final T clients;

    public ServerClientThread(Integer port, Logger logger, T clients) {
        this.port = port;
        this.logger = logger;
        this.clients = clients;
    }

    @Override
    public void run() {
        boolean trying = true;
        int newPort = port + 1;
        while (trying) {
            try {
                new Client("127.0.0.1", newPort > 8083 ? 8080 : newPort, clients);
                trying = false;
            } catch (Exception e) {
                try {
                    logger.warning("connection refused, retrying in 5 seconds");
                    Thread.sleep(5000L);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
            }
        }


    }
}
