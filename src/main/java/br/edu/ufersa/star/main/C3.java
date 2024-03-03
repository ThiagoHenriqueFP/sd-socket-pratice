package br.edu.ufersa.star.main;

import br.edu.ufersa.protocol.PortMapping;
import br.edu.ufersa.star.client.Client;

public class C3 {
    public static void main(String[] args) {
        new Client("127.0.0.1", PortMapping.P1.getPort());
    }
}
