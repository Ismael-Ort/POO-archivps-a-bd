package visual;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import org.jfree.chart.ChartPanel;

import visualGraficos.GraficaConsultasPorMes;
import visualGraficos.GraficaConsultasEspecialidad;
import visualGraficos.GraficaEnfermedadesDiagnosticas;
import visualGraficos.GraficaEnfermedadesVigilancia;
import visualGraficos.GraficaCitasAtendidas;
import visualGraficos.GraficaPacientesNuevosRecurrentes;
import visualGraficos.GraficaDoctoresConsultas;
import visualGraficos.GraficaSexoPacientes;
import visualGraficos.GraficaAlergiasComunes;

// PDFBOX
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;
import org.apache.pdfbox.pdmodel.graphics.image.LosslessFactory;

public class Reportes extends JDialog {

    private static final long serialVersionUID = 1L;

    private JPanel contentPanel;
    private JPanel panelGrafica;
    private JComboBox<String> cbReportes;

    public Reportes() {
        setTitle("Reportes de la Clínica");
        setBounds(100, 100, 1000, 650);
        setModal(true);
        setLocationRelativeTo(null);

        getContentPane().setLayout(new BorderLayout());

        contentPanel = new JPanel();
        contentPanel.setBackground(new Color(255, 235, 205));
        contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
        contentPanel.setLayout(null);
        getContentPane().add(contentPanel, BorderLayout.CENTER);

        JLabel lblTitulo = new JLabel("Reportes");
        lblTitulo.setFont(new Font("Segoe UI", Font.BOLD, 26));
        lblTitulo.setForeground(new Color(28, 63, 117));
        lblTitulo.setBounds(20, 10, 300, 40);
        contentPanel.add(lblTitulo);

        cbReportes = new JComboBox<>();
        cbReportes.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        cbReportes.setBounds(20, 70, 350, 35);

        cbReportes.addItem("Consultas atendidas por mes");
        cbReportes.addItem("Consultas por especialidad");
        cbReportes.addItem("Enfermedades más diagnosticadas");
        cbReportes.addItem("Enfermedades bajo vigilancia");
        cbReportes.addItem("Citas atendidas vs no atendidas");
        cbReportes.addItem("Pacientes nuevos vs recurrentes");
        cbReportes.addItem("Doctores con más consultas");
        cbReportes.addItem("Distribución por sexo");
        cbReportes.addItem("Alergias más comunes");

        contentPanel.add(cbReportes);

        JButton btnGenerar = new JButton("Generar");
        btnGenerar.setBackground(new Color(238, 232, 170));
        btnGenerar.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnGenerar.setBounds(400, 70, 120, 35);
        btnGenerar.addActionListener(e -> generarReporte());
        contentPanel.add(btnGenerar);

        JButton btnPdf = new JButton("Exportar PDF");
        btnPdf.setBackground(new Color(175, 238, 238));
        btnPdf.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        btnPdf.setBounds(540, 70, 150, 35);
        btnPdf.addActionListener(e -> exportarPDF());
        contentPanel.add(btnPdf);

        panelGrafica = new JPanel();
        panelGrafica.setBackground(new Color(240, 255, 255));
        panelGrafica.setBounds(20, 130, 940, 460);
        panelGrafica.setLayout(new BorderLayout());
        panelGrafica.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        contentPanel.add(panelGrafica);
    }

    private void generarReporte() {

        panelGrafica.removeAll();  
        ChartPanel panel = null;

        int opcion = cbReportes.getSelectedIndex();

        switch (opcion) {

            case 0:
                panel = new GraficaConsultasPorMes().getPanel();
                break;

            case 1:
                panel = new GraficaConsultasEspecialidad().getPanel();
                break;

            case 2:
                panel = new GraficaEnfermedadesDiagnosticas().getPanel();
                break;

            case 3:
                panel = new GraficaEnfermedadesVigilancia().getPanel();
                break;

            case 4:
                panel = new GraficaCitasAtendidas().getPanel();
                break;

            case 5:
                panel = new GraficaPacientesNuevosRecurrentes().getPanel();
                break;

            case 6:
                panel = new GraficaDoctoresConsultas().getPanel();
                break;

            case 7:
                panel = new GraficaSexoPacientes().getPanel();
                break;

            case 8:
                panel = new GraficaAlergiasComunes().getPanel();
                break;
        }

        if (panel != null) {
            panelGrafica.add(panel, BorderLayout.CENTER);
        }

        panelGrafica.revalidate();
        panelGrafica.repaint();
    }


    private void exportarPDF() {
        try {
            if (panelGrafica.getComponentCount() == 0) {
                JOptionPane.showMessageDialog(this, "Primero genere un reporte.");
                return;
            }

            // Crear carpeta si no existe
            File carpeta = new File("ReportesPDF");
            if (!carpeta.exists()) {
                carpeta.mkdir();
            }

            // Tomar gráfica
            ChartPanel cp = (ChartPanel) panelGrafica.getComponent(0);
            BufferedImage chartImage = cp.getChart().createBufferedImage(900, 550);

            PDDocument doc = new PDDocument();
            PDPage page = new PDPage(PDRectangle.LETTER);
            doc.addPage(page);

            PDImageXObject pdImage = LosslessFactory.createFromImage(doc, chartImage);
            PDPageContentStream content = new PDPageContentStream(doc, page);

            content.drawImage(pdImage, 30, 140, 550, 400);
            content.close();

            String nombre = cbReportes.getSelectedItem().toString()
                    .replace(" ", "_")
                    .replace("á", "a").replace("é", "e")
                    .replace("í", "i").replace("ó", "o")
                    .replace("ú", "u");

            String nombreBase = "Reporte_" + nombre;

            // Obtener nombre disponible dentro de la carpeta
            String archivo = generarNombreDisponibleEnCarpeta(nombreBase, carpeta);

            doc.save(archivo);
            doc.close();

            JOptionPane.showMessageDialog(this,
                    "PDF exportado exitosamente:\n" + archivo);

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error al exportar PDF.");
        }
    }

    private String generarNombreDisponibleEnCarpeta(String nombreBase, File carpeta) {

        String nombre = carpeta.getPath() + "/" + nombreBase + ".pdf";
        File archivo = new File(nombre);

        int contador = 1;

        while (archivo.exists()) {
            nombre = carpeta.getPath() + "/" + nombreBase + " (" + contador + ").pdf";
            archivo = new File(nombre);
            contador++;
        }

        return nombre;
    }
}
