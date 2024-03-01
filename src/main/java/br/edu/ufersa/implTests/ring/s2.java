package br.edu.ufersa.implTests.ring;

import br.edu.ufersa.protocol.PortMapping;
import br.edu.ufersa.server.ServerInstance;
import br.edu.ufersa.server.topology.RingStructure;

public class s2 {
    public static void main(String[] args) {
        new ServerInstance<>(PortMapping.P2, RingStructure.class);
    }
}