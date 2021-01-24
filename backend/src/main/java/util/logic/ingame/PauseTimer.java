package util.logic.ingame;

import util.Game;

import java.time.Instant;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * this timer is used to track the pause
 *
 * @author Christian Wendlinger
 */
public class PauseTimer {
    private Timer timer;
    private AtomicBoolean running = new AtomicBoolean();
    private Game game;

    /**
     * constructor
     *
     * @param game
     */
    public PauseTimer(Game game) {
        timer = new Timer();
        running.set(false);
        this.game = game;
    }

    /**
     * start the timer
     *
     * @param time seconds for the timer to run
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
     * generate timer task - resume the game after time is finished
     *
     * @param seconds - seconds to wait
     * @return new Task that waits seconds and than switches to next character if necessary
     */
    private TimerTask generateTimerTask(int seconds) {
        return new TimerTask() {
            @Override
            public void run() {
                try {
                    for (int i = 0; i < seconds; i++) {
                        Thread.sleep(1000);

                        // break if already stopped
                        if (!running.get()) {
                            break;
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                // enforce unpause if time is over
                if (running.get()) {
                    game.receiver.resume(true);
                }
            }
        };
    }
}
