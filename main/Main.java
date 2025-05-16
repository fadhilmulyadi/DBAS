package main;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        JFrame window = new JFrame("Dynamic Block Alocation System");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);

        //Tambah GamePanel Ke Window
        GamePanel gp = new GamePanel();
        window.add(gp);
        window.pack(); //ukuran game panel sesuai ukuran window
        
        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gp.launchGame();
    }
}