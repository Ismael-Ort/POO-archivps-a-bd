package visualGraficos;

import java.awt.BorderLayout;
import java.util.Map;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import logico.Clinica;

public class GraficaEnfermedadesVigilancia extends JFrame {

    private static final long serialVersionUID = 1L;

    private JFreeChart chart;
    private ChartPanel chartPanel;

    public GraficaEnfermedadesVigilancia() {

        setTitle("Enfermedades Bajo Vigilancia");
        setSize(800, 600);
        setLayout(new BorderLayout());

        DefaultPieDataset dataset = cargarDatos();

        chart = ChartFactory.createPieChart(
                "Enfermedades Bajo Vigilancia",
                dataset,
                true,
                true,
                false
        );

        chartPanel = new ChartPanel(chart);
    }

    private DefaultPieDataset cargarDatos() {

        DefaultPieDataset ds = new DefaultPieDataset();

        for (Map.Entry<String, Integer> entry :
                Clinica.getInstance().getReporteEnfermedades().entrySet()) {

            ds.setValue(entry.getKey(), entry.getValue());
        }

        return ds;
    }

    public ChartPanel getPanel() {
        return chartPanel;
    }
}
