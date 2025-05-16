package main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.Area;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import mino.Block;
import mino.Mino;
import mino.Mino_Bar;
import mino.Mino_L1;
import mino.Mino_L2;
import mino.Mino_Square;
import mino.Mino_T;
import mino.Mino_Z1;
import mino.Mino_Z2;

public class PlayManager {
    // Dimensi area permainan utama
    final int WIDHT = 360; // Lebar area permainan
    final int HEIGHT = 600; // Tinggi area permainan
    public static int left_x; // Koordinat x kiri area permainan
    public static int right_x; // Koordinat x kanan area permainan
    public static int top_y; // Koordinat y atas area permainan
    public static int bottom_y; // Koordinat y bawah area permainan

    // Mino (tetromino saat ini)
    Mino currentMino; // Tetromino yang sedang aktif
    final int MINO_START_X; // Koordinat x awal untuk tetromino
    final int MINO_START_Y; // Koordinat y awal untuk tetromino

    // Tetromino berikutnya
    Mino nextMino; // Tetromino yang akan muncul selanjutnya
    final int NEXTMINO_X; // Koordinat x untuk tetromino berikutnya
    final int NEXTMINO_Y; // Koordinat y untuk tetromino berikutnya

    // Menyimpan blok yang tidak aktif ke dalam staticBlocks
    public static ArrayList<Block> staticBlocks = new ArrayList<>();

    // Variabel lainnya
    public static int dropinterval = 60; // Interval drop blok setiap 60 frame
    boolean gameOver; // Status permainan

    // Efek
    boolean effectCounterOn; // Status untuk efek
    int effectCounter; // Penghitung efek
    ArrayList<Integer> effectY = new ArrayList<>(); // Menyimpan posisi y untuk efek

    // Skor
    int level = 1; // Level permainan
    int lines; // Jumlah garis yang dihapus
    int score; // Skor saat ini
    static int highScore = 0; // Skor tertinggi

    // Konstruktor PlayManager
    public PlayManager() {
        // Mengatur area permainan di tengah layar/window
        left_x = (GamePanel.WIDHTH / 2) - (WIDHT / 2); // Menghitung posisi x kiri
        right_x = left_x + WIDHT; // Menghitung posisi x kanan
        top_y = 50; // Posisi y atas
        bottom_y = top_y + HEIGHT; // Posisi y bawah

        // Mengatur posisi awal untuk tetromino
        MINO_START_X = left_x + (WIDHT / 2) - Block.SIZE; // Menghitung posisi x awal tetromino
        MINO_START_Y = top_y + Block.SIZE; // Menghitung posisi y awal tetromino

        // Mengatur posisi untuk tetromino berikutnya
        NEXTMINO_X = right_x + 175; // Posisi x untuk tetromino berikutnya
        NEXTMINO_Y = top_y + 500; // Posisi y untuk tetromino berikutnya

        // Mengatur blok awal
        currentMino = pickMino(); // Memilih tetromino acak
        currentMino.setXY(MINO_START_X, MINO_START_Y); // Mengatur posisi tetromino saat ini

        // Mengatur blok yang akan muncul selanjutnya
        nextMino = pickMino(); // Memilih tetromino berikutnya
        nextMino.setXY(NEXTMINO_X, NEXTMINO_Y); // Mengatur posisi tetromino berikutnya
    }

    private Mino pickMino() {
        // Memilih blok (tetromino) secara acak
        Mino mino = null; // Inisialisasi variabel mino
        int i = new Random().nextInt(7); // Menghasilkan angka acak antara 0 dan 6

        // Memilih jenis tetromino berdasarkan angka acak
        switch(i) {
            case 0: mino = new Mino_L1(); break; // Tetromino L1
            case 1: mino = new Mino_L2(); break; // Tetromino L2
            case 2: mino = new Mino_Square(); break; // Tetromino Square
            case 3: mino = new Mino_Bar(); break; // Tetromino Bar
            case 4: mino = new Mino_T(); break; // Tetromino T
            case 5: mino = new Mino_Z1(); break; // Tetromino Z1
            case 6: mino = new Mino_Z2(); break; // Tetromino Z2
        }
        return mino; // Mengembalikan tetromino yang dipilih
    }

    public void update() {
        // Memeriksa apakah blok saat ini aktif
        if (currentMino.active == false) {
            // Jika blok tidak aktif, tambahkan blok ke staticBlocks
            staticBlocks.add(currentMino.b[0]);
            staticBlocks.add(currentMino.b[1]);
            staticBlocks.add(currentMino.b[2]);
            staticBlocks.add(currentMino.b[3]);

            // Memeriksa kondisi Game Over
            if(currentMino.b[0].x == MINO_START_X && currentMino.b[0].y == MINO_START_Y) {
                // Jika blok baru muncul langsung terkena blok lain dan tidak bisa bergerak
                gameOver = true; // Menandakan permainan berakhir
                GamePanel.music.stop(); // Menghentikan musik
                GamePanel.se.play(4, false); // Memutar efek suara game over

                // Memperbarui high score jika skor saat ini lebih tinggi
                if (score > highScore) {
                    highScore = score; // Memperbarui high score
                    saveHighScore(); // Menyimpan high score
                }
            }

            currentMino.deactivating = false; // Menandai blok tidak aktif

            // Mengganti ke blok selanjutnya
            currentMino = nextMino; // Mengatur blok saat ini menjadi blok berikutnya
            currentMino.setXY(MINO_START_X, MINO_START_Y); // Mengatur posisi blok saat ini

            // Mengganti blok di frame next
            nextMino = pickMino(); // Memilih blok berikutnya
            nextMino.setXY(NEXTMINO_X, NEXTMINO_Y); // Mengatur posisi blok berikutnya

            // Memeriksa apakah ada baris yang harus dihapus
            checkDelete();
        } else {
            currentMino.update(); // Memperbarui blok saat ini jika masih aktif
        }
    }

    private void checkDelete() {
        // Memeriksa dan menghapus baris yang penuh
        int x = left_x; // Koordinat x untuk memeriksa blok
        int y = top_y; // Koordinat y untuk memeriksa blok
        int blockCount = 0; // Menghitung jumlah blok dalam satu baris
        int lineCount = 0; // Menghitung jumlah baris yang dihapus

        // Memeriksa setiap baris dari kiri ke kanan
        while(x < right_x && y < bottom_y) {
            // Memeriksa setiap blok statis untuk melihat apakah ada yang berada di posisi (x, y)
            for (int i = 0; i < staticBlocks.size(); i++) {
                if (staticBlocks.get(i).x == x && staticBlocks.get(i).y == y) {
                    // Menambah count jika ada static block
                    blockCount++;
                }
            }

            x += Block.SIZE; // Pindah ke blok berikutnya

            // Jika sudah mencapai batas kanan area permainan
            if (x == right_x) {
                // Menghitung jumlah blok per baris
                int blocksPerRow = (right_x - left_x) / Block.SIZE;
                // Jika jumlah blok dalam baris sama dengan jumlah blok per baris, maka baris tersebut dapat dihapus
                if(blockCount == blocksPerRow) {
                    effectCounterOn = true; // Jika ada baris yang dihapus, set effectCounterOn menjadi true
                    effectY.add(y); // Menyimpan posisi y untuk efek

                    // Menghapus semua blok di baris tersebut
                    for(int i = staticBlocks.size() - 1; i >= 0; i--) {
                        // Jika blok berada di baris yang sama, hapus blok tersebut
                        if(staticBlocks.get(i).y == y) {
                            staticBlocks.remove(i);
                        }
                    }

                    // Menambah jumlah garis yang dihapus
                    lineCount++;
                    lines++;

                    // Mengatur kecepatan drop
                    // Jika jumlah garis yang dihapus mencapai angka tertentu, kecepatan drop akan bertambah
                    if(lines % 10 == 0 && dropinterval > 1) {
                        // Setiap 10 baris, naik level dan kurangi drop interval
                        level++;
                        if(dropinterval > 10) {
                            dropinterval -= 10; // Mengurangi drop interval sebesar 10
                        } else {
                            dropinterval -= 1; // Mengurangi drop interval sebesar 1
                        }
                    }

                    // Jika baris sudah terhapus, turunkan semua blok di atas baris yang terhapus
                    for (int i = 0; i < staticBlocks.size(); i++) {
                        // Jika blok berada di atas baris yang terhapus, turunkan blok sebesar ukuran blok
                        if(staticBlocks.get(i).y < y) {
                            staticBlocks.get(i).y += Block.SIZE;
                        }
                    }
                }

                // Reset blockCount dan kembali ke posisi awal untuk memeriksa baris berikutnya
                blockCount = 0;
                x = left_x; // Reset x ke posisi kiri
                y += Block.SIZE; // Pindah ke baris berikutnya
            }
        }  

        // Menambahkan skor jika ada baris yang dihapus
        if (lineCount > 0) {
            GamePanel.se.play(3, false); // Memutar efek suara untuk menghapus baris
            int singleLineScore = 10 * level; // Menghitung skor untuk satu baris
            score += singleLineScore * lineCount; // Menambahkan skor total
        }
    }    

    public static void saveHighScore() {
        // Metode untuk menyimpan skor tertinggi ke dalam file
        try {
            FileWriter writer = new FileWriter("highscore.txt"); // Membuat FileWriter untuk file highscore.txt
            writer.write(String.valueOf(highScore)); // Menulis skor tertinggi ke dalam file
            writer.close(); // Menutup FileWriter
        } catch (IOException e) {
            e.printStackTrace(); // Menampilkan stack trace jika terjadi kesalahan
        }
    }

    public static int loadHighScore() {
        // Metode untuk memuat skor tertinggi dari file
        try {
            File file = new File("highscore.txt"); // Membuat objek File untuk file highscore.txt
            if (!file.exists()) return 0; // Jika file tidak ada, kembalikan 0

            BufferedReader br = new BufferedReader(new FileReader(file)); // Membuat BufferedReader untuk membaca file
            String line = br.readLine(); // Membaca baris pertama dari file
            br.close(); // Menutup BufferedReader

            return Integer.parseInt(line.trim()); // Mengembalikan nilai skor tertinggi setelah menghapus spasi
        } catch (Exception e) {
            e.printStackTrace(); // Menampilkan stack trace jika terjadi kesalahan
            return 0; // Kembalikan 0 jika terjadi kesalahan
        }
    }


public void draw(Graphics2D g2) {
    // Metode untuk menggambar area permainan dan elemen lainnya

    // Memuat font untuk teks
    Font pressStartFont = main.FontLoader.loadFont("res/font/PressStart2P-Regular.ttf", 20f);
    g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON); // Mengatur rendering hint untuk teks

    // Frame area permainan (Main Frame)
    // Data ukuran
    int frameX = left_x; // Koordinat x untuk frame
    int frameY = top_y; // Koordinat y untuk frame
    int frameW = WIDHT; // Lebar frame
    int frameH = HEIGHT; // Tinggi frame

    // Warna strip dan tebal strip
    Color[] stripColors = {
        new Color(40, 0, 60),  // Ungu gelap luar
        Color.BLACK,           // Hitam
        new Color(130, 80, 200), // Ungu terang
        new Color(100, 60, 150), // Ungu sedang
        new Color(40, 0, 60),  // Ungu gelap luar
        Color.BLACK            // Hitam dalam
    };

    int[] stripWidths = {4, 3, 2, 6, 4, 4}; // Lebar setiap strip

    // Menghitung total ketebalan strip
    int total = 0;
    for (int w : stripWidths) total += w;

    // Menggambar dari luar ke dalam
    int offset = 0; // Offset untuk menggambar strip
    for (int i = 0; i < stripColors.length; i++) {
        int thickness = stripWidths[i]; // Ketebalan strip saat ini
        g2.setColor(stripColors[i]); // Mengatur warna untuk strip

        // Menghitung koordinat dan ukuran untuk strip luar
        int outerX = frameX - total + offset;
        int outerY = frameY - total + offset;
        int outerW = frameW + 2 * (total - offset);
        int outerH = frameH + 2 * (total - offset);

        // Menghitung koordinat dan ukuran untuk strip dalam
        int innerX = outerX + thickness;
        int innerY = outerY + thickness;
        int innerW = outerW - 2 * thickness;
        int innerH = outerH - 2 * thickness;

        // Menggunakan Area untuk membuat border dengan lubang
        Area outer = new Area(new Rectangle2D.Double(outerX, outerY, outerW, outerH)); // Area luar
        Area inner = new Area(new Rectangle2D.Double(innerX, innerY, innerW, innerH)); // Area dalam
        outer.subtract(inner); // Menghapus area dalam dari area luar

        g2.fill(outer); // Menggambar area luar

        offset += thickness; // Menambah offset untuk strip berikutnya

        // Menggambar efek transparan di atas frame
        g2.setComposite(java.awt.AlphaComposite.SrcOver.derive(0.15f)); // Mengatur transparansi
        g2.setColor(Color.BLACK); // Mengatur warna hitam
        g2.fillRect(frameX, frameY, frameW, frameH); // Menggambar persegi panjang hitam

        // Kembalikan ke default agar elemen berikutnya tidak ikut transparan
        g2.setComposite(java.awt.AlphaComposite.SrcOver);
    }
        
        // Next tetromino frame (frame kecil di sebelah kanan)

        int x = right_x + 115; // Koordinat x awal untuk kotak "Next"
        int y = bottom_y - 160; // Koordinat y awal untuk kotak "Next"
        int boxWidth = 180; // Lebar kotak "Next"
        int boxHeight = 150; // Tinggi kotak "Next"

        int thickness = 6; // Ketebalan garis ungu bagian luar
        int blackThickness = 4; // Ketebalan frame hitam pemisah bagian dalam

        Color darkPurple = new Color(130, 80, 200); // Warna ungu tua untuk sisi atas dan kiri
        Color lightPurple = new Color(100, 60, 150); // Warna ungu muda untuk sisi bawah dan kanan
        Color black = Color.BLACK; // Warna hitam untuk frame pemisah bagian dalam
        Color innerFillColor = new Color(88, 77, 128); // Warna ungu solid untuk isi dalam kotak

        // Gambar sisi kiri vertikal dari kotak luar berwarna ungu tua
        g2.setColor(darkPurple);
        g2.fillRect(x, y, thickness, boxHeight); // Sisi kiri vertikal

        // Gambar sisi atas horizontal dari kotak luar berwarna ungu tua
        g2.fillRect(x, y, boxWidth, thickness); // Sisi atas horizontal

        // Ganti warna menjadi ungu muda untuk sisi kanan dan bawah
        g2.setColor(lightPurple);

        // Gambar sisi kanan vertikal dari kotak luar
        g2.fillRect(x + boxWidth - thickness, y, thickness, boxHeight); // Sisi kanan vertikal

        // Gambar sisi bawah horizontal dari kotak luar
        g2.fillRect(x, y + boxHeight - thickness, boxWidth, thickness); // Sisi bawah horizontal

        // Gambar frame hitam pemisah di bagian dalam kotak
        g2.setColor(black);
        g2.fillRect(
            x + thickness, y + thickness,
            boxWidth - 2 * thickness,
            boxHeight - 2 * thickness
        ); // Frame hitam bagian dalam

        // Isi bagian dalam kotak dengan warna ungu solid
        g2.setColor(innerFillColor);
        g2.fillRect(
            x + thickness + blackThickness,
            y + thickness + blackThickness,
            boxWidth - 2 * (thickness + blackThickness),
            boxHeight - 2 * (thickness + blackThickness)
        ); // Isi kotak bagian tengah

        // Atur font dan aktifkan antialiasing untuk tulisan
        g2.setFont(pressStartFont);
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.setColor(Color.WHITE); // Warna tulisan putih

        String nextText = "NEXT"; // Teks yang akan ditampilkan
        int nextTextWidth = g2.getFontMetrics().stringWidth(nextText); // Lebar teks untuk perataan
        int textX = x + (boxWidth - nextTextWidth) / 2; // Posisi x agar teks di tengah
        int textY = y - 10; // Posisi y agar teks berada di atas kotak

        g2.drawString(nextText, textX, textY); // Gambar tulisan "Next" di atas kotak

        x += 5;
        y = top_y + 90;
        int rightAlignX = x + 150; // Menentukan posisi sejajar kanan untuk tampilan angka

        // Menampilkan label LEVEL
        g2.setColor(Color.WHITE);
        g2.drawString("LEVEL", x, y);

        // Menampilkan angka level tepat 35 piksel di bawah label "LEVEL"
        g2.setColor(Color.YELLOW);
        String levelStr = String.valueOf(level);
        int levelWidth = g2.getFontMetrics().stringWidth(levelStr);
        g2.drawString(levelStr, rightAlignX - levelWidth, y + 35);

        y += 70;

        // Menampilkan label LINES (posisi dan perataan sama dengan LEVEL)
        g2.setColor(Color.WHITE);
        g2.drawString("LINES", x, y + 10);

        // Menampilkan jumlah garis yang telah dihapus
        g2.setColor(Color.YELLOW);
        String linesStr = String.valueOf(lines);
        int linesWidth = g2.getFontMetrics().stringWidth(linesStr);
        g2.drawString(linesStr, rightAlignX - linesWidth, y + 35);

        y += 70;

        // Menampilkan label SCORE (posisi dan perataan sejajar)
        g2.setColor(Color.WHITE);
        g2.drawString("SCORE", x, y + 10);

        // Menampilkan skor saat ini
        g2.setColor(Color.YELLOW);
        String scoreStr = String.valueOf(score);
        int scoreWidth = g2.getFontMetrics().stringWidth(scoreStr);
        g2.drawString(scoreStr, rightAlignX - scoreWidth, y + 35);

        y += 70;

        // Menampilkan label TOP (skor tertinggi)
        g2.setColor(Color.WHITE);
        g2.drawString("TOP", x, y + 10);

        // Menampilkan skor tertinggi, atau skor saat ini jika melebihi skor tertinggi
        g2.setColor(Color.YELLOW);
        int highScore = loadHighScore();

        if (score > highScore) {
            g2.drawString(scoreStr, x + 110, y + 35);
        } else {
            String highScoreStr = String.valueOf(highScore);
            g2.drawString(highScoreStr, x + 110, y + 35);
        }

        // Konfigurasi garis dekoratif
        Color purple = new Color(130, 80, 200);
        int lineThickness = 6; // Ketebalan garis
        int dashLength = 8; // Panjang segmen garis putus-putus
        int gapLength = 5; // Jarak antar segmen garis putus-putus

        // Menggambar garis putus-putus di atas bagian LEVEL
        g2.setColor(purple);
        for (int i = 0; i < 150; i += dashLength + gapLength) {
            g2.fillRect(x + i, y - 250, dashLength, lineThickness);
        }

        // Menggambar garis solid di bawah LEVEL
        g2.fillRect(x, y - 165, 150, lineThickness);

        // Menentukan posisi vertikal untuk bagian LINES
        int yLines = y + 60;

        // Menggambar garis solid di bawah LINES
        g2.fillRect(x, yLines - 155, 150, lineThickness);

        // Menggambar garis solid di bawah SCORE
        g2.fillRect(x, yLines - 85, 150, lineThickness);

        
        if (currentMino != null) {
           currentMino.drawGhost(g2);  // Menggambar bayangan (ghost) dari tetromino saat ini
           currentMino.draw(g2);       // Menggambar tetromino aktif (blok asli)
        }

        // Menampilkan tetromino berikutnya
        nextMino.draw(g2);

        // Menampilkan semua blok yang sudah tidak aktif (staticBlocks)
        for(int i = 0; i < staticBlocks.size(); i++) {
            staticBlocks.get(i).draw(g2);
        }

        // Menampilkan efek animasi ketika effectCounterOn aktif
        if(effectCounterOn) {
            effectCounter++; // Menambah nilai penghitung efek

            g2.setColor(Color.red);
            // Menggambar kotak merah sepanjang lebar area permainan pada posisi Y yang tersimpan di effectY
            for(int i = 0; i < effectY.size(); i++) {
                g2.fillRect(left_x, effectY.get(i), WIDHT, Block.SIZE);
            }

            // Jika efek telah berjalan selama 10 frame, matikan efek dan reset penghitungan serta posisi efek
            if(effectCounter == 10) {
                effectCounterOn = false;
                effectCounter = 0;
                effectY.clear();
            }
        }

        // Menampilkan status pause dan game over
        g2.setColor(Color.yellow);
        g2.setFont(g2.getFont().deriveFont(30f));

        if(gameOver) {
            // Jika permainan berakhir, tampilkan teks "GAME OVER" di tengah area permainan
            String gameOverText = "GAME OVER";
            Font font = g2.getFont().deriveFont(30f);
            g2.setFont(font);
            int textWidth = g2.getFontMetrics(font).stringWidth(gameOverText);
            x = left_x + (WIDHT - textWidth) / 2;
            y = top_y + 90;
            g2.drawString(gameOverText, x, y);
        }
        else if(InputHandler.pausePressed) {
            // Jika permainan sedang dijeda, tampilkan teks "PAUSED" di posisi tertentu
            x = left_x + 90;
            y = top_y + 320;
            g2.drawString("PAUSED", x, y);
        }

        // Menampilkan judul permainan
        x = 125;
        y = top_y + 35;
        g2.setColor(Color.yellow);
        g2.setFont(g2.getFont().deriveFont(40f));
        g2.drawString("D'BAS", x, y);

        // Menampilkan subjudul di bawah judul utama
        x = 32;
        y = top_y + 60;
        g2.setFont(g2.getFont().deriveFont(12.5f));
        g2.drawString("Dynamic Blocks Allocation System", x, y);

        // Menampilkan judul bagian bantuan (help)
        x = 120;
        y = top_y + 300;
        g2.setFont(g2.getFont().deriveFont(30f));
        g2.drawString("H E L P", x, y);

        // Menampilkan instruksi kontrol permainan secara vertikal dengan jarak baris tetap
        x = 50;
        y = top_y + 350;
        int lineHeight = 20;

        g2.setFont(g2.getFont().deriveFont(15f));
        g2.drawString("UP    : Rotate", x, y);
        g2.drawString("LEFT  : Left", x, y + lineHeight);
        g2.drawString("RIGHT : Right", x, y + lineHeight * 2);
        g2.drawString("DOWN  : Speed Up", x, y + lineHeight * 3);
        g2.drawString("SPACE : Hard drop", x, y + lineHeight * 4);
        g2.drawString("ESC   : Pause / Resume", x, y + lineHeight * 5);

    }
}