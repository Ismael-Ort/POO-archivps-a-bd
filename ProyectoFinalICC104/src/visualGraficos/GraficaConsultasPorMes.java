package visualGraficos;

import java.awt.BorderLayout;
import java.time.LocalDate;
import java.time.Month;
import java.util.HashMap;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import logico.Clinica;
import logico.Consulta;
import logico.Paciente;

public class GraficaConsultasPorMes extends JFrame {

    private static final long serialVersionUID = 1L;

    private JFreeChart chart;
    private ChartPanel chartPanel;

    public GraficaConsultasPorMes() {

        // Esta ventana no se usa directamente, pero la clase sí
        setTitle("Consultas atendidas por mes");
        setSize(800, 600);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultCategoryDataset dataset = cargarDatos();

        chart = ChartFactory.createBarChart(
                "Consultas atendidas por mes",
                "Mes",
                "Cantidad",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        chartPanel = new ChartPanel(chart);

        // NO se hace add(chartPanel) porque Reportes se encarga
    }

    // ============================================================
    //   Cargar datos desde el historial clínico de cada paciente
    // ============================================================
    private DefaultCategoryDataset cargarDatos() {

        HashMap<Month, Integer> contador = new HashMap<>();

        for (Month m : Month.values()) {
            contador.put(m, 0);
        }

        // TU ESTRUCTURA REAL: Paciente → Historial → Consultas
        for (Paciente p : Clinica.getInstance().getPacientes()) {
            if (p.getHistoriaClinica() != null) {
                for (Consulta c : p.getHistoriaClinica().getConsultas()) {

                    LocalDate fecha = c.getFechaConsulta();
                    Month mes = fecha.getMonth();

                    contador.put(mes, contador.get(mes) + 1);
                }
            }
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (Month m : Month.values()) {
            dataset.addValue(contador.get(m), "Consultas", m.toString());
        }

        return dataset;
    }

    // ============================================================
    //   ESTE MÉTODO ES CLAVE PARA REPORTES.JAVA
    // ============================================================
    public ChartPanel getPanel() {
        return chartPanel;
    }
}
