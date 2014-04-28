package SQLite;

import java.util.ArrayList;

import org.apache.http.NameValuePair;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class SQLite 
{
	private static final String TAG = "SQLite Log";
	private static String NOMBRE_BASEDATOS = "Resultados";
	private static final int VERSION_BASEDATOS = 1;

	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;
	
	public SQLite(Context ctx)
	{
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}
	
	private static class DatabaseHelper extends SQLiteOpenHelper
	{
		DatabaseHelper(Context context)
		{
			super(context, NOMBRE_BASEDATOS, null, VERSION_BASEDATOS);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db)
		{
			String[] creacion_tablas = {"CREATE TABLE IF NOT EXISTS bonoloto(Fecha text PRIMARY KEY NOT NULL,R1 integer NOT NULL,R2 integer NOT NULL,R3 integer NOT NULL,R4 integer NOT NULL,R5 integer NOT NULL,R6 integer NOT NULL,C integer NOT NULL,R integer NOT NULL);",
										"CREATE TABLE IF NOT EXISTS botes(TSorteo text NOT NULL,Fecha text NOT NULL,BM integer NOT NULL,BC integer NOT NULL,PRIMARY KEY (TSorteo,Fecha));",
										"CREATE TABLE IF NOT EXISTS euromillon(Fecha text PRIMARY KEY NOT NULL,R1 integer NOT NULL,R2 integer NOT NULL,R3 integer NOT NULL,R4 integer NOT NULL,R5 integer NOT NULL,E1 integer NOT NULL,E2 integer NOT NULL);",
										"CREATE TABLE IF NOT EXISTS gordo(Fecha text PRIMARY KEY NOT NULL,R1 integer NOT NULL,R2 integer NOT NULL,R3 integer NOT NULL,R4 integer NOT NULL,R5 integer NOT NULL,Clave integer NOT NULL);",
										"CREATE TABLE IF NOT EXISTS lototurf(Fecha text PRIMARY KEY NOT NULL,R1 integer NOT NULL,R2 integer NOT NULL,R3 integer NOT NULL,R4 integer NOT NULL,R5 integer NOT NULL,R6 integer NOT NULL,C integer NOT NULL,Reint integer NOT NULL);",
										"CREATE TABLE IF NOT EXISTS nacional(Fecha text PRIMARY KEY NOT NULL,PrimerPremio text NOT NULL,Fraccion integer NOT NULL,Serie integer NOT NULL,SegundoPremio text NOT NULL,Reint1 integer NOT NULL,Reint2 integer NOT NULL,Reint3 integer NOT NULL);",
										"CREATE TABLE IF NOT EXISTS primitiva(Fecha text PRIMARY KEY NOT NULL,R1 integer NOT NULL,R2 integer NOT NULL,R3 integer NOT NULL,R4 integer NOT NULL,R5 integer NOT NULL,R6 integer NOT NULL,C integer NOT NULL,R integer NOT NULL,JoKer integer NOT NULL);",
										"CREATE TABLE IF NOT EXISTS quiniela(Fecha text PRIMARY KEY NOT NULL,R1 text NOT NULL,R2 text NOT NULL,R3 text NOT NULL,R4 text NOT NULL,R5 text NOT NULL,R6 text NOT NULL,R7 text NOT NULL,R8 text NOT NULL,R9 text NOT NULL,R10 text NOT NULL,R11 text NOT NULL,R12 text NOT NULL,R13 text NOT NULL,R14 text NOT NULL,R15 text NOT NULL,J integer NOT NULL);",
										"CREATE TABLE IF NOT EXISTS quinigol(Fecha text PRIMARY KEY NOT NULL,R1 text NOT NULL,R2 text NOT NULL,R3 text NOT NULL,R4 text NOT NULL,R5 text NOT NULL,R6 text NOT NULL);",
										"CREATE TABLE IF NOT EXISTS quintuple(Fecha text PRIMARY KEY NOT NULL,C1 integer NOT NULL,C2 integer NOT NULL,C3 integer NOT NULL,C4 integer NOT NULL,C5 integer NOT NULL,C5_2 integer NOT NULL);"};
			try 
			{
				/* CREAR TABLAS */
				for(int i=0; i<creacion_tablas.length; i++)
				{
					db.execSQL(creacion_tablas[i]);
				}
			}
			catch(Exception e) { Log.i("ERROR AL CREAR LA BBDD: ", e.getMessage()); }
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
			Log.w(TAG, "Actualizando base de datos de versión " + oldVersion + " a " + newVersion + ", lo que destruirá todos los viejos datos.");
			db.execSQL("DROP TABLE IF EXISTS contactos");
			onCreate(db);
		}
	}
	
	public void Conectar()
	{
		try { db = DBHelper.getWritableDatabase(); }
		catch (SQLException e) {Log.w(TAG, "Error conectando a la BDD Android.");}
	}

	public void Desconectar() 
	{
		DBHelper.close();
	}

	public Cursor ejecutarConsulta(String consulta){
		 Cursor cur=db.rawQuery(consulta, null);
		 return cur;
	}
	
	public void insertarResultado(String tabla, ArrayList<NameValuePair> valores){
		ContentValues valoresIniciales = new ContentValues();
		for(int i=0; i < valores.size(); i++){
			valoresIniciales.put(valores.get(i).getName(), (valores.get(i).getValue()));
		}
		db.insert(tabla, null, valoresIniciales);
	}
	
	public void borrarResultado(String sentencia){ejecutarSentencia(sentencia);}
	
	public void ejecutarSentencia(String sentencia){db.execSQL(sentencia);}

}
