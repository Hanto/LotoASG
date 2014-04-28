package com.me.Game;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.os.AsyncTask;

public class BoletoHttpClient
{
	public String executeHttpGet(String sorteo, String numero) throws Exception 
	{
		String url = "http://loteriasreunidas.com/resultados/premiador/webcomprobador.php?" + "sorteo=" + sorteo + "&" + "numero=" + numero;
	    String resultado = new MiTarea().execute(url).get();
	    return resultado;
	}
	
	private class MiTarea extends AsyncTask<String, Void, String>
	{
        protected void onPreExecute() {}
        
        protected String doInBackground(String... url) 
        {
        	 BufferedReader in = null;
        	 String resultado = "ERROR: ";
             try 
             {
                 HttpClient client = new DefaultHttpClient();
                 HttpGet request = new HttpGet();
                 request.setURI (new URI(url[0]));
                 HttpResponse response = client.execute(request);
                 in = new BufferedReader (new InputStreamReader(response.getEntity().getContent()));
                 StringBuffer sb = new StringBuffer("");
                 String line = "";
                 String NL = System.getProperty("line.separator");
                 while ((line = in.readLine()) != null) 
                 {
                     sb.append(line + NL);
                 }
                 in.close();
                 resultado = sb.toString();    
             } 
             catch (URISyntaxException e) { resultado = resultado + e.getMessage(); } 
             catch (ClientProtocolException e) { resultado = resultado + e.getMessage(); } 
             catch (IOException e) { resultado = resultado + e.getMessage(); }
             finally
             {
            	 if (in != null) 
                 {
                     try { in.close(); } 
                     catch (IOException e) { e.printStackTrace();}
                 }
             }
             return resultado;
        }
	}
}
