package net.proyectosbeta.pruebabasedatos.basedatos;

import net.proyectosbeta.pruebabasedatos.modelos.Pelicula;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHandler extends SQLiteOpenHelper {

	private static String RUTA_BASE_DATOS = "/data/data/net.proyectosbeta.pruebabasedatos/databases/";

	private static String NOMBRE_BASE_DATOS = "prueba";

	private static final int VERSION_BASE_DATOS = 1;

	//private SQLiteDatabase base_datos;

	private Context contexto;

	private String SENTENCIA_SQL_CREAR_BASE_DATOS_PELICULA = "CREATE TABLE if not exists pelicula (_id INTEGER PRIMARY KEY autoincrement, "
			+ "nomPeli TEXT, director TEXT, dataEstrena TEXT, ruta_imagen TEXT)";

	public DatabaseHandler(Context context) {
		super(context, NOMBRE_BASE_DATOS, null, VERSION_BASE_DATOS);
		this.contexto = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(SENTENCIA_SQL_CREAR_BASE_DATOS_PELICULA);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

		db.execSQL("DROP TABLE IF EXISTS Pelicula");

		db.execSQL(SENTENCIA_SQL_CREAR_BASE_DATOS_PELICULA);
	}

	public void insertarPelicula(Pelicula pelicula) {
		ContentValues valores = new ContentValues();
		valores.put("nomPeli", pelicula.getNomPeli());
		valores.put("director", pelicula.getDirector());
		valores.put("dataEstrena", pelicula.getDataEstrena());
		valores.put("ruta_imagen", pelicula.getRutaImagen());
		this.getWritableDatabase().insert("Pelicula", null, valores);
	}

	public void actualizarRegistros(int id, String nomPeli, String director,
			String dataEstrena, String ruta_imagen) {
		ContentValues actualizarDatos = new ContentValues();
		actualizarDatos.put("nomPeli", nomPeli);
		actualizarDatos.put("director", director);
		actualizarDatos.put("dataEstrena", dataEstrena);
		actualizarDatos.put("ruta_imagen", ruta_imagen);
		String where = "_id=?";
		String[] whereArgs = new String[] { String.valueOf(id) };

		try {
			this.getReadableDatabase().update("Pelicula", actualizarDatos,
					where, whereArgs);
		} catch (Exception e) {
			String error = e.getMessage().toString();
		}
	}

	public Pelicula getPelicula(int p_id) {
		String[] columnas = new String[] { "_id", "nomPeli", "director",
				"dataEstrena", "ruta_imagen" };
		Cursor cursor = this.getReadableDatabase().query("Pelicula", columnas,
				"_id" + "= " + p_id, null, null, null, null);

		if (cursor != null) {
			cursor.moveToFirst();
		}

		Pelicula pelicula = new Pelicula(Integer.parseInt(cursor.getString(0)),
				cursor.getString(1), cursor.getString(2), cursor.getString(3),
				cursor.getString(4));

		return pelicula;
	}

	public void cerrar() {
		this.close();
	}

	public Cursor obtenerTodasPelicula() {
		String[] columnas = new String[] { "_id", "nomPeli", "director",
				"dataEstrena", "ruta_imagen" };
		Cursor cursor = this.getReadableDatabase().query("Pelicula", columnas,
				null, null, null, null, null);

		if (cursor != null) {
			cursor.moveToFirst();
		}
		return cursor;
	}

	public boolean eliminaPelicula(long id) {
		return this.getWritableDatabase().delete("Pelicula", "_id" + "=" + id,
				null) > 0;
	}
}