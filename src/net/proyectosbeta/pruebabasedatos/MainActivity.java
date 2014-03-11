package net.proyectosbeta.pruebabasedatos;

import net.proyectosbeta.pruebabasedatos.basedatos.DatabaseHandler;
import net.proyectosbeta.pruebabasedatos.modelos.Pelicula;
import net.proyectosbeta.pruebabasedatos.utilitarios.ImagenAdapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends Activity {
	
	private DatabaseHandler baseDatos;
	private ImagenAdapter cursorAdapter;
	private ListView listViewPelicula;
	private Button botonAgregarPelicula;

	
	private int CODIGO_RESULT_EDITAR_PELICULA = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_main);

		
		listViewPelicula = (ListView) findViewById(R.id.listViewPelicula);
		botonAgregarPelicula = (Button) findViewById(R.id.botonAgregarPelicula);

		
		botonAgregarPelicula.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				
				editarPelicula(0);
			}
		});

		
		recuperarTodasPelicula();

		
		registerForContextMenu(listViewPelicula);
	}

	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		super.onCreateContextMenu(menu, v, menuInfo);
		android.view.MenuInflater inflater = getMenuInflater();
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
		inflater.inflate(R.menu.opciones_pelicula, menu);
	}

	
	@Override
	public boolean onContextItemSelected(android.view.MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item
				.getMenuInfo();

		switch (item.getItemId()) {
		case R.id.menu_contextual_editar_pelicula:
			editarPelicula((int) info.id);
			return true;
		case R.id.menu_contextual_eliminar_pelicula:
			eliminarPelicula((int) info.id);
			recuperarTodasPelicula();
			return true;
		default:
			return super.onContextItemSelected((android.view.MenuItem) item);
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
		;
	}

	private void recuperarTodasPelicula() {
		try {
			baseDatos = new DatabaseHandler(this);

			Cursor cursor = baseDatos.obtenerTodasPelicula();

			String[] from = new String[] { "nomPeli", "director", "dataEstrena",
					"ruta_imagen" };

			int[] to = new int[] { R.id.TextViewNomPeli, R.id.TextViewDirector,
					R.id.TextViewDataEstrena, R.id.thumb_pelicula, };
			cursorAdapter = new ImagenAdapter(this, cursor, from, to);
			listViewPelicula.setAdapter(cursorAdapter);
		} catch (Exception e) {
			Log.d("Error", "El mensaje de error es: " + e.getMessage());
		} finally {
			baseDatos.cerrar();
		}
	}

	public void editarPelicula(int p_id) {
		if (p_id == 0) {
			Intent actividad_editarPelicula = new Intent(MainActivity.this,
					EditarPeliculaActivity.class);
			startActivityForResult(actividad_editarPelicula,
					CODIGO_RESULT_EDITAR_PELICULA);
		} else {
			Pelicula pelicula;

			try {
				pelicula = baseDatos.getPelicula(p_id);

				Intent actividad_editarPelicula = new Intent(this,
						EditarPeliculaActivity.class);

				actividad_editarPelicula.putExtra("id", p_id);
				actividad_editarPelicula.putExtra("nomPeli", pelicula.getNomPeli());
				actividad_editarPelicula.putExtra("director",
						pelicula.getDirector());
				actividad_editarPelicula.putExtra("dataEstrena", pelicula.getDataEstrena());
				actividad_editarPelicula.putExtra("ruta_imagen",
						pelicula.getRutaImagen());

				startActivityForResult(actividad_editarPelicula,
						CODIGO_RESULT_EDITAR_PELICULA);
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						"Error al editar pelicula!!!", Toast.LENGTH_SHORT)
						.show();
				e.printStackTrace();
			} finally {
				baseDatos.cerrar();
			}
		}
	}

	
	private void eliminarPelicula(int id_pelicula) {

		AlertDialog.Builder mensaje_dialogo = new AlertDialog.Builder(this);

		final int v_id_pelicula = id_pelicula;

		mensaje_dialogo.setTitle("Importante");
		mensaje_dialogo.setMessage("Estas seguro de eliminar esta pelicula?");
		mensaje_dialogo.setCancelable(false);
		mensaje_dialogo.setPositiveButton("Confirmar",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogo1, int id) {
						try {
							baseDatos.eliminaPelicula(v_id_pelicula);

							recuperarTodasPelicula();
						} catch (Exception e) {
							Toast.makeText(getApplicationContext(),
									"Error al eliminar!!!", Toast.LENGTH_SHORT)
									.show();
							e.printStackTrace();
						} finally {
							baseDatos.cerrar();
						}
					}
				});
		mensaje_dialogo.setNegativeButton("Cancelar",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialogo1, int id) {
						recuperarTodasPelicula();
					}
				});
		mensaje_dialogo.show();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode,
			Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		recuperarTodasPelicula();
	}
}