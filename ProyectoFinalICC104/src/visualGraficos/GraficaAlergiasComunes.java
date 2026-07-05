package visualGraficos;

import java.awt.BorderLayout;
import java.util.HashMap;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import logico.Clinica;
import logico.Paciente;
import logico.Alergia;

public class GraficaAlergiasComunes extends JFrame {

    private static final long serialVersionUID = 1L;

    private JFreeChart chart;
    private ChartPanel chartPanel;

    public GraficaAlergiasComunes() {

        setTitle("Alergias Más Comunes");
        setSize(800, 600);
        setLayout(new BorderLayout());

        DefaultCategoryDataset ds = cargarDatos();

        chart = ChartFactory.createBarChart(
                "Alergias Más Comunes",
                "Alergia",
                "Cantidad de Pacientes",
                ds,
                org.jfree.chart.plot.PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        chartPanel = new ChartPanel(chart);
    }

    private DefaultCategoryDataset cargarDatos() {

        HashMap<String, Integer> contador = new HashMap<>();

        for (Paciente p : Clinica.getInstance().getPacientes()) {

            if (p.getAlergias() != null) {
                for (Alergia a : p.getAlergias()) {

                    String nombre = a.getNombre();

                    contador.put(nombre, contador.getOrDefault(nombre, 0) + 1);
                }
            }
        }

        DefaultCategoryDataset ds = new DefaultCategoryDataset();

        for (String alergia : contador.keySet()) {
            ds.addValue(contador.get(alergia), "Pacientes", alergia);
        }

        return ds;
    }

    public ChartPanel getPanel() {
        return chartPanel;
    }
}
