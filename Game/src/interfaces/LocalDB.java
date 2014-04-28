package interfaces;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

public interface LocalDB 
{	
	public abstract void crearBaseDatos();
	
	public abstract void conectar ();
	
	public abstract void desconectar ();
	
	public abstract ArrayList<String[]> consultar(String consulta);

	public abstract void insertarResultado (String NombreTabla, ArrayList<NameValuePair> Resultado);
	
	public abstract void borrarResultado(String sentencia);
	
	public abstract void ejecutarSentencia(String sentencia);
}
