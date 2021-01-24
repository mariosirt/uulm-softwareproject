package com.sopra.ntts.model.NetworkStandard.Messages;

import com.sopra.ntts.model.NetworkStandard.MessageContainer.MessageContainer;
import com.sopra.ntts.model.NetworkStandard.MessageTypeEnum.MessageTypeEnum;

import java.util.Date;
import java.util.UUID;

/**
 * Message send by the client to inform server about client leaving the game.
 *
 * @author Standardisierungsdokument & Sedat Qaja
 */
public class GameLeaveMessage extends MessageContainer {
  public GameLeaveMessage(UUID playerId, MessageTypeEnum type, Date creationDate, String debugMessage) {
    super(playerId, MessageTypeEnum.GAME_LEAVE, creationDate, debugMessage);
  }
}
