package NetworkStandard.Messages;

import NetworkStandard.MessageContainer.MessageContainer;
import NetworkStandard.MessageTypeEnum.MessageTypeEnum;

import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

/**
 * Message send by the client to request meta information.
 *
 * @author Standardisierungsdokument & Sedat Qaja
 */
public class RequestMetaInformationMessage extends MessageContainer {
    private String[] keys;

    public RequestMetaInformationMessage(UUID clientId, MessageTypeEnum type, Date creationDate, String debugMessage, String[] keys) {
        super(clientId, type, creationDate, debugMessage);
        this.keys = keys;
    }

    public String[] getKeys() {
        return keys;
    }

    public void setKeys(String[] keys) {
        this.keys = keys;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RequestMetaInformationMessage that = (RequestMetaInformationMessage) o;
        return Arrays.equals(getKeys(), that.getKeys());
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(getKeys());
    }

    @Override
    public String toString() {
        return "RequestMetaInformationMessage{" +
            "keys=" + Arrays.toString(keys) +
            '}';
    }
}