package com.sopra.ntts.controller;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;

/**
 * @author Rinor Asaj
 */
public class MusicPlayer {
    /*
    Declaration of the background music and some sounds for exmaple for the buttons
     */
    public Music menu_music, game_music;
    public Sound button_sound;
    public Sound move_sound_1;
    public Sound move_sound_2;

    /***
     * MusicPlayer constructor. For each variable the path of the music file is written.
     */
    public MusicPlayer() {
        this.menu_music = Gdx.audio.newMusic(Gdx.files.internal("audio/Secret_Agent_2.mp3"));
        this.game_music = Gdx.audio.newMusic(Gdx.files.internal("audio/Secret_Agent_1.mp3"));
        this.button_sound = Gdx.audio.newSound(Gdx.files.internal("audio/button_click.mp3"));
        this.move_sound_1 = Gdx.audio.newSound(Gdx.files.internal("audio/move_sound_1.wav"));
        this.move_sound_2 = Gdx.audio.newSound(Gdx.files.internal("audio/move_sound_2.mp3"));
    }

    /***
     *
     * @param music
     */
    public void stopMusic(Music music) {
        if(music.isPlaying()) {
            music.stop();
        }
    }

    /***
     *
     * @param sound
     */
    public void playSound(Sound sound) {
        sound.play();
    }

    /***
     *
     * @param music
     * @param looping
     */
    public void playMusic(Music music, boolean looping) {
        if(!music.isPlaying()) {
            music.play();
            music.setLooping(looping);
        }
    }

    /***
     * method for disposing the music/sound
     */
    public void disposeAudio() {
        this.menu_music.dispose();
        this.game_music.dispose();
        this.button_sound.dispose();
        this.move_sound_1.dispose();
        this.move_sound_2.dispose();
    }
}
