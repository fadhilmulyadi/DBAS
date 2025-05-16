package main;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.InputStream;
import java.io.IOException;

public class GamePanel extends JPanel implements Runnable {

    // Ukuran panel permainan
    public static final int WIDHTH = 1280;
    public static final int HEIGHT = 720;

    // Target frame per detik
    final int FPS = 60;

    // Thread utama untuk menjalankan loop game
    Thread gameThread;

    // Objek manajer permainan yang mengatur logika game
    PlayManager pm;

    // Objek suara untuk musik latar dan efek suara
    public static Soound music = new Soound();
    public static Soound se = new Soound();

    // Gambar latar belakang permainan
    BufferedImage backgroundImage;

    // Menu utama dengan daftar pilihan
    String[] menuItems = {"START GAME", "SETTINGS", "CREDITS", "EXIT"};

    // Indeks item menu yang sedang dipilih
    int selectedMenuIndex = 0;

    // Status apakah sedang menampilkan menu utama
    boolean onMainMenu = true;

    // Menu pengaturan dengan daftar pilihan
    String[] settingsItems = {"SFX: ON", "MUSIC: ON", "MUSIC: Default ", "BACK"};

    // Indeks item pengaturan yang sedang dipilih
    int selectedSettingsIndex = 0;

    // Status apakah sedang di menu pengaturan
    boolean onSettings = true;

    // Status pengaturan efek suara (SFX) aktif atau tidak
    public static boolean sfxEnabled = true;

    // Status pengaturan musik aktif atau tidak
    boolean musicEnabled = true;

    // Daftar pilihan musik yang tersedia
    String[] musicList = {"Default", "Stecu", "Heavy Rotation"};

    // Indeks musik yang sedang dipilih atau dimainkan
    int currentMusicIndex = 0;

    // Listener keyboard khusus menu, disimpan agar bisa ditambahkan dan dihapus sesuai kebutuhan
    MenuKeyHandler menuKeyHandler = new MenuKeyHandler();

    // Listener input utama untuk kontrol game
    InputHandler inputHandler = new InputHandler();

    public GamePanel() {
        // Atur ukuran preferensi panel sesuai lebar dan tinggi yang telah ditentukan
        this.setPreferredSize(new Dimension(WIDHTH, HEIGHT));

        // Set warna latar belakang panel menjadi hitam
        this.setBackground(Color.BLACK);

        // Nonaktifkan layout manager default (menggunakan null layout)
        this.setLayout(null);

        // Tambahkan key listener khusus untuk menu agar bisa menerima input keyboard pada menu
        this.addKeyListener(menuKeyHandler);

        // Pastikan panel bisa menerima fokus agar key listener berfungsi
        this.setFocusable(true);

        // Memuat nilai skor tertinggi yang tersimpan
        PlayManager.loadHighScore();

        // Membuat instance PlayManager untuk mengatur logika permainan
        pm = new PlayManager();

        // Memuat gambar latar belakang dari resource file
        try {
            InputStream is = getClass().getResourceAsStream("/res/background panel.png");
            backgroundImage = ImageIO.read(is);
        } catch (IOException e) {
            System.out.println("Gagal memuat background.png");
            e.printStackTrace();
        }
    }

    public void launchGame() {
        // Membuat dan memulai thread utama game yang menjalankan loop permainan
        gameThread = new Thread(this);
        gameThread.start();

        // Memulai dan mengulang pemutaran musik latar secara terus-menerus
        music.play(0, true);
        music.loop();
    }

    @Override
    public void run() {
        // Interval waktu tiap frame dalam nanodetik berdasarkan target FPS
        double drawInterval = 1000000000 / FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        // Loop utama game berjalan selama thread tidak null
        while (gameThread != null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            // Ketika sudah cukup waktu untuk satu frame, jalankan update dan repaint
            if (delta >= 1) {
                update();
                repaint();
                delta--;
            }
        }
    }

    private void update() {
        // Update logika game hanya berjalan saat bukan di menu utama, bukan di menu pengaturan,
        // tidak dalam keadaan pause, dan permainan belum selesai (gameOver = false)
        if (!onMainMenu && !onSettings && !InputHandler.pausePressed && !pm.gameOver) {
            pm.update();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        // Memanggil metode paintComponent superclass untuk membersihkan panel terlebih dahulu
        super.paintComponent(g);

        // Konversi objek Graphics ke Graphics2D untuk fitur grafis yang lebih lengkap
        Graphics2D g2 = (Graphics2D) g;

        // Jika gambar latar belakang berhasil dimuat, gambar di seluruh area panel
        if (backgroundImage != null) {
            g2.drawImage(backgroundImage, 0, 0, WIDHTH, HEIGHT, null);
        }

        // Tampilkan menu utama jika sedang berada di menu utama
        if (onMainMenu) {
            drawMainMenu(g2);
        }
        // Tampilkan menu pengaturan jika sedang berada di menu pengaturan
        else if (onSettings) {
            drawSettingsMenu(g2);
        }
        // Jika tidak di menu, jalankan rendering game melalui PlayManager
        else {
            pm.draw(g2);
        }
    }

    private void drawMainMenu(Graphics2D g2) {
        Font customFont = FontLoader.loadFont("res/font/PressStart2P-Regular.ttf", 30f);
        g2.setFont(customFont);
        g2.setColor(Color.YELLOW);
        
        int startY = 280;
        int spacing = 70;
        
        // Atur font untuk judul game dengan ukuran 70
        Font titleFont = customFont.deriveFont(70f);
        g2.setFont(titleFont);

        FontMetrics fm = g2.getFontMetrics();
        
        String titleText = "D'BAS";
        int titleWidth = fm.stringWidth(titleText);
        g2.setColor(Color.YELLOW);
        // Gambar judul game di tengah layar
        g2.drawString(titleText, (WIDHTH - titleWidth) / 2, HEIGHT - 600);
        
        // Atur font untuk subjudul dengan ukuran 20
        Font subTitleFont = customFont.deriveFont(20f);
        g2.setFont(subTitleFont);
        
        fm = g2.getFontMetrics(); // update font metrics sesuai font baru
        String subTitleText = "DYNAMIC BLOCK ALLOCATION SYSTEM";
        int subTitleWidth = fm.stringWidth(subTitleText);
        // Gambar subjudul di tengah layar
        g2.drawString(subTitleText, (WIDHTH - subTitleWidth) / 2, HEIGHT - 550);

        
        for (int i = 0; i < menuItems.length; i++) {
            String item = menuItems[i];
            int textWidth = fm.stringWidth(item);
            int x = (WIDHTH - textWidth) / 2;
            int y = startY + i * spacing;

            // Ganti warna item menu yang aktif jadi kuning, yang lain putih
            g2.setColor(i == selectedMenuIndex ? Color.YELLOW : Color.WHITE);
            g2.drawString(item, x, y);

            // Gambar panah di kiri item yang sedang dipilih
            if (i == selectedMenuIndex) {
                g2.drawString("\u25B6", x - 50, y);
            }
        }
        String navText = "PRESS ↑ OR ↓ TO NAVIGATE";
        String selText = "PRESS ENTER TO SELECT";

        int navWidth = fm.stringWidth(navText);
        int selWidth = fm.stringWidth(selText);

        g2.setColor(Color.WHITE);
        // Gambar petunjuk navigasi di bawah layar
        g2.drawString(navText, (WIDHTH - navWidth) / 2, HEIGHT - 100);
        g2.drawString(selText, (WIDHTH - selWidth) / 2, HEIGHT - 50);
    }

    private void drawSettingsMenu(Graphics2D g2) {
        Font customFont = FontLoader.loadFont("res/font/PressStart2P-Regular.ttf", 30f);
        g2.setFont(customFont);
        FontMetrics fm = g2.getFontMetrics();

        int startY = 200;
        int spacing = 70;

        for (int i = 0; i < settingsItems.length; i++) {
            String text = settingsItems[i];
            int x = (WIDHTH - fm.stringWidth(text)) / 2;
            int y = startY + i * spacing;

            // Ganti warna item setting yang aktif jadi kuning, yang lain putih
            g2.setColor(i == selectedSettingsIndex ? Color.YELLOW : Color.WHITE);
            g2.drawString(text, x, y);

            // Gambar panah di kiri item yang dipilih
            if (i == selectedSettingsIndex) {
                g2.drawString("\u25B6", x - 50, y);
            }
        }
    }


    private class MenuKeyHandler extends KeyAdapter {
        @Override
        public void keyPressed(KeyEvent e) {
            if (onMainMenu) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    selectedMenuIndex--;
                    // Kembali ke pilihan terakhir jika indeks kurang dari 0
                    if (selectedMenuIndex < 0) selectedMenuIndex = menuItems.length - 1;
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    selectedMenuIndex++;
                    // Kembali ke pilihan pertama jika melewati batas akhir
                    if (selectedMenuIndex >= menuItems.length) selectedMenuIndex = 0;
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Menjalankan aksi berdasarkan menu yang dipilih
                    handleMenuSelection();
                } 
            } else if (onSettings) {
                if (e.getKeyCode() == KeyEvent.VK_UP) {
                    selectedSettingsIndex--;
                    // Kembali ke pengaturan terakhir jika indeks kurang dari 0
                    if (selectedSettingsIndex < 0) selectedSettingsIndex = settingsItems.length - 1;
                } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                    selectedSettingsIndex++;
                    // Kembali ke pengaturan pertama jika melewati batas akhir
                    if (selectedSettingsIndex >= settingsItems.length) selectedSettingsIndex = 0;
                } else if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    // Menjalankan aksi sesuai pengaturan yang dipilih
                    handleSettingsSelection();
                } else if (e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    // Menangani perubahan lagu jika berada pada pilihan musik
                    if (selectedSettingsIndex == 2) {
                        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
                            currentMusicIndex = (currentMusicIndex - 1 + musicList.length) % musicList.length;
                        } else {
                            currentMusicIndex = (currentMusicIndex + 1) % musicList.length;
                        }

                        // Memperbarui teks tampilan lagu saat ini
                        settingsItems[2] = "MUSIC TRACK: " + musicList[currentMusicIndex];

                        if (musicEnabled) {
                            music.stop(); // Menghentikan musik sebelumnya
                            music.play(currentMusicIndex, true); // Memutar musik baru
                            music.loop(); // Memutar musik secara berulang
                        }
                    }
                }
            }

            repaint(); // Memperbarui tampilan antarmuka
        }
    }

    private void handleMenuSelection() {
        // Menangani aksi berdasarkan item menu yang dipilih
        switch (menuItems[selectedMenuIndex]) {
            case "START GAME":
                onMainMenu = false;
                onSettings = false;
                // Mengalihkan kontrol dari menu ke permainan: hapus menuKeyHandler dan tambahkan inputHandler
                GamePanel.this.removeKeyListener(menuKeyHandler);
                GamePanel.this.addKeyListener(inputHandler);
                break;
            case "SETTINGS":
                onMainMenu = false;
                onSettings = true;
                selectedSettingsIndex = 0;
                repaint(); // Memperbarui tampilan untuk menampilkan menu pengaturan
                break;
            case "CREDITS":
                // Placeholder: aksi untuk menampilkan kredit (belum diimplementasikan)
                System.out.println("Credits selected");
                break;
            case "EXIT":
                // Keluar dari aplikasi
                System.exit(0);
                break;
        }
    }


    private void handleSettingsSelection() {
        // Menangani aksi berdasarkan indeks menu pengaturan yang dipilih
        switch (selectedSettingsIndex) {
            case 0: // SFX
                // Mengaktifkan atau menonaktifkan efek suara (SFX)
                sfxEnabled = !sfxEnabled;
                settingsItems[0] = "SFX: " + (sfxEnabled ? "ON" : "OFF");
                break;

            case 1: // Music
                // Mengaktifkan atau menonaktifkan musik latar
                musicEnabled = !musicEnabled;
                settingsItems[1] = "MUSIC: " + (musicEnabled ? "ON" : "OFF");

                if (musicEnabled) {
                    // Jika musik diaktifkan, mainkan lagu saat ini dan ulang terus-menerus
                    music.play(currentMusicIndex, true);
                    music.loop();
                } else {
                    // Jika musik dimatikan, hentikan pemutaran
                    music.stop();
                }
                break;

            case 2: // Ganti lagu
                // Mengganti track musik ke lagu berikutnya dalam daftar
                currentMusicIndex++;
                if (currentMusicIndex > 1) { // Jika melewati batas daftar lagu, kembali ke awal
                    currentMusicIndex = 0;
                }

                if (musicEnabled) {
                    // Jika musik aktif, hentikan lagu sebelumnya lalu mainkan lagu baru
                    music.stop();
                    music.play(currentMusicIndex, true);
                    music.loop();
                }
                break;

            case 3: // Back
                // Kembali ke menu utama dari menu pengaturan
                onSettings = false;
                onMainMenu = true;
                selectedSettingsIndex = 0; // Reset indeks pilihan pengaturan
                break;
        }
    }
}
