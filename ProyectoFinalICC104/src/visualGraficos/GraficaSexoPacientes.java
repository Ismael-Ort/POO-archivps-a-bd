package visualGraficos;

import java.awt.BorderLayout;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.general.DefaultPieDataset;

import logico.Clinica;
import logico.Paciente;

public class GraficaSexoPacientes extends JFrame {

    private static final long serialVersionUID = 1L;

    private JFreeChart chart;
    private ChartPanel chartPanel;

    public GraficaSexoPacientes() {

        setTitle("Distribución por Sexo");
        setSize(800, 600);
        setLayout(new BorderLayout());

        DefaultPieDataset dataset = cargarDatos();

        chart = ChartFactory.createPieChart(
                "Distribución por Sexo",
                dataset,
                true,
                true,
                false
        );

        chartPanel = new ChartPanel(chart);
    }

    // ============================================================
    //                   CARGAR DATOS DESDE LA CLÍNICA
    // ============================================================
    private DefaultPieDataset cargarDatos() {

        int masculino = 0;
        int femenino = 0;
        int otro = 0;

        for (Paciente p : Clinica.getInstance().getPacientes()) {

            char sexo = p.getSexo();   // ← AQUÍ USAMOS CHAR

            if (sexo == 'M' || sexo == 'm') {
                masculino++;
            }
            else if (sexo == 'F' || sexo == 'f') {
                femenino++;
            }
            else {
                otro++;
            }
        }

        DefaultPieDataset ds = new DefaultPieDataset();

        ds.setValue("Masculino", masculino);
        ds.setValue("Femenino", femenino);
        ds.setValue("Otro", otro);

        return ds;
    }

    // ============================================================
    //                  DEVOLVER PANEL A REPORTES
    // ============================================================
    public ChartPanel getPanel() {
        return chartPanel;
    }
}
