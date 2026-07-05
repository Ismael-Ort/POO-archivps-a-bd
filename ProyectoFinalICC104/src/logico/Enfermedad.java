package logico;
//cambios
import java.io.Serializable;

public class Enfermedad implements Serializable {

    private static final long serialVersionUID = 1L;

    private String codigoEnfermedad;
    private String nombre;
    private boolean bajoVigilancia;

    private String sintomasYSignos;        
    private int nivelGravedad;            
    private String potencialPropagacion;   
    private String tipo;                   

    public Enfermedad(String codigoEnfermedad,
                      String nombre,
                      boolean bajoVigilancia,
                      String sintomasYSignos,
                      int nivelGravedad,
                      String potencialPropagacion,
                      String tipo) {
        this.codigoEnfermedad = codigoEnfermedad;
        this.nombre = nombre;
        this.bajoVigilancia = bajoVigilancia;
        this.sintomasYSignos = sintomasYSignos;
        this.nivelGravedad = nivelGravedad;
        this.potencialPropagacion = potencialPropagacion;
        this.tipo = tipo;
    }

    public String getCodigoEnfermedad() {
        return codigoEnfermedad;
    }

    public void setCodigoEnfermedad(String codigoEnfermedad) {
        this.codigoEnfermedad = codigoEnfermedad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isBajoVigilancia() {
        return bajoVigilancia;
    }

    public void setBajoVigilancia(boolean bajoVigilancia) {
        this.bajoVigilancia = bajoVigilancia;
    }

    public String getSintomasYSignos() {
        return sintomasYSignos;
    }

    public void setSintomasYSignos(String sintomasYSignos) {
        this.sintomasYSignos = sintomasYSignos;
    }

    public int getNivelGravedad() {
        return nivelGravedad;
    }

    public void setNivelGravedad(int nivelGravedad) {
        this.nivelGravedad = nivelGravedad;
    }

    public String getPotencialPropagacion() {
        return potencialPropagacion;
    }

    public void setPotencialPropagacion(String potencialPropagacion) {
        this.potencialPropagacion = potencialPropagacion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void activarVigilancia() {
        this.bajoVigilancia = true;
    }

    public void desactivarVigilancia() {
        this.bajoVigilancia = false;
    }
}
