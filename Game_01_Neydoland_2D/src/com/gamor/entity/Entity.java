package com.gamor.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.gamor.main.Game;
import com.gamor.world.Camera;

public class Entity {
	
	//estão em static para ser carregado apenas uma vez e evitar de sobrecarregar
	public static BufferedImage LIFEPACK_EN = Game.spritesheet.getSprite(6*16, 0,16,16);
	public static BufferedImage WEAPON_EN = Game.spritesheet.getSprite(7*16, 0,16,16);
	public static BufferedImage BULLET_EN = Game.spritesheet.getSprite(6*16, 16,16,16);
	public static BufferedImage ENEMY_EN = Game.spritesheet.getSprite(7*16, 16,16,16);
	public static BufferedImage ENEMY_FEEDBACK = Game.spritesheet.getSprite(112, 32,16,16);
	
	public static BufferedImage GUN_RIGHT = Game.spritesheet.getSprite(128, 0,16,16); //para a arma para quando ficar na mão
	public static BufferedImage GUN_LEFT= Game.spritesheet.getSprite(128 +16, 0,16,16); //para a arma para quando ficar na mão
	public static BufferedImage GUN_RIGHT_WHILE = Game.spritesheet.getSprite(128, 32,16,16); //para a arma para quando ficar na mão tomando dano
	public static BufferedImage GUN_LEFT_WHILE= Game.spritesheet.getSprite(128 +16, 32,16,16); //para a arma para quando ficar na mão tomando dano
	
	protected double x;
	protected double y;
	protected double width;
	protected double height;
	
	private BufferedImage sprite;
	
	private int maskx,masky,maskw,maskh; //mascara de colisão da entidade

	public Entity(int x, int y, int width, int height, BufferedImage sprite) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.sprite = sprite;
		
		this.maskx = 0;
		this.masky = 0;
		this.maskw = width; 
		this.maskh = height;
	}
	
	public void setMask(int maskx,int masky,int maskw,int maskh) {
		this.maskx = maskx;
		this.masky = masky;
		this.maskw = maskw;
		this.maskh = maskh;
	}
	
	public void setMaskNoWidthHeight(int maskx,int masky) {
		this.maskx = maskx;
		this.masky = masky;
	}
	

	public void setX(int newX) {
		this.x = newX;
	}
	public void setY(int newY) {
		this.y = newY;
	}
	
	public int getX() {
		return (int)x;
	}
	public int getY() {
		return (int)y;
	}
	public double getWidth() {
		return width;
	}
	public double getHeight() {
		return height;
	}
	
	
	public void tick() {
		
	}
	
	public static boolean isColidding(Entity e1,Entity e2) { //metodo que verifica se duas entidades colidem
		Rectangle e1Mask = new Rectangle(e1.getX() + e1.maskx, e1.getY() + e1.masky, e1.maskw,e1.maskh);
		Rectangle e2Mask = new Rectangle(e2.getX() + e2.maskx, e2.getY() + e2.masky, e2.maskw,e2.maskh);
		
		return e1Mask.intersects(e2Mask);
	}
	
	public void render(Graphics g) {
		g.drawImage(sprite, this.getX() -Camera.x, this.getY() -Camera.y, null); //tem isso de -Camera.x e -Camera.y, para poder termos uma camera seguindo
		
		//ver a mascara de colizão, esses comando abaixo usados para debugar, ver a mascara de colisão
		//g.setColor(Color.red);
		//g.fillRect(this.getX() + maskx -Camera.x, this.getY() + masky -Camera.y, maskw, maskh);
		
		
	}
}
