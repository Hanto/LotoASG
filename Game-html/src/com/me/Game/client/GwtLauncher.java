package com.me.Game.client;


import interfaces.ActivityLauncher;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.me.Game.Loto;


public class GwtLauncher extends GwtApplication implements ActivityLauncher
{
	@Override
	public GwtApplicationConfiguration getConfig () {
		GwtApplicationConfiguration cfg = new GwtApplicationConfiguration(480, 320);
		return cfg;
	}

	@Override
	public ApplicationListener getApplicationListener () 
	{
		return new Loto(new GwtLauncher());
	}

	@Override
	public void abrirLectorCodigoBarras() 
	{
		// TODO Auto-generated method stub
		
	}

	@Override
	public void abrirNavegador() 
	{
		// TODO Auto-generated method stub
		
	}
}