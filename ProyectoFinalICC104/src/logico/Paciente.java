package logico;

import java.time.LocalDate;
import java.util.ArrayList;

public class Paciente extends Persona {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String codigoPaciente;
	private String tipoSangre;
	private ArrayList<Alergia> alergias;
	private LocalDate fechaRegistro;
	private HistoriaClinica historiaClinica;
	private ArrayList<RegistroVacuna> registrosVacunas;
	private ArrayList<VacunaVieja> vacunasViejas;
	private boolean activo;
	private float peso;
	private float estatura;
	private String doctorRegistrador;

	public Paciente(String cedula, String nombre, String apellido, String telefono, String codigoPaciente,
			String doctorRegistrador) {
		super(cedula, nombre, apellido, telefono, "", LocalDate.now(), 'M');
		this.codigoPaciente = codigoPaciente;
		this.tipoSangre = "";
		this.alergias = new ArrayList<>();
		this.fechaRegistro = LocalDate.now();
		this.historiaClinica = new HistoriaClinica(this, this.fechaRegistro);
		this.registrosVacunas = new ArrayList<>();
		this.vacunasViejas = new ArrayList<>();
		this.activo = true;
		this.peso = 0f;
		this.estatura = 0f;
		this.doctorRegistrador = doctorRegistrador;
	}

	/**
	 * Constructor simplificado para crear pacientes interesados (Solo con datos
	 * básicos desde agendar cita)
	 */
	public Paciente(String cedula, String nombre, String apellido, String telefono, String codigoPaciente) {
		this(cedula, nombre, apellido, telefono, codigoPaciente, null);
	}

	public String getDoctorRegistrador() {
		return doctorRegistrador;
	}

	public void setDoctorRegistrador(String doctorRegistrador) {
		this.doctorRegistrador = doctorRegistrador;
	}

	public String getTipoSangre() {
		return tipoSangre;
	}

	public void setTipoSangre(String tipoSangre) {
		this.tipoSangre = tipoSangre;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public String getCodigoPaciente() {
		return codigoPaciente;
	}

	public ArrayList<Alergia> getAlergias() {
		return alergias;
	}

	public LocalDate getFechaRegistro() {
		return fechaRegistro;
	}

	public HistoriaClinica getHistoriaClinica() {
		return historiaClinica;
	}

	public ArrayList<RegistroVacuna> getRegistrosVacunas() {
		return registrosVacunas;
	}

	public void agregarAlergia(Alergia alergia) {

		alergias.add(alergia);

	}

	public boolean eliminarAlergia(Alergia alergia) {

		if (alergia != null && alergias.contains(alergia)) {

			alergias.remove(alergia);
			return true;
		}

		return false;

	}

	public void registrarVacuna(RegistroVacuna registroVacuna) {

		registrosVacunas.add(registroVacuna);
	}

	public boolean tieneVacuna(Vacuna vacuna) {

		for (RegistroVacuna registroVacuna : registrosVacunas) {

			if (registroVacuna.getVacuna().equals(vacuna)) {
				return true;
			}

		}

		return false;
	}

	public ArrayList<Vacuna> obtenerVacunasFaltantes(ArrayList<Vacuna> vacunasActivas) {

		ArrayList<Vacuna> faltantes = new ArrayList<>();

		for (Vacuna vacunaActiva : vacunasActivas) {

			boolean tieneVacuna = false;

			for (RegistroVacuna registro : registrosVacunas) {

				if (registro.getVacuna().getCodigoVacuna().equals(vacunaActiva.getCodigoVacuna())) {
					tieneVacuna = true;
				}

			}

			if (tieneVacuna == false) {

				faltantes.add(vacunaActiva);

			}

		}

		return faltantes;

	}

	public void setAlergias(ArrayList<Alergia> alergias) {
		this.alergias = alergias;
	}

	public float getPeso() {
		return peso;
	}

	public void setPeso(float peso) {
		this.peso = peso;
	}

	public float getEstatura() {
		return estatura;
	}

	public void setEstatura(float estatura) {
		this.estatura = estatura;
	}

	public void setCodigoPaciente(String codigoPaciente) {
		this.codigoPaciente = codigoPaciente;
	}

	public ArrayList<VacunaVieja> getVacunasViejas() {
		return vacunasViejas;
	}

	public void setVacunasViejas(ArrayList<VacunaVieja> vacunasViejas) {
		this.vacunasViejas = vacunasViejas;
	}

	public void crearVacunaVieja(VacunaVieja vacu) {
		vacunasViejas.add(vacu);
	}

}
