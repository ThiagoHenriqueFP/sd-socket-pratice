package br.edu.ufersa.protocol;

public enum PortMapping {
    P1(8080),
    P2(8081),
    P3(8082),
    P4(8083);

    final int port;

    public int getPort() {
        return port;
    }

    public static int getPreviousPort(String port) {
        int parsed = Integer.parseInt(port);
        return switch (parsed) {
            case 8080 -> 8083;
            case 8081 -> 8080;
            case 8082 -> 8081;
            case 8083 -> 8082;
            default -> 8080;
        };
    }

    PortMapping(int port) {
        this.port = port;
    }
}
