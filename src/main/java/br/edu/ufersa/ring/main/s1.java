package br.edu.ufersa.ring.main;

import br.edu.ufersa.ring.protocol.PortMapping;
import br.edu.ufersa.ring.server.ServerInstance;
import br.edu.ufersa.ring.server.topology.RingStructure;

public class s1 {
    public static void main(String[] args) {
        new ServerInstance<>(PortMapping.P1, RingStructure.class);
    }
}