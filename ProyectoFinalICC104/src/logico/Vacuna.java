package logico;

import java.io.Serializable;
import java.util.Date;

public class Vacuna implements Serializable {

	private static final long serialVersionUID = 1L;

	private String codigoVacuna;
	private String numeroLote;
	private String nombre;
	private int cantidad;
	private Date fechaCaducidad;
	private String laboratorio; // CORREGIDO: de laboratio a laboratorio
	private String enfermedad; // Esto indica contra qué enfermedad protege la vacuna
	private boolean activa;

	public Vacuna() {
		super();
	}

	public Vacuna(String codigoVacuna, String numeroLote, String nombre, int cantidad, Date fechaCaducidad,
			boolean activa, String laboratorio, String enfermedad) {
		super();
		this.codigoVacuna = codigoVacuna;
		this.numeroLote = numeroLote;
		this.nombre = nombre;
		this.cantidad = cantidad;
		this.fechaCaducidad = fechaCaducidad;
		this.activa = activa;
		this.laboratorio = laboratorio; // CORREGIDO
		this.enfermedad = enfermedad;
	}

	// Getters y Setters
	public String getCodigoVacuna() {
		return codigoVacuna;
	}

	public void setCodigoVacuna(String codigoVacuna) {
		this.codigoVacuna = codigoVacuna;
	}

	public String getNumeroLote() {
		return numeroLote;
	}

	public void setNumeroLote(String numeroLote) {
		this.numeroLote = numeroLote;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public int getCantidad() {
		return cantidad;
	}

	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}

	public Date getFechaCaducidad() {
		return fechaCaducidad;
	}

	public void setFechaCaducidad(Date fechaCaducidad) {
		this.fechaCaducidad = fechaCaducidad;
	}

	public boolean isActiva() {
		return activa;
	}

	public void setActiva(boolean activa) {
		this.activa = activa;
	}

	// CORREGIDO: Cambiado de getLaboratio a getLaboratorio
	public String getLaboratorio() {
		return laboratorio;
	}

	// CORREGIDO: Cambiado de setLaboratio a setLaboratorio
	public void setLaboratorio(String laboratorio) {
		this.laboratorio = laboratorio;
	}

	public String getEnfermedad() {
		return enfermedad;
	}

	public void setEnfermedad(String enfermedad) {
		this.enfermedad = enfermedad;
	}

	public void activar() {
		this.activa = true;
	}

	public void desactivar() {
		this.activa = false;
	}

	// Método para verificar si está caducada
	public boolean estaCaducada() {
		if (fechaCaducidad == null)
			return false;
		return fechaCaducidad.before(new Date());
	}

	// Método para verificar si tiene stock disponible
	public boolean tieneStock() {
		return cantidad > 0;
	}
}