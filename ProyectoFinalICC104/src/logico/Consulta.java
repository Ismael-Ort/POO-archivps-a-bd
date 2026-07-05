package logico;
//cambios
import java.io.Serializable;
import java.time.LocalDate;

public class Consulta implements Serializable {

    private static final long serialVersionUID = 1L;

    private String codigoConsulta;
    private Paciente paciente;
    private Doctor doctor;
    private Cita cita;
    private LocalDate fechaConsulta;
    private String sintomas;
    private String diagnostico;
    private Tratamiento tratamiento;
    private String notasMedicas;
    private Enfermedad enfermedad;
    private boolean incluidaEnResumen;
    private boolean esEnfermedadVigilancia;

    public Consulta(String codigoConsulta, Paciente paciente, Doctor doctor,
                    Cita cita, LocalDate fechaConsulta,
                    String sintomas, String diagnostico,
                    Tratamiento tratamiento, String notasMedicas,
                    Enfermedad enfermedad) {

        this.codigoConsulta = codigoConsulta;
        this.paciente = paciente;
        this.doctor = doctor;
        this.cita = cita;
        this.fechaConsulta = fechaConsulta;
        this.sintomas = sintomas;
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.notasMedicas = notasMedicas;
        this.enfermedad = enfermedad;
        this.incluidaEnResumen = false;
        this.esEnfermedadVigilancia = false;
    }

    public String getCodigoConsulta() {
        return codigoConsulta;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public Cita getCita() {
        return cita;
    }

    public LocalDate getFechaConsulta() {
        return fechaConsulta;
    }

    public String getSintomas() {
        return sintomas;
    }

    public String getDiagnostico() {
        return diagnostico;
    }

    public Tratamiento getTratamiento() {
        return tratamiento;
    }

    public String getNotasMedicas() {
        return notasMedicas;
    }

    public Enfermedad getEnfermedad() {
        return enfermedad;
    }

    public boolean isIncluidaEnResumen() {
        return incluidaEnResumen;
    }

    public boolean isEsEnfermedadVigilancia() {
        return esEnfermedadVigilancia;
    }

    public void setEsEnfermedadVigilancia(boolean esEnfermedadVigilancia) {
        this.esEnfermedadVigilancia = esEnfermedadVigilancia;
    }

    public void marcarParaResumen() {
        this.incluidaEnResumen = true;
    }

    public void desmarcarParaResumen() {
        this.incluidaEnResumen = false;
    }

    public boolean vaAlResumen() {
        return incluidaEnResumen || esEnfermedadVigilancia;
    }
}
