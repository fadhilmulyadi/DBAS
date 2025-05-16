package mino;

import java.awt.Color;
import java.awt.Graphics2D;

import main.GamePanel;
import main.InputHandler;
import main.PlayManager;


public class Mino {
    public Block b[] = new Block[4];
    public Block tempB[] = new Block[4];
    public int autoDropCounter = 0;
    public int direction = 1; // Ada 4 arah (1/2/3/4/)
    public boolean leftCollision, rightCollision, bottomCollision; //Kita buat 3 boolean u/ batasan blok
    public boolean active = true;
    public boolean deactivating;
    public int deactivateCounter = 0;


    public void create(Color c) {
        b[0] = new Block(c);
        b[1] = new Block(c);
        b[2] = new Block(c);
        b[3] = new Block(c);
        tempB[0] = new Block(c);
        tempB[1] = new Block(c);
        tempB[2] = new Block(c);
        tempB[3] = new Block(c);
    }
    public void setXY(int x, int y) {}
    public void updateXY(int direction) {

        checkRotationCollision();

        if(leftCollision == false && rightCollision == false && bottomCollision == false) {

            this.direction = direction;
            //handle impact saat rotating tapi terkena fram/blok lain
            b[0].x = tempB[0].x;
            b[0].y = tempB[0].y;
            b[1].x = tempB[1].x;
            b[1].y = tempB[1].y;
            b[2].x = tempB[2].x;
            b[2].y = tempB[2].y;
            b[3].x = tempB[3].x;
            b[3].y = tempB[3].y;
        }
        
    }
    public void getDirection1 () {}
    public void getDirection2 () {}
    public void getDirection3 () {}
    public void getDirection4 () {} // Ada 2 jenis collision/tabrakan, kita memeriksa saat mino bergerak atau berputar
    public void checkMovementCollision () {

        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        // check static block collision
        checkStaticBlockCollision ();

        // Memeriksa frame collision
        // Frame kiri
        for(int i = 0; i < b.length; i++) {      // Memindai Array block
            if (b[i].x == PlayManager.left_x) {  // Memeriksa nilai x, jika sama dengan left x
                leftCollision = true;            // Maka left collision bernilai true
            }
        }

        // Frame kanan
        for (int i = 0; i < b.length; i++) {                   // Collision terjadi ketika x mino + ukuran blok sama dengan right x
            if (b[i].x + Block.SIZE == PlayManager.right_x) {  
                rightCollision =  true;                        // Maka right collision bernilai true
            }
        }

        // Frame Bawah
        for (int i = 0; i < b.length; i++) {                   // x mino + ukuran blok = bottom y
            if (b[i].y + Block.SIZE == PlayManager.bottom_y) {
                bottomCollision = true;                        // Maka bottom collision bernilai true
            }
        }
    }
    public void checkRotationCollision () {
        
        leftCollision = false;
        rightCollision = false;
        bottomCollision = false;

        // check static block collision
        checkStaticBlockCollision ();

        // Memeriksa frame collision
        // Frame kiri
        for(int i = 0; i < b.length; i++) {         // Memindai Array block
            if (tempB[i].x < PlayManager.left_x) {  // Memeriksa nilai x, jika lebih kecil dari left x
                leftCollision = true;               // Maka left collision bernilai true
            }
        }

        // Frame kanan
        for (int i = 0; i < b.length; i++) {                      // Collision terjadi ketika x mino + ukuran blok lebih besar dari right x
            if (tempB[i].x + Block.SIZE > PlayManager.right_x) {  
                rightCollision =  true;                           // Maka right collision bernilai true
            }
        }

        // Frame Bawah
        for (int i = 0; i < b.length; i++) {                       // x mino + ukuran blok > bottom y
            if (tempB[i].y + Block.SIZE > PlayManager.bottom_y) {
                bottomCollision = true;                            // Maka bottom collision bernilai true
            }
        }
    }
    private void checkStaticBlockCollision () {

        for(int i = 0; i < PlayManager.staticBlocks.size(); i++) {

            int targetX = PlayManager.staticBlocks.get(i).x;
            int targetY = PlayManager.staticBlocks.get(i).y;

            // check down
            for(int ii = 0; ii < b.length; ii++) {
                if(b[ii].y + Block.SIZE == targetY && b[ii].x == targetX) {
                    bottomCollision = true;
                }
            }
            // check kiri
            for(int ii = 0; ii < b.length; ii++) {
                if(b[ii].x - Block.SIZE == targetX && b[ii].y == targetY) {
                    leftCollision = true;
                }
            }
            // check kanan
            for(int ii = 0; ii < b.length; ii++) {
                if(b[ii].x + Block.SIZE == targetX && b[ii].y == targetY) {
                    rightCollision = true;
                }
            }
        }
    }
    public void update() {

        if(deactivating) {
            deactivating();
        }

        // gerak bloknya
        if(InputHandler.upPressed) {
            switch(direction) {
                case 1: getDirection2();break;
                case 2: getDirection3();break;
                case 3: getDirection4();break;
                case 4: getDirection1();break;
            }
            InputHandler.upPressed = false;
            GamePanel.se.play(3,false);
        }

        checkMovementCollision(); // Memeriksa apakah mino menyentuh frame/bottom atau tidak

        if(InputHandler.downPressed) {

            // Jika mino bottom tidak tersentuh, maka bisa turun
            if (bottomCollision == false) {
                b[0].y += Block.SIZE;
                b[1].y += Block.SIZE;
                b[2].y += Block.SIZE;
                b[3].y += Block.SIZE;

            //saat gerak ke bawah, autoDropCounter di reset
            autoDropCounter = 0;
            }

            InputHandler.downPressed = false;
        }

        if(InputHandler.leftPressed) {

            if (leftCollision == false) {
                b[0].x -= Block.SIZE;
                b[1].x -= Block.SIZE;
                b[2].x -= Block.SIZE;
                b[3].x -= Block.SIZE;
            }

            InputHandler.leftPressed = false;
        }
        
        if(InputHandler.rightPressed) {

            if (rightCollision == false) {
                b[0].x += Block.SIZE;
                b[1].x += Block.SIZE;
                b[2].x += Block.SIZE;
                b[3].x += Block.SIZE;
            }

            InputHandler.rightPressed = false;
        }
        
        if(bottomCollision) { // Stop autodrop Jika blok menyentuh frame/bottom
            if(deactivating == false) {
                GamePanel.se.play(4, false);
            }
            deactivating = true;
        }
        // auto drop (Animasi)
         else {
            autoDropCounter++;// counter nya bertambah tiap frame
             if (autoDropCounter == PlayManager.dropinterval) { 
                    // jika counter == interval drop maka tetromino akan drop
                    b[0].y += Block.SIZE;
                    b[1].y += Block.SIZE;
                    b[2].y += Block.SIZE;
                    b[3].y += Block.SIZE;
                    autoDropCounter = 0;
             }          
        }
    }
    private void deactivating() { //untuk memungkinkan slide
        
        deactivateCounter++;

        //paused 45 frames sbelum diactivate
        if(deactivateCounter == 45) {
            
            deactivateCounter = 0;
            checkMovementCollision(); // cek jika frame bawah masih tersentuh

            // jika masih tersentuh setelah 45 frames, maka blok diactivate
            if(bottomCollision) {
                active = false;
            }
        }
    }
    public void draw(Graphics2D g2) {
        
        int margin = 2;
        g2.setColor(b[0].c);
        g2.fillRect(b[0].x+margin, b[0].y+margin, Block.SIZE-(margin*2), Block.SIZE-(margin*2));
        g2.fillRect(b[1].x+margin, b[1].y+margin, Block.SIZE-(margin*2), Block.SIZE-(margin*2));
        g2.fillRect(b[2].x+margin, b[2].y+margin, Block.SIZE-(margin*2), Block.SIZE-(margin*2));
        g2.fillRect(b[3].x+margin, b[3].y+margin, Block.SIZE-(margin*2), Block.SIZE-(margin*2));
    }
}