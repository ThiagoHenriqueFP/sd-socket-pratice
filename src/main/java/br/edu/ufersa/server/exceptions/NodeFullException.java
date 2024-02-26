package br.edu.ufersa.server.exceptions;

public class NodeFullException extends RuntimeException {
    public NodeFullException(String message) {
        super(message);
    }
}
