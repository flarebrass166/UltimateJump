package TeamProject;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.Timer;
public class Game implements ActionListener, MouseListener, KeyListener{

    public static Game game;

    public final int WIDTH = 1250, HEIGHT = 800;

    public GameRenderer gRender;

    public Rectangle rect;

    public BufferedImage image, dead = null;

    public int ticks, yMotion;

    public User newUser = new User("New Player", 0);

    public ArrayList<Rectangle> columns;

    public Random rand;

    public boolean gameOver, started;

    public highScore hs;

    public static void main(String[] args) throws MalformedURLException{
        game = new Game();

    }
    public Game() throws MalformedURLException{
        JFrame frame = new JFrame();
        Timer timer = new Timer(20, this);
        JOptionPane enterName = new JOptionPane("Enter Name");
        hs = new highScore();

        gRender = new GameRenderer();
        rand = new Random();
        try
        {
            image = ImageIO.read(new File("yas sprite.png"));
            dead = ImageIO.read(new File("yas spritedead.png"));
            hs.readInScores();
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        frame.setTitle("Ultimate Jump");
        frame.add(gRender);
        frame.setSize(WIDTH, HEIGHT);
        frame.addMouseListener(this);
        frame.addKeyListener(this);
        frame.setVisible(true);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(frame.EXIT_ON_CLOSE);

        rect = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 30, 30);
        columns = new ArrayList<Rectangle>();
        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);
        timer.start();
    }
    public void addColumn(boolean start){
        int space = 300;
        int width = 100;
        int height = 50 + rand.nextInt(300);
        if(start){
            columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
        }
        else{
            columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 120, width, height));
            columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));		}
    }
    public void paintColumn(Graphics g, Rectangle column){
        g.setColor(Color.RED.darker());
        g.fillRect(column.x, column.y, column.width, column.height);
    }
    public void enterName() {
        String name = JOptionPane.showInputDialog(null,
                "Enter your name and press enter to save score");
        newUser.setUserName(name);
    }
    @Override
    public void actionPerformed (ActionEvent e) {
        int speed = 10;
        ticks++;

        if(newUser.getScore() > 6){
            speed +=2;
        }
        if(newUser.getScore() > 12){
            speed += 4;
        }
        if(newUser.getScore() > 18){
            speed += 4;
        }
        if(started){
            for(int i = 0; i < columns.size(); i++){

                Rectangle column = columns.get(i);
                column.x -= speed;
            }
            if(ticks % 2 == 0 && yMotion < 15){
                yMotion += 2;
            }
            for(int i = 0; i < columns.size(); i++){

                Rectangle column = columns.get(i);
                if (column.x + column.width < 0){
                    columns.remove(column);

                    if(column.y == 0){
                        addColumn(false);
                    }
                }
            }
            rect.y += yMotion;
            for(Rectangle column: columns){
                if(column.y == 0 && rect.x + rect.width / 2 > column.x + column.width / 2 - 10 && rect.x + rect.width / 2 < column.x + column.width / 2 + 5){
                    newUser.Score();
                }
                if (column.intersects(rect)){
                    gameOver = true;
                    if(rect.x <= column.x){
                        rect.x = column.x -rect.width;
                    }
                    else{
                        if(column.y != 0){
                            rect.y = column.y - rect.height;
                        }else if (rect.y < column.height){
                            rect.y = column.height;
                        }
                    }
                    rect.x = column.x - rect.width;
                }
                if(rect.y > HEIGHT - 120 || rect.y < 0){
                    gameOver = true;
                }
                if (rect.y + yMotion >= HEIGHT - 120){
                    rect.y = HEIGHT - 120 - rect.height;
                }
            }
        }
        gRender.repaint();

    }
    public void repaint(Graphics g) {
        image = image;
        String drawString = "Super Speed!!";
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(Color.GRAY);
        g.fillRect(0, HEIGHT - 120, WIDTH, 120);

        g.setColor(Color.GRAY.darker());
        g.fillRect(0, HEIGHT - 120 , WIDTH, 20);

        g.setColor(Color.BLACK);
        g.fillRect(rect.x, rect.y, rect.width, rect.height);
        g.drawImage(image, rect.x, rect.y, 65, 65, null);


        for(Rectangle column: columns){
            paintColumn(g, column);
        }

        g.setColor(Color.WHITE);
        g.setFont(new Font ("Arial", 1, 75));

        if(!started){
            g.drawString("Welcome to Ultimate Jump!!", 150, HEIGHT / 4 - 100);
            g.drawString("Jump through pipes to get points!!", 20, HEIGHT /2 - 150 );
            g.drawString("The higher your score gets. ", 150, HEIGHT / 2 );
            g.drawString("The harder the game gets.", 150, HEIGHT / 2 + 100);
            g.drawString("Click or press spacebar to start!", 20, HEIGHT - 150);

        }

        if(gameOver){
            columns.clear();
            try {
                image = ImageIO.read(new File("yas spritedead.png"));
            } catch (IOException e) {
                e.printStackTrace();
            };
            g.drawImage(dead, rect.x, rect.y, 65, 65, null);
            g.drawString("Game Over", 45, HEIGHT / 4 - 100);
            g.drawString("Top Scores", 400, HEIGHT / 2 - 220);
            g.setFont(new Font("Arial", 1, 50));
            g.drawString(hs.allScores(0), 50, HEIGHT/2 - 150);
            g.drawString(hs.allScores(1), 50, HEIGHT/2 - 80);
            g.drawString(hs.allScores(2), 50, HEIGHT/2 - 10);
            g.drawString(hs.allScores(3), 50, HEIGHT/2 + 60);
            g.drawString(hs.allScores(4), 50, HEIGHT/2 + 130);
            g.drawString("Your Score: " + newUser.getScore(), 650, HEIGHT / 4 - 100);

            g.drawString("Press enter to input name and save score", 20, HEIGHT  - 200);
            g.drawString("or press spacebar to try again", 20, HEIGHT  - 150);
        }

        if (!gameOver && started){
            try {
                image = ImageIO.read(new File("yas sprite.png"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            g.drawString(String.valueOf(newUser.getScore()), WIDTH / 10 - 25, 100);
            if(newUser.getScore() > 6 && newUser.getScore() < 12){
                g.drawString(drawString, 450, HEIGHT / 4 - 125 );
            }
            if(newUser.getScore() > 12 && newUser.getScore() < 18){
                drawString = "Ultra Speed!!";
                g.drawString(drawString, 450, HEIGHT / 4 - 125 );
            }
            if(newUser.getScore() > 18){
                drawString = "Ultimate Speed!!";
                g.drawString(drawString, 400, HEIGHT / 4 - 125 );
            }
        }
    }
    public void jump(){
        if(gameOver){
            hs.saveScore(newUser.getUserName(), newUser.getScore());
            rect = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 30, 30);
            columns.clear();
            yMotion = 0;
            newUser.setScore(0);

            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);

            gameOver = false;
        }
        if(!started){
            started = true;
        }
        else if(!gameOver){

            if(yMotion > 0){
                yMotion = 0;
            }
            yMotion -= 10;
        }
    }
    @Override
    public void mouseClicked(MouseEvent e) {
        jump();
    }
    @Override
    public void mouseEntered(MouseEvent e) {

    }
    @Override
    public void mouseExited(MouseEvent e) {

    }
    @Override
    public void mousePressed(MouseEvent e) {
    }
    @Override
    public void mouseReleased(MouseEvent e) {
    }
    @Override
    public void keyPressed(KeyEvent e) {
        if (gameOver == true && e.getKeyCode() == KeyEvent.VK_ENTER){
            enterName();
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE){
            jump();
        }
    }
    public void keyPressed1(KeyEvent g) {

    }
    @Override
    public void keyReleased(KeyEvent e) {
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }
}