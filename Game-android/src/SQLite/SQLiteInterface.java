package SQLite;

import interfaces.LocalDB;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.content.Context;
import android.database.Cursor;

public class SQLiteInterface implements LocalDB
{
	Context c; //ATENCION!!!! CONTEXTOOO!!!
	SQLite db = null;

	public void crearBaseDatos() {db = new SQLite(c);}
    
    public void conectar() {db.Conectar();}
    
    public void desconectar() {db.Desconectar();}
    
    public ArrayList<String[]> consultar(String consulta)
    {
    	Cursor c = db.ejecutarConsulta(consulta); //Ejecuta la consulta y devuelve un cursor (resultset)
    	
    	ArrayList<String[]> resultado = new ArrayList<String[]>(); //Este arraylist contiene una serie de arrays de strings. Cada array de Strings es una fila de resultados
    	
    	int columnas = c.getColumnCount(); //Obtener el numero de columnas
    	String[] fila = new String[columnas];        	
    	if (c.moveToFirst()) 
    	{
    	     //Recorremos el cursor hasta que no haya mas registros
    	     do 
    	     {
    	    	 //Recorremos la fila hasta que no haya mas columnas
    	    	 for(int x = 0; x<columnas; x++){
    	    		 try{
    	    			 fila[x] = c.getString(x);
    	    		 }catch(Exception e){
    	    			 fila[x] = String.valueOf(c.getInt(x));
    	    		 }
    	    	 }
    	    	 resultado.add(fila);
    	     } while (c.moveToNext());
    	}
    	return resultado;
    }
    
    public void insertarResultado(String tabla, ArrayList<NameValuePair> parameters)
    {
    	db.insertarResultado("bonoloto", parameters);
    }

    public void borrarResultado(String sentencia) {db.borrarResultado(sentencia);}
    
    public void ejecutarSentencia(String sentencia) {db.ejecutarSentencia(sentencia);}
}
