package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.JOptionPane;
import logico.Clinica;
import logico.Enfermedad;
import java.awt.Toolkit;
import java.awt.Color;

public class regEnfermedades extends JDialog {

	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtCodigo;
	private JTextField txtNombre;
	private JCheckBox chkBajoVigilancia;
	private JTextArea txtSintomasYSignos;
	private JSpinner spnNivelGravedad;
	private JComboBox<String> cbPotencialPropagacion;
	private JComboBox<String> cbTipo;
	private JButton btnGuardar;
	private Enfermedad enfermedadEditando = null;
	private boolean soloCambiarVigilancia = false;

	/**
	 * Constructor principal para registrar una nueva enfermedad.
	 * Recibe: nada.
	 * Hace: configura la ventana, inicializa todos los componentes visuales,
	 *       define los combos, botones y carga el código automático ENF-X.
	 * Devuelve: nada (constructor).
	 */
	public regEnfermedades() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(regEnfermedades.class.getResource("/recursos/enfc.jpg")));
		setTitle("Registrar Enfermedad");
		setBounds(100, 100, 650, 500);
		setLocationRelativeTo(null);
		setModal(true);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(240, 248, 255));
		contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		Font labelFont = new Font("Tahoma", Font.PLAIN, 12);

		// CÃ³digo
		JLabel lblCodigo = new JLabel("Código:");
		lblCodigo.setFont(labelFont);
		lblCodigo.setBounds(20, 20, 80, 22);
		contentPanel.add(lblCodigo);

		txtCodigo = new JTextField();
		txtCodigo.setBackground(new Color(224, 255, 255));
		txtCodigo.setEditable(false);
		txtCodigo.setBounds(120, 20, 150, 22);
		contentPanel.add(txtCodigo);

		// Nombre
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setFont(labelFont);
		lblNombre.setBounds(20, 55, 80, 22);
		contentPanel.add(lblNombre);

		txtNombre = new JTextField();
		txtNombre.setBounds(120, 55, 480, 22);
		contentPanel.add(txtNombre);

		// Bajo vigilancia
		chkBajoVigilancia = new JCheckBox("Enfermedad bajo vigilancia");
		chkBajoVigilancia.setBackground(new Color(240, 248, 255));
		chkBajoVigilancia.setFont(labelFont);
		chkBajoVigilancia.setBounds(120, 85, 250, 22);
		contentPanel.add(chkBajoVigilancia);

		// SÃ­ntomas y signos
		JLabel lblSintomas = new JLabel("Sintomas y signos:");
		lblSintomas.setFont(labelFont);
		lblSintomas.setBounds(20, 120, 120, 22);
		contentPanel.add(lblSintomas);

		txtSintomasYSignos = new JTextArea();
		txtSintomasYSignos.setLineWrap(true);
		txtSintomasYSignos.setWrapStyleWord(true);
		JScrollPane spSintomas = new JScrollPane(txtSintomasYSignos);
		spSintomas.setBounds(120, 145, 480, 90);
		contentPanel.add(spSintomas);

		// Nivel de gravedad
		JLabel lblNivelGravedad = new JLabel("Nivel de gravedad (1-4):");
		lblNivelGravedad.setFont(labelFont);
		lblNivelGravedad.setBounds(20, 245, 160, 22);
		contentPanel.add(lblNivelGravedad);

		spnNivelGravedad = new JSpinner(new SpinnerNumberModel(1, 1, 4, 1));
		spnNivelGravedad.setBounds(190, 245, 60, 22);
		contentPanel.add(spnNivelGravedad);

		// Potencial de propagaciÃ³n
		JLabel lblPotencial = new JLabel("Potencial de propagacion:");
		lblPotencial.setFont(labelFont);
		lblPotencial.setBounds(20, 280, 160, 22);
		contentPanel.add(lblPotencial);

		cbPotencialPropagacion = new JComboBox<>();
		cbPotencialPropagacion.addItem("Bajo");
		cbPotencialPropagacion.addItem("Leve");
		cbPotencialPropagacion.addItem("Medio");
		cbPotencialPropagacion.addItem("Elevado");
		cbPotencialPropagacion.addItem("Alto");
		cbPotencialPropagacion.setBounds(190, 280, 150, 22);
		contentPanel.add(cbPotencialPropagacion);

		// Tipo de enfermedad
		JLabel lblTipo = new JLabel("Tipo de enfermedad:");
		lblTipo.setFont(labelFont);
		lblTipo.setBounds(20, 315, 160, 22);
		contentPanel.add(lblTipo);

		cbTipo = new JComboBox<>();
		cbTipo.addItem("Seleccione...");
		cbTipo.addItem("Autoinmune");
		cbTipo.addItem("Cardiovascular");
		cbTipo.addItem("Congenita");
		cbTipo.addItem("Dermatologica");
		cbTipo.addItem("Endocrina");
		cbTipo.addItem("Gastrointestinal");
		cbTipo.addItem("Genetica");
		cbTipo.addItem("Hematologica");
		cbTipo.addItem("Iatrogenica");
		cbTipo.addItem("Inmunologica");
		cbTipo.addItem("Metabolica");
		cbTipo.addItem("Musculoesqueletica");
		cbTipo.addItem("Neoplasia o cancer");
		cbTipo.addItem("Neurologica");
		cbTipo.addItem("Nutricional");
		cbTipo.addItem("Oftalmologica");
		cbTipo.addItem("Otorrinolaringologica");
		cbTipo.addItem("Profesional");
		cbTipo.addItem("Psiquiatrica");
		cbTipo.addItem("Rara");
		cbTipo.addItem("Renal o urinaria");
		cbTipo.addItem("Respiratoria");
		cbTipo.addItem("Reumatologica");
		cbTipo.addItem("Toxicologica");
		cbTipo.addItem("Traumatica");
		cbTipo.addItem("Infecciosa");
		cbTipo.setBounds(190, 315, 250, 22);
		contentPanel.add(cbTipo);

		// Botones
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(255, 255, 224));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		btnGuardar = new JButton("Registrar");
		btnGuardar.setBackground(new Color(255, 228, 225));
		btnGuardar.addActionListener(e -> guardarEnfermedad());
		buttonPane.add(btnGuardar);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBackground(new Color(255, 239, 213));
		btnCancelar.addActionListener(e -> dispose());
		buttonPane.add(btnCancelar);

		inicializarCodigo();
	}

	/**
	 * Constructor usado para editar una enfermedad existente o cambiar solo su estado
	 * de vigilancia.
	 * Recibe: 
	 *   - Enfermedad enfermedad: la enfermedad que se desea modificar.
	 *   - boolean soloCambiarVigilancia: indica si solo se editará el checkbox de vigilancia.
	 * Hace:
	 *   - Carga todos los datos existentes de la enfermedad en los campos.
	 *   - Si soloCambiarVigilancia es true, deshabilita los demás campos y solo permite
	 *     modificar la vigilancia. 
	 * Devuelve: nada (constructor).
	 */
	public regEnfermedades(Enfermedad enfermedad, boolean soloCambiarVigilancia) {
		this();
		this.enfermedadEditando = enfermedad;
		this.soloCambiarVigilancia = soloCambiarVigilancia;

		if (enfermedad != null) {
			txtCodigo.setText(enfermedad.getCodigoEnfermedad());
			txtNombre.setText(enfermedad.getNombre());
			chkBajoVigilancia.setSelected(enfermedad.isBajoVigilancia());
			txtSintomasYSignos.setText(enfermedad.getSintomasYSignos());
			spnNivelGravedad.setValue(enfermedad.getNivelGravedad());
			cbPotencialPropagacion.setSelectedItem(enfermedad.getPotencialPropagacion());
			cbTipo.setSelectedItem(enfermedad.getTipo());
		}

		if (soloCambiarVigilancia) {
			setTitle("Modificar Vigilancia de Enfermedad");
			txtCodigo.setEnabled(false);
			txtNombre.setEnabled(false);
			txtSintomasYSignos.setEnabled(false);
			spnNivelGravedad.setEnabled(false);
			cbPotencialPropagacion.setEnabled(false);
			cbTipo.setEnabled(false);
			btnGuardar.setText("Guardar");
		} else {
			setTitle("Editar Enfermedad");
			btnGuardar.setText("Guardar");
		}
	}

	/**
	 * Genera y muestra un nuevo código automático para una enfermedad.
	 * Recibe: nada.
	 * Hace: toma el contador global de enfermedades de Clinica y arma el código ENF-XXX.
	 * Devuelve: nada.
	 */
	private void inicializarCodigo() {
		String codigo = "ENF-" + Clinica.getInstance().contadorEnfermedades;
		txtCodigo.setText(codigo);
	}

	/**
	 * Guarda o actualiza la información de una enfermedad.
	 * Recibe: nada.
	 * Hace: 
	 *   - Si se está editando solo la vigilancia, actualiza únicamente ese atributo.
	 *   - Si es un registro nuevo o una edición completa:
	 *       * Valida que nombre, síntomas y tipo estén completos.
	 *       * Toma todos los valores del formulario.
	 *       * Crea un objeto Enfermedad.
	 *       * Llama a Clinica.registrarEnfermedad() para guardarla.
	 *       * Limpia los campos y genera un nuevo código si el registro fue exitoso.
	 * Devuelve: nada.
	 */
	private void guardarEnfermedad() {
		if (enfermedadEditando != null && soloCambiarVigilancia) {
			boolean nuevoEstado = chkBajoVigilancia.isSelected();
			enfermedadEditando.setBajoVigilancia(nuevoEstado);

			JOptionPane.showMessageDialog(this, "Estado de vigilancia actualizado correctamente.", "Exito",
					JOptionPane.INFORMATION_MESSAGE);
			dispose();
			return;
		}

		String codigo = txtCodigo.getText().trim();
		String nombre = txtNombre.getText().trim();
		boolean bajoVigilancia = chkBajoVigilancia.isSelected();
		String sintomas = txtSintomasYSignos.getText().trim();
		int nivelGravedad = (int) spnNivelGravedad.getValue();
		String potencial = (String) cbPotencialPropagacion.getSelectedItem();
		String tipo = (String) cbTipo.getSelectedItem();

		if (nombre.isEmpty()) {
			JOptionPane.showMessageDialog(this, "El nombre de la enfermedad es obligatorio.", "Dato incompleto",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (sintomas.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Debe especificar los sintomas y signos de la enfermedad.",
					"Dato incompleto", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (cbTipo.getSelectedIndex() == 0) {
			JOptionPane.showMessageDialog(this, "Debe seleccionar el tipo de enfermedad.", "Dato incompleto",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		Enfermedad nueva = new Enfermedad(codigo, nombre, bajoVigilancia, sintomas, nivelGravedad, potencial, tipo);

		boolean agregado = Clinica.getInstance().registrarEnfermedad(nueva);

		if (agregado) {
			JOptionPane.showMessageDialog(this, "Enfermedad registrada correctamente.", "Exito",
					JOptionPane.INFORMATION_MESSAGE);

			inicializarCodigo();
			txtNombre.setText("");
			chkBajoVigilancia.setSelected(false);
			txtSintomasYSignos.setText("");
			spnNivelGravedad.setValue(1);
			cbPotencialPropagacion.setSelectedIndex(0);
			cbTipo.setSelectedIndex(0);
			txtNombre.requestFocus();

		} else {
			JOptionPane.showMessageDialog(this, "Ya existe una enfermedad con ese codigo o nombre.",
					"Error al registrar", JOptionPane.ERROR_MESSAGE);
		}
	}
}