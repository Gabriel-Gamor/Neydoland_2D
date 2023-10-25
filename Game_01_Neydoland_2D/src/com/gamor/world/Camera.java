package com.gamor.world;

public class Camera {
	
	public static int x;
	public static int y;
	
	public static int clamp(int Atual, int Min, int Max) { //para que a camera não saia do mapa que foi feito
		
		if (Atual < Min) {
			Atual = Min;
		}
		if (Atual > Max) {
			Atual = Max;
		}
		return Atual;
	}
	
}
