package com.sopra.ntts.model.NetworkStandard.Messages;

import com.sopra.ntts.model.NetworkStandard.MessageContainer.MessageContainer;
import com.sopra.ntts.model.NetworkStandard.MessageTypeEnum.MessageTypeEnum;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Message send by the server to inform the client about a received Strike.
 *
 * @author Standardisierungsdokument & Sedat Qaja
 */
public class Strike extends MessageContainer {
    private Integer strikeNr;
    private Integer strikeMax;
    private String reason;

    public Strike(UUID playerId, MessageTypeEnum type, Date creationDate, String debugMessage, Integer strikeNr, Integer strikeMax, String reason) {
        super(playerId, MessageTypeEnum.STRIKE, creationDate, debugMessage);
        this.strikeNr = strikeNr;
        this.strikeMax = strikeMax;
        this.reason = reason;
    }

    public Integer getStrikeNr() {
        return strikeNr;
    }

    public void setStrikeNr(Integer strikeNr) {
        this.strikeNr = strikeNr;
    }

    public Integer getStrikeMax() {
        return strikeMax;
    }

    public void setStrikeMax(Integer strikeMax) {
        this.strikeMax = strikeMax;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Strike strike = (Strike) o;
        return Objects.equals(getStrikeNr(), strike.getStrikeNr()) &&
                Objects.equals(getStrikeMax(), strike.getStrikeMax()) &&
                Objects.equals(getReason(), strike.getReason());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStrikeNr(), getStrikeMax(), getReason());
    }

    @Override
    public String toString() {
        return "Strike{" +
                "strikeNr=" + strikeNr +
                ", strikeMax=" + strikeMax +
                ", reason='" + reason + '\'' +
                '}';
    }
}
