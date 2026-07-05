package visualGraficos;

import java.awt.BorderLayout;
import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import logico.Clinica;
import logico.Paciente;
import logico.Consulta;

public class GraficaCitasAtendidas extends JFrame {

    private static final long serialVersionUID = 1L;

    private JFreeChart chart;
    private ChartPanel chartPanel;

    public GraficaCitasAtendidas() {

        setTitle("Citas Atendidas vs No Atendidas");
        setSize(800, 600);
        setLayout(new BorderLayout());

        DefaultPieDataset ds = cargarDatos();

        chart = ChartFactory.createPieChart(
                "Citas Atendidas vs No Atendidas",
                ds,
                true,
                true,
                false
        );

        chartPanel = new ChartPanel(chart);
    }

    private DefaultPieDataset cargarDatos() {

        int atendidas = 0;
        int noAtendidas = 0;

        for (Paciente p : Clinica.getInstance().getPacientes()) {

            if (p.getHistoriaClinica() != null) {

                for (Consulta c : p.getHistoriaClinica().getConsultas()) {

                    if (c.getCita() != null) {

                        String estado = c.getCita().getEstadoCita();

                        if (estado != null && estado.equalsIgnoreCase("Atendida")) {
                            atendidas++;
                        } else {
                            noAtendidas++;
                        }
                    }
                }
            }
        }

        DefaultPieDataset ds = new DefaultPieDataset();
        ds.setValue("Atendidas", atendidas);
        ds.setValue("No Atendidas", noAtendidas);

        return ds;
    }


    public ChartPanel getPanel() {
        return chartPanel;
    }
}
