package com.sopra.ntts.model.NetworkStandard.Messages;


import com.sopra.ntts.model.NetworkStandard.Characters.CharacterInformation;
import com.sopra.ntts.model.NetworkStandard.DataTypes.MatchConfig.Matchconfig;
import com.sopra.ntts.model.NetworkStandard.DataTypes.Szenario.Scenario;
import com.sopra.ntts.model.NetworkStandard.MessageContainer.MessageContainer;
import com.sopra.ntts.model.NetworkStandard.MessageTypeEnum.MessageTypeEnum;

import java.util.Date;
import java.util.UUID;

/**
 * OPTIONAL
 * Message send by the server initialized by a client request.
 *
 * @author Standardisierungsdokument & Sedat Qaja
 */
public class ReplayMessage extends MessageContainer {
    // Metadaten
    UUID sessionId;
    Date gameStart;
    Date gameEnd;
    UUID playerOneId;
    UUID playerTwoId;
    String playerOneName;
    String playerTwoName;
    Integer rounds;
    // Konfigurationsdaten
    Scenario level;
    Matchconfig settings;
    CharacterInformation[] character_settings;
    // Alle Nachrichten
    MessageContainer[] messages;

    public ReplayMessage(UUID playerId, MessageTypeEnum type, Date creationDate, String debugMessage, UUID sessionId, Date gameStart, Date gameEnd, UUID playerOneId, UUID playerTwoId, String playerOneName, String playerTwoName, Integer rounds, Scenario level, Matchconfig settings, CharacterInformation[] character_settings, MessageContainer[] messages) {
        super(playerId, MessageTypeEnum.REPLAY, creationDate, debugMessage);
        this.sessionId = sessionId;
        this.gameStart = gameStart;
        this.gameEnd = gameEnd;
        this.playerOneId = playerOneId;
        this.playerTwoId = playerTwoId;
        this.playerOneName = playerOneName;
        this.playerTwoName = playerTwoName;
        this.rounds = rounds;
        this.level = level;
        this.settings = settings;
        this.character_settings = character_settings;
        this.messages = messages;
    }
}
