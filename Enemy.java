import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

class Enemy extends Rectangle {
    public boolean alive;
    public int type;

    private static Image[][] image = new Image[3][2];
    static {
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 2; y++) {
                String filename = String.format("resources/%d-%d.png", x, y);
                image[x][y] = new ImageIcon(filename).getImage();
            }
        }
    }

    public Enemy(int x, int y, int type) {
        this.x = x;
        this.y = y;
        
        this.width = 40;
        this.height = 40;

        this.type = type;

        this.alive = true;
    }

    public void moveRight() {
        this.x += 5;
    }

    public void moveLeft() {
        this.x -= 5;
    }

    public void moveDown() {
        this.y += 16;
    }

    public void moveX(boolean direction) {
        if (direction) {
            moveRight();
        } else {
            moveLeft();
        }
    }

    public boolean isValid() {
        return this.alive;
    }

    public Image getImage(int frame) {
        return image[type][frame];
    }

}
