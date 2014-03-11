package net.infobosccoma.pruebabasedatos;

import java.io.Serializable;

public class Pelicula implements Serializable {

	private int id;
	private String nomPeli;
	private String director;
	private String dataEstrena;
	private String ruta_imagen;
	

	public Pelicula() {
	}

	public Pelicula(String p_nomPeli, String p_director, String p_dataEstrena,
			String p_ruta_imagen) {
		setNomPeli(p_nomPeli);
		setDirector(p_director);
		setDataEstrena(p_dataEstrena);
		setRutaImagen(p_ruta_imagen);
	}

	public Pelicula(int p_id, String p_nomPeli, String p_director,
			String p_edad, String p_ruta_imagen) {
		setId(p_id);
		setNomPeli(p_nomPeli);
		setDirector(p_director);
		setDataEstrena(p_edad);
		setRutaImagen(p_ruta_imagen);
	}

	public String getNomPeli() {
		return nomPeli;
	}

	public void setNomPeli(String nomPeli) {
		this.nomPeli = nomPeli;
	}

	public String getDirector() {
		return this.director;
	}

	public void setDirector(String director) {
		this.director = director;
	}

	public String getDataEstrena() {
		return dataEstrena;
	}

	public void setDataEstrena(String dataEstrena) {
		this.dataEstrena = dataEstrena;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getRutaImagen() {
		return this.ruta_imagen;
	}

	public void setRutaImagen(String ruta_imagen) {

		if (ruta_imagen == null) {
			this.ruta_imagen = "No tiene imagen.";
		} else {
			this.ruta_imagen = ruta_imagen;
		}
	}
}