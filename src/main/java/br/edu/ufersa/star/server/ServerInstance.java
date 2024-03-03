package br.edu.ufersa.star.server;

import br.edu.ufersa.protocol.PortMapping;
import br.edu.ufersa.star.server.topology.StarStructure;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class ServerInstance {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final Integer port;
    public static StarStructure clients = new StarStructure();

    public ServerInstance(PortMapping port) {
        this.port = port.getPort();
        this.startServer();
    }

    private void startServer() {
        try {
            logger.info("Starting server");

            ServerSocket server = new ServerSocket(this.port);

            logger.info("Server running at "
                    + server.getInetAddress().getHostAddress()
                    + ":"
                    + server.getLocalPort()
            );

            logger.info("Server ready to be connected");

            while (true) {
                Socket client = server.accept();
                ServerConnThread serverThread = new ServerConnThread(client, clients);

                Thread thread = new Thread(serverThread);
                thread.start();
            }
        } catch (IOException e) {
            logger.severe("IOException: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
