package br.edu.ufersa.ring.server;

import br.edu.ufersa.ring.protocol.PortMapping;
import br.edu.ufersa.server.topology.CustomTopology;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class ServerInstance<T extends CustomTopology> {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final Integer port;
    public T clients;

    public ServerInstance(PortMapping port, Class<T> clients) {
        this.port = port.getPort();
        try {
            this.clients = clients.getDeclaredConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            logger.severe("Error to construct new ServerInstance: " + e.getMessage());
            throw new RuntimeException(e);
        }
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

            logger.info("launching clients");

            new Thread(new ServerClientThread(port, logger, clients)).start();

            while (true) {
                Socket client = server.accept();
                ServerConnThread<T> serverThread = new ServerConnThread<>(client, clients);

                Thread thread = new Thread(serverThread);
                thread.start();
            }
        } catch (IOException e) {
            logger.severe("IOException: " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

}
