package com.sopra.ntts.model.NetworkStandard.Messages;

import com.sopra.ntts.model.NetworkStandard.DataTypes.Operations.Operation;
import com.sopra.ntts.model.NetworkStandard.MessageContainer.MessageContainer;
import com.sopra.ntts.model.NetworkStandard.MessageTypeEnum.MessageTypeEnum;

import java.util.Date;
import java.util.UUID;

/**
 * Message send by the client to notify the server about all the chosen operation.
 *
 * @author Standardisierungsdokument & Sedat Qaja
 */
public class GameOperationMessage extends MessageContainer {
    Operation operation;

  public GameOperationMessage(UUID playerId, MessageTypeEnum type, Date creationDate, String debugMessage, Operation operation) {
    super(playerId, MessageTypeEnum.GAME_OPERATION, creationDate, debugMessage);
    this.operation = operation;
  }
}
