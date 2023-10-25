package com.gamor.main;

import java.applet.Applet;
import java.applet.AudioClip;

@SuppressWarnings("removal")//não sei muito bem o porque mas o eclipse pediu, então botei
public class Sound {
	
	private AudioClip clip;
	
	public static final Sound musicBackground = new Sound("/musica.wav"); //dessa forma o som de fundo no game já vai está na variavel
	
	public static final Sound hurtEffect = new Sound("/hitHurtP.wav"); //pegando o som de hit no player
	public static final Sound hurtEffecthit = new Sound("/hitHurt.wav"); //pegando o som de hit no inimigo
	public static final Sound shootEffecthit = new Sound("/hurt.wav"); //pegando o som tiro
	public static final Sound lifeEffect = new Sound("/powerUpL.wav"); 
	public static final Sound bulletEffect = new Sound("/powerUpB.wav"); 
	public static final Sound gunEffect = new Sound("/powerUp.wav"); 

	//instanciar
	@SuppressWarnings("deprecation")//não sei muito bem o porque mas o eclipse pediu, então botei
	private Sound(String name) { 
		try {
			clip = Applet.newAudioClip(Sound.class.getResource(name)); //para colocar a som na variavel clip
		}catch(Throwable e){
		}
	}
	
	//metodos abaixo
	public void play() { //faz com que o som em questão toque apenas uma vez até seu fim
		try {
			new Thread() { //cria uma nova Thread para se tocar o som
				@SuppressWarnings("deprecation")//não sei muito bem o porque mas o eclipse pediu, então botei
				public void run() {
					clip.play();
				}
			}.start();
		}catch(Throwable e){
		}
	}
	
	public void loop() { //faz com que o som em questão toque varias vezes
		try {
			new Thread() { //cria uma nova Thread para se tocar o som
				@SuppressWarnings("deprecation")//não sei muito bem o porque mas o eclipse pediu, então botei
				public void run() {
					clip.loop();
				}
			}.start();
		}catch(Throwable e){
		}
	}
	
}
