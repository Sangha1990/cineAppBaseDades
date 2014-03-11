package net.infobosccoma.pruebabasedatos;

import java.io.File;

import net.proyectosbeta.pruebabasedatos.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class EditarPeliculaActivity extends Activity {

	private Button butonLimpiar;
	private Button butonGuardar;
	private EditText editTextNomPeli;
	private EditText editTextDirector;
	private EditText editTextDataEstrena;
	private DatabaseHandler baseDatos;
	private Bundle extras;
	private Button botonImagenPelicula;
	private ImageView imagenPelicula;
	private Mensaje mensaje;

	
	private String ruta_imagen; 
	private int SELECCIONAR_IMAGEN = 237487;
	

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editar_pelicula);


		butonGuardar = (Button) findViewById(R.id.botonGuardar);
		butonLimpiar = (Button) findViewById(R.id.botonLimpiar);
		editTextNomPeli = (EditText) findViewById(R.id.editTextNomPeli);
		editTextDirector = (EditText) findViewById(R.id.editTextDirector);
		editTextDataEstrena = (EditText) findViewById(R.id.editTextDataEstrena);
		botonImagenPelicula = (Button) findViewById(R.id.botonAgregarImagenPelicula);
		imagenPelicula = (ImageView) findViewById(R.id.imagenPelicula);


		mensaje = new Mensaje(getApplicationContext());


		botonImagenPelicula.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				ventanaImagen();
			}
		});
		
		extras = getIntent().getExtras();

		if (estadoEditarPelicula()) {
			editTextNomPeli.setText(extras.getString("nomPeli"));
			editTextDirector.setText(extras.getString("director"));
			editTextDataEstrena.setText(extras.getString("dataEstrena"));
			

			ruta_imagen = extras.getString("ruta_imagen");
			Bitmap bitmap = getBitmap(ruta_imagen);

			
			if (bitmap.getHeight() >= 2048 || bitmap.getWidth() >= 2048) {
				bitmap = Bitmap.createScaledBitmap(
						bitmap,
						(bitmap.getHeight() >= 2048) ? 2048 : bitmap
								.getHeight(),
						(bitmap.getWidth() >= 2048) ? 2048 : bitmap
								.getWidth(), true);
				
			}
			imagenPelicula.setImageBitmap(bitmap);
		}

		
		butonGuardar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				if (verificarCampoNomPeli() && verificarCampoDirector()
						&& verificarCampoDataEstrena()) {
					if (estadoEditarPelicula()) {
						editarPelicula();
					} else {
						insertarNuevoPelicula();
					}
				
					finish();
				} else {
					if (editTextNomPeli.getText().toString().equals("")) {
						mensaje.mostrarMensajeCorto("Introduzca un Nombre de Peicula!!!");
					}
					if (editTextDirector.getText().toString().equals("")) {
						mensaje.mostrarMensajeCorto("Introduzca un Nombre del Director!!!");
					}
					if (editTextDataEstrena.getText().toString().equals("")) {
						mensaje.mostrarMensajeCorto("Introduzca una Data de Estrena!!!");
					}
				}
			}
		});

		
		butonLimpiar.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				limpiarCampos();
			}
		});
	}


	private void limpiarCampos() {
		editTextNomPeli.setText("");
		editTextDirector.setText("");
		editTextDataEstrena.setText("");
	}

	
	private boolean verificarCampoNomPeli() {
		if (editTextNomPeli.getText().toString().equals("")) {
			return false;
		}
		return true;
	}


	private boolean verificarCampoDirector() {
		if (editTextDirector.getText().toString().equals("")) {
			return false;
		}
		return true;
	}


	private boolean verificarCampoDataEstrena() {
		if (editTextDataEstrena.getText().toString().equals("")) {
			return false;
		}
		return true;
	}

	
	private void insertarNuevoPelicula() {
		baseDatos = new DatabaseHandler(EditarPeliculaActivity.this);

		try {
		
			Pelicula pelicula = new Pelicula(editTextNomPeli.getText().toString(),
					editTextDirector.getText().toString(), editTextDataEstrena
							.getText().toString(), ruta_imagen);

			
			baseDatos.insertarPelicula(pelicula);
		} catch (Exception e) {
			mensaje.mostrarMensajeCorto("Error al insertar!!!");
			e.printStackTrace();
		} finally {
		
			baseDatos.cerrar();
		}
	}


	private void editarPelicula() {
		baseDatos = new DatabaseHandler(EditarPeliculaActivity.this);

		try {
		
			int id = extras.getInt("id");

			Pelicula pelicula = new Pelicula(id, editTextNomPeli.getText()
					.toString(), editTextDirector.getText().toString(),
					editTextDataEstrena.getText().toString(), ruta_imagen);

			baseDatos.actualizarRegistros(id, pelicula.getNomPeli(),
					pelicula.getDirector(), pelicula.getDataEstrena(),
					pelicula.getRutaImagen());
			mensaje.mostrarMensajeCorto("Se cambio correctamente el registro!!!");
		} catch (Exception e) {
			mensaje.mostrarMensajeCorto("Error al querer editar un registro!!!");
			e.printStackTrace();
		} finally {
			baseDatos.cerrar();
		}
	}


	public boolean estadoEditarPelicula() {
		
		if (extras != null) {
			return true;
		} else {
			return false;
		}
	}


	private void ventanaImagen() {
		try {
			final CharSequence[] items = { "Seleccionar de la galería" };

			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Seleccionar una foto");
			builder.setItems(items, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {
					switch (item) {
						case 0:
							Intent intentSeleccionarImagen = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.INTERNAL_CONTENT_URI);
							intentSeleccionarImagen.setType("image/*");
							startActivityForResult(intentSeleccionarImagen, SELECCIONAR_IMAGEN);
							break;
						}
				}
			});
			AlertDialog alert = builder.create();
			alert.show();
		} catch (Exception e) {
			mensaje.mostrarMensajeCorto("El error es: " + e.getMessage());
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		try {
			if (requestCode == SELECCIONAR_IMAGEN) {
				if (resultCode == Activity.RESULT_OK) {
					Uri selectedImage = data.getData();
					ruta_imagen = obtieneRuta(selectedImage);
					Bitmap bitmap = getBitmap(ruta_imagen);

					
					if (bitmap.getHeight() >= 2048 || bitmap.getWidth() >= 2048) {
						bitmap = Bitmap.createScaledBitmap(
								bitmap,
								(bitmap.getHeight() >= 2048) ? 2048 : bitmap
										.getHeight(),
								(bitmap.getWidth() >= 2048) ? 2048 : bitmap
										.getWidth(), true);
						imagenPelicula.setImageBitmap(bitmap);
					} else {
						imagenPelicula.setImageURI(selectedImage);
					}
				}
			}
		} catch (Exception e) {
		}

	}

	private Bitmap getBitmap(String ruta_imagen) {
		// Objetos.
		File imagenArchivo = new File(ruta_imagen);
		Bitmap bitmap = null;

		if (imagenArchivo.exists()) {
			bitmap = BitmapFactory.decodeFile(imagenArchivo.getAbsolutePath());
		}
		return bitmap;
	}


	private String obtieneRuta(Uri uri) {
		String[] projection = { android.provider.MediaStore.Images.Media.DATA };
		Cursor cursor = managedQuery(uri, projection, null, null, null);
		int column_index = cursor
				.getColumnIndexOrThrow(android.provider.MediaStore.Images.Media.DATA);
		cursor.moveToFirst();
		return cursor.getString(column_index);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			Intent intent = new Intent(this, MainActivity.class);
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(intent);

			
			EditarPeliculaActivity.this.finish();

			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
}
