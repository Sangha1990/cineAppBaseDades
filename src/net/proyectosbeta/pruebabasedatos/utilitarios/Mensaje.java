package net.proyectosbeta.pruebabasedatos.utilitarios;

import android.content.Context;
import android.widget.Toast;


public class Mensaje {
	
	private Context contexto;
	
	public Mensaje(Context contexto){
		this.contexto = contexto;
	}
	
	
	public Context getContexto(){
		return this.contexto;
	}
	
	
	public void mostrarMensajeCorto(String mensaje){
		Toast.makeText(getContexto(), mensaje, Toast.LENGTH_SHORT).show();
	}
	
	
	public void mostrarMensajeLargo(String mensaje){
		Toast.makeText(getContexto(), mensaje, Toast.LENGTH_LONG).show();
	}
}