package logico;

import java.io.Serializable;
import java.time.LocalDate;

public abstract class Persona implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected String cedula;
	protected String nombre;
	protected String apellido;
	protected String telefono;
	protected String direccion;
	protected LocalDate fechaNacimiento;
	protected char sexo;

	protected Persona(String cedula, String nombre, String apellido, String telefono, String direccion,
			LocalDate fechaNacimiento, char sexo) {
		super();
		this.cedula = cedula;
		this.nombre = nombre;
		this.apellido = apellido;
		this.telefono = telefono;
		this.direccion = direccion;
		this.fechaNacimiento = fechaNacimiento;
		this.sexo = sexo;
	}

	// Constructor vacío agregado para permitir que las clases hijas, como Doctor y Paciente, puedan crearse desde datos obtenidos de MySQL y completar sus atributos mediante setters.
	public Persona() {
		super(); // Este super() llama a Object, que siempre existe
	}

	// Setter agregado para asignar la cédula al reconstruir objetos desde la base de datos.
	public void setCedula(String cedula) {
		this.cedula = cedula;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getApellido() {
		return apellido;
	}

	public void setApellido(String apellido) {
		this.apellido = apellido;
	}

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public String getDireccion() {
		return direccion;
	}

	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	public LocalDate getFechaNacimiento() {
		return fechaNacimiento;
	}

	public void setFechaNacimiento(LocalDate fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	public char getSexo() {
		return sexo;
	}

	public void setSexo(char sexo) {
		this.sexo = sexo;
	}

	public String getCedula() {
		return cedula;
	}

}
