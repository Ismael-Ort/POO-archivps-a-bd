package logico;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Clinica implements Serializable {

	private static final long serialVersionUID = 1L;

	private static Clinica instance = null;

	private String nombreClinica;
	private String direccion;
	private String telefono;
	private ArrayList<Paciente> pacientes;
	private ArrayList<Doctor> doctores;
	private ArrayList<Enfermedad> enfermedades;
	private ArrayList<Vacuna> vacunas;
	private ArrayList<Tratamiento> tratamientos;
	private ArrayList<Cita> citas;
	private ArrayList<Alergia> alergias;
	private ArrayList<Paciente> interesados;
	private boolean primerIngresp = false;
	private HashMap<String, Integer> reporteEnfermedades;

	public int contadorPacientes = 1;
	public int contadorDoctores = 1;
	public int contadorCitas = 1;
	public int contadorConsultas = 1;
	public int contadorTratamientos = 1;
	public int contadorEnfermedades = 1;
	public int contadorVacunas = 1;

	private static final int DURACION_CITA_MINUTOS = 45;

	private Clinica() {
		super();
		this.nombreClinica = "Clinica Los tres mosqueteros";
		this.direccion = "";
		this.telefono = "";
		this.pacientes = new ArrayList<>();
		this.doctores = new ArrayList<>();
		this.enfermedades = new ArrayList<>();
		this.vacunas = new ArrayList<>();
		this.tratamientos = new ArrayList<>();
		this.citas = new ArrayList<>();
		this.alergias = new ArrayList<>();
		this.interesados = new ArrayList<>();
		PersistenciaManager.cargarDatos();
		setarContadores();
		cargarReporteEnfermedades();///////////////////////////////

	}

	public static Clinica getInstance() {
		if (instance == null) {
			instance = new Clinica();
		}
		return instance;
	}

	public static int getDuracionCitaMinutos() {
		return DURACION_CITA_MINUTOS;
	}

	public static void setInstance(Clinica instance) {
		Clinica.instance = instance;
	}

	public String getNombreClinica() {
		return nombreClinica;
	}

	public String getDireccion() {
		return direccion;
	}

	public String getTelefono() {
		return telefono;
	}

	public ArrayList<Paciente> getPacientes() {
		return pacientes;
	}

	public ArrayList<Doctor> getDoctores() {
		return doctores;
	}

	public ArrayList<Enfermedad> getEnfermedades() {
		return enfermedades;
	}

	public ArrayList<Vacuna> getVacunas() {
		return vacunas;
	}

	public ArrayList<Tratamiento> getTratamientos() {
		return tratamientos;
	}

	public ArrayList<Cita> getCitas() {
		return citas;
	}

	public HashMap<String, Integer> getReporteEnfermedades() {
		return reporteEnfermedades;
	}


	public void recalcularContadorTratamientos() {
		this.contadorTratamientos = tratamientos.size() + 1;
	}

	// doctores

	public boolean registrarDoctor(Doctor doctor) {
		if (doctor == null) {
			return false;
		}

		// Validar cÃ©dula duplicada
		if (isCedulaRegistrada(doctor.getCedula())) {
			return false;
		}

		// Validar licencia duplicada
		if (isLicenciaRegistrada(doctor.getNumeroLicencia())) {
			return false;
		}

		doctores.add(doctor);
		contadorDoctores++;
		PersistenciaManager.guardarDatos();
		return true;
	}

	public Doctor buscarDoctorPorCedula(String cedula) {
		for (Doctor doctor : doctores) {
			if (doctor.getCedula().equals(cedula)) {
				return doctor;
			}
		}
		return null;
	}

	public Doctor buscarDoctorPorCodigo(String codigo) {
		for (Doctor doctor : doctores) {
			if (doctor.getCodigoDoctor().equals(codigo)) {
				return doctor;
			}
		}
		return null;
	}


	public Doctor buscarDoctorPorNumeroLicencia(String numeroLicencia) {
		if (numeroLicencia == null || numeroLicencia.trim().isEmpty()) {
			return null;
		}

		String licBuscada = numeroLicencia.trim();

		for (Doctor d : doctores) { // usa el nombre real de tu lista de doctores
			if (d != null && d.getNumeroLicencia() != null && d.getNumeroLicencia().equalsIgnoreCase(licBuscada)) {
				return d;
			}
		}
		return null;
	}


	public void recalcularContadorDoctores() {
		this.contadorDoctores = doctores.size() + 1; // ajusta doctores al nombre real
	}

	public boolean modificarDoctor(Doctor doctorActualizado) {
		if (doctorActualizado == null) {
			return false;
		}

		for (Doctor doctor : doctores) {
			if (doctor.getCedula().equals(doctorActualizado.getCedula())) {
				// Actualizar todos los atributos
				doctor.setNombre(doctorActualizado.getNombre());
				doctor.setApellido(doctorActualizado.getApellido());
				doctor.setTelefono(doctorActualizado.getTelefono());
				doctor.setDireccion(doctorActualizado.getDireccion());
				doctor.setFechaNacimiento(doctorActualizado.getFechaNacimiento());
				doctor.setSexo(doctorActualizado.getSexo());
				doctor.setEspecialidad(doctorActualizado.getEspecialidad());
				doctor.setCitasPorDia(doctorActualizado.getCitasPorDia());
				doctor.setHorarioInicio(doctorActualizado.getHorarioInicio());
				doctor.setHorarioFin(doctorActualizado.getHorarioFin());
				doctor.setActivo(doctorActualizado.isActivo());
				return true;
			}
		}

		return false;
	}

	public ArrayList<Doctor> listarDoctores() {
		return doctores;
	}

	public ArrayList<Doctor> listarDoctoresPorEspecialidad(String especialidad) {
		ArrayList<Doctor> doctoresFiltrados = new ArrayList<>();

		for (Doctor doctor : doctores) {
			if (doctor.getEspecialidad().equalsIgnoreCase(especialidad)) {
				doctoresFiltrados.add(doctor);
			}
		}

		return doctoresFiltrados;
	}

	public ArrayList<Doctor> listarDoctoresActivos() {
		ArrayList<Doctor> doctoresActivos = new ArrayList<>();

		for (Doctor doctor : doctores) {
			if (doctor.isActivo()) {
				doctoresActivos.add(doctor);
			}
		}

		return doctoresActivos;
	}

	// pacientes

	public boolean registrarPaciente(Paciente paciente) {
		if (paciente == null) {
			return false;
		}

		if (isCedulaRegistrada(paciente.getCedula())) {
			return false;
		}

		for (Paciente pacienteExistente : pacientes) {
			if (pacienteExistente.getCedula().equals(paciente.getCedula())) {
				return false;
			}
		}
		pacientes.add(paciente);
		contadorPacientes++;
		return true;
	}

	public Paciente buscarPacientePorCedula(String cedula) {
		for (Paciente paciente : pacientes) {
			if (paciente.getCedula().equals(cedula)) {
				return paciente;
			}
		}
		return null;
	}

	public Paciente buscarPacientePorCodigo(String codigo) {
		for (Paciente paciente : pacientes) {
			if (paciente.getCodigoPaciente().equals(codigo)) {
				return paciente;
			}
		}
		return null;
	}

	public boolean verificarSiEsPaciente(String cedula) {
		for (Paciente paciente : pacientes) {
			if (paciente.getCedula().equals(cedula)) {
				return true;
			}
		}
		return false;
	}

	public boolean modificarPaciente(Paciente pacienteActualizado) {
		if (pacienteActualizado == null) {
			return false;
		}

		for (Paciente paciente : pacientes) {
			if (paciente.getCedula().equals(pacienteActualizado.getCedula())) {

				paciente.setNombre(pacienteActualizado.getNombre());
				paciente.setApellido(pacienteActualizado.getApellido());
				paciente.setTelefono(pacienteActualizado.getTelefono());
				paciente.setDireccion(pacienteActualizado.getDireccion());
				paciente.setFechaNacimiento(pacienteActualizado.getFechaNacimiento());
				paciente.setSexo(pacienteActualizado.getSexo());
				paciente.setTipoSangre(pacienteActualizado.getTipoSangre());
				paciente.setActivo(pacienteActualizado.isActivo());
				paciente.setAlergias(pacienteActualizado.getAlergias());
				return true;
			}
		}

		return false;
	}

	public ArrayList<Paciente> listarPacientes() {
		return pacientes;
	}

	public ArrayList<Paciente> listarPacientesActivos() {
		ArrayList<Paciente> pacientesActivos = new ArrayList<>();

		for (Paciente paciente : pacientes) {
			if (paciente.isActivo()) {
				pacientesActivos.add(paciente);
			}
		}

		return pacientesActivos;
	}

	// enfermedades

	public boolean registrarEnfermedad(Enfermedad enfermedad) {
		if (enfermedad == null) {
			return false;
		}

		for (Enfermedad enfermedadExistente : enfermedades) {
			if (enfermedadExistente.getCodigoEnfermedad().equals(enfermedad.getCodigoEnfermedad())) {
				return false;
			}
		}
		enfermedades.add(enfermedad);
		contadorEnfermedades++;
		return true;
	}

	public Enfermedad buscarEnfermedadPorCodigo(String codigo) {
		for (Enfermedad enfermedad : enfermedades) {
			if (enfermedad.getCodigoEnfermedad().equals(codigo)) {
				return enfermedad;
			}
		}
		return null;
	}

	public Enfermedad buscarEnfermedadPorNombre(String nombre) {
		for (Enfermedad enfermedad : enfermedades) {
			if (enfermedad.getNombre().equalsIgnoreCase(nombre)) {
				return enfermedad;
			}
		}
		return null;
	}

	public boolean activarVigilanciaEnfermedad(String codigoEnfermedad) {
		Enfermedad enfermedad = buscarEnfermedadPorCodigo(codigoEnfermedad);

		if (enfermedad != null) {
			enfermedad.activarVigilancia();
			return true;
		}

		return false;
	}

	public boolean desactivarVigilanciaEnfermedad(String codigoEnfermedad) {
		Enfermedad enfermedad = buscarEnfermedadPorCodigo(codigoEnfermedad);

		if (enfermedad != null) {
			enfermedad.desactivarVigilancia();
			return true;
		}

		return false;
	}

	public ArrayList<Enfermedad> listarEnfermedades() {
		return enfermedades;
	}

	public ArrayList<Enfermedad> listarEnfermedadesVigiladas() {
		ArrayList<Enfermedad> enfermedadesVigiladas = new ArrayList<>();

		for (Enfermedad enfermedad : enfermedades) {
			if (enfermedad.isBajoVigilancia()) {
				enfermedadesVigiladas.add(enfermedad);
			}
		}

		return enfermedadesVigiladas;
	}

	public boolean esEnfermedadVigilada(String nombreEnfermedad) {
		for (Enfermedad enfermedad : enfermedades) {

			if (enfermedad.getNombre().equalsIgnoreCase(nombreEnfermedad)) {
				return enfermedad.isBajoVigilancia();
			}
		}
		return false;
	}

	// vacunas

	public boolean agregarVacuna(Vacuna vacuna) {
		if (vacuna == null) {
			return false;
		}

		for (Vacuna vacunaExistente : vacunas) {
			if (vacunaExistente.getCodigoVacuna().equals(vacuna.getCodigoVacuna())) {
				return false;
			}
		}

		vacunas.add(vacuna);
		contadorVacunas++;
		return true;
	}

	public Vacuna buscarVacunaPorCodigo(String codigo) {
		for (Vacuna vacuna : vacunas) {
			if (vacuna.getCodigoVacuna().equals(codigo)) {
				return vacuna;
			}
		}
		return null;
	}

	public boolean activarVacuna(String codigoVacuna) {
		Vacuna vacuna = buscarVacunaPorCodigo(codigoVacuna);

		if (vacuna != null) {
			vacuna.activar();
			return true;
		}

		return false;
	}

	public boolean desactivarVacuna(String codigoVacuna) {
		Vacuna vacuna = buscarVacunaPorCodigo(codigoVacuna);

		if (vacuna != null) {
			vacuna.desactivar();
			return true;
		}

		return false;
	}

	public ArrayList<Vacuna> listarVacunas() {
		return vacunas;
	}

	public ArrayList<Vacuna> listarVacunasActivas() {
		ArrayList<Vacuna> vacunasActivas = new ArrayList<>();

		for (Vacuna vacuna : vacunas) {
			if (vacuna.isActiva()) {
				vacunasActivas.add(vacuna);
			}
		}

		return vacunasActivas;
	}

	// tratamientos

	// tratamientos

	public boolean agregarTratamiento(Tratamiento tratamiento) {
		if (tratamiento == null) {
			return false;
		}

		for (Tratamiento tratamientoExistente : tratamientos) {
			if (tratamientoExistente.getCodigoTratamiento().equals(tratamiento.getCodigoTratamiento())) {
				return false;
			}
		}

		tratamientos.add(tratamiento);
		contadorTratamientos++;
		return true;
	}

	public Tratamiento buscarTratamientoPorCodigo(String codigo) {
		for (Tratamiento tratamiento : tratamientos) {
			if (tratamiento.getCodigoTratamiento().equals(codigo)) {
				return tratamiento;
			}
		}
		return null;
	}

	// << NUEVO >>

	public ArrayList<Tratamiento> listarTratamientos() {
		return tratamientos;
	}

	// cistas

	public Cita agendarCita(Paciente paciente, String cedulaDoctor, LocalDate fecha, LocalTime hora, String motivo) {

		if (fecha.isBefore(LocalDate.now())) {

			return null;

		}

		if (paciente == null) {
			return null; // Paciente no existe
		}

		Doctor doctor = buscarDoctorPorCedula(cedulaDoctor);
		if (doctor == null) {
			return null; // Doctor no existe
		}

		if (!verificarDisponibilidadDoctor(cedulaDoctor, fecha, hora)) {
			return null; // Doctor no disponible a esa hora
		}

		int citasDelDia = contarCitasDelDia(cedulaDoctor, fecha);
		if (citasDelDia >= doctor.getCitasPorDia()) {
			return null; // Doctor ya llegÃƒÆ’Ã‚Â³ al lÃƒÆ’Ã‚Â­mite de citas
		}

		if (!doctor.puedeAtenderEnHorario(hora)) {
			return null; // Fuera del horario laboral
		}

		// Todo OK, crear la cita
		String codigoCita = ("CIT-" + contadorCitas);
		Cita nuevaCita = new Cita(codigoCita, paciente, doctor, fecha, hora, motivo);
		citas.add(nuevaCita);
		contadorCitas++;
		return nuevaCita;
	}

	public boolean verificarDisponibilidadDoctor(String cedulaDoctor, LocalDate fecha, LocalTime hora) {
		Doctor doctor = buscarDoctorPorCedula(cedulaDoctor);
		if (doctor == null) {
			return false;
		}

		// Verificar que la hora estÃƒÆ’Ã‚Â© dentro del horario del doctor
		if (!doctor.puedeAtenderEnHorario(hora)) {
			return false;
		}

		// Calcular hora de fin de la nueva cita
		LocalTime horaFinNuevaCita = hora.plusMinutes(DURACION_CITA_MINUTOS);

		// Verificar que no haya conflictos con otras citas
		for (Cita cita : citas) {
			boolean mismoDoctorFecha = cita.getDoctor().getCedula().equals(cedulaDoctor)
					&& cita.getFechaCita().equals(fecha);
			boolean citaValida = !cita.getEstadoCita().equals("Cancelada");

			if (mismoDoctorFecha && citaValida) {
				LocalTime horaInicioCitaExistente = cita.getHoraCita();
				LocalTime horaFinCitaExistente = horaInicioCitaExistente.plusMinutes(DURACION_CITA_MINUTOS);

				// Verificar solapamiento de horarios
				boolean haySolapamiento = (hora.isBefore(horaFinCitaExistente)
						&& horaFinNuevaCita.isAfter(horaInicioCitaExistente));

				if (haySolapamiento) {
					return false;
				}
			}
		}

		return true;
	}

	// Obtiene una lista de horarios disponibles para un doctor en una fecha
	// especÃƒÆ’Ã‚Â­fica

	public ArrayList<LocalTime> obtenerHorariosDisponibles(String cedulaDoctor, LocalDate fecha) {
		ArrayList<LocalTime> horariosDisponibles = new ArrayList<>();

		Doctor doctor = buscarDoctorPorCedula(cedulaDoctor);
		if (doctor == null || !doctor.isActivo()) {
			return horariosDisponibles;
		}

		// Verificar que el doctor no haya alcanzado el lÃƒÆ’Ã‚Â­mite de citas
		int citasDelDia = contarCitasDelDia(cedulaDoctor, fecha);
		if (citasDelDia >= doctor.getCitasPorDia()) {
			return horariosDisponibles; // Ya no hay cupos
		}

		// Generar intervalos de tiempo desde horarioInicio hasta horarioFin
		LocalTime horaActual = doctor.getHorarioInicio();
		LocalTime horarioFin = doctor.getHorarioFin();

		while (horaActual.plusMinutes(DURACION_CITA_MINUTOS).isBefore(horarioFin)
				|| horaActual.plusMinutes(DURACION_CITA_MINUTOS).equals(horarioFin)) {

			if (verificarDisponibilidadDoctor(cedulaDoctor, fecha, horaActual)) {
				horariosDisponibles.add(horaActual);
			}

			// Avanzar al siguiente intervalo de 45 minutos
			horaActual = horaActual.plusMinutes(DURACION_CITA_MINUTOS);
		}

		return horariosDisponibles;
	}

	// Obtiene una representaciÃƒÆ’Ã‚Â³n en texto del doctor para mostrar en ComboBox

	public String obtenerInfoDoctorParaCombo(Doctor doctor) {
		return doctor.getNombre() + " " + doctor.getApellido() + " - " + doctor.getEspecialidad();
	}

	public int contarCitasDelDia(String cedulaDoctor, LocalDate fecha) {
		int contador = 0;

		for (Cita cita : citas) {
			boolean mismoDoctorFecha = cita.getDoctor().getCedula().equals(cedulaDoctor)
					&& cita.getFechaCita().equals(fecha);
			boolean citaValida = !cita.getEstadoCita().equals("Cancelada");

			if (mismoDoctorFecha && citaValida) {
				contador++;
			}
		}

		return contador;
	}

	// Obtiene cuÃƒÆ’Ã‚Â¡ntas citas le quedan disponibles a un doctor en una fecha

	public String obtenerCitasDisponiblesTexto(String cedulaDoctor, LocalDate fecha) {
		Doctor doctor = buscarDoctorPorCedula(cedulaDoctor);
		if (doctor == null) {
			return "Doctor no encontrado";
		}

		int citasUsadas = contarCitasDelDia(cedulaDoctor, fecha);
		int citasDisponibles = doctor.getCitasPorDia() - citasUsadas;

		return citasDisponibles + " de " + doctor.getCitasPorDia() + " citas disponibles";
	}

	public Cita buscarCita(String codigoCita) {
		for (Cita cita : citas) {
			if (cita.getCodigoCita().equals(codigoCita)) {
				return cita;
			}
		}
		return null;
	}

	public ArrayList<Cita> listarCitasPorDoctor(String cedulaDoctor, LocalDate fecha) {
		ArrayList<Cita> citasDoctor = new ArrayList<>();

		for (Cita cita : citas) {
			boolean mismoDoctorFecha = cita.getDoctor().getCedula().equals(cedulaDoctor)
					&& cita.getFechaCita().equals(fecha);

			if (mismoDoctorFecha) {
				citasDoctor.add(cita);
			}
		}

		return citasDoctor;
	}

	public ArrayList<Cita> listarCitasPorPaciente(String cedulaPaciente) {
		ArrayList<Cita> citasPaciente = new ArrayList<>();

		for (Cita cita : citas) {
			if (cita.getPaciente().getCedula().equals(cedulaPaciente)) {
				citasPaciente.add(cita);
			}
		}

		return citasPaciente;
	}

	public boolean cancelarCita(String codigoCita) {
		Cita cita = buscarCita(codigoCita);

		if (cita != null) {
			cita.cambiarEstado("Cancelada");
			return true;
		}

		return false;
	}

	public boolean completarCita(String codigoCita) {
		Cita cita = buscarCita(codigoCita);

		if (cita != null) {
			cita.cambiarEstado("Completada");
			return true;
		}

		return false;
	}

	public ArrayList<Cita> listarCitasPendientes() {
		ArrayList<Cita> pendientes = new ArrayList<>();

		for (Cita c : citas) {
			if (c == null)
				continue;

			String estado = c.getEstadoCita();
			if (estado == null)
				continue;

			// Normalizamos para evitar errores invisibles (espacios, mayÃºsculas, etc.)
			estado = estado.trim().toLowerCase();

			if (estado.equals("pendiente")) {
				pendientes.add(c);
			}
		}

		return pendientes;
	}

	public ArrayList<Cita> listarCitasCompletadas() {
		ArrayList<Cita> citasCompletadas = new ArrayList<>();

		for (Cita cita : citas) {
			if (cita.getEstadoCita().equals("Completada")) {
				citasCompletadas.add(cita);
			}
		}

		return citasCompletadas;
	}

	public ArrayList<Cita> listarCitasPorFecha(LocalDate fecha) {
		ArrayList<Cita> citasPorFecha = new ArrayList<>();

		for (Cita cita : citas) {
			if (cita.getFechaCita().equals(fecha)) {
				citasPorFecha.add(cita);
			}
		}

		return citasPorFecha;
	}
	
	public void modificarCita (Cita nueva) {
		for (Cita buscando: citas) {
			if (buscando.getCodigoCita().equalsIgnoreCase(nueva.getCodigoCita())) {
				buscando.setEstadoCita(nueva.getEstadoCita());
			}
		}
	}

	// consultas
	public Consulta registrarConsulta(String codigoCita, String sintomas, String diagnostico, String codigoTratamiento,
			String notas, String codigoEnfermedad) {

		Cita cita = buscarCita(codigoCita);
		if (cita == null) {
			return null;
		}

		Paciente paciente = cita.getPaciente();
		Doctor doctor = Control.getDoctorLogeado();

		Tratamiento tratamiento = buscarTratamientoPorCodigo(codigoTratamiento);
		if (tratamiento == null) {
			return null;
		}

		Enfermedad enfermedad = null;
		if (codigoEnfermedad != null) {
			enfermedad = buscarEnfermedadPorCodigo(codigoEnfermedad);
		}

		String codigoConsulta = generarCodigoConsulta();		

		Consulta nuevaConsulta = new Consulta(codigoConsulta, paciente, doctor, cita, LocalDate.now(), sintomas,
				diagnostico, tratamiento, notas, enfermedad);

		if (enfermedad != null && enfermedad.isBajoVigilancia()) {
			nuevaConsulta.setEsEnfermedadVigilancia(true);

			// ====== AÃ‘ADE ESTAS 3 LÃ�NEAS ======
			// Agregar la enfermedad al reporte si no existe
			agregarEnfermedadVigilancia(enfermedad.getNombre());
			// Incrementar el contador de casos
			agregarCasoEnfermedad(enfermedad.getNombre());
			// ==================================
		}

		paciente.getHistoriaClinica().agregarConsulta(nuevaConsulta);
		cita.setEstadoCita("Completada");
		modificarCita(cita);

		cita.cambiarEstado("Completada");
		

		return nuevaConsulta;
	}

	public Paciente asegurarPacienteRegistrado(Paciente p) {
		if (p == null)
			return null;

		Paciente real = buscarPacientePorCedula(p.getCedula());

		if (real != null) {
			return real; // Ya existe â†’ usar el paciente real
		}

		// NO existe â†’ registrarlo como paciente normal
		registrarPaciente(p);

		return p; // ya estÃ¡ agregado a la lista
	}

	public Consulta buscarConsulta(String codigoConsulta) {

		for (Paciente paciente : pacientes) {

			ArrayList<Consulta> consultas = paciente.getHistoriaClinica().getConsultas();

			for (Consulta consulta : consultas) {
				if (consulta.getCodigoConsulta().equals(codigoConsulta)) {
					return consulta;
				}
			}
		}

		return null;
	}

	public boolean marcarConsultaParaResumen(String codigoConsulta) {
		Consulta consulta = buscarConsulta(codigoConsulta);

		if (consulta != null) {
			consulta.marcarParaResumen();
			return true;
		}

		return false;
	}

	public boolean desmarcarConsultaDeResumen(String codigoConsulta) {
		Consulta consulta = buscarConsulta(codigoConsulta);

		if (consulta != null) {
			consulta.desmarcarParaResumen();
			return true;
		}

		return false;
	}

	public ArrayList<Consulta> listarConsultasPorPaciente(String cedulaPaciente) {
		Paciente paciente = buscarPacientePorCedula(cedulaPaciente);

		if (paciente != null) {
			return paciente.getHistoriaClinica().getConsultas();
		}

		return new ArrayList<>();
	}

	public ArrayList<Consulta> listarConsultasPorDoctor(String numLicenciaDoctor) {
		ArrayList<Consulta> resultado = new ArrayList<>();

		if (numLicenciaDoctor == null || numLicenciaDoctor.trim().isEmpty()) {
			return resultado;
		}

		// Recorremos todos los pacientes del sistema
		for (Paciente p : pacientes) { // ajusta "pacientes" si tu lista se llama distinto
			if (p.getHistoriaClinica() == null) {
				continue;
			}

			ArrayList<Consulta> consultasPaciente = p.getHistoriaClinica().getConsultas();
			if (consultasPaciente == null || consultasPaciente.isEmpty()) {
				continue;
			}

			for (Consulta c : consultasPaciente) {
				if (c == null || c.getDoctor() == null) {
					continue;
				}

				// CONSULTA VISIBLE SI:
				// - la enfermedad estÃƒÆ’Ã‚Â¡ bajo vigilancia (pÃƒÆ’Ã‚Âºblica)
				// - O el doctor que la hizo tiene ese nÃƒÆ’Ã‚Âºmero de licencia
				if (c.isEsEnfermedadVigilancia()
						|| c.getDoctor().getNumeroLicencia().equalsIgnoreCase(numLicenciaDoctor)) {

					resultado.add(c);
				}
			}
		}

		return resultado;
	}

	public ArrayList<Consulta> listarConsultasVisiblesParaDoctor(String numLicenciaDoctor) {
		ArrayList<Consulta> visibles = new ArrayList<>();

		if (numLicenciaDoctor == null || numLicenciaDoctor.trim().isEmpty()) {
			return visibles;
		}

		// 1) Agregar TODAS las consultas pÃƒÆ’Ã‚Âºblicas (enfermedad bajo vigilancia)
		for (Paciente p : pacientes) { // <--- ajusta "pacientes" si tiene otro nombre
			if (p.getHistoriaClinica() == null) {
				continue;
			}
			ArrayList<Consulta> consultasPaciente = p.getHistoriaClinica().getConsultas();
			if (consultasPaciente == null || consultasPaciente.isEmpty()) {
				continue;
			}

			for (Consulta c : consultasPaciente) {
				if (c == null) {
					continue;
				}

				if (c.isEsEnfermedadVigilancia()) {
					// evitar duplicados por cÃƒÆ’Ã‚Â³digo de consulta
					if (!contieneConsultaPorCodigo(visibles, c.getCodigoConsulta())) {
						visibles.add(c);
					}
				}
			}
		}

		// 2) Agregar las consultas propias del doctor (aunque no estÃƒÆ’Ã‚Â©n bajo
		// vigilancia)
		ArrayList<Consulta> propias = listarConsultasPorDoctor(numLicenciaDoctor);
		for (Consulta c : propias) {
			if (!contieneConsultaPorCodigo(visibles, c.getCodigoConsulta())) {
				visibles.add(c);
			}
		}

		return visibles;
	}

	private boolean contieneConsultaPorCodigo(ArrayList<Consulta> lista, String codigoConsulta) {
		if (codigoConsulta == null) {
			return false;
		}
		for (Consulta c : lista) {
			if (c != null && codigoConsulta.equalsIgnoreCase(c.getCodigoConsulta())) {
				return true;
			}
		}
		return false;
	}

	// Historia clinica

	public HistoriaClinica obtenerHistoriaClinica(String cedulaPaciente) {
		Paciente paciente = buscarPacientePorCedula(cedulaPaciente);

		if (paciente != null) {
			return paciente.getHistoriaClinica();
		}

		return null;
	}

	public String generarResumenAutomatico(String cedulaPaciente) {
		Paciente paciente = buscarPacientePorCedula(cedulaPaciente);

		if (paciente != null) {
			return paciente.getHistoriaClinica().generarResumen();
		}

		return "Paciente no encontrado";
	}

	public ArrayList<Consulta> obtenerConsultasParaResumen(String cedulaPaciente) {
		Paciente paciente = buscarPacientePorCedula(cedulaPaciente);

		if (paciente != null) {
			return paciente.getHistoriaClinica().obtenerConsultasParaResumen();
		}

		return new ArrayList<>();
	}

	// vacunacion

	public boolean registrarVacunaPaciente(Paciente elqueva, RegistroVacuna regi) {
		elqueva.getRegistrosVacunas().add(regi);
		modificarPaciente(elqueva);
		modificarCantVacuna(regi.getVacuna());
		return true;
	}

	public boolean verificarVacunaAplicada(String cedulaPaciente, String codigoVacuna) {

		Paciente paciente = buscarPacientePorCedula(cedulaPaciente);
		if (paciente == null) {
			return false;
		}

		Vacuna vacuna = buscarVacunaPorCodigo(codigoVacuna);
		if (vacuna == null) {
			return false;
		}

		return paciente.tieneVacuna(vacuna);
	}

	public boolean modificarCantVacuna(Vacuna vacunacion) {
		if (vacunacion == null) {
			return false;
		} else {
			for (Vacuna buscando : vacunas) {
				if (buscando.getCodigoVacuna().equalsIgnoreCase(vacunacion.getCodigoVacuna())) {
					buscando.setCantidad(buscando.getCantidad() - 1);
					return true;
				}
			}
		}
		return false;
	}

	public Vacuna buscarVacunaPorNombre(String nombre) {
		for (Vacuna vacuna : vacunas) {
			if (vacuna.getNombre().equalsIgnoreCase(nombre)) {
				return vacuna;
			}
		}
		return null;
	}

	public ArrayList<Vacuna> obtenerVacunasFaltantes(String cedulaPaciente) {

		Paciente paciente = buscarPacientePorCedula(cedulaPaciente);
		if (paciente == null) {
			return new ArrayList<>();
		}

		ArrayList<Vacuna> vacunasActivas = listarVacunasActivas();

		return paciente.obtenerVacunasFaltantes(vacunasActivas);
	}

	public ArrayList<RegistroVacuna> obtenerHistorialVacunacion(String cedulaPaciente) {

		Paciente paciente = buscarPacientePorCedula(cedulaPaciente);

		if (paciente != null) {
			return paciente.getRegistrosVacunas();
		}

		return new ArrayList<>();
	}

	public boolean isPrimerIngresp() {
		return primerIngresp;
	}

	public void setPrimerIngresp(boolean primerIngresp) {
		this.primerIngresp = primerIngresp;
	}

	// Alergias

	public ArrayList<Alergia> getAlergias() {
		return alergias;
	}

	public void setAlergias(ArrayList<Alergia> alergias) {
		this.alergias = alergias;
	}

	public void registrarAlergias(Alergia alergia) {
		alergias.add(alergia);
	}

	public boolean validarExistenciaAlergia(String alergiarev) {
		for (Alergia revisando : alergias) {
			if (revisando.getNombre().equalsIgnoreCase(alergiarev)) {
				return false;
			}
		}
		return true;
	}

	public ArrayList<Paciente> getInteresados() {
		return interesados;
	}

	public void setInteresados(ArrayList<Paciente> interesados) {
		this.interesados = interesados;
	}

	public void registarInteresados(Paciente elpaci) {
		interesados.add(elpaci);
	}

	public String generarCodigoConsulta() {
		return "CON-" + contadorConsultas++;
	}

	public void setarContadores() {
		contadorCitas = citas.size() + 1;
		contadorVacunas = vacunas.size() + 1;
		contadorTratamientos = tratamientos.size() + 1;
		contadorEnfermedades = enfermedades.size() + 1;
		contadorPacientes = pacientes.size() + 1;
		contadorDoctores = doctores.size() + 1;

	}

	// ============================================================
	// MÃ‰TODOS DE VALIDACIÃ“N - Agregar estos mÃ©todos a Clinica.java
	// Reemplazar el mÃ©todo isCedulaRegistrada existente y agregar los nuevos
	// ============================================================

	/**
	 * Verifica si una cÃ©dula ya estÃ¡ registrada (pacientes + doctores)
	 */
	public boolean isCedulaRegistrada(String cedula) {
		if (cedula == null || cedula.trim().isEmpty()) {
			return false;
		}

		String cedulaLimpia = cedula.replaceAll("[^0-9]", "");
		if (cedulaLimpia.length() != 11) {
			return false;
		}

		// Buscar en pacientes
		for (Paciente p : pacientes) {
			if (p != null && p.getCedula() != null) {
				String cedulaPaciente = p.getCedula().replaceAll("[^0-9]", "");
				if (cedulaPaciente.equals(cedulaLimpia)) {
					return true;
				}
			}
		}

		// Buscar en doctores
		for (Doctor d : doctores) {
			if (d != null && d.getCedula() != null) {
				String cedulaDoctor = d.getCedula().replaceAll("[^0-9]", "");
				if (cedulaDoctor.equals(cedulaLimpia)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Verifica si una licencia mÃ©dica ya estÃ¡ registrada
	 */
	public boolean isLicenciaRegistrada(String numeroLicencia) {
		if (numeroLicencia == null || numeroLicencia.trim().isEmpty()) {
			return false;
		}

		String licenciaLimpia = numeroLicencia.trim().toUpperCase().replaceAll("[^A-Z0-9]", "");

		if (licenciaLimpia.length() < 4) {
			return false;
		}

		for (Doctor d : doctores) {
			if (d != null && d.getNumeroLicencia() != null) {
				String licenciaExistente = d.getNumeroLicencia().trim().toUpperCase().replaceAll("[^A-Z0-9]", "");
				if (licenciaExistente.equals(licenciaLimpia)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Verifica si una licencia estÃ¡ registrada, excluyendo un doctor especÃ­fico
	 * (Ãºtil para modificaciones)
	 */
	public boolean isLicenciaRegistrada(String numeroLicencia, String cedulaExcluir) {
		if (numeroLicencia == null || numeroLicencia.trim().isEmpty()) {
			return false;
		}

		String licenciaLimpia = numeroLicencia.trim().toUpperCase().replaceAll("[^A-Z0-9]", "");
		String cedulaExcluirLimpia = (cedulaExcluir != null) ? cedulaExcluir.replaceAll("[^0-9]", "") : "";

		for (Doctor d : doctores) {
			if (d != null && d.getNumeroLicencia() != null && d.getCedula() != null) {
				String cedulaDoctorLimpia = d.getCedula().replaceAll("[^0-9]", "");

				// No comparar con el doctor que estamos modificando
				if (!cedulaDoctorLimpia.equals(cedulaExcluirLimpia)) {
					String licenciaExistente = d.getNumeroLicencia().trim().toUpperCase().replaceAll("[^A-Z0-9]", "");
					if (licenciaExistente.equals(licenciaLimpia)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Verifica si un telÃ©fono ya estÃ¡ registrado en el sistema (Ãštil pero no
	 * crÃ­tico - puede haber familias con mismo telÃ©fono)
	 */
	public boolean isTelefonoRegistrado(String telefono) {
		if (telefono == null || telefono.trim().isEmpty()) {
			return false;
		}

		String telefonoLimpio = telefono.replaceAll("[^0-9]", "");
		if (telefonoLimpio.length() != 10) {
			return false;
		}

		// Buscar en pacientes
		for (Paciente p : pacientes) {
			if (p != null && p.getTelefono() != null) {
				String telefonoPaciente = p.getTelefono().replaceAll("[^0-9]", "");
				if (telefonoPaciente.equals(telefonoLimpio)) {
					return true;
				}
			}
		}

		// Buscar en doctores
		for (Doctor d : doctores) {
			if (d != null && d.getTelefono() != null) {
				String telefonoDoctor = d.getTelefono().replaceAll("[^0-9]", "");
				if (telefonoDoctor.equals(telefonoLimpio)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Verifica si un telÃ©fono estÃ¡ registrado, excluyendo una cÃ©dula especÃ­fica
	 * (Para modificaciones)
	 */

	public boolean isTelefonoRegistrado(String telefono, String cedulaExcluir) {
		if (telefono == null || telefono.trim().isEmpty()) {
			return false;
		}

		String telefonoLimpio = telefono.replaceAll("[^0-9]", "");
		String cedulaExcluirLimpia = (cedulaExcluir != null) ? cedulaExcluir.replaceAll("[^0-9]", "") : "";

		// Buscar en pacientes
		for (Paciente p : pacientes) {
			if (p != null && p.getTelefono() != null && p.getCedula() != null) {
				String cedulaPaciente = p.getCedula().replaceAll("[^0-9]", "");

				if (!cedulaPaciente.equals(cedulaExcluirLimpia)) {
					String telefonoPaciente = p.getTelefono().replaceAll("[^0-9]", "");
					if (telefonoPaciente.equals(telefonoLimpio)) {
						return true;
					}
				}
			}
		}

		// Buscar en doctores
		for (Doctor d : doctores) {
			if (d != null && d.getTelefono() != null && d.getCedula() != null) {
				String cedulaDoctor = d.getCedula().replaceAll("[^0-9]", "");

				if (!cedulaDoctor.equals(cedulaExcluirLimpia)) {
					String telefonoDoctor = d.getTelefono().replaceAll("[^0-9]", "");
					if (telefonoDoctor.equals(telefonoLimpio)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	// ========== MÃ‰TODOS PARA VACUNAS ==========

	/**
	 * Verifica si un nÃºmero de lote ya estÃ¡ registrado
	 */
	public boolean isLoteRegistrado(String numeroLote) {
		if (numeroLote == null || numeroLote.trim().isEmpty()) {
			return false;
		}

		String loteLimpio = numeroLote.trim().toUpperCase();

		for (Vacuna v : vacunas) {
			if (v != null && v.getNumeroLote() != null) {
				String loteExistente = v.getNumeroLote().trim().toUpperCase();
				if (loteExistente.equals(loteLimpio)) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Verifica si un lote estÃ¡ registrado, excluyendo una vacuna especÃ­fica (para
	 * modificaciones)
	 */
	public boolean isLoteRegistrado(String numeroLote, String codigoExcluir) {
		if (numeroLote == null || numeroLote.trim().isEmpty()) {
			return false;
		}

		String loteLimpio = numeroLote.trim().toUpperCase();

		for (Vacuna v : vacunas) {
			if (v != null && v.getNumeroLote() != null && v.getCodigoVacuna() != null) {
				// No comparar con la vacuna que estamos modificando
				if (!v.getCodigoVacuna().equals(codigoExcluir)) {
					String loteExistente = v.getNumeroLote().trim().toUpperCase();
					if (loteExistente.equals(loteLimpio)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Busca vacuna por nÃºmero de lote
	 */
	public Vacuna buscarVacunaPorLote(String numeroLote) {
		if (numeroLote == null || numeroLote.trim().isEmpty()) {
			return null;
		}

		String loteLimpio = numeroLote.trim().toUpperCase();

		for (Vacuna v : vacunas) {
			if (v != null && v.getNumeroLote() != null) {
				String loteExistente = v.getNumeroLote().trim().toUpperCase();
				if (loteExistente.equals(loteLimpio)) {
					return v;
				}
			}
		}

		return null;
	}

	private void cargarReporteEnfermedades() {
		try {
			File f = new File("reporteEnfermedades.dat");

			if (f.exists()) {
				FileInputStream fis = new FileInputStream(f);
				ObjectInputStream ois = new ObjectInputStream(fis);
				reporteEnfermedades = (HashMap<String, Integer>) ois.readObject();
				ois.close();
				fis.close();
			} else {
				reporteEnfermedades = new HashMap<>();
				guardarReporteSerializado();
				guardarReporteTXT();
			}

		} catch (Exception e) {
			System.out.println("Error cargando reporte: " + e.getMessage());
			reporteEnfermedades = new HashMap<>();
		}
	}

	public void agregarEnfermedadVigilancia(String nombre) {
		if (!reporteEnfermedades.containsKey(nombre)) {
			reporteEnfermedades.put(nombre, 0);
			guardarReporteSerializado();
			guardarReporteTXT();
		}
	}

	public void guardarReporteSerializado() {
		try {
			ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("reporteEnfermedades.dat"));
			oos.writeObject(reporteEnfermedades);
			oos.close();
		} catch (Exception e) {
			System.out.println("Error guardando archivo serializado: " + e.getMessage());
		}
	}

	public void guardarReporteTXT() {
		try {
			PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter("reporteEnfermedades.txt")));
			pw.println("=== ENFERMEDADES BAJO VIGILANCIA ===\n");

			for (String enf : reporteEnfermedades.keySet()) {
				pw.println(enf + ": " + reporteEnfermedades.get(enf) + " casos");
			}

			pw.close();

		} catch (Exception e) {
			System.out.println("Error guardando TXT: " + e.getMessage());
		}
	}

	public void agregarCasoEnfermedad(String nombre) {
		if (!reporteEnfermedades.containsKey(nombre)) {
			reporteEnfermedades.put(nombre, 0);
		}

		reporteEnfermedades.put(nombre, reporteEnfermedades.get(nombre) + 1);

		guardarReporteSerializado();
		guardarReporteTXT();
	}

	/**
	 * Busca un doctor por su nombre de usuario
	 * 
	 * @param usuario Nombre de usuario del doctor
	 * @return El doctor con ese usuario, o null si no se encuentra
	 */
	public Doctor buscarDoctorPorUsuario(String usuario) {
		if (usuario == null || usuario.trim().isEmpty()) {
			return null;
		}

		for (Doctor doctor : doctores) {
			if (doctor != null && usuario.equals(doctor.getUsuario())) {
				return doctor;
			}
		}
		return null;
	}

	// Agrega este mÃ©todo en Clinica.java:
	public boolean existeUsuarioDoctor(String usuario) {
		if (usuario == null || usuario.trim().isEmpty()) {
			return false;
		}

		for (Doctor doctor : doctores) {
			if (doctor.getUsuario() != null && doctor.getUsuario().equalsIgnoreCase(usuario.trim())) {
				return true;
			}
		}

		return false;
	}

	// ========== AGREGAR ESTOS MÃ‰TODOS AL FINAL DE Clinica.java ==========

	/**
	 * Lista los pacientes que un doctor puede ver: 1. Pacientes que Ã©l registrÃ³ 2.
	 * Todos los pacientes si es administrador
	 */
	public ArrayList<Paciente> listarPacientesVisiblesParaDoctor(String licenciaDoctor, boolean esAdmin) {
		ArrayList<Paciente> pacientesVisibles = new ArrayList<>();

		if (licenciaDoctor == null) {
			return pacientesVisibles;
		}

		if (esAdmin) {
			// Admin ve todos los pacientes
			return new ArrayList<>(pacientes);
		}

		// Doctor normal solo ve los que Ã©l registrÃ³
		for (Paciente p : pacientes) {
			if (p.getDoctorRegistrador() != null && p.getDoctorRegistrador().equals(licenciaDoctor)) {
				pacientesVisibles.add(p);
			}
		}

		return pacientesVisibles;
	}

	/**
	 * Lista consultas que un doctor puede ver segÃºn las reglas: 1. Sus propias
	 * consultas 2. Consultas de enfermedades bajo vigilancia
	 * (esEnfermedadVigilancia = true) 3. Admin ve TODO
	 */
	public ArrayList<Consulta> listarConsultasVisiblesParaDoctorV3(String licenciaDoctor, boolean esAdmin) {
		ArrayList<Consulta> consultasVisibles = new ArrayList<>();

		if (licenciaDoctor == null) {
			return consultasVisibles;
		}

		for (Paciente p : pacientes) {
			if (p.getHistoriaClinica() == null)
				continue;

			for (Consulta c : p.getHistoriaClinica().getConsultas()) {
				if (c == null)
					continue;

				// ADMIN VE TODO
				if (esAdmin) {
					consultasVisibles.add(c);
					continue;
				}

				// DOCTOR NORMAL
				boolean puedeVer = false;

				// 1. Si es consulta propia
				if (c.getDoctor() != null && c.getDoctor().getNumeroLicencia().equals(licenciaDoctor)) {
					puedeVer = true;
				}

				// 2. Si es enfermedad bajo vigilancia
				else if (c.isEsEnfermedadVigilancia()) {
					puedeVer = true;
				}

				// 3. Si estÃ¡ marcada para resumen (OPCIONAL, segÃºn tu requerimiento)
				else if (c.isIncluidaEnResumen()) {
					puedeVer = true;
				}

				if (puedeVer && !contieneConsultaPorCodigo(consultasVisibles, c.getCodigoConsulta())) {
					consultasVisibles.add(c);
				}
			}
		}

		return consultasVisibles;
	}

	/**
	 * Obtiene la licencia del doctor logeado actualmente
	 */
	public String getLicenciaDoctorLogeado() {
		Doctor doctor = Control.getDoctorLogeado();
		if (doctor != null) {
			return doctor.getNumeroLicencia();
		}
		return null;
	}
	
	
	// AGREGAR este mÃ©todo a Clinica.java

	/**
	 * Actualiza todas las citas de un paciente interesado para que apunten al paciente real
	 * @param cedulaPaciente CÃ©dula del paciente
	 * @param pacienteReal Objeto Paciente real registrado
	 */
	public void actualizarCitasDeInteresado(String cedulaPaciente, Paciente pacienteReal) {
	    if (cedulaPaciente == null || pacienteReal == null) {
	        return;
	    }
	    
	    // Buscar todas las citas que pertenecen a este paciente (por cÃ©dula)
	    for (Cita cita : citas) {
	        if (cita != null && cita.getPaciente() != null 
	            && cita.getPaciente().getCedula().equals(cedulaPaciente)) {
	            
	            // Actualizar la referencia al paciente real
	            cita.setPaciente(pacienteReal);
	            
	            System.out.println("Cita " + cita.getCodigoCita() + " actualizada: " 
	                + "Interesado -> Paciente Real (" + pacienteReal.getCodigoPaciente() + ")");
	        }
	    }
	}

	public int getContadorPacientes() {
		return contadorPacientes;
	}

	public void setContadorPacientes(int contadorPacientes) {
		this.contadorPacientes = contadorPacientes;
	}

	public int getContadorDoctores() {
		return contadorDoctores;
	}

	public void setContadorDoctores(int contadorDoctores) {
		this.contadorDoctores = contadorDoctores;
	}

	public int getContadorCitas() {
		return contadorCitas;
	}

	public void setContadorCitas(int contadorCitas) {
		this.contadorCitas = contadorCitas;
	}

	public int getContadorConsultas() {
		return contadorConsultas;
	}

	public void setContadorConsultas(int contadorConsultas) {
		this.contadorConsultas = contadorConsultas;
	}

	public int getContadorTratamientos() {
		return contadorTratamientos;
	}

	public void setContadorTratamientos(int contadorTratamientos) {
		this.contadorTratamientos = contadorTratamientos;
	}

	public int getContadorEnfermedades() {
		return contadorEnfermedades;
	}

	public void setContadorEnfermedades(int contadorEnfermedades) {
		this.contadorEnfermedades = contadorEnfermedades;
	}

	public int getContadorVacunas() {
		return contadorVacunas;
	}

	public void setContadorVacunas(int contadorVacunas) {
		this.contadorVacunas = contadorVacunas;
	}

}