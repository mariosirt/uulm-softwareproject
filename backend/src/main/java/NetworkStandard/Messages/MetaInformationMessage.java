package NetworkStandard.Messages;

import NetworkStandard.MessageContainer.MessageContainer;
import NetworkStandard.MessageTypeEnum.MessageTypeEnum;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Message send by the server with meta information.
 *
 * @author Standardisierungsdokument & Sedat Qaja
 */
public class MetaInformationMessage extends MessageContainer {
    private Map<String, Object> information;

    public MetaInformationMessage(UUID playerId, MessageTypeEnum type, Date creationDate, String debugMessage) {
        super(playerId, type, creationDate, debugMessage);
    }

    public Map<String, Object> getInformation() {
        return information;
    }

    public void setInformation(Map<String, Object> information) {
        this.information = information;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MetaInformationMessage that = (MetaInformationMessage) o;
        return Objects.equals(getInformation(), that.getInformation());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getInformation());
    }

    @Override
    public String toString() {
        return "MetaInformationMessage{" +
            "information=" + information +
            '}';
    }
}
