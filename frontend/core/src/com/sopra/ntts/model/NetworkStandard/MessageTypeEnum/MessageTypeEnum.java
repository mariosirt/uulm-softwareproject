package com.sopra.ntts.model.NetworkStandard.MessageTypeEnum;

/**
 * List of all MessageType.
 *
 * @author Standardisierungsdokument & Sedat Qaja
 */
public enum MessageTypeEnum {
    // Spielinitialisierung
    HELLO,
    HELLO_REPLY,
    RECONNECT,
    CONNECTION_ERROR,
    GAME_STARTED,
    // Wahlphase
    REQUEST_ITEM_CHOICE,
    ITEM_CHOICE,
    REQUEST_EQUIPMENT_CHOICE,
    EQUIPMENT_CHOICE,
    // Spielphase
    GAME_STATUS,
    REQUEST_GAME_OPERATION,
    GAME_OPERATION,
    SPECTATOR_REVEAL,
    // Spielende
    STATISTICS,
    // Kontrollnachrichten
    GAME_LEAVE,
    GAME_LEFT,
    REQUEST_GAME_PAUSE,
    GAME_PAUSE,
    REQUEST_CONFIG_DELIVERY,
    CONFIG_DELIVERY,
    STRIKE,
    // Optionale Komponenten
    REQUEST_REPLAY,
    REPLAY,
    ERROR,
    META_INFORMATION,
    REQUEST_META_INFORMATION
}
