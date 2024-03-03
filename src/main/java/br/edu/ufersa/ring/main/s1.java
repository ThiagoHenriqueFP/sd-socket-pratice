package br.edu.ufersa.ring.main;

import br.edu.ufersa.protocol.PortMapping;
import br.edu.ufersa.ring.server.ServerInstance;

public class s1 {
    public static void main(String[] args) {
        new ServerInstance(PortMapping.P1);
    }
}