package main;

import entity.Player;
import tile.TileManager;

import javax.swing.*;
import java.awt.*;

public class GamePanel extends JPanel implements Runnable{

    //screen settings
    final int originalTileSize = 16; //16x16 tile size, standard size for many 2D games
    final int scale = 3; //common scale for retro-style game
    public final int tileSize = originalTileSize * scale; //makes it 48x48
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12; //creates a 4x3 ratio
    public final int screenWidth = tileSize * maxScreenCol; //768 pixels
    public final int screenHeight = tileSize*maxScreenRow; // 576 pixels

    //WORLD SETTING
    public final int maxWorldCol = 50;
    public final int maxWorldRow = 50;
    public final int worldWidth = tileSize*maxWorldCol;
    public final int worldHeight = tileSize*maxWorldRow;

    TileManager tileMan = new TileManager(this);
    KeyHandler keyH = new KeyHandler(); //instantiates key handler
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public Player player = new Player(this, keyH);

    int FPS = 60;

    public GamePanel(){
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);
    }

    public void startGameThread(){
        gameThread = new Thread(this);
        gameThread.start(); //this then calls the run() method because of "Runnable"
    }

    @Override
    //"Delta Method" Game loop
    public void run(){
        double drawInterval = 1000000000/FPS;
        double delta = 0;
        long lastTime = System.nanoTime();
        long currentTime;
        long timer = 0;
        int drawCount = 0;

        while(gameThread!= null) {
            currentTime = System.nanoTime();
            delta += (currentTime - lastTime)/drawInterval;
            timer += (currentTime - lastTime);
            lastTime = currentTime;

            if(delta >=1) {
                update();
                repaint();
                delta--; //resets delta interval
                drawCount++;
            }
        }
    }

    public void update(){
        player.update();
    }
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        Graphics2D g2 = (Graphics2D)g;

        tileMan.draw(g2);//this is a layer, if drawn after player.draw, it will end up on top of the player and we won't see it

        player.draw(g2);

        g2.dispose();
    }
}



//if loop shows FPS in console, add to "Delta Method" after existing if loop
//            if(timer>=1000000000){
//                System.out.println("FPS: "+drawCount);
//                drawCount =0;
//                timer = 0;
//            }

//"Sleep Method" for game loop
//Thread.sleep() may cause millisecond delay
//
//    public void run() {
//
//        double drawInterval = 1000000000/FPS; //1000000000 is a second, divided by 60FPS, means the screen is being drawn 60 times a second
//        double nextDrawTime = System.nanoTime() + drawInterval;
//
//        while(gameThread!=null){ //as long as the game thread exists, repeat process within loop
//
//            long currentTime = System.nanoTime();
//            //long currentTime2 = System.currentTimeMillis();//less precise than nano time
//            System.out.println("current time: "+ currentTime);
//
//            // 1 UPDATE: update information such as character positions
//            update();
//
//            // 2 DRAW: draw the screen with the updated information
//            repaint(); //this is calling the paint component method
//
//            try {
//                double remainingTime = nextDrawTime - System.nanoTime();//calculates time left over so we can "put the loop to sleep" for the 'remaining time'
//                remainingTime = remainingTime/1000000;//sleep() only accepts milliseconds, so we convert nano to milli here for that
//                if(remainingTime <0){ //in case the time is negative for some reasonf
//                    remainingTime = 0;
//                }
//                Thread.sleep((long) remainingTime);
//                nextDrawTime += drawInterval;
//
//            } catch (InterruptedException e){
//                e.printStackTrace();
//            }
//        }
//    }