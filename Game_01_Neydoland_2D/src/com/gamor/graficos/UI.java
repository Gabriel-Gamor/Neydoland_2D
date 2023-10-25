package com.gamor.graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.gamor.entity.Player;
import com.gamor.main.Game;

public class UI { //interface do usuario que aparce por cima, vida e tals
	
	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(7, 3, 70,11);
		if (Game.player.life == 100) {
			g.setColor(Color.green);
			g.fillRect(7, 3, (int)((Game.player.life/Game.player.maxLife)*70),11);
			g.setColor(Color.white);
			g.setFont(new Font("arial", Font.BOLD,12));
			g.drawString(((int)(Game.player.life) + "/" + (int)(Game.player.maxLife)), 19, 13);
		}else if(Game.player.life != 100 && Game.player.life > 9){
			g.setColor(Color.green);
			g.fillRect(7, 3, (int)((Game.player.life/Game.player.maxLife)*70),11);
			g.setColor(Color.white);
			g.setFont(new Font("arial", Font.BOLD,12));
			g.drawString(((int)(Game.player.life) + "/" + (int)(Game.player.maxLife)), 23, 13);
		}else {
			g.setColor(Color.green);
			g.fillRect(7, 3, (int)((Game.player.life/Game.player.maxLife)*70),11);
			g.setColor(Color.white);
			g.setFont(new Font("arial", Font.BOLD,12));
			g.drawString(((int)(Game.player.life) + "/" + (int)(Game.player.maxLife)), 26, 13);
		}
	}
	
}
