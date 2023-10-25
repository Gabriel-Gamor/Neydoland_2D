package com.gamor.world;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

import com.gamor.entity.Bullet;
import com.gamor.entity.Enemy;
import com.gamor.entity.Entity;
import com.gamor.entity.Lifepack;
import com.gamor.entity.Player;
import com.gamor.entity.Weapon;
import com.gamor.graficos.Spritesheet;
import com.gamor.main.Game;

public class World {
	
	public static Tile[] tiles;
	public static int WIDTH,HEIGHT; //tamanho do mapa
	public static int TILE_SIZE = 16; //muitos locais se usa o tamanho do tile, entao criei uma variavel para ele, mas não alterei isso em alguns locais, deixando o 16 mesmo
	
	public World(String path) {//criação do mapa
		try {
			BufferedImage map = ImageIO.read(getClass().getResource(path)); //lendo map.png
			int[] pixels = new int[map.getWidth() * map.getHeight()]; //definindo o numero de pixels
			
			WIDTH = map.getWidth();
			HEIGHT = map.getHeight();
			
			tiles = new Tile[map.getWidth() * map.getHeight()]; //definindo os tiles
			
			map.getRGB(0, 0, map.getWidth(), map.getHeight(), pixels, 0, map.getWidth()); //metodo do BufferedImage para criar esse mapa
			
		//	for(int i = 0; i < pixels.length;i++ ) { 
		//		if (pixels[i] == 0xFFFF0000) {}  //um FF antes da cor em se que é a opacidade, que é a opacidade	
		//		}
			
			for(int xx = 0; xx < map.getWidth(); xx++) { //mapeando um mapa apartir do map.png feito
				for(int yy = 0; yy < map.getHeight(); yy++) {
					int pixelAtual = pixels[xx + (yy *map.getHeight())];
					
					tiles[xx + (yy*WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR);//por padrão floor, no caso chão
					if (pixelAtual == 0xFF000000) {
						//chão
						tiles[xx + (yy*WIDTH)] = new FloorTile(xx*16,yy*16,Tile.TILE_FLOOR);
					}else if (pixelAtual == 0xFFFFFCFD) {
						//parede
						WallTile wall = new WallTile(xx*16,yy*16,Tile.TILE_WALL);
						tiles[xx + (yy*WIDTH)] = wall;
					}
					
					else if (pixelAtual == 0xFF0033FF) {
						//player
						Game.player.setX(xx*16);
						Game.player.setY(yy*16);
					}else if (pixelAtual == 0xFFFF0000) {
						//enemy
						Enemy en = new Enemy(xx*16,yy*16,16,16,Entity.ENEMY_EN);
						Game.entities.add(en);
						Game.enemies.add(en);
					}
					
					else if (pixelAtual == 0xFFFFFF00) {
						//weapon
						Game.entities.add(new Weapon(xx*16,yy*16,16,16,Entity.WEAPON_EN));
					}else if (pixelAtual == 0xFF3EFF38) {
						//life pack
						Lifepack pack = new Lifepack(xx*16,yy*16,16,16,Entity.LIFEPACK_EN);
						pack.setMask(4, 7, 8, 8);
						Game.entities.add(pack);
						Game.lifepacks.add(pack);
					}else if (pixelAtual == 0xFFFFBD4C) {
						//bullet
						Bullet bullet = new Bullet(xx*16,yy*16,16,16,Entity.BULLET_EN);
						bullet.setMask(3, 7, 10, 9);
						Game.entities.add(bullet);
						Game.armos.add(bullet);
					}

				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static boolean isFree(int xnext, int ynext) { //verificando todas as posições proximas do player, para ver se tem parede
		int x1 = (xnext+1) / TILE_SIZE;
		int y1 = (ynext+1) / TILE_SIZE;
		
		int x2 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y2 = (ynext+1) / TILE_SIZE;
		
		int x3 = (xnext+1) / TILE_SIZE;
		int y3 = (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		int x4 = (xnext+TILE_SIZE-1) / TILE_SIZE;
		int y4 = (ynext+TILE_SIZE-1) / TILE_SIZE;
		
		return !((tiles[x1 + (y1*World.WIDTH)] instanceof WallTile) || 
				     (tiles[x2 + (y2*World.WIDTH)] instanceof WallTile) || 
				     (tiles[x3 + (y3*World.WIDTH)] instanceof WallTile) ||
				     (tiles[x4 + (y4*World.WIDTH)] instanceof WallTile));
	} // como está verificando se em qualquer lugar ao redor do player tem uma parece, então , temas que inverter com o !, para indicar qunado não tem nada
	
	public static void restartgame(String level) {
		//limpa todas as listas e cria novas listas, e refaz tudo, reseta o jogo
		Game.entities.clear();
		Game.enemies.clear();
		Game.lifepacks.clear();
		Game.armos.clear();
		Game.entities = new ArrayList<Entity>();
		Game.enemies = new ArrayList<Enemy>();
		Game.lifepacks = new ArrayList<Entity>();
		Game.armos = new ArrayList<Entity>();
		Game.spritesheet = new Spritesheet("/spritesheet.png");
		Game.player = new Player(0,0,16,16,Game.spritesheet.getSprite(32, 32, 16, 16));
		Game.entities.add(Game.player);
		Game.world = new World("/"+level);//reiniciar tudo, só que no local dependendo do level
		return;
	}
	
	public void render(Graphics g){
		
		//para renderizar apenas o que está sendo visto
		int xstart = Camera.x >> 4; //tem que dividir pelo tamanho do tile
		int ystart = Camera.y >> 4;
		// >> 4 é igual a dividir por 16, como um shift left, casas para esquerda
		int xfinal = xstart + (Game.WIDTH >> 4); //Game.WIDTH / 16 é a quantantidade tile que cabe na tela
		int yfinal = ystart + (Game.HEIGHT >> 4); 
		
		//mapeando um mapa apartir do map.png feito
		
	//	for(int xx = 0; xx < WIDTH; xx++) { 
	//		for(int yy = 0; yy < HEIGHT; yy++) {   forma facil sem otimisar , deixando aparece só o que aparece
		
		for(int xx = xstart; xx <= xfinal; xx++) {  //isso é de forma otimizada, para não carregar o mapa que não está visivel
			for(int yy = ystart; yy <= yfinal; yy++) { //caso, seja um a mais ou um a menos, o jogo vai carregar errado o que tem na tela, deixando parte sem mapa
				if(xx < 0 || yy < 0 || xx >= WIDTH || yy >= HEIGHT) { //para evitar que tenha um indice negativo, para evitar que tenha confitlos
					continue;
					}
				Tile tile = tiles[xx + (yy*WIDTH)];
				tile.render(g);;
				}
		}
	}
	
}
