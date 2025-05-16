package mino;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

// Blok yang digunakan dalam permainan tetris
 
public class Block extends Rectangle{
    
    // koordinat x dan y dari blok
    public int x, y;
    
    // ukuran blok, 30x30
    public static final int SIZE = 30;
    
    // warna blok
    public Color c;

    
     // Konstruktor untuk membuat blok dengan warna yang ditentukan
     // @param c warna blok
   
    public Block(Color c) {
        this.c = c;
    }

    
    // Membuat blok pada koordinat x dan y yang ditentukan
    // @param g2 objek Graphics2D yang digunakan untuk menggambar
     
    public void draw(Graphics2D g2) {
        int margine = 2; // jarak antara blok dengan border
        
        // set warna dan menggambar blok
        g2.setColor(c);
        g2.fillRect(x+margine, y+margine, SIZE-(margine*2), SIZE-(margine*2));
    }
}

