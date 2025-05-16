package mino;

import java.awt.Color;

public class Mino_T  extends Mino{
    
    public Mino_T() {
        // Buat Mino T dengan warna magenta
        create(Color.magenta); 
    }

    public void setXY(int x, int y){
        // Bentuk awal Mino T
        //  o
        //o o o
        b[0].x = x;
        b[0].y = y;
        b[1].x = b[0].x;
        b[1].y = b[0].y - Block.SIZE;
        b[2].x = b[0].x - Block.SIZE;
        b[2].y = b[0].y;
        b[3].x = b[0].x + Block.SIZE;
        b[3].y = b[0].y;
    }
    public void getDirection1(){
        // Bentuk Mino T ketika berputar 90 derajat
        //  o
        //o o o
        //
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x;
        tempB[1].y = b[0].y - Block.SIZE;
        tempB[2].x = b[0].x - Block.SIZE;
        tempB[2].y = b[0].y;
        tempB[3].x = b[0].x + Block.SIZE;
        tempB[3].y = b[0].y;

        // Update posisi block sesuai dengan posisi mino yang baru
        updateXY(1);
    }
    public void getDirection2(){
        // Bentuk Mino T ketika berputar 90 derajat
        //  o
        //  o o
        //  o
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x + Block.SIZE;
        tempB[1].y = b[0].y;
        tempB[2].x = b[0].x;
        tempB[2].y = b[0].y - Block.SIZE;
        tempB[3].x = b[0].x;
        tempB[3].y = b[0].y + Block.SIZE;

        // Update posisi block sesuai dengan posisi mino yang baru
        updateXY(2);
    }
    public void getDirection3(){
        // Bentuk Mino T ketika berputar 90 derajat
        //  
        //  o o o
        //    o
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x;
        tempB[1].y = b[0].y + Block.SIZE;
        tempB[2].x = b[0].x + Block.SIZE;
        tempB[2].y = b[0].y;
        tempB[3].x = b[0].x - Block.SIZE;
        tempB[3].y = b[0].y;

        // Update posisi block sesuai dengan posisi mino yang baru
        updateXY(3);
    }
    public void getDirection4(){
        // Bentuk Mino T ketika berputar 90 derajat
        //    o
        //  o o 
        //    o
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x - Block.SIZE;
        tempB[1].y = b[0].y;
        tempB[2].x = b[0].x;
        tempB[2].y = b[0].y + Block.SIZE;
        tempB[3].x = b[0].x;
        tempB[3].y = b[0].y - Block.SIZE;

        // Update posisi block sesuai dengan posisi mino yang baru
        updateXY(4);
    }
}

