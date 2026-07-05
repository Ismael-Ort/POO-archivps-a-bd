package logico;

import java.io.Serializable;
import java.time.LocalDate;

public class VacunaVieja implements Serializable{
    private static final long serialVersionUID = 1L;
	private String enfermedad;
	private LocalDate fecha;
	public VacunaVieja(String enfermedad, LocalDate fecha) {
		super();
		this.enfermedad = enfermedad;
		this.fecha = fecha;
	}
	public String getEnfermedad() {
		return enfermedad;
	}
	public void setEnfermedad(String enfermedad) {
		this.enfermedad = enfermedad;
	}
	public LocalDate getFecha() {
		return fecha;
	}
	public void setFecha(LocalDate fecha) {
		this.fecha = fecha;
	}

}
