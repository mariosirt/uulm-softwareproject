package com.sopra.ntts.model.NetworkStandard.Messages;

import com.sopra.ntts.model.NetworkStandard.DataTypes.Operations.Statistics;
import com.sopra.ntts.model.NetworkStandard.DataTypes.Operations.VictoryEnum;
import com.sopra.ntts.model.NetworkStandard.MessageContainer.MessageContainer;
import com.sopra.ntts.model.NetworkStandard.MessageTypeEnum.MessageTypeEnum;

import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Message send by the server after game end to inform about outcome.
 *
 * @author Standardisierungsdokument & Sedat Qaja
 */
public class StatisticsMessage extends MessageContainer {
    private Statistics statistics;
    private UUID winner;
    private VictoryEnum reason;
    private Boolean hasReplay;

    public StatisticsMessage(UUID playerId, MessageTypeEnum type, Date creationDate, String debugMessage, Statistics statistics, UUID winner, VictoryEnum reason, Boolean hasReplay) {
        super(playerId, MessageTypeEnum.STATISTICS, creationDate, debugMessage);
        this.statistics = statistics;
        this.winner = winner;
        this.reason = reason;
        this.hasReplay = hasReplay;
    }

    public Statistics getStatistics() {
        return statistics;
    }

    public void setStatistics(Statistics statistics) {
        this.statistics = statistics;
    }

    public UUID getWinner() {
        return winner;
    }

    public void setWinner(UUID winner) {
        this.winner = winner;
    }

    public VictoryEnum getReason() {
        return reason;
    }

    public void setReason(VictoryEnum reason) {
        this.reason = reason;
    }

    public Boolean getHasReplay() {
        return hasReplay;
    }

    public void setHasReplay(Boolean hasReplay) {
        this.hasReplay = hasReplay;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StatisticsMessage that = (StatisticsMessage) o;
        return getStatistics() == that.getStatistics() &&
                Objects.equals(getWinner(), that.getWinner()) &&
                getReason() == that.getReason() &&
                Objects.equals(getHasReplay(), that.getHasReplay());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getStatistics(), getWinner(), getReason(), getHasReplay());
    }

    @Override
    public String toString() {
        return "StatisticsMessage{" +
                "statistics=" + statistics +
                ", winner=" + winner +
                ", reason=" + reason +
                ", hasReplay=" + hasReplay +
                '}';
    }
}
