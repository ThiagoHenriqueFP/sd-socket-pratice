package br.edu.ufersa;

import br.edu.ufersa.server.ServerInstance;
import br.edu.ufersa.server.topology.RingStructure;

public class ServerApplicationMain {
    public static void main(String[] args) {
        new ServerInstance<>(8080, RingStructure.class);
    }
}