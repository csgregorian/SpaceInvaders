import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

class Bullet extends Rectangle {
    // Bullets for both the player and the enemies

    public static Image pImage = new ImageIcon("resources/pBullet.png").getImage();
    public static Image eImage = new ImageIcon("resources/eBullet.png").getImage();

    public int speed;
    public boolean alive;

    public Bullet(int x, int y, int speed) {
        this.x = x - 3;
        this.y = y;
        
        this.width = 3;
        this.height = 5;
        
        this.speed = speed;

        this.alive = true;
    }

    public void pMove() {
        this.y -= speed;
    }

    public void eMove() {
        this.y += speed;
    }

    public boolean isValid() {
        return (alive && -5 <= this.y && this.y <= 800);
    }
}
