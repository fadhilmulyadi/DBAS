package main;

import java.net.URL;

import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.AudioInputStream;

public class Soound {
    
    Clip musicClip;
    URL url[] = new URL[10];

    public Soound() {
        url[0] = getClass().getResource("/res/BackgroundMusic.wav"); // changed to wav
        url[1] = getClass().getResource("/res/delete line.wav");
        url[2] = getClass().getResource("/res/gameover.wav");
        url[3] = getClass().getResource("/res/rotation.wav");
        url[4] = getClass().getResource("/res/touch floor.wav");

        for (int i = 0; i < url.length; i++) {
            if (url[i] == null) {
                System.err.println("Audio resource not found at index " + i);
            }
        }
    }

    public void play(int i, boolean music) {

        try {
            if (url[i] == null) {
                System.err.println("Audio URL is null for index " + i);
                return;
            }
            System.out.println("Playing audio index: " + i + " URL: " + url[i]);
            AudioInputStream ais = AudioSystem.getAudioInputStream(url[i]);
            Clip clip = AudioSystem.getClip();
            
            if(music) {
                musicClip = clip;
            }

            clip.open(ais);
            clip.addLineListener(new LineListener() {
                @Override
                public void update(LineEvent event) {
                    if(event.getType() == LineEvent.Type.STOP) {
                        clip.close();
                    }
                }
            });
            ais.close();
            clip.start();

        } catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    public void loop() {
        if(musicClip != null) {
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }
    
    public void stop() {
        if(musicClip != null) {
            musicClip.stop();
            musicClip.close();
        }
    }

}
