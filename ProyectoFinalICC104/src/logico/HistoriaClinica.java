package logico;

//cambios
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class HistoriaClinica implements Serializable {

	private static final long serialVersionUID = 1L;

	private Paciente paciente;
	private ArrayList<Consulta> consultas;
	private LocalDate fechaCreacion;

	public HistoriaClinica(Paciente paciente, LocalDate fechaCreacion) {
		super();
		this.paciente = paciente;
		this.consultas = new ArrayList<>();
		this.fechaCreacion = fechaCreacion;
	}

	public Paciente getPaciente() {
		return paciente;
	}

	public ArrayList<Consulta> getConsultas() {
		return consultas;
	}

	public LocalDate getFechaCreacion() {
		return fechaCreacion;
	}

	public void agregarConsulta(Consulta consulta) {
		consultas.add(consulta);
	}

	public ArrayList<Consulta> obtenerTodasConsultas() {
		return consultas;
	}

	public ArrayList<Consulta> obtenerConsultasParaResumen() {
		ArrayList<Consulta> resumen = new ArrayList<>();
		for (Consulta consulta : consultas) {
			if (consulta.vaAlResumen()) {
				resumen.add(consulta);
			}
		}
		return resumen;
	}

	public ArrayList<Consulta> obtenerConsultasEnfermedadVigilancia() {
		ArrayList<Consulta> vigilancia = new ArrayList<>();
		for (Consulta consulta : consultas) {
			if (consulta.isEsEnfermedadVigilancia()) {
				vigilancia.add(consulta);
			}
		}
		return vigilancia;
	}

	public ArrayList<Consulta> obtenerConsultasPorFecha(LocalDate inicio, LocalDate fin) {
		ArrayList<Consulta> porFechas = new ArrayList<>();
		for (Consulta consulta : consultas) {
			LocalDate fecha = consulta.getFechaConsulta();
			if ((fecha.isEqual(inicio) || fecha.isAfter(inicio)) && (fecha.isEqual(fin) || fecha.isBefore(fin))) {
				porFechas.add(consulta);
			}
		}
		return porFechas;
	}

	public ArrayList<Consulta> obtenerUltimasConsultas(int cantidad) {
		ArrayList<Consulta> ultimas = new ArrayList<>();
		int total = consultas.size();

		if (cantidad > total) {
			cantidad = total;
		}

		for (int i = total - cantidad; i < total; i++) {
			ultimas.add(consultas.get(i));
		}

		return ultimas;
	}

	public int cantidadConsultas() {
		return consultas.size();
	}

	public String generarResumen() {
		StringBuilder resumen = new StringBuilder();

		resumen.append("===============================================================\n");
		resumen.append("                   RESUMEN DE HISTORIA CLÍNICA                 \n");
		resumen.append("===============================================================\n\n");

		// INFORMACIÓN DEL PACIENTE
		resumen.append("INFORMACIÓN DEL PACIENTE\n");
		resumen.append("---------------------------------------------------------------\n");
		resumen.append("Nombre: ").append(paciente.getNombre()).append(" ").append(paciente.getApellido()).append("\n");
		resumen.append("Cédula: ").append(paciente.getCedula()).append("\n");
		resumen.append("Código: ").append(paciente.getCodigoPaciente()).append("\n");
		resumen.append("Tipo de Sangre: ").append(paciente.getTipoSangre()).append("\n");
		resumen.append("Fecha de Registro: ").append(fechaCreacion).append("\n");

		// ALERGIAS
		resumen.append("Alergias: ");
		ArrayList<Alergia> alergias = paciente.getAlergias();
		if (alergias != null && !alergias.isEmpty()) {
			resumen.append("\n");
			for (Alergia a : alergias) {
				resumen.append("  - ").append(a.getNombre());
				if (a.getTipo() != null && !a.getTipo().isEmpty()) {
					resumen.append(" (").append(a.getTipo()).append(")");
				}
				resumen.append("\n");
			}
		} else {
			resumen.append("No presenta\n");
		}
		resumen.append("\n");

		// CONSULTAS EN RESUMEN
		ArrayList<Consulta> consultasResumen = obtenerConsultasParaResumen();

		resumen.append("RESUMEN DE CONSULTAS MÉDICAS\n");
		resumen.append("---------------------------------------------------------------\n");

		if (consultasResumen.isEmpty()) {
			resumen.append("No hay consultas marcadas para resumen.\n\n");
		} else {
			for (int i = 0; i < consultasResumen.size(); i++) {
				Consulta c = consultasResumen.get(i);
				resumen.append((i + 1) + ". ").append(c.getFechaConsulta()).append(" - ")
						.append(c.getDoctor().getEspecialidad()).append("\n");
				resumen.append("   Codigo: ").append(c.getCodigoConsulta()).append("\n");
				resumen.append("   Doctor: Dr. ").append(c.getDoctor().getNombre()).append(" ")
						.append(c.getDoctor().getApellido()).append("\n");
				resumen.append("   Sintomas: ").append(acortarTexto(c.getSintomas(), 60)).append("\n");
				resumen.append("   Diagnostico: ").append(acortarTexto(c.getDiagnostico(), 60)).append("\n");

				// Información del tratamiento
				if (c.getTratamiento() != null) {
					resumen.append("   Tratamiento: ").append(c.getTratamiento().getNombreTratamiento()).append("\n");
					resumen.append("   Duracion: ").append(c.getTratamiento().getDuracion()).append("\n");
				}

				if (c.isEsEnfermedadVigilancia()) {
					resumen.append("   [ENFERMEDAD BAJO VIGILANCIA EPIDEMIOLOGICA]\n");
				}

				if (c.isIncluidaEnResumen() && !c.isEsEnfermedadVigilancia()) {
					resumen.append("   [MARCADA MANUALMENTE PARA RESUMEN]\n");
				}
				resumen.append("\n");
			}
		}

		// ESTADÍSTICAS
		resumen.append("ESTADISTICAS\n");
		resumen.append("---------------------------------------------------------------\n");
		resumen.append("Total de consultas en sistema: ").append(consultas.size()).append("\n");
		resumen.append("Consultas incluidas en resumen: ").append(consultasResumen.size()).append("\n");
		resumen.append("Enfermedades bajo vigilancia: ").append(obtenerConsultasEnfermedadVigilancia().size())
				.append("\n");
		resumen.append("Consultas marcadas manualmente: ").append(contarConsultasMarcadasManual()).append("\n\n");

		resumen.append("INFORMACIÓN DEL REPORTE\n");
		resumen.append("---------------------------------------------------------------\n");
		resumen.append("Fecha de generacion: ").append(LocalDate.now()).append("\n");
		resumen.append("Generado automaticamente por el sistema\n");
		resumen.append("Paciente desde: ").append(fechaCreacion).append("\n");
		resumen.append("===============================================================\n");

		return resumen.toString();
	}

	// Método auxiliar para contar consultas marcadas manualmente
	private int contarConsultasMarcadasManual() {
		int count = 0;
		for (Consulta c : consultas) {
			if (c.isIncluidaEnResumen() && !c.isEsEnfermedadVigilancia()) {
				count++;
			}
		}
		return count;
	}

	// Método auxiliar para acortar texto largo
	private String acortarTexto(String texto, int maxLength) {
		if (texto == null)
			return "No especificado";
		if (texto.length() <= maxLength)
			return texto;
		return texto.substring(0, maxLength - 3) + "...";
	}
}