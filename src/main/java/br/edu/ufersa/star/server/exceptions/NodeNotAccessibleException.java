package br.edu.ufersa.star.server.exceptions;

public class NodeNotAccessibleException extends RuntimeException {
    public NodeNotAccessibleException() {
        super("this node is unregistered or closed");
    }
}
