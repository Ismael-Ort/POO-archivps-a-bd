package visual;

///Subida de clase
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import logico.Clinica;
import logico.Medicamento;
import logico.Tratamiento;

import java.awt.*;
import java.util.ArrayList;

public class regTratamiento extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();

    private JTextField txtNombre;
    private JTextField txtDuracion;
    private JTextArea txtDescripcion;
    private JTextArea txtIndicaciones;
    private JList<String> listMedicamentos;
    private DefaultListModel<String> modeloLista;

    private ArrayList<Medicamento> medicamentos = new ArrayList<>();
    private Tratamiento tratamientoEditar;

    /**
     * Constructor para registrar un nuevo tratamiento.
     */
    public regTratamiento() {
        setTitle("Agregar Tratamiento");
        setBounds(100, 100, 560, 560);
        setModal(true);
        setResizable(false);
        setLocationRelativeTo(null);

        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(224, 255, 224));
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        construirFormulario();
    }

    /**
     * Constructor para modificar un tratamiento existente.
     */
    public regTratamiento(Tratamiento existente) {
        this.tratamientoEditar = existente;

        setTitle("Modificar Tratamiento");
        setBounds(100, 100, 560, 560);
        setModal(true);
        setResizable(false);
        setLocationRelativeTo(null);

        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(224, 255, 224));
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        construirFormulario();
        cargarDatos(existente);
    }

    private void construirFormulario() {

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(20, 20, 100, 25);
        contentPanel.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(130, 20, 380, 25);
        contentPanel.add(txtNombre);

        JLabel lblDuracion = new JLabel("Duracion:");
        lblDuracion.setBounds(20, 60, 100, 25);
        contentPanel.add(lblDuracion);

        txtDuracion = new JTextField();
        txtDuracion.setBounds(130, 60, 200, 25);
        contentPanel.add(txtDuracion);

        JLabel lblDescripcion = new JLabel("Descripcion:");
        lblDescripcion.setBounds(20, 100, 100, 25);
        contentPanel.add(lblDescripcion);

        txtDescripcion = new JTextArea();
        txtDescripcion.setLineWrap(true);
        txtDescripcion.setWrapStyleWord(true);
        JScrollPane spDescripcion = new JScrollPane(txtDescripcion);
        spDescripcion.setBounds(130, 100, 380, 70);
        contentPanel.add(spDescripcion);

        JLabel lblIndicaciones = new JLabel("Indicaciones:");
        lblIndicaciones.setBounds(20, 180, 100, 25);
        contentPanel.add(lblIndicaciones);

        txtIndicaciones = new JTextArea();
        txtIndicaciones.setLineWrap(true);
        txtIndicaciones.setWrapStyleWord(true);
        JScrollPane spIndicaciones = new JScrollPane(txtIndicaciones);
        spIndicaciones.setBounds(130, 180, 380, 70);
        contentPanel.add(spIndicaciones);

        JLabel lblMedicamentos = new JLabel("Medicamentos:");
        lblMedicamentos.setBounds(20, 260, 150, 25);
        contentPanel.add(lblMedicamentos);

        modeloLista = new DefaultListModel<>();
        listMedicamentos = new JList<>(modeloLista);
        JScrollPane spLista = new JScrollPane(listMedicamentos);
        spLista.setBounds(20, 290, 490, 130);
        contentPanel.add(spLista);

        JButton btnAgregarMed = new JButton("Agregar medicamento");
        btnAgregarMed.setBounds(20, 430, 220, 28);
        btnAgregarMed.addActionListener(e -> agregarMedicamento());
        contentPanel.add(btnAgregarMed);

        JButton btnQuitarMed = new JButton("Quitar medicamento");
        btnQuitarMed.setBounds(250, 430, 220, 28);
        btnQuitarMed.addActionListener(e -> quitarMedicamento());
        contentPanel.add(btnQuitarMed);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(new Color(240, 255, 240));
        getContentPane().add(panelBotones, BorderLayout.SOUTH);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardar());
        panelBotones.add(btnGuardar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        panelBotones.add(btnCancelar);
    }

    private void cargarDatos(Tratamiento t) {
        txtNombre.setText(t.getNombreTratamiento());
        txtDuracion.setText(t.getDuracion());
        txtDescripcion.setText(t.getDescripcion());
        txtIndicaciones.setText(t.getIndicaciones());

        medicamentos = new ArrayList<>(t.getMedicamentos());
        actualizarListaMedicamentos();
    }

    private void agregarMedicamento() {
        regMedicamento dialogo = new regMedicamento();
        dialogo.setVisible(true);

        Medicamento nuevo = dialogo.getMedicamento();
        if (nuevo != null) {
            medicamentos.add(nuevo);
            actualizarListaMedicamentos();
        }
    }

    private void quitarMedicamento() {
        int seleccionado = listMedicamentos.getSelectedIndex();

        if (seleccionado == -1) {
            JOptionPane.showMessageDialog(this, "Seleccione un medicamento de la lista", "Sin seleccion", JOptionPane.WARNING_MESSAGE);
            return;
        }

        medicamentos.remove(seleccionado);
        actualizarListaMedicamentos();
    }

    private void actualizarListaMedicamentos() {
        modeloLista.clear();
        for (Medicamento m : medicamentos) {
            modeloLista.addElement(m.getNombre() + " (" + m.getDosisMg() + "mg cada " + m.getFrecuenciaHoras() + "h)");
        }
    }

    private void guardar() {
        String nombre = txtNombre.getText().trim();
        String duracion = txtDuracion.getText().trim();
        String descripcion = txtDescripcion.getText().trim();
        String indicaciones = txtIndicaciones.getText().trim();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el nombre del tratamiento.");
            return;
        }

        if (duracion.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar la duracion del tratamiento.");
            return;
        }

        if (tratamientoEditar != null) {
            tratamientoEditar.setNombreTratamiento(nombre);
            tratamientoEditar.setDuracion(duracion);
            tratamientoEditar.setDescripcion(descripcion);
            tratamientoEditar.setIndicaciones(indicaciones);
            tratamientoEditar.setMedicamentos(new ArrayList<>(medicamentos));
            dispose();
            return;
        }

        String codigo = "TRAT-" + Clinica.getInstance().getContadorTratamientos();
        Tratamiento nuevo = new Tratamiento(codigo, nombre, descripcion, indicaciones, duracion);
        nuevo.setMedicamentos(new ArrayList<>(medicamentos));

        Clinica.getInstance().agregarTratamiento(nuevo);
        dispose();
    }
}
