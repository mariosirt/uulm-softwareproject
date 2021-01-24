package com.sopra.ntts.model.NetworkStandard.Messages;

import com.sopra.ntts.model.NetworkStandard.MessageContainer.MessageContainer;
import com.sopra.ntts.model.NetworkStandard.MessageTypeEnum.MessageTypeEnum;

import java.util.Date;
import java.util.UUID;

/**
 * OPTIONAL
 * Message send by the client to ask the server for a reply.
 *
 * @author Standardisierungsdokument & Sedat Qaja
 */
public class RequestReplayMessage extends MessageContainer {
  public RequestReplayMessage(UUID playerId, MessageTypeEnum type, Date creationDate, String debugMessage) {
    super(playerId, MessageTypeEnum.REQUEST_REPLAY, creationDate, debugMessage);
  }
}
