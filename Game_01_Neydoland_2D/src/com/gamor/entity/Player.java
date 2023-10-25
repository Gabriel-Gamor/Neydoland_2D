package com.gamor.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.gamor.graficos.Spritesheet;
import com.gamor.main.Game;
import com.gamor.main.Sound;
import com.gamor.world.Camera;
import com.gamor.world.World;

public class Player extends Entity{

	public boolean right, up, left, down;
	public int right_dir = 0, left_dir = 1, up_dir = 2, down_dir = 3;
	public int dir = right_dir; //direção do player
	public int dirwelpon = right_dir; //direção para arma
	public double speed = 1.4;
	
	public static int score = 0;
	
	private int frames = 0, maxFrames = 5, index = 0, maxIndex = 4;
	private boolean moved = false;
	private BufferedImage[] rightPlayer;
	private BufferedImage[] leftPlayer;
	private BufferedImage[] upPlayer;
	private BufferedImage[] downPlayer;
	
	private BufferedImage playerDamage;
	private int damageFrames = 0;
	
	public boolean shoot = false;//verificar se está atirando
	public boolean mouseshoot = false;//verificar se está atirando com mouse
	
	private boolean arma = false;
	
	public int armo = 0; //munição
	
	public boolean isDamaged = false;
	
	public double life = 100, maxLife = 100;
	
	public int mx,my;//posição do mouse
	
	public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
		upPlayer = new BufferedImage[4];
		downPlayer = new BufferedImage[4];
		
		playerDamage = Game.spritesheet.getSprite(0, 16, 16, 16);
		
		for(int i = 0; i<4; i ++) {
		rightPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 32, 16, 16);
		}
		for(int i = 0; i<4; i ++) {
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 48, 16, 16);
		}
		for(int i = 0; i<4; i ++) {
			upPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 16, 16, 16);
		}
		for(int i = 0; i<4; i ++) {
			downPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 0, 16, 16);
		}
		
	}
	
	public void tick(){
		moved = false; //isso é para não mostrar animação de movendo no caso de em uma parede ficar parado
		if(right && World.isFree((int)(x+speed),this.getY())) {  // o World.isFree é para conferir se não tem nenhuma parede ao redor do player
			moved = true; 
			dir = right_dir;
			dirwelpon = right_dir;
			x+=speed;
		}else if(left && World.isFree((int)(x-speed),this.getY())) {
			moved = true;
			dir = left_dir;
			dirwelpon = left_dir;
			x-=speed;
		}
		if(up && World.isFree(this.getX(),(int)(y-speed))) {
			moved = true;
			dir = up_dir;
			y-=speed;
		}else if(down && World.isFree(this.getX(),(int)(y+speed))) {
			moved = true;
			dir = down_dir;
			y+=speed;
		}
		//place_free() é para conferis se os tile staticos, parados, parede no caso, está ocupando o local onde o player quer ir, se sim, impede de ir
		
		if (moved) { //para poder mover
			frames ++;
			if (frames == maxFrames) {
				frames = 0;
				index++;
				if (index == maxIndex) {
					index = 0;
				}
			}
		}
		
		this.checkCollisionLifePack();//checar o item kitmedico
		this.checkCollisionArmo();//checar o item munição
		this.checkCollisionGun();//checar colizão com a arma
		
		if (isDamaged) {//se tomar dano
			this.damageFrames++;
			if(this.damageFrames == 8) {
				this.damageFrames = 0;
				isDamaged = false; //retira o estadus de tomar dano, para voltar ao normal
			}
		}
		
		if(shoot) { //atirando
			shoot = false;
			if(arma && armo > 0) {
				armo --;
				//Criar bala e atirar
				int px = 0;
				int py = 8;
				int dx = 0;
				if (dirwelpon == right_dir) {
					dx = 1;
					px = 17;
				}else if (dirwelpon == left_dir) {
					dx = -1;
					px = -5;
				}
			
				BulletShoot bullet = new BulletShoot(this.getX()+px,this.getY()+py,3,3,null,dx,0);
				Game.bullets.add(bullet);
			}
		}
		
		if(mouseshoot) {//atirando de mouse
			//System.out.println("atirou mouse");
			mouseshoot = false;
			
			if(arma && armo > 0) {
				armo --;
				//Criar bala e atirar
				
				int px = 0;
				int py = 0;
				
				double angle = 0;
				if (dirwelpon == right_dir) {
					px = 17;
					py = 8;
					angle = Math.atan2(my - (this.getY() + 8 - Camera.y), mx - (this.getX() + 8 - Camera.x)); //pega o angulo atual do cursor do mouse em relação ao player
				}else if (dirwelpon == left_dir) {
					px = -5;
					py = 8;
					angle = Math.atan2(my - (this.getY() + 8 - Camera.y), mx - (this.getX() + 8 - Camera.x)); //pega o angulo atual do cursor do mouse em relação ao player
				}
				double dx = Math.cos(angle);
				double dy = Math.sin(angle);
				//System.out.println(angle);
				BulletShoot bullet = new BulletShoot(this.getX()+px,this.getY()+py,3,3,null,dx,dy); //cria um bala em certa direção
				Game.bullets.add(bullet);
			}
		}
		
		
		if (life <= 0) { //se a vida chegar em 0
			
			//GAME OVER
			Game.gameState="GameOver";
			//World.restartgame("level"+Game.getCUR_LEVEL()+".png"); //resetar o level diretamente pelo player
		}
		
		updateCamera();
		
	}
	
	public void updateCamera() {
		Camera.x = Camera.clamp(this.getX() - (Game.WIDTH/2), 0, (World.WIDTH*16) - Game.WIDTH); //isso é bem simples até, pega a posição do jogador,  e ver qunato que falta para ir para o centro da tela
		Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2), 0, (World.HEIGHT*16) - Game.HEIGHT); //por isso fica sempre no centro
	}
	
	private void checkCollisionArmo() {
		for(int i = 0; i < Game.armos.size(); i++) {
			Entity atual = Game.armos.get(i);
			//if(atual instanceof Bullet) {} //caso não criase uma lista exclusiva para o Bullet
			if(Entity.isColidding(this, atual)) {//se o atual estiver colidindo com esse, no caso o player colidir com a vida
				armo += 20;
				//System.out.println("Munição:"+armo); //apenas metodo de debug para ver se estava tudo certinho
				Game.entities.remove(atual);//remover o que ja foi peque
				Game.armos.remove(atual);
				Sound.bulletEffect.play();
			}
		}
	}

	private void checkCollisionLifePack() {
		for(int i = 0; i < Game.lifepacks.size(); i++) {
			Entity atual = Game.lifepacks.get(i);
			//if(atual instanceof Lifepack) {} //caso não criase uma lista exclusiva para o lifepack
			if(Entity.isColidding(this, atual)) {//se o atual estiver colidindo com esse, no caso o player colidir com a vida
				life+=Game.rand.nextInt(7, 20);//randomisar o valor da vida ganho
				if (life>100) {life = 100;}
				if (life<0) {life = 0;}
				Game.entities.remove(atual);//remover o que ja foi peque
				Game.lifepacks.remove(atual);
				Sound.lifeEffect.play();
			}
		}
	}
	
	private void checkCollisionGun() {
		for(int i = 0; i < Game.entities.size(); i++) {
			Entity atual = Game.entities.get(i);
			if(atual instanceof Weapon) {
				if(Entity.isColidding(this, atual)) {//se o atual estiver colidindo com esse, no caso o player colidir com a vida
					arma = true;
					//System.out.println("Pegou a arma");
					Game.entities.remove(atual);//remover o que ja foi peque
					Sound.shootEffecthit.play();
				}
			}
		}
	}

	public void render(Graphics g) {
		if (!isDamaged) { //caso não esteja sendo atacado
			if(dir == right_dir) {
				g.drawImage(rightPlayer[index], this.getX() -Camera.x, this.getY() -Camera.y, null);
				if(arma) {
					//Arma para direita
					g.drawImage(Entity.GUN_RIGHT, this.getX()+8 -Camera.x, this.getY()+2 -Camera.y, null);
				}
			}else if(dir == left_dir){
				g.drawImage(leftPlayer[index], this.getX() -Camera.x, this.getY() -Camera.y, null);
				if(arma) {
					//Arma para esquerda
					g.drawImage(Entity.GUN_LEFT, this.getX()-8 -Camera.x, this.getY() +2 -Camera.y, null);
				}
			}
			if(dir == up_dir) {
				g.drawImage(upPlayer[index], this.getX() -Camera.x, this.getY() -Camera.y, null);
				if(arma) {
					if (dirwelpon == left_dir) {
						g.drawImage(Entity.GUN_LEFT, this.getX()-8 -Camera.x, this.getY() +2 -Camera.y, null);
					}else{
						g.drawImage(Entity.GUN_RIGHT, this.getX()+8 -Camera.x, this.getY()+2 -Camera.y, null);
					}
				}
			}else if(dir == down_dir){
				g.drawImage(downPlayer[index], this.getX() -Camera.x, this.getY() -Camera.y, null);
				if(arma) {
					if (dirwelpon == left_dir) {
						g.drawImage(Entity.GUN_LEFT, this.getX()-8 -Camera.x, this.getY() +2 -Camera.y, null);
					}else{
						g.drawImage(Entity.GUN_RIGHT, this.getX()+8 -Camera.x, this.getY()+2 -Camera.y, null);
					}
				}
			}
		}else {
			g.drawImage(playerDamage, this.getX() -Camera.x, this.getY() -Camera.y, null);
			if(arma) {
				if (dirwelpon == left_dir) {
					g.drawImage(Entity.GUN_LEFT_WHILE, this.getX()-8 -Camera.x, this.getY() +2 -Camera.y, null);
				}else{
					g.drawImage(Entity.GUN_RIGHT_WHILE, this.getX()+8 -Camera.x, this.getY()+2 -Camera.y, null);
				}
			}
		}
	}
	
}
