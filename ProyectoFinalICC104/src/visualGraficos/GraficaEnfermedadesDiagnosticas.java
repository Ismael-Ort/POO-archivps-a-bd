package visualGraficos;

import java.awt.BorderLayout;
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
import logico.Enfermedad;

public class GraficaEnfermedadesDiagnosticas extends JFrame {

    private static final long serialVersionUID = 1L;

    private JFreeChart chart;
    private ChartPanel chartPanel;

    public GraficaEnfermedadesDiagnosticas() {

        setTitle("Enfermedades Más Diagnosticadas");
        setSize(800, 600);
        setLayout(new BorderLayout());

        DefaultCategoryDataset dataset = cargarDatos();

        chart = ChartFactory.createBarChart(
                "Enfermedades Más Diagnosticadas",
                "Enfermedad",
                "Cantidad de Diagnósticos",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        chartPanel = new ChartPanel(chart);
    }

    private DefaultCategoryDataset cargarDatos() {

        HashMap<String, Integer> cont = new HashMap<>();

        for (Paciente p : Clinica.getInstance().getPacientes()) {

            if (p.getHistoriaClinica() != null) {

                for (Consulta c : p.getHistoriaClinica().getConsultas()) {

                    Enfermedad enf = c.getEnfermedad();

                    if (enf != null) {
                        String nombre = enf.getNombre();

                        cont.put(nombre, cont.getOrDefault(nombre, 0) + 1);
                    }
                }
            }
        }

        DefaultCategoryDataset ds = new DefaultCategoryDataset();

        for (String e : cont.keySet()) {
            ds.addValue(cont.get(e), "Diagnósticos", e);
        }

        return ds;
    }

    public ChartPanel getPanel() {
        return chartPanel;
    }
}
