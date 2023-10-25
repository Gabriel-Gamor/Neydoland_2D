package com.gamor.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.gamor.main.Game;
import com.gamor.main.Sound;
import com.gamor.world.Camera;
import com.gamor.world.World;

public class Enemy extends Entity{
	
	public double speed = Game.rand.nextDouble(0.5,2.7);
	
	private int maskx = 8, masky = 8, maskw = 6, maskh = 6;
	private int frames = 0, maxFrames = 20, index = 0, maxIndex = 4;
	
	private BufferedImage[] sprites;
	
	private int life = 10;
	
	private boolean isDamaged = false;
	private int damageFrames = 7,damageCurrent = 0;
	
	public Enemy(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		sprites = new BufferedImage[4];
		sprites[0] = Game.spritesheet.getSprite(112, 16, 16, 16);
		sprites[1] = Game.spritesheet.getSprite(128, 16, 16, 16);
		sprites[2] = Game.spritesheet.getSprite(112, 16, 16, 16);
		sprites[3] = Game.spritesheet.getSprite(128+16, 16, 16, 16);
	}
	
	public void tick(){
		
		if(Game.rand.nextInt(100) < 30) { //criar uma aleatoriedade para evitar de todos fizerem tudo igual
			
			if(isColiddingWithPlayer() == false) {
				//se nao estiver colidindo com o player
				//movimentação do inimigo, coorendo atras do jogador e conferindo se tem parede ou se esta colidindo com outro inimigo 
				if((int)x < Game.player.getX() && World.isFree((int)(x+speed), getY()) && !isColidding((int)(x+speed), getY())) {
					x+=speed;
				}else if((int)x > Game.player.getX() && World.isFree((int)(x-speed), getY()) && !isColidding((int)(x-speed), getY())) {
					x-=speed;
				}
				if((int)y < Game.player.getY()  && World.isFree(getX(), (int)(y+speed)) && !isColidding(getX(), (int)(y+speed))) {
					y+=speed;
				}else if((int)y > Game.player.getY() && World.isFree(getX(), (int)(y-speed)) && !isColidding(getX(), (int)(y-speed))) {
					y-=speed;
				}
			}else {
				//caso o inimigo colida com o player
				
				if(Game.rand.nextInt(100) < 25) { //aleatorizando a chance de acerto
					Sound.hurtEffect.play();//vai rodar apenas uma vez ao tomar dano
					Game.player.isDamaged = true;
					Game.player.life-= Game.rand.nextInt(2,8); //aleatorizando o dano
					if (Game.player.life < 0) {
						Game.player.life = 0;
					}
					//System.out.println(Game.player.life);
				}
				//fechar o jogo se o player morrer
				//if(Game.player.life <= 0) {
				//	System.exit(1);
				//}
				
			}
		}
		/* caso queira com o inimigo com apenas uma divisãow
		if((int)x < Game.player.getX()) {
			x+=speed;
		}else if((int)x > Game.player.getX()) {
			x-=speed;
		}else if((int)y < Game.player.getY()) {
			y-=speed;
		}else if((int)y > Game.player.getY()) {
			y+=speed;
		} */	
		
		frames ++;
		if (frames == maxFrames) {
			frames = 0;
			index++;
			if (index == maxIndex) {
				index = 0;
			}
		}
		
		CollidingBullet();
		
		if (life <=0) {
			destroySelf();
			return;
		}
		
		if (isDamaged) { //ao levar o dano, recuperar a animação normal, apos passar pelos frames setados em damageCurrent
			this.damageCurrent ++;
			if (this.damageCurrent == this.damageFrames) {
				this.damageCurrent = 0;
				this.isDamaged = false;
			}
		}
		
	}
	
	private void destroySelf() {
		Game.player.score++;
		Game.enemies.remove(this);
		Game.entities.remove(this);
	}

	private void CollidingBullet() {
		for (int i = 0; i < Game.bullets.size(); i ++) {
			BulletShoot e = Game.bullets.get(i);
			if (BulletShoot.isColidding(this, e)) {
				Sound.hurtEffecthit.play();
				isDamaged = true;
				life--;
				Game.bullets.remove(i);
				return;
			}
		}
	}

	public boolean isColiddingWithPlayer() { //colidino com o player
		Rectangle enemyCurrent = new Rectangle(this.getX()+5,this.getY()+6,maskw,maskh+3); //colizão do inimigo
		Rectangle player = new Rectangle(Game.player.getX(), Game.player.getY(),World.TILE_SIZE,World.TILE_SIZE); //colizão do player
		return enemyCurrent.intersects(player);
	}
	
	public boolean isColidding(int xnext, int ynext) { //colizão
		
		Rectangle enemyCurrent = new Rectangle(xnext+ maskx-3,ynext+ masky,maskw,maskh); //World.TILE_SIZE = 16
		
		for (int i = 0; i < Game.enemies.size(); i++) {//confere todos os enymigos na lista de inimigos, e verifica se não é o proprio inimgo pois ele tambem faz parte da classe
			Enemy e = Game.enemies.get(i);
			if(e == this) { //verifica se é esse objeto
				continue;
			}
			//se chegou aqui é outro inimigo
			
			Rectangle targetEnemy = new Rectangle(e.getX()+ maskx-3,e.getY()+ masky,maskw,maskh); //World.TILE_SIZE = 16
			
			if (enemyCurrent.intersects(targetEnemy)) { // isso é para gerar a colizão dos inimigos pelo Rectangle
				return true;
			}
		}
		return false;
	}
	
	public void render(Graphics g) {
		//super.render(g);
		if(!isDamaged) {//se não tiver tomando dano
			g.drawImage(sprites[index], this.getX() -Camera.x, this.getY() -Camera.y, null);
		}else {
			g.drawImage(ENEMY_FEEDBACK, this.getX() -Camera.x, this.getY() -Camera.y, null);
		}
		/*
		//definindo uma mascara de colisão,, comandos para debug, para testar
		g.setColor(Color.blue);
		g.fillRect(this.getX() + maskx-3 - Camera.x, this.getY() + masky - Camera.y, maskw, maskh);
		*/
	}
	
}
