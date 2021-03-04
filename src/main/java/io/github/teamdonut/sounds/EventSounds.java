package io.github.teamdonut.sounds;

import io.github.teamdonut.proj.utils.Logger;
import javafx.scene.media.AudioClip;

public final class EventSounds {
    private static EventSounds instance;
    private final String buttonSound1 = "button_sound_1.mp3";
    private final String buttonSound2 = "button_sound_2.mp3";
    private AudioClip clip;

    public static EventSounds getInstance() {
        if (instance == null)
            instance = new EventSounds();
        return instance;
    }

    private EventSounds() {}

    public void playButtonSound1() {
        try {
            clip = new AudioClip(getClass().getResource(buttonSound1).toString());
            clip.play();
        } catch (Exception e) {
            Logger.log(e);
        }
    }

    public void playButtonSound2() {
        try {
            clip = new AudioClip(getClass().getResource(buttonSound2).toString());
            clip.play();
        } catch (Exception e) {
            Logger.log(e);
        }
    }
}