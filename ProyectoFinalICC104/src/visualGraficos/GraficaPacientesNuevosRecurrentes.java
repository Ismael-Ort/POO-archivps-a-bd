package visualGraficos;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import logico.Clinica;
import logico.Paciente;

public class GraficaPacientesNuevosRecurrentes extends JFrame {

    private static final long serialVersionUID = 1L;

    private JFreeChart chart;
    private ChartPanel chartPanel;

    public GraficaPacientesNuevosRecurrentes() {

        setTitle("Pacientes Nuevos vs Recurrentes");
        setSize(800, 600);
        setLayout(new BorderLayout());

        DefaultPieDataset ds = cargarDatos();

        chart = ChartFactory.createPieChart(
                "Pacientes Nuevos vs Recurrentes",
                ds,
                true,
                true,
                false
        );

        chartPanel = new ChartPanel(chart);
    }

    private DefaultPieDataset cargarDatos() {

        int nuevos = 0;
        int recurrentes = 0;

        for (Paciente p : Clinica.getInstance().getPacientes()) {

            if (p.getHistoriaClinica() != null) {
                int totalConsultas = p.getHistoriaClinica().getConsultas().size();

                if (totalConsultas <= 1) nuevos++;
                else recurrentes++;
            }
        }

        DefaultPieDataset ds = new DefaultPieDataset();
        ds.setValue("Nuevos", nuevos);
        ds.setValue("Recurrentes", recurrentes);

        return ds;
    }

    public ChartPanel getPanel() {
        return chartPanel;
    }
}
