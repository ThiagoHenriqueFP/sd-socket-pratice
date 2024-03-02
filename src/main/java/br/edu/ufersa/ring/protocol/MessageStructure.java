package br.edu.ufersa.ring.protocol;

import java.io.Serializable;

public record MessageStructure(
        Boolean isBroadcast,
        String receiverId,
        String senderId,
        String body

)
        implements Serializable{

}
