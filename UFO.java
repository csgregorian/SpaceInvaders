import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

class UFO extends Rectangle {
    // A small, randomly spawned enemy that flys across your screen

    private static Image image = new ImageIcon("resources/ufo.png").getImage();

    public boolean direction;

    public UFO(int x, int y, boolean direction) {
        this.x = x;
        this.y = y;
        
        this.width = 40;
        this.height = 40;

        this.direction = direction;
    }

    public void move() {
        if (direction) {
            this.x += 2;
        } else {
            this.x -= 2;
        }
    }

    public Image getImage() {
        return image;
    }
}
