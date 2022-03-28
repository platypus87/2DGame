package tile;

import main.GamePanel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TileManager {
    GamePanel gp;
    public Tile[] tile;
    public int[][] mapTileNum;

    public TileManager(GamePanel gp){
        this.gp = gp;
        tile = new Tile[10]; //creating ten kinds of tiles, can be increased if we need more
        mapTileNum = new int[gp.maxWorldCol][gp.maxWorldRow];//this will store the numbers of the tiles from the map file

        getTileImage(); //getTileImage() loads the tiles pictures into the tile[] array for loadMap() to pull from
        loadMap("/res/maps/world01.txt");//use filePath so that new maps can be loaded from a file
    }
    public void getTileImage(){
        try{
            tile[0] = new Tile();
            tile[0].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/grass01.png"));

            tile[1] = new Tile();
            tile[1].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/wall.png"));
            tile[1].collision = true;

            tile[2] = new Tile();
            tile[2].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/water01.png"));
            tile[2].collision = true;

            tile[3] = new Tile();
            tile[3].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/earth.png"));

            tile[4] = new Tile();
            tile[4].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/tree.png"));
            tile[4].collision = true;

            tile[5] = new Tile();
            tile[5].image = ImageIO.read(getClass().getResourceAsStream("/res/tiles/sand.png"));

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void loadMap(String filePath){
        try{
            InputStream is = getClass().getResourceAsStream(filePath);//use inputStream to import the text file
            BufferedReader reader = new BufferedReader(new InputStreamReader(is)); //reader will be used to READ the contents of the text file

            int col = 0;
            int row = 0;

            while(col<gp.maxWorldCol && row<gp.maxWorldRow){
                String line = reader.readLine(); //this is only reading in...
                while(col<gp.maxWorldCol){
                    String numbers[] = line.split(" ");//...so we split the line up by space...
                    int num = Integer.parseInt(numbers[col]);//...and assign it to an array
                    mapTileNum[col][row]=num;
                    col++;
                }
                if (col == gp.maxWorldCol){
                    col = 0;
                    row ++;
                }
            }

            reader.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void draw(Graphics2D g2){

        int worldCol = 0;
        int worldRow = 0;


        while(worldCol<gp.maxWorldCol && worldRow< gp.maxWorldRow){

            int tileNum = mapTileNum[worldCol][worldRow];//because of the loadMap(), everything is stored in mapTileNum[][]

            //Explanation of below happens at 14:48 of "World and Camera - How to Make a 2D Game in Java #5"
            int worldX = worldCol*gp.tileSize;
            int worldY = worldRow*gp.tileSize;
            int screenX = worldX - gp.player.worldX + gp.player.screenX;
            int screenY = worldY - gp.player.worldY + gp.player.screenY;

            //this loop makes it so ONLY the screen that the player is on is being drawn
            //overall, this is better performance because the game isn't drawing the whole image
            //however, this may not be great if the "speed" of the game is too fast
            //it might be worth drawing a few "tiles" in either direction if the movement is super fast...IDK
            if(worldX + gp.tileSize > gp.player.worldX - gp.player.screenX &&
            worldX - gp.tileSize < gp.player.worldX + gp.player.screenX &&
            worldY + gp.tileSize > gp.player.worldY - gp.player.screenY &&
            worldY - gp.tileSize < gp.player.worldY + gp.player.screenY){

                g2.drawImage(tile[tileNum].image,screenX,screenY,gp.tileSize, gp.tileSize,null);
            }


            worldCol++;


            if(worldCol == gp.maxWorldCol){
                worldCol=0;

                worldRow++;

            }

        }
    }
}
