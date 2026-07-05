package visual;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Calendar;
import java.util.Date;
import logico.Clinica;
import logico.Enfermedad;
import logico.Vacuna;

public class regVacuna extends JDialog {

	private JPanel contentPanel = new JPanel();
	private JTextField txtCodigo;
	private JTextField txtNumeroLote;
	private JTextField txtNombre;
	private JSpinner spnCantidad;
	private JSpinner spnFechaCaducidad;
	private JTextField txtLaboratorio;
	private JComboBox<Enfermedad> cbEnfermedad;
	private JButton btnRegistrar;
	private JLabel lblInfo;

	public regVacuna() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(regVacuna.class.getResource("/recursos/agu.jpg")));
		setTitle("Registrar Vacuna");
		setBounds(100, 100, 537, 400);
		setLocationRelativeTo(null);
		setModal(true);
		setResizable(false);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(230, 230, 250));
		contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		// Configurar fuentes
		Font labelFont = new Font("Tahoma", Font.PLAIN, 12);
		Font titleFont = new Font("Tahoma", Font.BOLD, 13);
		Font infoFont = new Font("Tahoma", Font.ITALIC, 11);

		// Título
		JLabel lblTitulo = new JLabel("REGISTRO DE VACUNA");
		lblTitulo.setFont(titleFont);
		lblTitulo.setBounds(180, 10, 200, 20);
		contentPanel.add(lblTitulo);

		// Información
		lblInfo = new JLabel("Campos obligatorios. Seleccione la enfermedad contra la que protege la vacuna.");
		lblInfo.setBackground(new Color(230, 230, 250));
		lblInfo.setFont(infoFont);
		lblInfo.setForeground(Color.BLACK);
		lblInfo.setBounds(20, 35, 450, 15);
		contentPanel.add(lblInfo);

		// Código (no editable)
		JLabel lblCodigo = new JLabel("Código:");
		lblCodigo.setFont(labelFont);
		lblCodigo.setBounds(350, 55, 80, 20);
		contentPanel.add(lblCodigo);

		txtCodigo = new JTextField();
		txtCodigo.setEditable(false);
		txtCodigo.setBounds(350, 75, 120, 25);
		txtCodigo.setBackground(new Color(224, 255, 255));
		contentPanel.add(txtCodigo);

		// Nombre
		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setFont(labelFont);
		lblNombre.setBounds(20, 55, 80, 20);
		contentPanel.add(lblNombre);

		txtNombre = new JTextField();
		txtNombre.setBounds(20, 75, 300, 25);
		contentPanel.add(txtNombre);

		// Número de lote
		JLabel lblNumeroLote = new JLabel("Número de Lote:");
		lblNumeroLote.setFont(labelFont);
		lblNumeroLote.setBounds(20, 115, 120, 20);
		contentPanel.add(lblNumeroLote);

		txtNumeroLote = new JTextField();
		txtNumeroLote.setBounds(20, 135, 200, 25);
		contentPanel.add(txtNumeroLote);

		// Laboratorio
		JLabel lblLaboratorio = new JLabel("Laboratorio:");
		lblLaboratorio.setFont(labelFont);
		lblLaboratorio.setBounds(240, 115, 100, 20);
		contentPanel.add(lblLaboratorio);

		txtLaboratorio = new JTextField();
		txtLaboratorio.setBounds(240, 135, 230, 25);
		contentPanel.add(txtLaboratorio);

		// Enfermedad (COMBOBOX en lugar de TEXTFIELD)
		JLabel lblEnfermedad = new JLabel("Enfermedad:");
		lblEnfermedad.setFont(labelFont);
		lblEnfermedad.setBounds(20, 175, 100, 20);
		contentPanel.add(lblEnfermedad);

		cbEnfermedad = new JComboBox<>();
		cbEnfermedad.setBounds(20, 195, 300, 25);
		cbEnfermedad.setToolTipText("Seleccione la enfermedad contra la que protege esta vacuna");
		cargarEnfermedades();
		contentPanel.add(cbEnfermedad);

		// Botón para agregar nueva enfermedad si no existe
		JButton btnNuevaEnfermedad = new JButton("Nueva Enfermedad");
		btnNuevaEnfermedad.setBounds(332, 195, 170, 25);
		btnNuevaEnfermedad.addActionListener(e -> abrirRegistroEnfermedad());
		contentPanel.add(btnNuevaEnfermedad);

		// Cantidad
		JLabel lblCantidad = new JLabel("Cantidad:");
		lblCantidad.setFont(labelFont);
		lblCantidad.setBounds(350, 235, 80, 20);
		contentPanel.add(lblCantidad);

		spnCantidad = new JSpinner(new SpinnerNumberModel(1, 1, 100000, 1));
		spnCantidad.setBounds(326, 255, 120, 25);
		contentPanel.add(spnCantidad);

		// Fecha de caducidad
		JLabel lblFechaCad = new JLabel("Fecha de Caducidad:");
		lblFechaCad.setFont(labelFont);
		lblFechaCad.setBounds(20, 235, 150, 20);
		contentPanel.add(lblFechaCad);

		spnFechaCaducidad = new JSpinner();
		spnFechaCaducidad.setModel(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_YEAR));
		JSpinner.DateEditor editorFecha = new JSpinner.DateEditor(spnFechaCaducidad, "dd/MM/yyyy");
		spnFechaCaducidad.setEditor(editorFecha);
		spnFechaCaducidad.setBounds(20, 255, 150, 25);
		contentPanel.add(spnFechaCaducidad);

		// Panel de botones
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(255, 250, 205));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		btnRegistrar = new JButton("Registrar");
		btnRegistrar.setBackground(new Color(211, 211, 211));
		btnRegistrar.addActionListener(e -> registrarVacuna());
		buttonPane.add(btnRegistrar);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBackground(new Color(255, 228, 196));
		btnCancelar.addActionListener(e -> dispose());
		buttonPane.add(btnCancelar);

		// Inicializar código
		inicializarCodigo();

		// Agregar listeners para validación en tiempo real
		agregarListenersValidacion();

		// Establecer fecha por defecto (6 meses desde hoy)
		establecerFechaPorDefecto();
	}

	private void cargarEnfermedades() {
		cbEnfermedad.removeAllItems();
		cbEnfermedad.addItem(null); // Opción vacía

		for (Enfermedad enfermedad : Clinica.getInstance().getEnfermedades()) {
			if (enfermedad != null) {
				cbEnfermedad.addItem(enfermedad);
			}
		}

		// Personalizar la visualización de las enfermedades en el ComboBox
		cbEnfermedad.setRenderer(new DefaultListCellRenderer() {
			@Override
			public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
					boolean cellHasFocus) {
				super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);

				if (value instanceof Enfermedad) {
					Enfermedad enf = (Enfermedad) value;
					String texto = enf.getNombre();
					if (enf.isBajoVigilancia()) {
						texto += " "; // Icono de advertencia para enfermedades bajo vigilancia
					}
					setText(texto);
				} else if (value == null) {
					setText("-- Seleccione una enfermedad --");
				}
				return this;
			}
		});
	}

	private void abrirRegistroEnfermedad() {
		regEnfermedades dialog = new regEnfermedades();
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);

		// Recargar enfermedades después de cerrar el diálogo
		cargarEnfermedades();
	}

	private void inicializarCodigo() {
		String codigo = "VAC-" + Clinica.getInstance().contadorVacunas;
		txtCodigo.setText(codigo);
	}

	private void establecerFechaPorDefecto() {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 6);
		spnFechaCaducidad.setValue(calendar.getTime());
	}

	private void agregarListenersValidacion() {
		// Validar que los campos obligatorios no estén vacíos
		javax.swing.event.DocumentListener listener = new javax.swing.event.DocumentListener() {
			public void insertUpdate(javax.swing.event.DocumentEvent e) {
				validarCampos();
			}

			public void removeUpdate(javax.swing.event.DocumentEvent e) {
				validarCampos();
			}

			public void changedUpdate(javax.swing.event.DocumentEvent e) {
				validarCampos();
			}
		};

		txtNombre.getDocument().addDocumentListener(listener);
		txtNumeroLote.getDocument().addDocumentListener(listener);
		txtLaboratorio.getDocument().addDocumentListener(listener);

		// Validar selección de enfermedad
		cbEnfermedad.addActionListener(e -> validarCampos());

		// Validar formato del lote en tiempo real
		txtNumeroLote.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
			public void insertUpdate(javax.swing.event.DocumentEvent e) {
				validarFormatoLote();
			}

			public void removeUpdate(javax.swing.event.DocumentEvent e) {
				validarFormatoLote();
			}

			public void changedUpdate(javax.swing.event.DocumentEvent e) {
				validarFormatoLote();
			}
		});
	}

	private void validarCampos() {
		boolean camposValidos = !txtNombre.getText().trim().isEmpty() && !txtNumeroLote.getText().trim().isEmpty()
				&& !txtLaboratorio.getText().trim().isEmpty() && cbEnfermedad.getSelectedItem() != null;

		btnRegistrar.setEnabled(camposValidos);
	}

	private void validarFormatoLote() {
		String lote = txtNumeroLote.getText().trim();
		if (!lote.isEmpty()) {
			if (!lote.matches(".*[A-Za-z].*") || !lote.matches(".*[0-9].*")) {
				lblInfo.setText("El lote debe contener letras y números");
				lblInfo.setForeground(Color.RED);
			} else {
				lblInfo.setText("* Campos obligatorios. Seleccione la enfermedad contra la que protege la vacuna.");
				lblInfo.setForeground(Color.BLUE);
			}
		}
	}

	// ============================================================
	// REEMPLAZAR el método registrarVacuna() en regVacuna.java
	// ============================================================

	private void registrarVacuna() {
		// Validaciones completas
		if (!validarDatos()) {
			return;
		}

		try {
			String codigo = txtCodigo.getText().trim();
			String numeroLote = txtNumeroLote.getText().trim().toUpperCase();
			String nombre = txtNombre.getText().trim();
			int cantidad = (int) spnCantidad.getValue();
			Date fechaCad = (Date) spnFechaCaducidad.getValue();
			String laboratorio = txtLaboratorio.getText().trim();

			// Obtener enfermedad seleccionada
			Enfermedad enfermedadSeleccionada = (Enfermedad) cbEnfermedad.getSelectedItem();
			String nombreEnfermedad = enfermedadSeleccionada.getNombre();

			// ========== VALIDAR LOTE DUPLICADO ==========
			if (Clinica.getInstance().isLoteRegistrado(numeroLote)) {
				JOptionPane.showMessageDialog(this,
						"Ya existe una vacuna con este número de lote: " + numeroLote + "\n\n"
								+ "Cada lote debe ser único en el sistema.",
						"Lote Duplicado", JOptionPane.ERROR_MESSAGE);
				txtNumeroLote.requestFocus();
				txtNumeroLote.selectAll();
				return;
			}

			// ========== VALIDAR FECHA DE CADUCIDAD ==========
			if (fechaCad.before(new Date())) {
				int respuesta = JOptionPane.showConfirmDialog(this,
						"La fecha de caducidad es pasada.\n" + "Esta vacuna ya está vencida.\n\n"
								+ "¿Desea registrarla de todas formas?",
						"Fecha Caducada", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

				if (respuesta != JOptionPane.YES_OPTION) {
					spnFechaCaducidad.requestFocus();
					return;
				}
			}

			// ========== CREAR VACUNA ==========
			Vacuna nueva = new Vacuna();
			nueva.setCodigoVacuna(codigo);
			nueva.setNumeroLote(numeroLote);
			nueva.setNombre(nombre);
			nueva.setCantidad(cantidad);
			nueva.setFechaCaducidad(fechaCad);
			nueva.setActiva(true);
			nueva.setLaboratorio(laboratorio);
			nueva.setEnfermedad(nombreEnfermedad);

			// ========== REGISTRAR ==========
			boolean registrado = Clinica.getInstance().agregarVacuna(nueva);

			if (registrado) {
				String estado = nueva.estaCaducada() ? " (CADUCADA)" : "";

				JOptionPane.showMessageDialog(this,
						"Vacuna registrada exitosamente" + estado + "\n\n" + "Código: " + codigo + "\n" + "Nombre: "
								+ nombre + "\n" + "Lote: " + numeroLote + "\n" + "Cantidad: " + cantidad + "\n"
								+ "Protege contra: " + nombreEnfermedad,
						"Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);

				limpiarFormulario();
				inicializarCodigo();
				establecerFechaPorDefecto();
				txtNombre.requestFocus();
			} else {
				JOptionPane.showMessageDialog(this, "Error: Ya existe una vacuna con este código.", "Error de Registro",
						JOptionPane.ERROR_MESSAGE);
				Clinica.getInstance().contadorVacunas--; // Revertir contador
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error inesperado: " + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
			Clinica.getInstance().contadorVacunas--; // Revertir contador
		}
	}

	private boolean validarDatos() {
		// Validar campos obligatorios
		if (txtNombre.getText().trim().isEmpty()) {
			mostrarError("El nombre de la vacuna es obligatorio.", txtNombre);
			return false;
		}

		if (txtNumeroLote.getText().trim().isEmpty()) {
			mostrarError("El número de lote es obligatorio.", txtNumeroLote);
			return false;
		}

		if (txtLaboratorio.getText().trim().isEmpty()) {
			mostrarError("El laboratorio es obligatorio.", txtLaboratorio);
			return false;
		}

		if (cbEnfermedad.getSelectedItem() == null) {
			JOptionPane.showMessageDialog(this, "Debe seleccionar la enfermedad contra la que protege la vacuna.",
					"Enfermedad Requerida", JOptionPane.WARNING_MESSAGE);
			cbEnfermedad.requestFocus();
			return false;
		}

		// Validar cantidad
		int cantidad = (int) spnCantidad.getValue();
		if (cantidad <= 0) {
			JOptionPane.showMessageDialog(this, "La cantidad debe ser mayor a 0.", "Cantidad Inválida",
					JOptionPane.ERROR_MESSAGE);
			spnCantidad.requestFocus();
			return false;
		}

		// Validar formato del número de lote (debe contener letras y números)
		String lote = txtNumeroLote.getText().trim();
		if (!lote.matches(".*[A-Za-z].*") || !lote.matches(".*[0-9].*")) {
			JOptionPane.showMessageDialog(this,
					"El número de lote debe contener letras y números.\n" + "Ejemplo: AB123, XYZ456, LOTE789",
					"Formato Inválido", JOptionPane.ERROR_MESSAGE);
			txtNumeroLote.requestFocus();
			txtNumeroLote.selectAll();
			return false;
		}

		return true;
	}

	private void mostrarError(String mensaje, JTextField campo) {
		JOptionPane.showMessageDialog(this, mensaje, "Dato Requerido", JOptionPane.WARNING_MESSAGE);
		campo.requestFocus();
		campo.selectAll();
	}

	private void limpiarFormulario() {
		txtNombre.setText("");
		txtNumeroLote.setText("");
		txtLaboratorio.setText("");
		cbEnfermedad.setSelectedIndex(0);
		spnCantidad.setValue(1);
	}
}