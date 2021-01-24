package util;

/**
 * This class represents anything / anybody that can connect to the server.
 *
 * @author Christian Wendlinger
 */

import NetworkStandard.DataTypes.RoleEnum;
import org.java_websocket.WebSocket;

import java.util.Objects;
import java.util.UUID;

public class User {
    private UUID PlayerId;
    private WebSocket connection;
    private String username;
    private RoleEnum role;
    private boolean disconnected;
    private int strikeCount;
    private int drunkCocktail;
    private int spilledCocktail;

    /**
     * Constructor.
     *
     * @param playerId   generated by the Server
     * @param connection generated by the Connection
     * @param username   given from the user
     * @param role       given from the user
     */
    public User(UUID playerId, WebSocket connection, String username, RoleEnum role) {
        PlayerId = playerId;
        this.connection = connection;
        this.username = username;
        this.role = role;
        this.disconnected = false;
        this.strikeCount = 0;
        this.drunkCocktail = 0;
        this.spilledCocktail = 0;
    }

    /**
     * Getter and Setter.
     */
    public UUID getPlayerId() {
        return PlayerId;
    }

    public void setPlayerId(UUID playerId) {
        PlayerId = playerId;
    }

    public WebSocket getConnection() {
        return connection;
    }

    public void setConnection(WebSocket connection) {
        this.connection = connection;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public RoleEnum getRole() {
        return role;
    }

    public void setRole(RoleEnum role) {
        this.role = role;
    }

    public boolean isDisconnected() {
        return disconnected;
    }

    public void setDisconnected(boolean disconnected) {
        this.disconnected = disconnected;
    }

    public int getStrikeCount() {
        return strikeCount;
    }

    public void setStrikeCount(int strikeCount) {
        this.strikeCount = strikeCount;
    }

    public int getDrunkCocktail() {
        return drunkCocktail;
    }

    public void iterateDrunkCocktail() {
        this.drunkCocktail++;
    }

    public int getSpilledCocktail() {
        return spilledCocktail;
    }

    public void iterateSpilledCocktail() {
        this.spilledCocktail++;
    }

    /**
     * Equals and Hashcode.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return isDisconnected() == user.isDisconnected() &&
                getStrikeCount() == user.getStrikeCount() &&
                getDrunkCocktail() == user.getDrunkCocktail() &&
                getSpilledCocktail() == user.getSpilledCocktail() &&
                Objects.equals(getPlayerId(), user.getPlayerId()) &&
                Objects.equals(getConnection(), user.getConnection()) &&
                Objects.equals(getUsername(), user.getUsername()) &&
                getRole() == user.getRole();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPlayerId(), getConnection(), getUsername(), getRole(), isDisconnected(), getStrikeCount(), getDrunkCocktail(), getSpilledCocktail());
    }

    /**
     * toString.
     */
    @Override
    public String toString() {
        return "User{" +
                "PlayerId=" + PlayerId +
                ", connection=" + connection +
                ", username='" + username + '\'' +
                ", role=" + role +
                ", disconnected=" + disconnected +
                ", strikeCount=" + strikeCount +
                ", drunkCocktail=" + drunkCocktail +
                ", spilledCocktail=" + spilledCocktail +
                '}';
    }
}