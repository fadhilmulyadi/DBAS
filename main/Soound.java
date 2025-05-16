package main;

import java.net.URL;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.AudioInputStream;

public class Soound {
    
    Clip musicClip; // Objek Clip untuk musik
    URL url[] = new URL[10]; // Array untuk menyimpan URL audio

    public static boolean sfxEnabled = true; // Status untuk mengaktifkan/menonaktifkan efek suara
    private final int musicStartIndex = 0; // Indeks awal untuk musik
    private final int musicEndIndex = 2; // Indeks akhir untuk musik

    // Konstruktor untuk menginisialisasi URL audio
    public Soound() {
        url[0] = getClass().getResource("/res/sound/music/BackgroundMusic.wav"); // Musik latar
        url[1] = getClass().getResource("/res/sound/music/BackgroundMusicSTCU.wav"); // Musik latar alternatif
        url[2] = getClass().getResource("/res/sound/music/BackgroundMusicHR.wav"); // Musik latar alternatif lainnya

        url[3] = getClass().getResource("/res/sound/sfx/delete line.wav"); // Efek suara untuk menghapus garis
        url[4] = getClass().getResource("/res/sound/sfx/gameover.wav"); // Efek suara untuk game over
        url[5] = getClass().getResource("/res/sound/sfx/rotation.wav"); // Efek suara untuk rotasi
        url[6] = getClass().getResource("/res/sound/sfx/touch floor.wav"); // Efek suara untuk menyentuh lantai

        // Memeriksa apakah semua URL audio berhasil diinisialisasi
        for (int i = 0; i < url.length; i++) {
            if (url[i] == null) {
                System.err.println("Audio resource not found at index " + i); // Menampilkan pesan kesalahan jika URL tidak ditemukan
            }
        }
    }

    // Metode untuk memutar audio berdasarkan indeks
    public void play(int i, boolean music) {
        try {
            // Memeriksa apakah URL audio valid
            if (url[i] == null) {
                System.err.println("Audio URL is null for index " + i);
                return;
            }

            // Memeriksa apakah efek suara dinonaktifkan
            if (!isMusicIndex(i) && !sfxEnabled) {
                System.out.println("SFX is disabled. Skipping sound index: " + i);
                return;
            }

            // Menampilkan informasi tentang audio yang sedang diputar
            System.out.println("Playing audio index: " + i + " URL: " + url[i]);
            AudioInputStream ais = AudioSystem.getAudioInputStream(url[i]); // Mendapatkan aliran audio
            Clip clip = AudioSystem.getClip(); // Mendapatkan objek Clip
            
            // Menyimpan clip musik jika indeksnya adalah musik
            if(isMusicIndex(i)) {
                musicClip = clip;
            }

            clip.open(ais); // Membuka clip dengan aliran audio
            clip.addLineListener(new LineListener() { // Menambahkan pendengar untuk mendeteksi event
                @Override
                public void update(LineEvent event) {
                    if(event.getType() == LineEvent.Type.STOP) {
                        clip.close(); // Menutup clip jika sudah berhenti
                    }
                }
            });
            ais.close(); // Menutup aliran audio
            clip.start(); // Memulai pemutaran audio

        } catch(Exception e) {
            e.printStackTrace(); // Menampilkan stack trace jika terjadi kesalahan
        }
    }
    
    // Metode untuk memutar musik secara berulang
    public void loop() {
        if(musicClip != null) {
            musicClip.loop(Clip.LOOP_CONTINUOUSLY); // Memutar musik secara terus menerus
        }
    }
    
    // Metode untuk menghentikan pemutaran musik
    public void stop() {
        if(musicClip != null) {
            musicClip.stop(); // Menghentikan pemutaran musik
            musicClip.close(); // Menutup clip
        }
    }

    // Metode untuk memeriksa apakah indeks yang diberikan adalah indeks musik
    protected boolean isMusicIndex(int i) {
        return i >= musicStartIndex && i <= musicEndIndex; // Mengembalikan true jika indeks dalam rentang musik
    }
}
