package entity;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;

import main.GamePanel;
import main.KeyHandler;

public class Player extends Entity{
	
	GamePanel gp;
	KeyHandler keyH;
	
	public final int screenX;
	public final int screenY;
	public int hasKey = 0;
	
	public Player(GamePanel gp, KeyHandler keyH) {
		
		this.gp = gp;
		this.keyH = keyH;
		
		screenX = gp.screenWidth/2 - (gp.tileSize/2);
		screenY = gp.screenHeigth/2 - (gp.tileSize/2);

		solidArea = new Rectangle();
		solidArea.x = 8;
		solidArea.y = 16;
		solidAreaDefaultX = solidArea.x;
		solidAreaDefaultY = solidArea.y;
		solidArea.width = 32;
		solidArea.height = 32;
		
		setDefaultValues();
		getPlayerImage();
	}
	
	// default spawn
	public void setDefaultValues() {
		
		worldX = gp.tileSize*11;
		worldY = gp.tileSize*16;
		speed = 4;
		direction = "right";
	}
	
	// player image resource
	public void getPlayerImage() {
		try {
			
			up1 = ImageIO.read(getClass().getResourceAsStream("/player/up_1.png"));
			up2 = ImageIO.read(getClass().getResourceAsStream("/player/up_2.png"));
			left1 = ImageIO.read(getClass().getResourceAsStream("/player/left_1.png"));
			left2 = ImageIO.read(getClass().getResourceAsStream("/player/left_2.png"));
			down1 = ImageIO.read(getClass().getResourceAsStream("/player/down_1.png"));
			down2 = ImageIO.read(getClass().getResourceAsStream("/player/down_2.png"));
			right1 = ImageIO.read(getClass().getResourceAsStream("/player/right_1.png"));
			right2  = ImageIO.read(getClass().getResourceAsStream("/player/right_2.png"));
			
		}catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void update(){
		
		if(keyH.upPressed == true || keyH.leftPressed == true || keyH.downPressed ==true || keyH.rightPressed == true) {

			if(keyH.upPressed == true) {
				direction = "up";	
			}
			else if(keyH.downPressed == true) {
				direction = "down";	
			}
			else if(keyH.leftPressed == true) {
				direction = "left";
			}
			else if(keyH.rightPressed == true) {
				direction = "right";	
			}
			
			// Tile Collision checker
			collisionOn = false;
			gp.cChecker.checkTile(this);
			
			// object collision checker
			int objIndex =  gp.cChecker.checkObject(this,  true);
			pickUpObject(objIndex);
			
			// if collision false, player can move
			if(collisionOn == false) {
				
				switch(direction) {
				case "up":
					worldY = worldY - speed;
					break;
				case "down":
					worldY = worldY + speed;
					break;
				case "left":
					worldX = worldX - speed;
					break;
				case "right":
					worldX = worldX + speed;
					break;
				}
			}
			
			// player sprites changes every 10 frames
			spriteCounter++;
			if(spriteCounter > 10) {
				if(spriteNum == 1) {
					spriteNum = 2;
				}
				else if(spriteNum == 2) {
					spriteNum = 1;
				}
				spriteCounter = 0;
			}
		}
		
	}
	public void pickUpObject(int i) {
		
		if(i != 999) {
			
			String objectName = gp.obj[i].name;
			
			switch(objectName) {
			case"Key":
				hasKey++;
				gp.obj[i] = null;
				gp.ui.showMessage("You got a Key!");
				break;
			case"Door":
				if(hasKey > 0 ) {
					gp.obj[i] = null;
					hasKey--;
					gp.ui.gameFinished = true;
				}
				else  {
					gp.ui.showMessage("You need a Key!");
				}
				System.out.println("Key:"+hasKey);
				break;
			}
		}
	}
	
	public void draw(Graphics2D g2) {
		
		BufferedImage image = null;
		
		switch(direction) {
		case "up":
			if(spriteNum == 1) {
				image = up1;
			}
			if(spriteNum == 2) {
				image = up2;
			}
			break;
			
		case "left":
			if(spriteNum == 1) {
				image = left1;
			}
			if(spriteNum == 2) {
				image = left2;
			}
			break;
			
		case "down":
			if(spriteNum == 1) {
				image = down1;
			}
			if(spriteNum == 2) {
				image = down2;
			}
			break;
			
		case "right":
			if(spriteNum == 1) {
				image = right1;
			}
			if(spriteNum == 2) {
				image = right2;
			}
			break;
		}
		g2.drawImage(image, screenX, screenY, gp.tileSize, gp.tileSize, null);
		
	}
}
