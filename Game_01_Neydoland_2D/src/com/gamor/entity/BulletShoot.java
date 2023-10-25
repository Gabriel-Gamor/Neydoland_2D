package com.gamor.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import com.gamor.main.Game;
import com.gamor.main.Sound;
import com.gamor.world.Camera;
import com.gamor.world.World;

public class BulletShoot extends Entity{
	
	private double dx = 0;
	private double dy = 0;
	private double spd = 4;
	
	private int life = 80, curLife = 0; //criando um tempo de vida para a bala, para evitar de encher de balas na memoria
	
	public BulletShoot(int x, int y, int width, int height, BufferedImage sprite, double dx, double dy) {
		super(x, y, width, height, sprite);
		this.dx = dx;
		this.dy = dy;
	}

	public void tick() {
		x+=dx*spd;
		y+=dy*spd;
	//	if () {  //Bater na parece de a bala morrer
	//		Game.bullets.remove(this);
	//	}else {
	//		x+=dx*spd;
	//		y+=dy*spd;
	//	}
		
		//sistema de destruir as balas
		if(curLife == 0) {
			Sound.shootEffecthit.play();
		}
		curLife++;
		if(curLife == life) {
			dx = 0;
			dy = 0;
			Game.bullets.remove(this);
			return;
		}
	}
	
	public void render(Graphics g) { //renderizando balas
		g.setColor(Color.YELLOW);
		g.fillOval(this.getX() - Camera.x, this.getY() - Camera.y, (int)width, (int)height);
	}
}
