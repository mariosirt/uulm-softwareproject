package util.logic.ingame;

import util.Game;

import java.time.Instant;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * this timer handles the time limit to reconnect
 *
 * @author Christian Wendlinger
 */
public class ReconnectTimer {
    private Timer timer;
    private AtomicBoolean running = new AtomicBoolean();
    private Game game;
    private int disconnectedUser;

    /**
     * constructor
     *
     * @param game
     * @param disconnectedUser
     */
    public ReconnectTimer(Game game, int disconnectedUser) {
        timer = new Timer();
        running.set(false);
        this.game = game;
        this.disconnectedUser = disconnectedUser;
    }

    /**
     * start the timer
     *
     * @param time seconds to run
     */
    public void schedule(int time) {
        running.set(true);
        timer.schedule(generateTimerTask(time), Date.from(Instant.now()));
    }

    /**
     * stop the timer
     */
    public void stop() {
        running.set(false);
    }

    /**
     * generate timer task - disqualify the player after the time is up
     *
     * @param seconds - seconds to wait
     * @return new Task that waits seconds and than switches to next character if necessary
     */
    private TimerTask generateTimerTask(int seconds) {
        return new TimerTask() {
            @Override
            public void run() {
                for (int i = 0; i < seconds; i++) {
                    try {
                        Thread.sleep(1000);

                        if (!running.get()) {
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // end game
                if (running.get()) {
                    game.getGameHandler().disqualify(disconnectedUser);
                }
            }
        };
    }
}
