package br.edu.ufersa.ring.protocol;

public enum PortMapping {
    P1(8080),
    P2(8081),
    P3(8082),
    P4(8083);

    final int port;

    public int getPort() {
        return port;
    }

    PortMapping(int port) {
        this.port = port;
    }
}
