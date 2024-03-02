package br.edu.ufersa.ring.server.exceptions;

public class NodeFullException extends RuntimeException {
    public NodeFullException(String message) {
        super(message);
    }
}
