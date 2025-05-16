package mino;

import java.awt.Color;

public class Mino_L1 extends Mino {
    
    public Mino_L1() {
        // Buat mino L1 dengan warna orange
        create(Color.orange) ;   
    }

    public void setXY(int x, int y) {
        // Atur posisi awal mino L1
        // o
        // o
        // o o
        b[0].x = x;
        b[0].y = y;
        b[1].x = b[0].x;
        b[1].y = b[0].y - Block.SIZE;
        b[2].x = b[0].x;
        b[2].y = b[0].y + Block.SIZE;
        b[3].x = b[0].x + Block.SIZE;
        b[3].y = b[0].y + Block.SIZE;
    }
    public void getDirection1() {
        // Atur posisi mino L1 ketika berputar 90 derajat
        // o
        // o
        // o o
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x;
        tempB[1].y = b[0].y - Block.SIZE;
        tempB[2].x = b[0].x;
        tempB[2].y = b[0].y + Block.SIZE;
        tempB[3].x = b[0].x + Block.SIZE;
        tempB[3].y = b[0].y + Block.SIZE;

        // Update posisi block sesuai dengan posisi mino yang baru
        updateXY(1);
    }
    public void getDirection2() {
        // Atur posisi mino L1 ketika berputar 90 derajat
        // O O O
        // O
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x + Block.SIZE;
        tempB[1].y = b[0].y;
        tempB[2].x = b[0].x + 2 * Block.SIZE;
        tempB[2].y = b[0].y;
        tempB[3].x = b[0].x;
        tempB[3].y = b[0].y + Block.SIZE;

        // Update posisi block sesuai dengan posisi mino yang baru
        updateXY(2);
    }

    public void getDirection3() {
        // Atur posisi mino L1 ketika berputar 90 derajat
        //   O O
        //     O
        //     O
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x + Block.SIZE;
        tempB[1].y = b[0].y;
        tempB[2].x = b[0].x + Block.SIZE;
        tempB[2].y = b[0].y + Block.SIZE;
        tempB[3].x = b[0].x + Block.SIZE;
        tempB[3].y = b[0].y + 2 * Block.SIZE;
        
        // Update posisi block sesuai dengan posisi mino yang baru
        updateXY(3);
    }

    public void getDirection4() {
        // Atur posisi mino L1 ketika berputar 90 derajat
        //     o 
        // o o o
        //   
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x - Block.SIZE;
        tempB[1].y = b[0].y;
        tempB[2].x = b[0].x + Block.SIZE;
        tempB[2].y = b[0].y;
        tempB[3].x = b[0].x + Block.SIZE;
        tempB[3].y = b[0].y - Block.SIZE;

        // Update posisi block sesuai dengan posisi mino yang baru
        updateXY (4);
    }
}

