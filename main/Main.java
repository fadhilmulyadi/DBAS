package main;

import javax.swing.JFrame;

public class Main {
    public static void main(String[] args) {
        // Membuat jendela baru dengan judul "Dynamic Block Allocation System"
        JFrame window = new JFrame("Dynamic Block Alocation System");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Mengatur operasi penutupan jendela
        window.setResizable(false); // Menonaktifkan kemampuan untuk mengubah ukuran jendela

        // Menambahkan GamePanel ke jendela
        GamePanel gp = new GamePanel(); // Membuat instance GamePanel
        window.add(gp); // Menambahkan GamePanel ke jendela
        window.pack(); // Mengatur ukuran jendela sesuai dengan ukuran GamePanel
        
        window.setLocationRelativeTo(null); // Menempatkan jendela di tengah layar
        window.setVisible(true); // Menampilkan jendela

        gp.launchGame(); // Memulai permainan
    }
}
