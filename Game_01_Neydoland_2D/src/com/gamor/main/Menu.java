package com.gamor.main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Menu {
	
	public String[] options = {"novo jogo","carregar jogo","sair"};
	
	public int currentOption = 0;
	public int maxOpition = options.length - 1; //o menos 1 é porque começa em 0
	
	public boolean up,down,enter;
	
	public boolean pause = false;
	
	public void tick () {
		
		//navegando pelo menu
		if(up) {
			up = false;
			currentOption --;
			if (currentOption<0) {
				currentOption = maxOpition;
			}
		}
		if(down) {
			down = false;
			currentOption ++;
			if (currentOption>maxOpition) {
				currentOption = 0;
			}
		}
		
		//acionado opções do menu
		if(enter) {
			enter = false;
			if(options[currentOption] == "novo jogo" || options[currentOption] == "continuar") {
				Game.gameState = "Normal";
				//Musica
				//Sound.musicBackground.loop();//a musica vai ficar rodando de fundo, em loop
				pause = false;
			}else if(options[currentOption] == "carregar jogo") {
				
			}else if(options[currentOption] == "sair") {
				System.exit(1);
			}
		}
	}
	
	public void render(Graphics g1) {
		Graphics2D g = (Graphics2D) g1;
		g.setColor(new Color(0,0,0,100));
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE, Game.HEIGHT*Game.SCALE); //quadradão na tela inteira
		
		//Nome do jogo
		g.setColor(Color.BLUE);
		g.setFont(new Font("arial", Font.BOLD,50));
		g.drawString("Neydoland",((Game.WIDTH*Game.SCALE)/2)-116,((Game.HEIGHT*Game.SCALE)/2-100));
		
		//opções
		g.setColor(Color.white);
		g.setFont(new Font("arial", Font.BOLD,25));
		
		if (!pause) {g.drawString("Novo Jogo",((Game.WIDTH*Game.SCALE)/2)-45,((Game.HEIGHT*Game.SCALE)/2-5));
		}else {g.drawString("Resumir",((Game.WIDTH*Game.SCALE)/2)-35,((Game.HEIGHT*Game.SCALE)/2-5));} //distinguir de pause e menuninicial
		g.drawString("Carregar Jogo",((Game.WIDTH*Game.SCALE)/2)-67,((Game.HEIGHT*Game.SCALE)/2+28));
		g.drawString("Sair",((Game.WIDTH*Game.SCALE)/2)-10,((Game.HEIGHT*Game.SCALE)/2+64));
		
		if(options[currentOption] == "novo jogo" || options[currentOption] == "continuar") {
			g.setColor(Color.red);
			if (!pause) {g.drawString("Novo Jogo",((Game.WIDTH*Game.SCALE)/2)-45,((Game.HEIGHT*Game.SCALE)/2-5));
			}else {g.drawString("Resumir",((Game.WIDTH*Game.SCALE)/2)-35,((Game.HEIGHT*Game.SCALE)/2-5));} //distinguir de pause e menuninicial
		}else if(options[currentOption] == "carregar jogo") {
			g.setColor(Color.red);
			g.drawString("Carregar Jogo",((Game.WIDTH*Game.SCALE)/2)-67,((Game.HEIGHT*Game.SCALE)/2+28));
		}else if(options[currentOption] == "sair") {
			g.setColor(Color.red);
			g.drawString("Sair",((Game.WIDTH*Game.SCALE)/2)-10,((Game.HEIGHT*Game.SCALE)/2+64));
		}
		//System.out.println(options[currentOption]); //teste
	}
}
