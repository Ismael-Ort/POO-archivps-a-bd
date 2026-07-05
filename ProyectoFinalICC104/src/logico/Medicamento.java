package logico;

import java.io.Serializable;

public class Medicamento implements Serializable {

    private static final long serialVersionUID = 1L;

    private String nombre;
    private double dosisMg;
    private int frecuenciaHoras;
    private int duracionDias;
    private String via;
    private String indicaciones;

    public Medicamento(String nombre, double dosisMg, int frecuenciaHoras,
                       int duracionDias, String via, String indicaciones) {
        this.nombre = nombre;
        this.dosisMg = dosisMg;
        this.frecuenciaHoras = frecuenciaHoras;
        this.duracionDias = duracionDias;
        this.via = via;
        this.indicaciones = indicaciones;
    }

    // GETTERS / SETTERS
    public String getNombre() { return nombre; }
    public double getDosisMg() { return dosisMg; }
    public int getFrecuenciaHoras() { return frecuenciaHoras; }
    public int getDuracionDias() { return duracionDias; }
    public String getVia() { return via; }
    public String getIndicaciones() { return indicaciones; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setDosisMg(double dosisMg) { this.dosisMg = dosisMg; }
    public void setFrecuenciaHoras(int frecuenciaHoras) { this.frecuenciaHoras = frecuenciaHoras; }
    public void setDuracionDias(int duracionDias) { this.duracionDias = duracionDias; }
    public void setVia(String via) { this.via = via; }
    public void setIndicaciones(String indicaciones) { this.indicaciones = indicaciones; }
}
