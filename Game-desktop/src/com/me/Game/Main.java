package com.me.Game;

import interfaces.ActivityLauncher;

import UI.GeneraTablas;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.me.Game.Loto;

public class Main implements ActivityLauncher
{
	public static void main(String[] args) {
		LwjglApplicationConfiguration cfg = new LwjglApplicationConfiguration();
		cfg.title = "Loterias ASG";
		cfg.useGL20 = false;
		cfg.width = 480;
		cfg.height = 800;
		cfg.vSyncEnabled = true;
		//cfg.fullscreen = true;
		//cfg.foregroundFPS = 5000;
		
		new LwjglApplication(new Loto(new Main()), cfg);
	}

	@Override
	public void abrirLectorCodigoBarras() 
	{
		GeneraTablas.generarAlerta("Función no disponible en la versión de Escritorio", false);
	}

	@Override
	public void abrirNavegador() 
	{
		GeneraTablas.generarAlerta("Función no disponible en la versión de Escritorio", false);	
	}
}
