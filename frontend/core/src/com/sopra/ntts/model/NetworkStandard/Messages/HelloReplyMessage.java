package com.sopra.ntts.model.NetworkStandard.Messages;

import com.sopra.ntts.model.NetworkStandard.Characters.CharacterInformation;
import com.sopra.ntts.model.NetworkStandard.DataTypes.MatchConfig.Matchconfig;
import com.sopra.ntts.model.NetworkStandard.DataTypes.Szenario.Scenario;
import com.sopra.ntts.model.NetworkStandard.MessageContainer.MessageContainer;
import com.sopra.ntts.model.NetworkStandard.MessageTypeEnum.MessageTypeEnum;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

/**
 * Initial message when connection is established, send by the client.
 *
 * @author Standardisierungsdokument & Sedat Qaja
 */
public class HelloReplyMessage extends MessageContainer {
    private UUID sessionId;
    private Scenario level;
    private Matchconfig settings;
    private CharacterInformation[] character_settings;

    public HelloReplyMessage(UUID playerId, MessageTypeEnum type, Date creationDate, String debugMessage, UUID sessionId, Scenario level, Matchconfig settings, CharacterInformation[] character_settings) {
        super(playerId, MessageTypeEnum.HELLO_REPLY, creationDate, debugMessage);
        this.sessionId = sessionId;
        this.level = level;
        this.settings = settings;
        this.character_settings = character_settings;
    }

    public UUID getSessionId() {
        return sessionId;
    }

    public void setSessionId(UUID sessionId) {
        this.sessionId = sessionId;
    }

    public Scenario getLevel() {
        return level;
    }

    public void setLevel(Scenario level) {
        this.level = level;
    }

    public Matchconfig getSettings() {
        return settings;
    }

    public void setSettings(Matchconfig settings) {
        this.settings = settings;
    }

    public CharacterInformation[] getCharacter_settings() {
        return character_settings;
    }

    public void setCharacter_settings(CharacterInformation[] character_settings) {
        this.character_settings = character_settings;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HelloReplyMessage message = (HelloReplyMessage) o;
        return Objects.equals(getSessionId(), message.getSessionId()) &&
                Objects.equals(getLevel(), message.getLevel()) &&
                Objects.equals(getSettings(), message.getSettings()) &&
                Arrays.equals(getCharacter_settings(), message.getCharacter_settings());
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(getSessionId(), getLevel(), getSettings());
        result = 31 * result + Arrays.hashCode(getCharacter_settings());
        return result;
    }

    @Override
    public String toString() {
        return "HelloReplyMessage{" +
                "sessionId=" + sessionId +
                ", level=" + level +
                ", settings=" + settings +
                ", character_settings=" + Arrays.toString(character_settings) +
                '}';
    }
}
