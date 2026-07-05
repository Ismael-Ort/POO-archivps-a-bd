package logico;

import java.io.Serializable;
import java.time.LocalDate;

public class RegistroVacuna implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Vacuna vacuna;
	private LocalDate fechaAplicacion;
	private String aplicadaPor;

	public RegistroVacuna(Vacuna vacuna, LocalDate fechaAplicacion, String numeroLote, String aplicadaPor
			) {
		super();
		this.vacuna = vacuna;
		this.fechaAplicacion = fechaAplicacion;
		this.aplicadaPor = aplicadaPor;
	}

	public Vacuna getVacuna() {
		return vacuna;
	}

	public void setVacuna(Vacuna vacuna) {
		this.vacuna = vacuna;
	}

	public LocalDate getFechaAplicacion() {
		return fechaAplicacion;
	}

	public void setFechaAplicacion(LocalDate fechaAplicacion) {
		this.fechaAplicacion = fechaAplicacion;
	}

	public String getAplicadaPor() {
		return aplicadaPor;
	}

	public void setAplicadaPor(String aplicadaPor) {
		this.aplicadaPor = aplicadaPor;
	}


}
