package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.JPanel;

public class GamePanel extends JPanel implements Runnable {

    public static final int WIDHTH = 1280;
    public static final int HEIGHT = 720;
    final int FPS = 60;
    Thread gameThread; //Thread class u/ run game loop
    PlayManager pm;
    public static Soound music = new Soound();//musik
    public static Soound se = new Soound();//sound effect

    public GamePanel() {

        // Panel Settings
        this.setPreferredSize(new Dimension(WIDHTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setLayout(null);

        // implement Keylistener
        this.addKeyListener(new InputHandler());
        this.setFocusable(true); //saat input key pada window yg di fokus kita mendapakan input

        pm = new PlayManager();
    }

    public void launchGame() {
        gameThread = new Thread(this);
        gameThread.start(); //start thread akan langsung call method run
        
        music.play(0, true);
        music.loop();
    }

    @Override
    public void run() {
        
        // Game Loop "Update and Draw"
        double drawInterval = 1000000000/FPS; // 1/60 detik
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;

        while(gameThread != null) {

            currentTime = System.nanoTime();

            delta += (currentTime - lastTime) / drawInterval;
            lastTime = currentTime;

            if(delta >= 1) {
                // di ulang 60x per detik
                update();
                repaint();
                delta--;
            }
        }
    }
    private void update() {
        
        if(InputHandler.pausePressed == false && pm.gameOver == false) {
            pm.update();
        }
    }

    @Override
    public void paintComponent(Graphics g) { //dari play manager
        super.paintComponent(g);
        
        Graphics2D g2 = (Graphics2D) g;
        pm.draw(g2);
    }
    
}

