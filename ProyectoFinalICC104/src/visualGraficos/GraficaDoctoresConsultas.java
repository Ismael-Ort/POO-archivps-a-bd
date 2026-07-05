package visualGraficos;

import java.awt.BorderLayout;
import java.util.HashMap;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.category.DefaultCategoryDataset;

import logico.Clinica;
import logico.Consulta;
import logico.Paciente;
import logico.Doctor;

public class GraficaDoctoresConsultas extends JFrame {

    private static final long serialVersionUID = 1L;

    private JFreeChart chart;
    private ChartPanel chartPanel;

    public GraficaDoctoresConsultas() {

        setTitle("Doctores con Más Consultas");
        setSize(800, 600);
        setLayout(new BorderLayout());

        DefaultCategoryDataset ds = cargarDatos();

        chart = ChartFactory.createBarChart(
                "Doctores con Más Consultas",
                "Doctor",
                "Cantidad de Consultas",
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
            if (p.getHistoriaClinica() != null) {
                for (Consulta c : p.getHistoriaClinica().getConsultas()) {

                    Doctor doc = c.getDoctor();

                    if (doc != null) {
                        String nombre = doc.getNombre() + " " + doc.getApellido();

                        contador.put(
                            nombre,
                            contador.getOrDefault(nombre, 0) + 1
                        );
                    }
                }
            }
        }

        DefaultCategoryDataset ds = new DefaultCategoryDataset();

        for (String doc : contador.keySet()) {
            ds.addValue(contador.get(doc), "Consultas", doc);
        }

        return ds;
    }

    public ChartPanel getPanel() {
        return chartPanel;
    }
}
