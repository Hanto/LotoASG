package com.me.Game;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;

public class ComprobarBoleto extends Activity
{	
	public static String numeroCodigoBarras = "-1";
	public static String resultadoCodigoBarras = "";
	
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
	    StrictMode.setThreadPolicy(policy);
	    
		new IntentIntegrator(this).initiateScan();		//Llamar al escaner
	    //numeroCodigoBarras = "50663010010429327976"; 	//para hacer pruebas
	    //consultarCodigoBarras();						//para hacer pruebas
	}
	
	//El siguiente metodo se llama cuando ya se ha escaneado el boleto.
	protected void onActivityResult(int requestCode, int resultCode, Intent data) 
	{
		try
		{
			switch(requestCode) 
			{
				case IntentIntegrator.REQUEST_CODE: { if (resultCode != RESULT_CANCELED)	{ IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
																							  if (scanResult != null) { numeroCodigoBarras = scanResult.getContents(); consultarCodigoBarras(); }
																							} 
													  break; }
			}
		} catch (Exception e) { numeroCodigoBarras = "-1"; }
	}
	
	protected void consultarCodigoBarras ()
	{
		
		if (numeroCodigoBarras.length()>=16)
		{	
			String id_sorteo = numeroCodigoBarras.substring(1,4);
			String numero = numeroCodigoBarras.substring(11, 16);
	
			BoletoHttpClient handler = new BoletoHttpClient();
			
			try { resultadoCodigoBarras = handler.executeHttpGet(id_sorteo, numero); } 
			catch (Exception e) { resultadoCodigoBarras = "ERROR: Codigo Ininteligible"; }
		}
		else resultadoCodigoBarras = "ERROR: Codigo Erroneo";
		
		resultadoCodigoBarras = resultadoCodigoBarras.replaceAll("\\n", "");
		
		UI.GeneraTablas.generarAlerta(resultadoCodigoBarras, true);
		
		//Se cierra la activity del Scaner
		finish();
		//Se cierra la activity del Comprobar Boleto
		finish();
	}
}
