package kz.bsbnb.common.consts;

/**
 * Created by Olzhas.Pazyldayev on 22.12.2017.
 */
public enum MessageType {
    INCOMING,
    OUTGOING;

    public static MessageType getMessageType(String messageType) {
        return valueOf(messageType);
    }
}
