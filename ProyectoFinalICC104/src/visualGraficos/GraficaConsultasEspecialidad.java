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
import logico.Doctor;
import logico.Paciente;

public class GraficaConsultasEspecialidad extends JFrame {

    private static final long serialVersionUID = 1L;

    private JFreeChart chart;
    private ChartPanel chartPanel;

    public GraficaConsultasEspecialidad() {

        setTitle("Consultas por Especialidad");
        setSize(800, 600);
        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        DefaultCategoryDataset dataset = cargarDatos();

        chart = ChartFactory.createBarChart(
                "Consultas por Especialidad",
                "Especialidad",
                "Cantidad de Consultas",
                dataset,
                PlotOrientation.VERTICAL,
                false,
                true,
                false
        );

        chartPanel = new ChartPanel(chart);
    }

    // =================================================================
    //      CARGAR DATOS DESDE EL HISTORIAL DE CADA PACIENTE
    // =================================================================
    private DefaultCategoryDataset cargarDatos() {

        HashMap<String, Integer> contadorEspecialidades = new HashMap<>();

        // Recorrer todos los pacientes
        for (Paciente p : Clinica.getInstance().getPacientes()) {

            if (p.getHistoriaClinica() != null) {
                for (Consulta c : p.getHistoriaClinica().getConsultas()) {

                    Doctor doc = c.getDoctor();  // El doctor que atendió la consulta

                    if (doc != null) {
                        String esp = doc.getEspecialidad();

                        contadorEspecialidades.put(
                                esp,
                                contadorEspecialidades.getOrDefault(esp, 0) + 1
                        );
                    }
                }
            }
        }

        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        for (String especialidad : contadorEspecialidades.keySet()) {
            dataset.addValue(contadorEspecialidades.get(especialidad),
                    "Consultas", especialidad);
        }

        return dataset;
    }

    // =================================================================
    //      DEVOLVER EL PANEL PARA QUE REPORTES LO USE
    // =================================================================
    public ChartPanel getPanel() {
        return chartPanel;
    }
}
