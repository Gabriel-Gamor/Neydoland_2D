package com.gamor.main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import com.gamor.entity.BulletShoot;
import com.gamor.entity.Enemy;
import com.gamor.entity.Entity;
import com.gamor.entity.Player;
import com.gamor.graficos.Spritesheet;
import com.gamor.graficos.UI;
import com.gamor.world.World;

public class Game extends Canvas implements Runnable,KeyListener,MouseListener{
	
	private static final long serialVersionUID = 1L;
	public static JFrame frame; //do proprio java para criar janela
	private Thread thread;
	private boolean isRunning = false;
	public static final int WIDTH = 240; // é final para assim não poder mudar o valor da variavel   //tamanho da guia do jogo
	public static final int HEIGHT = 160; 
	public static final int SCALE = 3; // foi declarado a largura e altuda acima e aqui é a escala,, escalapara ver quanto almenta
	
	private BufferedImage image;
	
	private static int CUR_LEVEL = 1, MAX_LEVEL = 3;//nivel atual e maior nivel
	
	public static int getCUR_LEVEL() {
		return CUR_LEVEL;
	}
	public void setCUR_LEVEL(int cUR_LEVEL) {
		CUR_LEVEL = cUR_LEVEL;
	}

	public static List<Entity> entities; //é uma lista, pois vai ter varias entidades, não importa se é player ou outra, o que vai mudar é a ação que cada um vai fazer
	public static List<Entity> lifepacks;
	public static List<Entity> armos;
	public static List<Enemy> enemies;
	public static List<BulletShoot> bullets;
	
	public static Spritesheet spritesheet;
	public static World world;
	public static Player player;
	public static Random rand; 
	public Menu menu; 
	
	public UI ui;
	
	public static String gameState = "Menu";//estado do game, muitooo importate isso, se normal é para aconcetecer tudo, gameover tela de game over, menu fica no menu
	
	//para nos animarmos lá na frente
	private boolean showMassageGameOver = false;
	private int framesGameOver = 0;
	
	private boolean restartGame = false;
	
	public Game() {
		//Musica
		//Sound.musicBackground.loop();//a musica vai ficar rodando de fundo, em loop
		
		rand = new Random();
		
		addKeyListener(this);//nessa classe que vai estar os eventos do teclado
		addMouseListener(this);//nessa classe que vai estar os eventos do mouse
		
		setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE)); //definindo o tamanho da janela com a biblioteca Dimension
		initFrame();
		//Inicializando objetos
		ui = new UI();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
		entities = new ArrayList<Entity>();
		enemies = new ArrayList<Enemy>();
		lifepacks = new ArrayList<Entity>();
		armos = new ArrayList<Entity>();
		bullets = new ArrayList<BulletShoot>();
		spritesheet = new Spritesheet("/spritesheet.png");
		
		player = new Player(0,0,16,16,spritesheet.getSprite(32, 32, 16, 16));
		entities.add(player);		//que coisa linda, fica facinho com tudo terceirizado ks
		
		world = new World("/level1.png"); //iniciando mapa do jogo, por padrão level 1
		
		menu = new Menu();
	}
	
	
	public void initFrame() {
		frame = new JFrame("Game 1"); //tambem da para nomear o titulo ao instanciar o JFrame
		frame.add(this); //adicionando o Canva a janela, pegando todas as proriedades daqui
		frame.setResizable(false);//para não poder redimencionar
		frame.pack(); //para calcular certo as dimenções
		frame.setLocationRelativeTo(null); //para centralizar a janela
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//para quando fecha a janela, encerrar o processo
		frame.setVisible(true);//para qunado inicializar ficar visivel
	}
	
	
	public synchronized void start() {
		thread = new Thread(this); //nessa mesma classe
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop() {
		isRunning = false;
		try {
			thread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void main(String args[]) {
		Game game = new Game();
		game.start();
	}

	
	public void tick() { //logica de jogo
		
		if (gameState == "Normal") {//se estatus do game for de normal
			
			this.restartGame = false; //uma prevenção para que caso o jogador aperter enter no meio da partida, não aconteça nada
		
			for(int i = 0; i < entities.size(); i++) {//todas as entidades tem uma logica que precia está rodando
				Entity e = entities.get(i);
				/*
				if(e instanceof Player) { //verifica se a calsse atual rodando é do tipo player
					//Estou dando tick no player
				}*/
				e.tick();
			}
		
			for(int i = 0; i < bullets.size(); i++) {//atualizações das balas
				bullets.get(i).tick();
			}
		
			if (enemies.size()==0) {//qunado todos os inimigos forem mortos
				//System.out.println("Proximo level"); //teste
				//Avança para o proximo level
				CUR_LEVEL++;//Avança um nivel
				if (CUR_LEVEL > MAX_LEVEL) {
					//significa que chegamos no final do jogo,, no caso vou ta reiniciando para a faze 1
					CUR_LEVEL = 1;
				}
				String newWorld = "level"+CUR_LEVEL+".png"; //para assim usar de forma dinamica para escolher o arquivo de mundo, ex: level1.png
				World.restartgame(newWorld);
			}
			
		}else if(gameState == "GameOver") { //Normal é normal
			//System.out.println("GameOver"); //player morreu
			
			//isso para animarmors a mensagen de enter
			this.framesGameOver ++;
			if (this.framesGameOver == 30) {
				this.framesGameOver = 0;
				if (this.showMassageGameOver) { //se ta verdade vira false e o inverso tambem
					this.showMassageGameOver = false;
				}else {
					this.showMassageGameOver = true;
				}
			}
			
			if (restartGame) {
				this.gameState = "Normal";
				CUR_LEVEL = 1;
				String newWorld = "level"+CUR_LEVEL+".png"; //para assim usar de forma dinamica para escolher o arquivo de mundo, ex: level1.png
				World.restartgame(newWorld);
			}
			
		}else if(gameState == "Menu") {
			menu.tick();
		}
		
	}
	
	public void render() {//renderiza o jogo
		BufferStrategy bs = this.getBufferStrategy(); //criando buffes para otimizar a renderização,,, proficinal e perfirmace
		if (bs == null) { //se bs == null então não exite um BufferStrategy, e abaixo ira criar um
			this.createBufferStrategy(3);//entre 2 ou 3, mas 3 é reconmendado
			return; // quebra o metodo aqui, para qunado voltar a chamar o metodo, já ter os buffes
		}
		Graphics g = image.getGraphics(); //no lugar de pegar os graficos do BufferStrategy, agora pega o tamnho da image
		g.setColor(new Color(0,0,0));//seta uma cor
		g.fillRect(0, 0, WIDTH, HEIGHT);//cria um retangulo, os dois primeiros é a posição, e os dois ultimos o tamanho
		
		//Renderização do jogo
		world.render(g);
		for(int i = 0; i < entities.size(); i++) {//todas as entidades tem uma rederização que precia está rodando
			Entity e = entities.get(i);
			e.render(g);
		}
		
		for(int i = 0; i < bullets.size(); i++) {//renderizando as balas
			bullets.get(i).render(g);
		}
		
		ui.render(g);
		
		/**/
		g.dispose(); //metodo para otimisar o jogo, ele limpa dados que tem na tela que não serão mais utilizados
		g = bs.getDrawGraphics();//primeiro foi desenhado na image e depois é que desenhamos na tela
		g.drawImage(image, 0, 0,WIDTH*SCALE, HEIGHT*SCALE, null); //imprime a imagem que foi feita com os o tamanho almentado pela escala
		
		//imprimir por cima de tudo sem ter o almento de scala, pora evitar da letra ficar ruim
		g.setFont(new Font("arial", Font.BOLD,20));
		g.setColor(new Color(221,129,0));
		g.drawString("Munição: " + player.armo,20,63);
		
		//imprimir por cima de tudo sem ter o almento de scala, pora evitar da letra ficar ruim
		g.setFont(new Font("arial", Font.BOLD,30));
		g.setColor(new Color(255,0,55));
		g.drawString("Score: " + player.score,237,37);
		
		//renderizar acima de tudo isso
		if (gameState == "GameOver") {//se o stato do jogo for de game over
			Graphics2D g2 = (Graphics2D) g; //mudando o que o g é
			g2.setColor(new Color(0,0,0,100)); //seta uma cor com opacidade
			g2.fillRect(0, 0, WIDTH*SCALE, HEIGHT*SCALE);//cria um retangulo na tela inteira, para fazer o efeito de escurecer da tela ao morrer
			
			g.setFont(new Font("arial", Font.BOLD,40));
			g.setColor(Color.WHITE);
			g.drawString("Morreu",((WIDTH*SCALE)/2)-77,((HEIGHT*SCALE)/2+5));
			
			g.setFont(new Font("arial", Font.BOLD,25));
			g.setColor(Color.WHITE);
			if (showMassageGameOver) {
				g.drawString("<Click 'Enter' para reiniciar>",((WIDTH*SCALE)/2)-167,((HEIGHT*SCALE)/2)+40);
			}
		}else if(gameState == "Menu") {
			menu.render(g);
		}
		
		bs.show();; // para mostra os graficos
	}
	
	public void run() {
		long lastTime = System.nanoTime(); //pega o ultimo momento em nanoTime
		double amountOfTicks = 60.0; //define a constante para definir o valor do fps
		double ns = 1000000000 / amountOfTicks; //calculo para  pegar o momeno certo que a gente deve fazer o update do jogo
		double delta = 0;
		int frames = 0;
		double timer = System.currentTimeMillis(); // peguei o tempo em milesegundo para medir os frames
		
		requestFocus();//para assim que abrir o jogo o foco ir para a aba aberta
		
		while (isRunning) {
			long now = System.nanoTime(); //pega o ultimo momento em nanoTime
			delta+= (now - lastTime) / ns; //subritai o tempo atual pela ultima vez, acontece certo intervalo, dividindo por ns, se for igual a 1, deu um segundo certinho
			lastTime = now; //last time recebe now e volta o loop
			if(delta >= 1) { //Game loop mais avançado
				tick(); // logica
				render(); // grafico
				frames++; // isso é para medir os frames
				delta--; // reseta o delta
			}
			if(System.currentTimeMillis() - timer >= 1000) { // para verificar se o FPS esta correto no previsto
				System.out.println("FPS = "+frames);
				frames = 0;
				timer+=1000;
			}
		}
		stop(); //não é obrigado a fazer, mas é para caso de algum problema é para as threads relacionadas a esse programa pararem pararem, caso aconteça algum conflit e tals
	}


	@Override
	public void keyTyped(KeyEvent e) {
		
	}


	@Override
	public void keyPressed(KeyEvent e) { //ao apertar
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
			
			//pro menu
			if(gameState == "Menu") {
				menu.up = true;
			}
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
			
			//pro menu
			if(gameState == "Menu") {
				menu.down = true;
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_X || e.getKeyCode() == KeyEvent.VK_J) {
			player.shoot = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.restartGame = true;
			
			//pro menu
			if(gameState == "Menu") {
				menu.enter = true;
			}
		}
		
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			Game.gameState = "Menu"; //pausa do jogo
			menu.pause = true;
		}
	}


	@Override
	public void keyReleased(KeyEvent e) {//ao soltar
		if(e.getKeyCode() == KeyEvent.VK_RIGHT || e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT || e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}
		if(e.getKeyCode() == KeyEvent.VK_UP || e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN || e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
		}
	}


	@Override
	public void mouseClicked(MouseEvent e) {
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		player.mouseshoot = true;
		player.mx = (e.getX()/3);
		player.my = (e.getY()/3);
		//System.out.println(player.mx);  //teste
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}
}
