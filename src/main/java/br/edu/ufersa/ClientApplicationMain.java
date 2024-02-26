package br.edu.ufersa;

import br.edu.ufersa.client.Client;

public class ClientApplicationMain {
    public static void main(String[] args) {
        new Client("127.0.0.1", 8080);
    }
}
