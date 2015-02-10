import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;

class Block extends Rectangle {
    // Makes up the bunkers and deteriorates on hit

    private static Image image[] = new Image[4];

    static {
        for (int i = 0; i < 4; i++) {
            String filename = String.format("resources/block%d.png", i);
            image[i] = new ImageIcon(filename).getImage();
        }
    }
    
    public int level;

    public Block(int x, int y) {
        this.x = x;
        this.y = y;
        
        this.width = 16;
        this.height = 16;

        this.level = 3;
    }

    public void hit() {
        level--;
    }

    public Image getImage() {
        return image[level];
    }

    public boolean isValid() {
        return this.level >= 0;
    }
}
