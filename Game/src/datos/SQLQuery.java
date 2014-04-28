package datos;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.concurrent.Callable;


import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;



 
public class SQLQuery implements Callable<String>
{
	public static final int HTTP_TIMEOUT = 30 * 1000; // The time it takes for our client to timeout (milliseconds)
	private static HttpClient mHttpClient;
	private static String result;
	private static String consultaSQL;
	private static String nombreBBDD;
	
	//Constructor que recibe el string de la consultaSQL
	public SQLQuery (String ConsultaSQL, String nBD)
	{
		consultaSQL = ConsultaSQL;
		nombreBBDD = nBD;
	}
	
	//El metodo principal que se ejecuta cuando se crea el Objeto
	public String call() 
	{
		try { executeHttpPost(); } 
		catch (Exception e) { System.out.println("ERROR INESPERADO: "+ e.getMessage());	e.printStackTrace();}
		return result;
	}
	
	//Devuelve un objeto HttpClient con los parámetros de conexión
	private HttpClient getHttpClient() 
	{
		if (mHttpClient == null) 
		{
			mHttpClient = new DefaultHttpClient();
			final HttpParams params = mHttpClient.getParams();
			HttpConnectionParams.setConnectionTimeout(params, HTTP_TIMEOUT);
			HttpConnectionParams.setSoTimeout(params, HTTP_TIMEOUT);
			//ConnManagerParams.setTimeout(params, HTTP_TIMEOUT);
		}
		return mHttpClient;
	}

	//Realiza una petición POST a la url especificada, con los parámetros especificados.
	//Recibe la url y los parámetros
	//Devuelve el resultado de la petición.
	public void executeHttpPost() throws Exception 
	{
		BufferedReader in = null;
		try 
		{
			HttpClient client = getHttpClient();
			HttpPost request = new HttpPost(Constantes.DIRECCION_SCRIPT_CONEXION_SQL);
			
			// declarar los parametros que se enviaran al script
			ArrayList<NameValuePair> postParameters = new ArrayList<NameValuePair>();
			
			// Le pasamos la consulta como parametro
			postParameters.add(new BasicNameValuePair("bd", nombreBBDD));
			postParameters.add(new BasicNameValuePair("consulta", consultaSQL));
			
			UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);

			request.setEntity(formEntity);
			HttpResponse response = client.execute(request);
			
			in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			StringBuffer sb = new StringBuffer("");
			String line = "";
			String NL = System.getProperty("line.separator");
			
			while ((line = in.readLine()) != null) { sb.append(line + NL); }
			
			in.close();
			result = sb.toString();
		} 
		finally 
		{
			if (in != null) 
			{
				try { in.close(); } 
				catch (IOException e) { System.out.println("Error converting result "+e.toString()); e.printStackTrace(); }
			}
		}
	}

	 public String executeHttpGet(String url) throws Exception 
	 {
	
		 BufferedReader in = null;
		 try 
		 {
			 HttpClient client = getHttpClient();
			 HttpGet request = new HttpGet();
			 request.setURI(new URI(url));
			 HttpResponse response = client.execute(request);
			 in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
	 
			 StringBuffer sb = new StringBuffer("");
			 String line = "";
			 String NL = System.getProperty("line.separator");
	
			 while ((line = in.readLine()) != null) { sb.append(line + NL);}
			 in.close();
	
			 String result = sb.toString();
			 return result;
	
		 } 
		 finally 
		 {
			 if (in != null) 
			 {
				 try {in.close();} 
				 catch (IOException e) { System.out.println("Error converting result "+e.toString()); e.printStackTrace(); }
			 }
		 }
	 }
}