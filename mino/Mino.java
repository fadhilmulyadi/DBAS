package mino;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;

import main.GamePanel;
import main.InputHandler;
import main.PlayManager;


public abstract class Mino {
    public Block b[] = new Block[4];
    public Block tempB[] = new Block[4];
    public int autoDropCounter = 0;
    public int direction = 1; // Ada 4 arah (1/2/3/4/)
    public boolean leftCollision, rightCollision, bottomCollision; //Kita buat 3 boolean u/ batasan blok
    public boolean active = true;
    public boolean deactivating;
    public int deactivateCounter = 0;


    public void create(Color c) { // Buat blok
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
    public abstract void getDirection1 ();
    public abstract void getDirection2 ();
    public abstract void getDirection3 ();
    public abstract void getDirection4 (); // Ada 2 jenis collision/tabrakan, kita memeriksa saat mino bergerak atau berputar
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
    
    // Cek tabrakan antara mino yang sedang dipindah dengan static block
    // Cek tabrakan antara mino yang sedang dipindah dengan frame bawah, kiri, kanan
    private void checkStaticBlockCollision () {

        for(int i = 0; i < PlayManager.staticBlocks.size(); i++) {

            int targetX = PlayManager.staticBlocks.get(i).x;
            int targetY = PlayManager.staticBlocks.get(i).y;

            // Cek tabrakan bawah
            for(int ii = 0; ii < b.length; ii++) {
                if(b[ii].y + Block.SIZE == targetY && b[ii].x == targetX) {
                    bottomCollision = true;
                }
            }
            // Cek tabrakan kiri
            for(int ii = 0; ii < b.length; ii++) {
                if(b[ii].x - Block.SIZE == targetX && b[ii].y == targetY) {
                    leftCollision = true;
                }
            }
            // Cek tabrakan kanan
            for(int ii = 0; ii < b.length; ii++) {
                if(b[ii].x + Block.SIZE == targetX && b[ii].y == targetY) {
                    rightCollision = true;
                }
            }
        }
    }

    public Block[] getGhostBlocks() {
        Block[] ghostBlocks = new Block[4];
        // Salin posisi blok asli ke ghostBlocks
        for(int i = 0; i < 4; i++) {
            ghostBlocks[i] = new Block(b[i].c);
            ghostBlocks[i].x = b[i].x;
            ghostBlocks[i].y = b[i].y;
        }

        boolean bottomCollision = false;

        // Terus turunkan ghostBlocks selama belum tabrakan
        while(!bottomCollision) {
            // Cek collision bawah untuk ghostBlocks
            bottomCollision = false;
            // Cek tabrakan dengan frame
            for(int i = 0; i < ghostBlocks.length; i++) {
                if(ghostBlocks[i].y + Block.SIZE >= PlayManager.bottom_y) {
                    bottomCollision = true;
                    break;
                }
            }

            // Cek tabrakan dengan block lainnya yg sudah berhenti
            // Jika ghostBlocks belum tabrakan bawah, cek tabrakan dengan block lainnya
            if(!bottomCollision) {
                for(int i = 0; i < ghostBlocks.length; i++) {
                    for(int j = 0; j < PlayManager.staticBlocks.size(); j++) {
                        Block staticBlock = PlayManager.staticBlocks.get(j);
                        // Jika ghostBlocks tabrakan dengan block lainnya, maka bottomCollision bernilai true
                        if(ghostBlocks[i].x == staticBlock.x && ghostBlocks[i].y + Block.SIZE == staticBlock.y) {
                            bottomCollision = true;
                            break;
                        }
                    }
                    // Jika sudah ketemu tabrakan, keluar dari loop
                    if(bottomCollision) break;
                }
            }

            // Jika ghostBlocks belum tabrakan bawah, maka geser ghostBlocks ke bawah
            if(!bottomCollision) {
                for(int i = 0; i < ghostBlocks.length; i++) {
                    ghostBlocks[i].y += Block.SIZE;
                }
            }
        }

        // Return ghostBlocks setelah sudah diatur posisinya
        return ghostBlocks;
    }

    public void drawGhost(Graphics2D g2) {
        Block[] ghostBlocks = getGhostBlocks();

        // Gunakan warna block asli tapi full opacity untuk outline
        g2.setColor(b[0].c); // asumsi semua block satu warna sama b[0].c
        
        Stroke oldStroke = g2.getStroke();
        g2.setStroke(new BasicStroke(3)); // tebal outline 3 pixel

        for (Block block : ghostBlocks) {
            // gambar kotak outline saja, tanpa isi (transparan di tengah)
            g2.drawRect(block.x, block.y, Block.SIZE, Block.SIZE);
        }

    g2.setStroke(oldStroke); // reset stroke ke default agar tidak berpengaruh ke gambar lain
}

    public void update() {

        // Memeriksa apakah proses deactivating sedang berlangsung
        if(deactivating) {
            deactivating(); // Memanggil metode deactivating
        }

        // Memeriksa apakah tombol hard drop ditekan
        if (InputHandler.hardDropPressed) {
            // Melakukan hard drop sampai terjadi benturan bawah
            while (!bottomCollision) {
                checkMovementCollision(); // Memeriksa collision/tabrakan saat bergerak
                if (!bottomCollision) { // Jika tidak ada benturan bawah
                    // Geser semua blok mino ke bawah
                    b[0].y += Block.SIZE;
                    b[1].y += Block.SIZE;
                    b[2].y += Block.SIZE;
                    b[3].y += Block.SIZE;
                }
            }
            // Setelah mendarat, langsung proses deactivating
            if (GamePanel.sfxEnabled) { // Cek dulu apakah SFX aktif
                GamePanel.se.play(6, false); // efek suara drop
            }
            deactivating = true; // Set deactivating menjadi true
            InputHandler.hardDropPressed = false; // Reset status tombol hard drop
            return; // Keluar dari metode update
        }
    


        // Gerak bloknya, jika tombol atas ditekan
        if (InputHandler.upPressed) {
            // Ganti bentuk mino sesuai dengan arah yang diinginkan
            switch (direction) {
                case 1:
                    getDirection2();
                    break;
                case 2:
                    getDirection3();
                    break;
                case 3:
                    getDirection4();
                    break;
                case 4:
                    getDirection1();
                    break;
            }
            // Set ulang status tombol atas
            InputHandler.upPressed = false;
            // Mainkan efek suara jika SFX diaktifkan
            if (GamePanel.sfxEnabled) {
                GamePanel.se.play(5, false);
            }
        }

        checkMovementCollision(); // Memeriksa apakah mino menyentuh frame/bottom atau tidak

        // Jika tombol bawah ditekan
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

            // Set ulang status tombol bawah
            InputHandler.downPressed = false;
        }

        // Jika tombol kiri ditekan
        if(InputHandler.leftPressed) {

            // Jika tidak ada tabrakan di kiri, geser semua blok ke kiri
            if (!leftCollision) {
                b[0].x -= Block.SIZE;
                b[1].x -= Block.SIZE;
                b[2].x -= Block.SIZE;
                b[3].x -= Block.SIZE;
            }

            // Set ulang status tombol kiri
            InputHandler.leftPressed = false;
        }
        
        // Jika tombol kanan ditekan
        if(InputHandler.rightPressed) {

            // Jika tidak ada tabrakan di kanan, geser semua blok ke kanan
            if (rightCollision == false) {
                b[0].x += Block.SIZE;
                b[1].x += Block.SIZE;
                b[2].x += Block.SIZE;
                b[3].x += Block.SIZE;
            }

            // Set ulang status tombol kanan
            InputHandler.rightPressed = false;
        }

        if(bottomCollision) { // Stop autodrop Jika blok menyentuh frame/bottom
            if(deactivating == false) {
                if(GamePanel.sfxEnabled) {
                    GamePanel.se.play(6, false);
                }
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
        // menggambar 4 buah block dengan posisi yang diatur sesuai dengan posisi b[0], b[1], b[2] dan b[3]
        // margin digunakan untuk memberikan jarak antara block dengan border
        // Color dari block di set berdasarkan warna yang di set saat pembuatan objek Mino
        int margin = 2;
        g2.setColor(b[0].c);
        // menggambar block pertama dengan posisi b[0]
        g2.fillRect(b[0].x+margin, b[0].y+margin, Block.SIZE-(margin*2), Block.SIZE-(margin*2));
        // menggambar block kedua dengan posisi b[1]
        g2.fillRect(b[1].x+margin, b[1].y+margin, Block.SIZE-(margin*2), Block.SIZE-(margin*2));
        // menggambar block ketiga dengan posisi b[2]
        g2.fillRect(b[2].x+margin, b[2].y+margin, Block.SIZE-(margin*2), Block.SIZE-(margin*2));
        // menggambar block keempat dengan posisi b[3]
        g2.fillRect(b[3].x+margin, b[3].y+margin, Block.SIZE-(margin*2), Block.SIZE-(margin*2));
    }
    // Method untuk mengatur posisi tengah mino
    public void setPosition(int centerX, int centerY) {
        // Implementasi pengaturan posisi belum tersedia
        throw new UnsupportedOperationException("Unimplemented method 'setPosition'");
    }

    // Method untuk mendapatkan panjang baris mino
    public int getRowLength() {
        throw new UnsupportedOperationException("Unimplemented method 'getRowLength'");
    }
}