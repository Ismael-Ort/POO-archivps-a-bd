package logico;

import java.io.Serializable;
import java.util.ArrayList;

public class Tratamiento implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    // CODIGO
    private String codigoTratamiento;
    
    // INFORMACION DEL TRATAMIENTO (CATALOGO)
    private String nombreTratamiento;
    private String descripcion;
    private String indicaciones;
    private String duracion;
    
    // MEDICAMENTOS
    private ArrayList<Medicamento> medicamentos;


    
    // ============================================================
    //                     CONSTRUCTOR
    // ============================================================
    public Tratamiento(String codigoTratamiento, String nombreTratamiento, 
                       String descripcion, String indicaciones, String duracion) {
        this.codigoTratamiento = codigoTratamiento;
        this.nombreTratamiento = nombreTratamiento;
        this.descripcion = descripcion;
        this.indicaciones = indicaciones;
        this.duracion = duracion;
        this.medicamentos = new ArrayList<>();
    }
    
    // ============================================================
    //                  GETTERS Y SETTERS
    // ============================================================
    public String getCodigoTratamiento() {
        return codigoTratamiento;
    }
    
    public String getNombreTratamiento() {
        return nombreTratamiento;
    }
    
    public void setNombreTratamiento(String nombreTratamiento) {
        this.nombreTratamiento = nombreTratamiento;
    }
    
    public String getDescripcion() {
        return descripcion;
    }
    
    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getIndicaciones() {
        return indicaciones;
    }
    
    public void setIndicaciones(String indicaciones) {
        this.indicaciones = indicaciones;
    }
    
    public String getDuracion() {
        return duracion;
    }
    
    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }
    
    public ArrayList<Medicamento> getMedicamentos() {
        return medicamentos;
    }
    
    public void setMedicamentos(ArrayList<Medicamento> medicamentos) {
        this.medicamentos = medicamentos;
    }
    
    // ============================================================
    //                METODOS - MEDICAMENTOS
    // ============================================================
    public void agregarMedicamento(Medicamento m) {
        medicamentos.add(m);
    }
    
    public void eliminarMedicamento(int index) {
        if (index >= 0 && index < medicamentos.size()) {
            medicamentos.remove(index);
        }
    }
    
    public void eliminarMedicamento(Medicamento m) {
        medicamentos.remove(m);
    }
}