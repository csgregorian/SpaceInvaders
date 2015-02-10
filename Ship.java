import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

class Ship extends Rectangle {
    public int speed;
    private Image image = new ImageIcon("resources/ship.png").getImage();
    
    public Ship(int x, int y) {
        this.x = x;
        this.y = y;
        
        this.width = 56;
        this.height = 24;

        this.speed = 3;
    }

    public void moveRight() {
        x = Integer.min(1024 - width, x + speed);
    }

    public void moveLeft() {
        x = Integer.max(0, x - speed);
    }

    public Image getImage() {
        return image;
    }
}
