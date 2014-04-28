package com.me.Game;

import interfaces.ActivityLauncher;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import datos.Constantes;

public class MainActivity extends AndroidApplication implements ActivityLauncher
{
	protected AdView adView;
	
	private final int SHOW_ADS = 1;
	private final int HIDE_ADS = 0;
	
	/*
	protected Handler handler = new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what)
			{
				case SHOW_ADS: { adView.setVisibility(View.VISIBLE); break; }
				case HIDE_ADS: { adView.setVisibility(View.GONE); break; }
			}
		}
	};*/
	
    @Override
    public void onCreate(Bundle savedInstanceState) 
    {
        super.onCreate(savedInstanceState);  
        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useGL20 = false;
        
        //Crear el layout
        RelativeLayout layout = new RelativeLayout(this);
        
        //Configurar manualmente el initialize()
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                
        //Crear la View de libgdx
        View gameView = initializeForView(new Loto(this), cfg);
                
        //Crear y configurar la View de Admob
        adView = new AdView(this, AdSize.SMART_BANNER, "ca-app-pub-0229403835501528/7502294591");
        adView.loadAd(new AdRequest());
                
        //Agregar la View libgdx
        layout.addView(gameView);
                
        //Agregar la View AdMob
        RelativeLayout.LayoutParams adParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        adParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
        
        layout.addView(adView, adParams);
        
        //Mostrarlo todo
        setContentView(layout);
    }

    public void showAds(boolean show)
    {
    	handler.sendEmptyMessage(show ? SHOW_ADS : HIDE_ADS);
    }
    
	@Override
	public void abrirLectorCodigoBarras() 
	{
		Intent intent = new Intent(this, ComprobarBoleto.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}

	@Override
	public void abrirNavegador() 
	{
		Intent navegador = new Intent(Intent.ACTION_VIEW, Uri.parse(Constantes.TIENDA_ASG));
		startActivity(navegador);
	}
   
}