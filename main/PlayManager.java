package main;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
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

public class PlayManager { // u/ play areanya, manage tetromino, dan handling gameplay (hapusLines, skor, dll.)

    //main play areanya
    final int WIDHT = 360;
    final int HEIGHT = 600;
    public static int left_x;
    public static int right_x;
    public static int top_y;
    public static int bottom_y;

    //Mino
    Mino currentMino;
    final int MINO_START_X;
    final int MINO_START_Y;

    //Next bloknya
    Mino nextMino;
    final int NEXTMINO_X;
    final int NEXTMINO_Y;

    //kita pindahkan blok yg tdk aktif ke staticBlocks
    public static ArrayList<Block> staticBlocks = new ArrayList<>();

    //others
    public static int dropinterval = 60; //blok drop tiap 60frame
    boolean gameOver;
    
    //effect
    boolean effectCounterOn;
    int effectCounter;
    ArrayList<Integer> effectY = new ArrayList<>();

    //Score
    int level = 1;
    int lines;
    int score;

    public PlayManager() {

        // Main frame play areanya
        // Di tengah layar/window
        left_x = (GamePanel.WIDHTH/2) - (WIDHT/2); // 1280/2 - 360/2 = 460
        right_x = left_x + WIDHT;
        top_y = 50;
        bottom_y = top_y + HEIGHT;

        MINO_START_X = left_x + (WIDHT/2) - Block.SIZE;
        MINO_START_Y = top_y + Block.SIZE;

        NEXTMINO_X = right_x + 175;
        NEXTMINO_Y = top_y + 500;

        // set blok awal
        currentMino = pickMino();
        currentMino.setXY(MINO_START_X, MINO_START_Y);

        // set blok yg akan muncul selanjutnya
        nextMino = pickMino();
        nextMino.setXY(NEXTMINO_X, NEXTMINO_Y); 
    }
    private Mino pickMino(){

        //pick random block
        Mino mino = null;
        int i = new Random().nextInt(7);

        switch(i) {
            case 0: mino = new Mino_L1();break;
            case 1: mino = new Mino_L2();break;
            case 2: mino = new Mino_Square();break;
            case 3: mino = new Mino_Bar();break;
            case 4: mino = new Mino_T();break;
            case 5: mino = new Mino_Z1();break;
            case 6: mino = new Mino_Z2();break;
        }
        return mino;
    }
    public void update() {

        // cek apakah blok saat ini aktif
        if (currentMino.active == false) {

            // jika blok tidak aktif, set blok ke staticBlocks 
            staticBlocks.add(currentMino.b[0]);
            staticBlocks.add(currentMino.b[1]);
            staticBlocks.add(currentMino.b[2]);
            staticBlocks.add(currentMino.b[3]);

            //untuk cek GameOver!!!
            if(currentMino.b[0].x == MINO_START_X && currentMino.b[0].y == MINO_START_Y) {
                //ini berarti block yg baru muncul langsung terkena blok lain dan tidak bisa bergerak
                // jadi XY nya sama dengan startXYnya/block selanjutnya
                gameOver = true;
                GamePanel.music.stop();
                GamePanel.se.play(2,false);
            }

            currentMino.deactivating = false;

            //ganti ke blok selanjutnya
            currentMino = nextMino;
            currentMino.setXY(MINO_START_X, MINO_START_Y);

            //ganti blok di frame next
            nextMino = pickMino();
            nextMino.setXY(NEXTMINO_X, NEXTMINO_Y);

            //cek apakah ada baris yg harus dihapus
            checkDelete();
        }
        else {
            currentMino.update();
        }
    }
    private void checkDelete() {

        int x = left_x;
        int y = top_y;
        int blockCount = 0;
        int lineCount = 0;

        while(x < right_x && y < bottom_y) {

            for (int i = 0; i < staticBlocks.size(); i++) {
                if (staticBlocks.get(i).x == x && staticBlocks.get(i).y == y) {
                    //menambah count jika ada static block
                    blockCount++;
                }
            }

            x += Block.SIZE;

            
            if (x == right_x) {

                // jika block count == 12, maka kita dapat menghapus baris itu
                int blocksPerRow = (right_x - left_x) / Block.SIZE;
                if(blockCount == blocksPerRow) {
                    
                    effectCounterOn = true; //jika ada baris yg dihapus, maka effectCounterOn bernilai true
                    effectY.add(y); //menggunakan array agar dapat menghapus beberapa garis

                    for(int i = staticBlocks.size() - 1; i >= 0; i--) {
                        // hapus semua block di baris tersebut
                        if(staticBlocks.get(i).y == y) {
                            staticBlocks.remove(i);
                        }
                    }

                    //add LINES int
                    lineCount ++;
                    lines++;
                    
                    //Drop Speed
                    //Jika line score telah mencapai angka tertentu, drop speed tertambah
                    // 1 yang Tercepat
                    if(lines % 10 == 0 && dropinterval > 1) {
                    // setiap 10 baris naik level & Drop interval -10 kecuali jika dropinterval sudah sampai 10 maka dropinterval akan berkurang -1
                        level++;
                        if(dropinterval > 10) {
                            dropinterval -= 10;
                        }
                        else {
                            dropinterval -= 1;
                        }
                    }

                    //jika baris sudah terhapus, maka harus menurunkan semua block di atas baris yang terhapus
                    for (int i = 0; i < staticBlocks.size(); i++) {
                        //jika block di atas baris yang terhapus, turunkan block sebesar size block
                        if(staticBlocks.get(i).y < y) {
                            staticBlocks.get(i).y += Block.SIZE;
                        }
                    }
                }

                blockCount = 0;
                x = left_x;
                y += Block.SIZE;
            }
        }  

        //Add Score
        if (lineCount > 0) {
            GamePanel.se.play(1, false);
            int singleLineScore = 10 * level;
            score += singleLineScore * lineCount;
        }
    }    
    public void draw(Graphics2D g2) {
        
        //Frame play areanya (Main Frame)
        g2.setColor(Color.white);
        g2.setStroke(new BasicStroke(4f));
        g2.drawRect(left_x-4, top_y-4, WIDHT+8, HEIGHT+8); // -&+ tebal frame 

        // next tetromino/block frame (Frame Kicik Di Sebelahnya)
        int x = right_x + 100;
        int y = bottom_y - 200;
        g2.drawRect(x, y, 200, 200); 
        g2.setFont(new Font("Arial", Font.PLAIN, 30));
        g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        g2.drawString("NEXT", x+60, y+60);

        //bikin Score
        g2.drawRect(x, top_y, 250, 300);
        x += 40;
        y = top_y + 90;
        g2.drawString("LEVEL: " + level, x, y); y += 70;
        g2.drawString("LINES: " + lines, x, y); y += 70;
        g2.drawString("SCORE: " + score, x, y); 

        // Tampilkan Blok saat ini (Start Blok>NextBlok)
        if(currentMino != null) {
            currentMino.draw(g2);
        }

        //Tampilkan Next Blok
        nextMino.draw(g2);

        //Tampilkan Static Blok
        for(int i = 0; i < staticBlocks.size(); i++) {
            staticBlocks.get(i).draw(g2);
        }

        //Tampilkan Effect Counter
        if(effectCounterOn) {
            effectCounter++;

            g2.setColor(Color.red);
            for(int i = 0; i < effectY.size(); i++) { //memindai effectY
                g2.fillRect(left_x, effectY.get(i), WIDHT, Block.SIZE); //menggambar kotak warna merah yang lebar dan tingginya sama dengan block
            }
            if(effectCounter == 10) {    //jika effectCounter = 10
                effectCounterOn = false;
                effectCounter = 0;
                effectY.clear();        //menghapus effectY dan berhenti menggambar kotak warna merah
            }
        }

        // Bikin Pause & Game Over
        g2.setColor(Color.yellow);
        g2.setFont(g2.getFont().deriveFont(50f));
        if(gameOver) {
            String gameOverText = "GAME OVER";
            Font font = g2.getFont().deriveFont(50f);
            g2.setFont(font);
            int textWidth = g2.getFontMetrics(font).stringWidth(gameOverText);
            x = left_x + (WIDHT - textWidth) / 2;
            y = top_y + 320;
            g2.drawString(gameOverText, x, y);
        }
        else if(InputHandler.pausePressed) {
            x = left_x + 70;
            y = top_y + 320;
            g2.drawString("PAUSED", x, y);
        }

        //Bikin Judul Game
        x = 125;
        y = top_y + 320;
        g2.setColor(Color.white);
        g2.setFont(new Font ("Times New Roman", Font.ITALIC,60));
        g2.drawString("D'BAS", x, y);
        
        x = 32;
        y = top_y + 365;
        g2.setFont(new Font ("ARIAL", Font.PLAIN,26));
        g2.drawString("Dynamic Blocks Allocation System", x, y);


    }
}