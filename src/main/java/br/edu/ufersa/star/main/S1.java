package br.edu.ufersa.star.main;

import br.edu.ufersa.protocol.PortMapping;
import br.edu.ufersa.star.server.ServerInstance;

public class S1 {
    public static void main(String[] args) {
        new ServerInstance(PortMapping.P1);
    }
}
