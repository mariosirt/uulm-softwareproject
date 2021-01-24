package util.logic.ingame;

import NetworkStandard.Characters.Character;
import util.Game;

import java.time.Instant;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * this timer is used during a turn phase
 *
 * @author Christian Wendlinger
 */
public class OperationTimer {
    private Timer timer;
    private AtomicBoolean running = new AtomicBoolean();
    private Game game;

    private int secondsPassed = 0;

    /**
     * constructor
     */
    public OperationTimer(Game game) {
        timer = new Timer();
        running.set(false);
        this.game = game;
    }

    /**
     * start the timer
     *
     * @param time - seconds to run
     */
    public void schedule(int time) {
        running.set(true);
        timer.schedule(generateTimerTask(time), Date.from(Instant.now()));
    }

    /**
     * stop the running timer
     *
     * @return seconds that passed since schedule was called
     */
    public int stop() {
        running.set(false);
        return secondsPassed;
    }

    /**
     * generate timer task - disqualify player after time is up
     *
     * @param seconds - seconds to wait
     * @return new Task that waits seconds and than switches to next character if necessary
     */
    private TimerTask generateTimerTask(int seconds) {
        return new TimerTask() {
            @Override
            public void run() {
                Character active = game.getGameHandler().getCurrentCharacter();

                for (int i = 0; i < seconds; i++) {
                    try {
                        Thread.sleep(1000);
                        secondsPassed++;

                        // break if already stopped
                        if (!running.get()) {
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // disqualify Player
                if (running.get()) {
                    int toDisqualify = 0;

                    if (game.getPlayerOneCharacters().contains(active)) toDisqualify = 1;
                    else if (game.getPlayerTwoCharacters().contains(active)) toDisqualify = 2;


                    if (toDisqualify != 0) {
                        game.getGameHandler().disqualify(toDisqualify);
                    }
                }
            }
        };
    }
}
