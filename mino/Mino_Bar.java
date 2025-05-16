package mino;

import java.awt.Color;

public class Mino_Bar extends Mino {
    
    // Konstruktor untuk Mino Bar
    public Mino_Bar(){
        create(Color.cyan);
    }
    
    // Method untuk mengatur posisi awal Mino Bar
    public void setXY(int x, int y){
        // Bentuk Mino Bar
        // 
        //o o o o
        //
        b[0].x = x;
        b[0].y = y;
        b[1].x = b[0].x - Block.SIZE;
        b[1].y = b[0].y;
        b[2].x = b[0].x + Block.SIZE;
        b[2].y = b[0].y;
        b[3].x = b[0].x + 2 * Block.SIZE;
        b[3].y = b[0].y;
    }
    
    // Method untuk mengatur bentuk Mino Bar
    public void getDirection1(){
        // Bentuk Mino Bar
        // 
        //o o o o
        //
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x - Block.SIZE;
        tempB[1].y = b[0].y;
        tempB[2].x = b[0].x + Block.SIZE;
        tempB[2].y = b[0].y;
        tempB[3].x = b[0].x + 2 * Block.SIZE;
        tempB[3].y = b[0].y;
        updateXY(1);
    }
    
    // Method untuk mengatur bentuk Mino Bar
    public void getDirection2(){
        // Bentuk Mino Bar
        //   o
        //   o
        //   o
        //   o
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x;
        tempB[1].y = b[0].y - Block.SIZE;
        tempB[2].x = b[0].x;
        tempB[2].y = b[0].y + Block.SIZE;
        tempB[3].x = b[0].x;
        tempB[3].y = b[0].y + 2 * Block.SIZE;

        updateXY(2);
    }
    
    // Method untuk mengatur bentuk Mino Bar
    public void getDirection3(){
        getDirection1();
    }
    
    // Method untuk mengatur bentuk Mino Bar
    public void getDirection4(){
        getDirection2();
    }
}

