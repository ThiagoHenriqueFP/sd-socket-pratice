package br.edu.ufersa.ring.client;

import br.edu.ufersa.ring.server.topology.RingStructure;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Logger;

public class Client {
    private final Logger logger = Logger.getLogger(this.getClass().toString());
    private final Integer id;
    private final String ip;
    private final Integer port;
    private final RingStructure clients;


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

            ClientConnThread client = new ClientConnThread(socket, this.clients);
            new Thread(client).start();

//            ClientOutput output = new ClientOutput(socket);
//            Thread out = new Thread(output);
//            out.setDaemon(true);
//            out.start();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Client(String host, Integer port, RingStructure clients) {
        this.id = port;
        this.ip = host;
        this.port = port;
        this.clients = clients;
        this.run();
    }
}