package mino;

import java.awt.Color;

public class Mino_Z2 extends Mino {

    // Konstruktor untuk membuat objek Mino_Z2
    public Mino_Z2(){
        create(Color.green);
    }

    // Metode untuk mengatur posisi Mino_Z2
    public void setXY(int x, int y){
        //  o
        //  o o
        //    o
        b[0].x = x;
        b[0].y = y;
        b[1].x = b[0].x;
        b[1].y = b[0].y - Block.SIZE;
        b[2].x = b[0].x + Block.SIZE;
        b[2].y = b[0].y;
        b[3].x = b[0].x + Block.SIZE;
        b[3].y = b[0].y + Block.SIZE;
    }

    // Metode untuk mengatur posisi Mino_Z2 ketika berputar 90 derajat
    //  o
    //  o o
    //    o
    public void getDirection1() {
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x;
        tempB[1].y = b[0].y - Block.SIZE;
        tempB[2].x = b[0].x + Block.SIZE;
        tempB[2].y = b[0].y;
        tempB[3].x = b[0].x + Block.SIZE;
        tempB[3].y = b[0].y + Block.SIZE;

        updateXY(1);
    }

    // Metode untuk mengatur posisi Mino_Z2 ketika berputar 90 derajat
    //  o o
    //o o
    //
    public void getDirection2() {
        tempB[0].x = b[0].x;
        tempB[0].y = b[0].y;
        tempB[1].x = b[0].x - Block.SIZE;
        tempB[1].y = b[0].y;
        tempB[2].x = b[0].x;
        tempB[2].y = b[0].y - Block.SIZE;
        tempB[3].x = b[0].x + Block.SIZE;
        tempB[3].y = b[0].y - Block.SIZE;

        updateXY(2);
    }

    // Metode untuk mengatur posisi Mino_Z2 ketika berputar 90 derajat
    //  o
    //  o o
    //    o
    public void getDirection3() {
        getDirection1();
    }

    // Metode untuk mengatur posisi Mino_Z2 ketika berputar 90 derajat
    //  o o
    //o o
    //
    public void getDirection4() {
        getDirection2();
    }
}

