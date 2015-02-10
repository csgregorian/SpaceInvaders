import java.util.ArrayList;
import java.util.Random;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import java.io.*;
import javax.imageio.*;
// import javax.sound.sampled.*;

/* Space Invaders 2k15
 * Christopher Gregorian
 * A polished and accurate clone of the original game with powerups. 
 */

public class main extends JFrame implements ActionListener {
    Timer myTimer;
    GamePanel game;
    
    public main() {
        super("Space Invaders");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1024, 768);
        
        int fps = 100;
        myTimer = new Timer(1000/fps, this);
        myTimer.start();
        
        game = new GamePanel();
        add(game);
        
        setResizable(false);
        setVisible(true);
    }
    
    public void actionPerformed(ActionEvent evt) {
        if (game != null) {
            game.repaint();
        }
    }
    
    public static void main(String[] arguments) {
        main frame = new main();
    }
}


class GamePanel extends JPanel implements KeyListener {
    private Random random;

    private boolean[] keys;
    private final int sizex;
    private final int sizey;

    private int gamespeed, counter, frame, score, lives;
    private boolean direction, switch_direction;

    private int bulletframe, speedframe, deathframe;

    private Ship ship;
    private Bullet pBullet;
    private ArrayList<Bullet> enemy_bullets;
    private ArrayList<Enemy> enemies;
    private ArrayList<Block> blocks;
    private UFO alien;

    // private Clip clip1;
    // private Clip clip2;
    
    private enum State {
        INTRO,
        GAME,
        PAUSE,
        WIN,
        LOSE
    }
    private State game_state;
    
    public GamePanel() {
        keys = new boolean[KeyEvent.KEY_LAST+1];
        sizex = 1024;
        sizey = 768;
        random = new Random();

        init();

        // States
        game_state = State.INTRO;

        // Sound
        // Sound causes thread to be interrupted and events to be dropped
        // try {
        //    clip1 = AudioSystem.getClip();
        //    clip1.open(AudioSystem.getAudioInputStream(new File("resources/s1.wav").getAbsoluteFile()));
        //    clip2 = AudioSystem.getClip();
        //    clip2.open(AudioSystem.getAudioInputStream(new File("resources/s2.wav").getAbsoluteFile()));
        // } catch (UnsupportedAudioFileException e) {
        //     System.out.println(e.getMessage());
        // } catch (LineUnavailableException e) {
        //     System.out.println(e.getMessage());
        // } catch (IOException e) {
        //     System.out.println(e.getMessage());
        // }

        // Event Listeners
        addKeyListener(this);

        // Allows key capturing
        setFocusable(true);
    }

    public void init() {
        counter = 1; // Frame counter
        frame = 0; // Animation stage
        gamespeed = 100; // Scaling difficulty

        lives = 4;
        score = 0;

        direction = true; // Alien direction
        switch_direction = false; // Cue for aliens to drop down

        bulletframe = -1000; // Power-up
        speedframe = -1000;
        deathframe = -1000;

        // Ship creation
        ship = new Ship(sizex / 2, sizey - 100);
        
        // Bullets
        pBullet = null;
        enemy_bullets = new ArrayList<Bullet>();

        // Enemy spawns
        enemies = new ArrayList<Enemy>();
        for (int x = 128; x < 768; x += 64) {
            enemies.add(new Enemy(x, 100, 0));
            enemies.add(new Enemy(x, 150, 1));
            enemies.add(new Enemy(x, 200, 1));
            enemies.add(new Enemy(x, 250, 2));
            enemies.add(new Enemy(x, 300, 2));
        }

        alien = null;

        // Block creation
        blocks = new ArrayList<Block>();
        for (int x = 128; x < 224; x += 16) {
            for (int y = 576; y < 640; y += 16) {
                blocks.add(new Block(x, y));
            }
        }

        for (int x = 800; x < 896; x += 16) {
            for (int y = 576; y < 640; y += 16) {
                blocks.add(new Block(x, y));
            }
        }

        for (int x = 464; x < 560; x += 16) {
            for (int y = 576; y < 640; y += 16) {
                blocks.add(new Block(x, y));
            }
        }
    }
    
    // KeyListener implementation
    public void keyTyped(KeyEvent e) { }
    
    public void keyPressed(KeyEvent e) {
        keys[e.getKeyCode()] = true;

        switch (e.getKeyCode()) {
            case KeyEvent.VK_SPACE:
                // Shoots bullet
                if (pBullet == null){
                    pBullet = new Bullet(ship.x + ship.width / 2, ship.y,
                                         counter - bulletframe < 1000 ? 15 : 5);
                }
                break;

            case KeyEvent.VK_ENTER:
                // Starts game
                if (game_state != State.GAME) {
                    init();
                    game_state = State.GAME;
                }
                break;

            case KeyEvent.VK_ESCAPE:
                // Pauses game
                if (game_state == State.PAUSE) {
                    game_state = State.GAME;
                } else if (game_state == State.GAME) {
                    game_state = State.PAUSE;
                }
                break;

            // Debug mode
            case KeyEvent.VK_W:
                game_state = State.WIN;
                break;

            case KeyEvent.VK_L:
                game_state = State.LOSE;
                break;

            default:
                break;
        }
    }
    
    public void keyReleased(KeyEvent e) {
        keys[e.getKeyCode()] = false;
    }
    
    public void paintComponent(Graphics g) {
        counter++;

        // Win/Lose condition check
        if (lives == 0) {
            game_state = State.LOSE;
        } else if (enemies.size() == 0) {
            game_state = State.WIN;
        }

        // Background
        g.setColor(Color.black);
        g.fillRect(0, 0, sizex, sizey);

        // Intro Screen
        if (game_state == State.INTRO) {
            g.setColor(Color.white);
            g.setFont(new Font("Courier New", Font.PLAIN, 64));

            // Scrolling text
            if (counter < 210) {
                g.drawString("Space Invaders".substring(0, counter / 15), 235, 200);
            } else {
                g.drawString("Space Invaders", 235, 200);
            }

            // Flashing text
            if (counter > 250 && counter / 50 % 2 == 0) {
                g.setFont(new Font("Courier New", Font.PLAIN, 32));
                g.drawString("Insert a coin to begin", 300, 450);
            }

            return;
        }

        // Pause Screen
        if (game_state == State.PAUSE) {
            g.setColor(Color.white);
            g.setFont(new Font("Courier New", Font.PLAIN, 64));
            g.drawString("PAUSED", 375, 350);
            return;
        }

        // Win screen
        if (game_state == State.WIN) {
            g.setColor(Color.white);
            g.setFont(new Font("Courier New", Font.PLAIN, 64));
            g.drawString("YOU WIN", 350, 250);
            g.setFont(new Font("Courier New", Font.PLAIN, 32));
            g.drawString("Score: " + Integer.toString(score), 400, 350);

            if (counter / 50 % 2 == 0) {
                g.setFont(new Font("Courier New", Font.PLAIN, 32));
                g.drawString("Insert a coin to play again", 250, 450);
            }
            
            return;
        }

        // Lose screen
        if (game_state == State.LOSE) {
            g.setColor(Color.white);
            g.setFont(new Font("Courier New", Font.PLAIN, 64));
            g.drawString("YOU HAVE FAILED", 220, 250);
            g.setFont(new Font("Courier New", Font.PLAIN, 32));
            g.drawString("Score: " + Integer.toString(score), 400, 350);

            if (counter / 50 % 2 == 0) {
                g.setFont(new Font("Courier New", Font.PLAIN, 32));
                g.drawString("Insert a coin to try again", 250, 450);
            }

            return;
        }

        // Animation frame
        if (counter % gamespeed == 0) {
            frame = (frame+1) % 2;
            // Sound causes thread to be interrupted and events to be dropped
            // if (frame == 0) {
            //     clip1.setFramePosition(0);
            //     clip1.start();
            // } else {
            //     clip2.setFramePosition(0);
            //     clip2.start();
            // }      
        }

        // Powerups
        if (counter - speedframe < 1000) {
            ship.speed = 6;
        } else {
            ship.speed = 3;
        }

        // Ship
        if (counter - deathframe > 200) {
            // Make sure not dead
            if (keys[KeyEvent.VK_RIGHT]) {
                ship.moveRight();
            }
            if (keys[KeyEvent.VK_LEFT]) {
                ship.moveLeft();
            }

            g.drawImage(ship.getImage(), ship.x, ship.y, null);
        }

        // Friendly bullets
        if (pBullet != null) {
            pBullet.pMove();
            if (pBullet.y <= -20) {
                pBullet = null;
            } else {
                g.drawImage(pBullet.pImage, pBullet.x, pBullet.y, null);
            }
        }

        // Enemies
        for (Enemy enemy : enemies) {
            // Hits player
            if (enemy.intersects(ship)) {
                // Auto lose on contact
                game_state = State.LOSE;
            }

            // Gets shot
            if (pBullet != null && enemy.intersects(pBullet)) {
                enemy.alive = false;
                pBullet = null;

                // Speeds up game
                gamespeed *= 0.95;
                if (gamespeed < 5) {
                    gamespeed = 5;
                }

                switch (enemy.type) {
                    // Each case falls through, stacking the scores.
                    case 0: score += 10;
                    case 1: score += 10;
                    case 2: score += 10;
                            break;
                }

                continue;
            }

            // Movement
            if (counter % gamespeed == 0) {
                enemy.moveX(direction);
                // Shooting
                if (random.nextInt(50) == 0) {
                    // Random shooting
                    enemy_bullets.add(new Bullet(enemy.x, enemy.y, 5));
                }
            }

            // Hits edge of screen
            if (enemy.x > 960 || enemy.x < 64) {
                switch_direction = true;
            }

            g.drawImage(enemy.getImage(frame), enemy.x, enemy.y, null);
        }

        // Direction change
        if (switch_direction) {
            switch_direction = false;
            direction = !direction;

            // Speed up
            gamespeed *= 0.9;
            if (gamespeed < 5) {
                gamespeed = 5;
            }


            for (Enemy enemy : enemies) {
                // Next level
                enemy.moveDown();
                enemy.moveX(direction);
            }
        }
        

        // Enemy Bullets
        for (Bullet bullet : enemy_bullets) {
            bullet.eMove();

            if (bullet.intersects(ship)) {
                bullet.alive = false;
                deathframe = counter;
                lives--;
            }

            g.drawImage(bullet.eImage, bullet.x, bullet.y, null);
        }

        // Blocks
        for (Block block : blocks) {
            for (Bullet bullet : enemy_bullets) {
                // Enemy bullet hits block
                if (bullet.intersects(block)) {
                    bullet.alive = false;
                    block.hit();
                }
            }

            if (pBullet != null && pBullet.intersects(block)) {
                // Player bullet hits block
                pBullet = null;
                block.hit();
            }

            if (block.isValid()) {
                g.drawImage(block.getImage(), block.x, block.y, null);
            }
        }

        // Filters out invalid entities
        enemies.removeIf(e -> !e.isValid());
        blocks.removeIf(b -> !b.isValid());
        enemy_bullets.removeIf(b -> !b.isValid());

        // UFO
        if (alien != null) {
            // Why did the alien cross the screen?
            // To get to the space bar!
            alien.move();
            if (alien.x < -20 || alien.x > 1024) {
                // Despawn alien
                alien = null;

            } else if (pBullet != null && alien.intersects(pBullet)) {
                // Remove alien and bullet
                alien = null;
                pBullet = null;
                // Random score (50, 100, 150, 200)
                score += (random.nextInt(4) + 1) * 50;
                // Random powerup
                if (random.nextBoolean()) {
                    bulletframe = counter;
                } else {
                    speedframe = counter;
                }

            } else {
                g.drawImage(alien.getImage(), alien.x, alien.y, null);
            }

        } else if (counter % 100 == 0 && random.nextInt(8) == 0) {
            // Generates new ufo every 8 seconds or so
            alien = new UFO(1024, 64, random.nextBoolean());
        }

        // Score Display
        g.setColor(Color.white);
        g.setFont(new Font("Courier New", Font.PLAIN, 24)); 
        g.drawString("Score: " + Integer.toString(score), 0, 18);

        // Lives display
        for (int i = 0; i < lives - 1; i++) {
            g.drawImage(ship.getImage(), i * 64 + 300, 8, null);
        }
    }
    
    // Helper Methods
    public void fillRect(Graphics g, Rectangle rect) {
        g.fillRect(rect.x, rect.y, rect.width, rect.height);
    }
}
