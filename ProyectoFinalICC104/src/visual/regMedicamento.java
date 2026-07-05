package visual;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import logico.Medicamento;

import java.awt.*;

public class regMedicamento extends JDialog {

    private static final long serialVersionUID = 1L;
    private final JPanel contentPanel = new JPanel();

    private JTextField txtNombre;
    private JSpinner spDosis;
    private JSpinner spFrecuencia;
    private JSpinner spDuracion;
    private JComboBox<String> cbVia;
    private JTextArea txtIndicaciones;

    private Medicamento medicamentoTemporal;
    private Medicamento medicamentoEditar;

    /**
     * Constructor para registrar un nuevo medicamento.
     * Recibe: nada.
     * Hace: configura la ventana, inicializa todos los componentes del formulario
     *       (nombre, dosis, frecuencia, duración, vía, indicaciones).
     * Devuelve: nada (constructor).
     */
    public regMedicamento() {
    	setIconImage(Toolkit.getDefaultToolkit().getImage(regMedicamento.class.getResource("/recursos/agu.jpg")));

        setTitle("Agregar medicamento");
        setBounds(100, 100, 520, 460);
        setModal(true);
        setResizable(false);
        setLocationRelativeTo(null);

        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBackground(new Color(255, 235, 205));
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        construirFormulario();
    }

    /**
     * Constructor que permite editar un medicamento previamente creado.
     * Recibe: un objeto Medicamento existente que se desea modificar.
     * Hace: configura la ventana, inicializa todos los campos y carga en ellos
     *       los valores del medicamento recibido.
     * Devuelve: nada (constructor).
     */
    public regMedicamento(Medicamento existente) {

        this.medicamentoEditar = existente;

        setTitle("Editar medicamento");
        setBounds(100, 100, 520, 460);
        setModal(true);
        setResizable(false);
        setLocationRelativeTo(null);

        getContentPane().setLayout(new BorderLayout());
        contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
        getContentPane().add(contentPanel, BorderLayout.CENTER);
        contentPanel.setLayout(null);

        construirFormulario();
        cargarDatos(existente);
    }
    

    /**
     * Construye todos los elementos visuales del formulario de registro.
     * Recibe: nada.
     * Hace: crea labels, campos de texto, combos, spinners, área de texto,
     *       botones de acción y los agrega al panel principal.
     * Devuelve: nada.
     */
    private void construirFormulario() {

        JLabel lblNombre = new JLabel("Nombre:");
        lblNombre.setBounds(20, 20, 120, 25);
        contentPanel.add(lblNombre);

        txtNombre = new JTextField();
        txtNombre.setBounds(150, 20, 330, 25);
        contentPanel.add(txtNombre);

        JLabel lblVia = new JLabel("Vía:");
        lblVia.setBounds(20, 60, 120, 25);
        contentPanel.add(lblVia);

        cbVia = new JComboBox<>();
        cbVia.setModel(new DefaultComboBoxModel<>(new String[]{
                "< Seleccione >", "Oral", "Intravenosa", "Intramuscular",
                "Subcutánea", "Tópica", "Inhalada", "Rectal", "Oftálmica", "Ótica"
        }));
        cbVia.setBounds(150, 60, 150, 25);
        contentPanel.add(cbVia);

        JLabel lblDuracion = new JLabel("Duración (días):");
        lblDuracion.setBounds(20, 100, 120, 25);
        contentPanel.add(lblDuracion);

        spDuracion = new JSpinner(new SpinnerNumberModel(1, 1, 365, 1));
        spDuracion.setBounds(150, 100, 70, 25);
        contentPanel.add(spDuracion);

        JLabel lblFrecuencia = new JLabel("Cada (horas):");
        lblFrecuencia.setBounds(250, 100, 120, 25);
        contentPanel.add(lblFrecuencia);

        spFrecuencia = new JSpinner(new SpinnerNumberModel(1, 1, 24, 1));
        spFrecuencia.setBounds(360, 100, 70, 25);
        contentPanel.add(spFrecuencia);

        JLabel lblDosis = new JLabel("Dosis (mg):");
        lblDosis.setBounds(20, 140, 120, 25);
        contentPanel.add(lblDosis);

        spDosis = new JSpinner(new SpinnerNumberModel(1.0, 0.1, 2000.0, 0.1));
        spDosis.setBounds(150, 140, 80, 25);
        contentPanel.add(spDosis);

        JLabel lblInd = new JLabel("Indicaciones:");
        lblInd.setBounds(20, 180, 120, 25);
        contentPanel.add(lblInd);

        txtIndicaciones = new JTextArea();
        txtIndicaciones.setLineWrap(true);
        txtIndicaciones.setWrapStyleWord(true);

        JScrollPane spInd = new JScrollPane(txtIndicaciones);
        spInd.setBounds(150, 180, 330, 180);
        contentPanel.add(spInd);

        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelBotones.setBackground(new Color(240, 248, 255));
        getContentPane().add(panelBotones, BorderLayout.SOUTH);

        JButton btnGuardar = new JButton("Guardar");
        btnGuardar.addActionListener(e -> guardar());
        panelBotones.add(btnGuardar);

        JButton btnCancelar = new JButton("Cancelar");
        btnCancelar.addActionListener(e -> dispose());
        panelBotones.add(btnCancelar);
    }

    /**
     * Carga en el formulario los datos de un medicamento existente.
     * Recibe: un objeto Medicamento m que contiene los valores a mostrar.
     * Hace: asigna en los campos del formulario el nombre, dosis, vía,
     *       duración, frecuencia e indicaciones del medicamento.
     * Devuelve: nada.
     */
    private void cargarDatos(Medicamento m) {

        txtNombre.setText(m.getNombre());
        spDosis.setValue(m.getDosisMg());
        spFrecuencia.setValue(m.getFrecuenciaHoras());
        spDuracion.setValue(m.getDuracionDias());
        cbVia.setSelectedItem(m.getVia());
        txtIndicaciones.setText(m.getIndicaciones());
    }

    /**
     * Guarda o actualiza un medicamento según el modo del formulario.
     * Recibe: nada.
     * Hace:
     *   - Valida que el nombre y la vía hayan sido ingresados.
     *   - Toma todos los datos escritos por el usuario.
     *   - Si medicamentoEditar no es null: actualiza ese medicamento.
     *   - Si es un registro nuevo: crea un nuevo objeto Medicamento.
     *   - Guarda temporalmente el medicamento resultante para ser recuperado
     *     mediante getMedicamento().
     *   - Cierra la ventana al finalizar.
     * Devuelve: nada.
     */
    private void guardar() {

        String nombre = txtNombre.getText().trim();
        String via = cbVia.getSelectedItem().toString();
        String indicaciones = txtIndicaciones.getText().trim();

        double dosis = (Double) spDosis.getValue();
        int frecuencia = (Integer) spFrecuencia.getValue();
        int duracion = (Integer) spDuracion.getValue();

        if (nombre.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar el nombre del medicamento.");
            return;
        }

        if (via.equals("< Seleccione >")) {
            JOptionPane.showMessageDialog(this, "Debe seleccionar una vía de administración.");
            return;
        }

        if (medicamentoEditar != null) {
            medicamentoEditar.setNombre(nombre);
            medicamentoEditar.setVia(via);
            medicamentoEditar.setIndicaciones(indicaciones);
            medicamentoEditar.setDosisMg(dosis);
            medicamentoEditar.setFrecuenciaHoras(frecuencia);
            medicamentoEditar.setDuracionDias(duracion);

            medicamentoTemporal = medicamentoEditar;
            dispose();
            return;
        }

        medicamentoTemporal = new Medicamento(nombre, dosis, frecuencia, duracion, via, indicaciones);
        dispose();
    }

    /**
     * Devuelve el medicamento registrado o editado en esta ventana.
     * Recibe: nada.
     * Hace: simplemente retorna la referencia del medicamento temporal.
     * Devuelve: un objeto Medicamento con los datos finales ingresados.
     */
    public Medicamento getMedicamento() {
        return medicamentoTemporal;
    }
}
