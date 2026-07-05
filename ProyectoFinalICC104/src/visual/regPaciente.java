package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;
import logico.Alergia;
import logico.Clinica;
import logico.Control;
import logico.Doctor;
import logico.Paciente;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import java.util.Date;
import java.util.ArrayList;
import java.util.Calendar;
import javax.swing.JComboBox;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.SpinnerNumberModel;
import javax.swing.ButtonGroup;
import java.awt.Toolkit;
import java.awt.event.ItemListener;
import java.awt.event.ItemEvent;
import java.awt.Color;

public class regPaciente extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtCodigo;
	private JTextField txtNombre;
	private JTextField txtApellido;
	private JFormattedTextField txtCedula;
	private JFormattedTextField txtTelefono;
	private JTextArea txtdireccion;
	private ArrayList<Alergia> alegecitas;
	private Paciente pacienteCar = null;
	private JRadioButton rdbtnHombre;
	private JRadioButton rdbtnMujer;
	private JSpinner spnFechaNacimiento;
	private JComboBox<String> cbxTipoSangre;
	private JSpinner spnFechaActual;
	private JSpinner spnEstatura;
	private JSpinner spnPeso;
	private JCheckBox chckbxAlergias;
	private boolean esModificacion = false;
	private JButton btnModificar;

	private String telefonoOriginal = "";
	private String direccionOriginal = "";
	private float pesoOriginal = 0;
	private float estaturaOriginal = 0;
	private boolean esInteresado = false;

	public static void main(String[] args) {
		try {
			regPaciente dialog = new regPaciente(null);
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public regPaciente(Paciente elpaci) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(regPaciente.class.getResource("/recursos/pac.jpg")));
		pacienteCar = elpaci;
		esModificacion = (pacienteCar != null && !pacienteCar.getCodigoPaciente().equals("XX"));
		esInteresado = (pacienteCar != null && pacienteCar.getCodigoPaciente().startsWith("INT-"));

		setTitle(esInteresado ? "Registrar Paciente (Completar Datos)"
				: esModificacion ? "Modificar Paciente" : "Registro de Pacientes");
		setBounds(100, 100, 600, 558);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(220, 220, 220));
		contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		setLocationRelativeTo(null);

		crearComponentes();
		configurarModoEdicion();

		if (pacienteCar != null) {
			cargarPaciente();
		}

		if (esModificacion && !esInteresado) {
			configurarDeteccionCambios();
		}
	}

	private void crearComponentes() {
		JLabel lblCodigo = new JLabel("Código");
		lblCodigo.setBounds(15, 23, 69, 20);
		contentPanel.add(lblCodigo);

		txtCodigo = new JTextField();
		txtCodigo.setBackground(new Color(224, 255, 255));
		txtCodigo.setEnabled(false);
		txtCodigo.setBounds(111, 21, 120, 25);
		contentPanel.add(txtCodigo);
		txtCodigo.setColumns(10);

		// Determinar código inicial
		if (esInteresado) {
			txtCodigo.setText("PAC-" + Clinica.getInstance().contadorPacientes);
		} else {
			txtCodigo.setText("PAC-" + Clinica.getInstance().contadorPacientes);
		}

		JLabel lblNombre = new JLabel("Nombre(s)");
		lblNombre.setBounds(15, 57, 80, 20);
		contentPanel.add(lblNombre);

		txtNombre = new JTextField();
		txtNombre.setBounds(111, 55, 296, 25);
		contentPanel.add(txtNombre);
		txtNombre.setColumns(10);

		JLabel lblApellido = new JLabel("Apellido(s)");
		lblApellido.setBounds(15, 95, 80, 20);
		contentPanel.add(lblApellido);

		txtApellido = new JTextField();
		txtApellido.setBounds(111, 93, 296, 25);
		contentPanel.add(txtApellido);
		txtApellido.setColumns(10);

		JLabel lblCedula = new JLabel("Cédula");
		lblCedula.setBounds(15, 133, 69, 20);
		contentPanel.add(lblCedula);

		try {
			MaskFormatter cedulaMask = new MaskFormatter("###-#######-#");
			cedulaMask.setPlaceholderCharacter('_');
			txtCedula = new JFormattedTextField(cedulaMask);
		} catch (Exception e) {
			txtCedula = new JFormattedTextField();
		}
		txtCedula.setBounds(111, 131, 196, 25);
		contentPanel.add(txtCedula);

		JLabel lblTipoSangre = new JLabel("Tipo de sangre");
		lblTipoSangre.setBounds(322, 133, 100, 20);
		contentPanel.add(lblTipoSangre);

		cbxTipoSangre = new JComboBox<String>();
		cbxTipoSangre.setModel(new DefaultComboBoxModel<String>(
				new String[] { "<Tipo>", "A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-" }));
		cbxTipoSangre.setBounds(450, 128, 100, 25);
		contentPanel.add(cbxTipoSangre);

		JLabel lblEstatura = new JLabel("Estatura(cm)");
		lblEstatura.setBounds(450, 57, 99, 20);
		contentPanel.add(lblEstatura);

		spnEstatura = new JSpinner();
		spnEstatura.setModel(new SpinnerNumberModel(150.0, 1.0, 300.0, 1.0));
		spnEstatura.setBounds(450, 79, 80, 25);
		contentPanel.add(spnEstatura);

		JLabel lblPeso = new JLabel("Peso(lb)");
		lblPeso.setBounds(15, 166, 69, 20);
		contentPanel.add(lblPeso);

		spnPeso = new JSpinner();
		spnPeso.setModel(new SpinnerNumberModel(150.0, 1.0, 500.0, 1.0));
		spnPeso.setBounds(111, 164, 94, 25);
		contentPanel.add(spnPeso);

		JLabel lblFechaNacimiento = new JLabel("Fecha de nacimiento");
		lblFechaNacimiento.setBounds(287, 169, 120, 20);
		contentPanel.add(lblFechaNacimiento);

		spnFechaNacimiento = new JSpinner();
		spnFechaNacimiento.setModel(new SpinnerDateModel(new Date(), null, new Date(), Calendar.DAY_OF_YEAR));
		JSpinner.DateEditor editor = new JSpinner.DateEditor(spnFechaNacimiento, "dd/MM/yyyy");
		spnFechaNacimiento.setEditor(editor);
		spnFechaNacimiento.setBounds(414, 166, 136, 25);
		contentPanel.add(spnFechaNacimiento);

		JLabel lblTelefono = new JLabel("Teléfono");
		lblTelefono.setBounds(15, 214, 69, 20);
		contentPanel.add(lblTelefono);

		try {
			MaskFormatter telefonoMask = new MaskFormatter("(###) ###-####");
			telefonoMask.setPlaceholderCharacter('_');
			txtTelefono = new JFormattedTextField(telefonoMask);
		} catch (Exception e) {
			txtTelefono = new JFormattedTextField();
		}
		txtTelefono.setBounds(111, 212, 196, 25);
		contentPanel.add(txtTelefono);

		JLabel lblSexo = new JLabel("Sexo");
		lblSexo.setBounds(336, 214, 69, 20);
		contentPanel.add(lblSexo);

		rdbtnHombre = new JRadioButton("Masculino");
		rdbtnHombre.setBackground(new Color(220, 220, 220));
		rdbtnHombre.setSelected(true);
		rdbtnHombre.setBounds(388, 212, 94, 25);
		contentPanel.add(rdbtnHombre);

		rdbtnMujer = new JRadioButton("Femenino");
		rdbtnMujer.setBackground(new Color(220, 220, 220));
		rdbtnMujer.setBounds(483, 212, 91, 25);
		contentPanel.add(rdbtnMujer);

		ButtonGroup grupoSexo = new ButtonGroup();
		grupoSexo.add(rdbtnHombre);
		grupoSexo.add(rdbtnMujer);

		JLabel lblDireccion = new JLabel("Dirección");
		lblDireccion.setBounds(15, 287, 69, 20);
		contentPanel.add(lblDireccion);

		JScrollPane scrollDireccion = new JScrollPane();
		scrollDireccion.setBounds(100, 258, 454, 80);
		contentPanel.add(scrollDireccion);

		txtdireccion = new JTextArea();
		scrollDireccion.setViewportView(txtdireccion);
		txtdireccion.setLineWrap(true);
		txtdireccion.setWrapStyleWord(true);

		JLabel lblAlergias = new JLabel("¿Padece de alguna alergia?");
		lblAlergias.setBounds(15, 351, 200, 20);
		contentPanel.add(lblAlergias);

		chckbxAlergias = new JCheckBox("Sí, padezco de alergias");
		chckbxAlergias.setBackground(new Color(220, 220, 220));
		chckbxAlergias.setBounds(222, 349, 200, 25);
		contentPanel.add(chckbxAlergias);

		JLabel lblFecha = new JLabel("Fecha Registro");
		lblFecha.setBounds(15, 400, 100, 20);
		contentPanel.add(lblFecha);

		spnFechaActual = new JSpinner();
		spnFechaActual.setEnabled(false);
		spnFechaActual.setModel(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_YEAR));
		JSpinner.DateEditor editorActual = new JSpinner.DateEditor(spnFechaActual, "dd/MM/yyyy");
		spnFechaActual.setEditor(editorActual);
		spnFechaActual.setBounds(111, 398, 120, 25);
		contentPanel.add(spnFechaActual);

		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(220, 220, 220));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		btnModificar = new JButton(esInteresado ? "Registrar Paciente" : esModificacion ? "Modificar" : "Registrar");
		btnModificar.setBackground(new Color(224, 255, 255));
		btnModificar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				procesarPaciente();
			}
		});
		btnModificar.setActionCommand("OK");
		buttonPane.add(btnModificar);
		getRootPane().setDefaultButton(btnModificar);

		JButton cancelButton = new JButton("Cancelar");
		cancelButton.setBackground(new Color(255, 228, 181));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
	}

	private void configurarModoEdicion() {
		if (esInteresado) {
			// ========== INTERESADO -> Registro completo ==========
			setTitle("Registrar Paciente (Completar Datos)");
			btnModificar.setText("Registrar Paciente");

			// Mostrar mensaje informativo
			JOptionPane.showMessageDialog(this,
					"Este paciente está registrado como INTERESADO.\n"
							+ "Complete TODOS los datos para registrarlo como paciente completo en el sistema.",
					"Completar Registro de Paciente", JOptionPane.INFORMATION_MESSAGE);

			// Campos bloqueados (ya vienen de la cita)
			txtCedula.setEnabled(false);
			txtNombre.setEnabled(false);
			txtApellido.setEnabled(false);
			txtTelefono.setEnabled(false); // Teléfono también viene de la cita

			// HABILITAR todos los demás campos
			cbxTipoSangre.setEnabled(true);
			txtdireccion.setEnabled(true);
			spnFechaNacimiento.setEnabled(true);
			rdbtnHombre.setEnabled(true);
			rdbtnMujer.setEnabled(true);
			spnPeso.setEnabled(true);
			spnEstatura.setEnabled(true);
			chckbxAlergias.setEnabled(true);
			chckbxAlergias.setVisible(true);

		} else if (esModificacion) {
			// ========== PACIENTE REAL -> Modificación limitada ==========
			setTitle("Modificar Paciente");
			btnModificar.setText("Modificar");

			// Campos NO editables
			txtCedula.setEnabled(false);
			txtNombre.setEnabled(false);
			txtApellido.setEnabled(false);
			spnFechaNacimiento.setEnabled(false);
			rdbtnHombre.setEnabled(false);
			rdbtnMujer.setEnabled(false);
			cbxTipoSangre.setEnabled(false);
			chckbxAlergias.setEnabled(false);
			chckbxAlergias.setVisible(false);

			JOptionPane
					.showMessageDialog(this,
							"Para pacientes ya registrados solo puede modificar:\n" + "• Teléfono\n" + "• Dirección\n"
									+ "• Peso\n" + "• Estatura",
							"Modificación Limitada", JOptionPane.INFORMATION_MESSAGE);

		} else if (pacienteCar != null && pacienteCar.getCodigoPaciente().equals("XX")) {
			// Caso especial antiguo
			JOptionPane.showMessageDialog(this, "Complete los datos del paciente", "Información",
					JOptionPane.INFORMATION_MESSAGE);
			txtCedula.setEnabled(false);
			txtNombre.setEnabled(false);
			txtApellido.setEnabled(false);
			txtTelefono.setEnabled(false);
			chckbxAlergias.setEnabled(true);
			chckbxAlergias.setVisible(true);

		} else {
			// ========== NUEVO PACIENTE desde cero ==========
			setTitle("Registro de Nuevo Paciente");
			btnModificar.setText("Registrar");
			// Todos los campos habilitados por defecto
			chckbxAlergias.setEnabled(true);
			chckbxAlergias.setVisible(true);
		}
	}

	private void configurarDeteccionCambios() {
		btnModificar.setEnabled(false);

		DocumentListener cambioListener = new DocumentListener() {
			public void insertUpdate(DocumentEvent e) {
				verificarCambios();
			}

			public void removeUpdate(DocumentEvent e) {
				verificarCambios();
			}

			public void changedUpdate(DocumentEvent e) {
				verificarCambios();
			}
		};

		txtTelefono.getDocument().addDocumentListener(cambioListener);
		txtdireccion.getDocument().addDocumentListener(cambioListener);

		spnPeso.addChangeListener(e -> verificarCambios());
		spnEstatura.addChangeListener(e -> verificarCambios());
	}

	private void verificarCambios() {
		if (!esModificacion || esInteresado)
			return;

		String telefonoActual = txtTelefono.getText().replaceAll("[^0-9]", "");
		String direccionActual = txtdireccion.getText().trim();
		float pesoActual = ((Number) spnPeso.getValue()).floatValue();
		float estaturaActual = ((Number) spnEstatura.getValue()).floatValue();

		boolean hubocambios = !telefonoActual.equals(telefonoOriginal) || !direccionActual.equals(direccionOriginal)
				|| pesoActual != pesoOriginal || estaturaActual != estaturaOriginal;

		btnModificar.setEnabled(hubocambios);
	}

	private void cargarPaciente() {
		if (pacienteCar == null)
			return;

		txtdireccion.setText(pacienteCar.getDireccion() != null ? pacienteCar.getDireccion() : "");
		txtApellido.setText(pacienteCar.getApellido() != null ? pacienteCar.getApellido() : "");
		txtCedula.setText(pacienteCar.getCedula() != null ? pacienteCar.getCedula() : "");

		// Para pacientes interesados, generar código PAC- nuevo
		if (esInteresado) {
			txtCodigo.setText("PAC-" + Clinica.getInstance().contadorPacientes);
		} else if (pacienteCar.getCodigoPaciente().equals("XX")) {
			txtCodigo.setText("PAC-" + Clinica.getInstance().contadorPacientes);
		} else {
			txtCodigo.setText(pacienteCar.getCodigoPaciente());
		}

		txtNombre.setText(pacienteCar.getNombre() != null ? pacienteCar.getNombre() : "");
		txtTelefono.setText(pacienteCar.getTelefono() != null ? pacienteCar.getTelefono() : "");

		if (pacienteCar.getEstatura() > 0) {
			spnEstatura.setValue((double) pacienteCar.getEstatura());
		}

		if (pacienteCar.getPeso() > 0) {
			spnPeso.setValue((double) pacienteCar.getPeso());
		}

		if (pacienteCar.getFechaNacimiento() != null) {
			Date fechaNacDate = Date
					.from(pacienteCar.getFechaNacimiento().atStartOfDay(ZoneId.systemDefault()).toInstant());
			spnFechaNacimiento.setValue(fechaNacDate);
		}

		if (pacienteCar.getFechaRegistro() != null) {
			Date fechaRegDate = Date
					.from(pacienteCar.getFechaRegistro().atStartOfDay(ZoneId.systemDefault()).toInstant());
			spnFechaActual.setValue(fechaRegDate);
		}

		if (pacienteCar.getSexo() == 'M') {
			rdbtnHombre.setSelected(true);
		} else if (pacienteCar.getSexo() == 'F') {
			rdbtnMujer.setSelected(true);
		}

		if (pacienteCar.getTipoSangre() != null) {
			for (int i = 0; i < cbxTipoSangre.getItemCount(); i++) {
				if (pacienteCar.getTipoSangre().equals(cbxTipoSangre.getItemAt(i))) {
					cbxTipoSangre.setSelectedIndex(i);
					break;
				}
			}
		}

		if (pacienteCar.getAlergias() != null && !pacienteCar.getAlergias().isEmpty()) {
			alegecitas = pacienteCar.getAlergias();
			chckbxAlergias.setSelected(true);
		}

		telefonoOriginal = pacienteCar.getTelefono() != null ? pacienteCar.getTelefono().replaceAll("[^0-9]", "") : "";
		direccionOriginal = pacienteCar.getDireccion() != null ? pacienteCar.getDireccion() : "";
		pesoOriginal = pacienteCar.getPeso();
		estaturaOriginal = pacienteCar.getEstatura();
	}

	private boolean validarNombre(String texto, String campo) {
		if (!texto.matches("[a-záéíóúñüA-ZÁÉÍÓÚÑÜ ]+")) {
			JOptionPane.showMessageDialog(this, campo + " solo puede contener letras", "Error",
					JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	private void tomarAlergias() {
		TomaAlergias dialogAlergias = new TomaAlergias();
		dialogAlergias.setModal(true);
		dialogAlergias.setVisible(true);
		alegecitas = dialogAlergias.AlergiasSeleccionadas();

		if (alegecitas == null || alegecitas.isEmpty()) {
			chckbxAlergias.setSelected(false);
			alegecitas = null;
		}
		
		else {
			chckbxAlergias.setSelected(true);
		}
		dialogAlergias.dispose();
	}

	private void procesarPaciente() {
		String cedulaLimpia = txtCedula.getText().replaceAll("[^0-9]", "");
		String telefonoLimpio = txtTelefono.getText().replaceAll("[^0-9]", "");

		// Validaciones básicas
		if (txtNombre.getText().trim().isEmpty() || txtApellido.getText().trim().isEmpty()
				|| cedulaLimpia.length() != 11 || telefonoLimpio.length() != 10
				|| txtdireccion.getText().trim().isEmpty() || cbxTipoSangre.getSelectedIndex() == 0) {

			JOptionPane.showMessageDialog(this, "Complete todos los campos", "Incompleto", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (!validarNombre(txtNombre.getText().trim(), "El nombre")) {
			txtNombre.requestFocus();
			return;
		}

		if (!validarNombre(txtApellido.getText().trim(), "El apellido")) {
			txtApellido.requestFocus();
			return;
		}

		// Validar duplicados para nuevo paciente o interesado
		if (!esModificacion || esInteresado) {
			if (Clinica.getInstance().isCedulaRegistrada(cedulaLimpia)) {
				JOptionPane.showMessageDialog(this, "Cédula ya registrada", "Duplicado", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

		// Validar teléfono duplicado
		if (!telefonoLimpio.equals(telefonoOriginal)) {
			if (Clinica.getInstance().isTelefonoRegistrado(telefonoLimpio, cedulaLimpia)) {
				JOptionPane.showMessageDialog(this, "Teléfono ya registrado", "Duplicado", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

		float peso = ((Number) spnPeso.getValue()).floatValue();
		float estatura = ((Number) spnEstatura.getValue()).floatValue();

		if (peso < 1 || peso > 500) {
			JOptionPane.showMessageDialog(this, "Peso: 1-500 libras", "Inválido", JOptionPane.WARNING_MESSAGE);
			return;
		}

		if (estatura < 1 || estatura > 300) {
			JOptionPane.showMessageDialog(this, "Estatura: 1-300 cm", "Inválido", JOptionPane.WARNING_MESSAGE);
			return;
		}

		char sexo = rdbtnHombre.isSelected() ? 'M' : 'F';

		// Procesar alergias para nuevo paciente o interesado
		if ((!esModificacion || esInteresado) && chckbxAlergias.isSelected() && alegecitas == null) {
			tomarAlergias();
			if (alegecitas == null) {
				int confirmacion = JOptionPane.showConfirmDialog(this, "¿Continuar sin alergias?", "Sin alergias",
						JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

				if (confirmacion == JOptionPane.NO_OPTION) {
					return;
				}
			}
		}

		try {
			if (esInteresado) {
				// ========== CONVERSIÓN DE INTERESADO A PACIENTE REAL ==========
				Date fechaNacDate = (Date) spnFechaNacimiento.getValue();
				LocalDate fechaNac = fechaNacDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

				if (fechaNac.isAfter(LocalDate.now())) {
					JOptionPane.showMessageDialog(this, "Fecha no puede ser futura", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				// OBTENER DOCTOR QUE REGISTRA AL PACIENTE
				Doctor doctorRegistrador = Control.getDoctorLogeado();
				String licenciaDoctor = (doctorRegistrador != null) ? doctorRegistrador.getNumeroLicencia() : "Sistema";

				// Crear nuevo paciente REAL
				Paciente nuevoPaciente = new Paciente(cedulaLimpia, txtNombre.getText().trim(),
						txtApellido.getText().trim(), telefonoLimpio, txtCodigo.getText(), licenciaDoctor);
				selecVacunas vacuciejas = new selecVacunas(nuevoPaciente);
				vacuciejas.setModal(true);
				vacuciejas.setVisible(true);

				// Configurar datos adicionales
				nuevoPaciente.setDireccion(txtdireccion.getText().trim());
				nuevoPaciente.setFechaNacimiento(fechaNac);
				nuevoPaciente.setSexo(sexo);
				nuevoPaciente.setTipoSangre(cbxTipoSangre.getSelectedItem().toString());
				nuevoPaciente.setPeso(peso);
				nuevoPaciente.setEstatura(estatura);
				nuevoPaciente.setAlergias(alegecitas != null ? alegecitas : new ArrayList<>());

				// Registrar paciente
				if (Clinica.getInstance().registrarPaciente(nuevoPaciente)) {
					// Actualizar citas del interesado para que apunten al paciente real
					Clinica.getInstance().actualizarCitasDeInteresado(cedulaLimpia, nuevoPaciente);

					// Mostrar mensaje de confirmación
					String mensaje = "¡PACIENTE REGISTRADO EXITOSAMENTE!\n\n" + "• Código: " + txtCodigo.getText()
							+ "\n" + "• Nombre: " + txtNombre.getText() + " " + txtApellido.getText() + "\n"
							+ "• Cédula: " + cedulaLimpia + "\n" + "• Doctor registrador: "
							+ (doctorRegistrador != null ? doctorRegistrador.getNombre() : "Sistema") + "\n\n"
							+ "Se han actualizado todas las citas asociadas a este paciente.";

					JOptionPane.showMessageDialog(this, mensaje, "Conversión Exitosa", JOptionPane.INFORMATION_MESSAGE);

					dispose();
				}

			} else if (esModificacion) {
				// ========== MODIFICACIÓN DE PACIENTE REAL ==========
				pacienteCar.setDireccion(txtdireccion.getText().trim());
				pacienteCar.setTelefono(telefonoLimpio);
				pacienteCar.setEstatura(estatura);
				pacienteCar.setPeso(peso);

				if (Clinica.getInstance().modificarPaciente(pacienteCar)) {
					JOptionPane.showMessageDialog(this, "Paciente modificado", "Éxito",
							JOptionPane.INFORMATION_MESSAGE);
					dispose();
				}
			} else {
				// ========== NUEVO PACIENTE DESDE CERO ==========
				Date fechaNacDate = (Date) spnFechaNacimiento.getValue();
				LocalDate fechaNac = fechaNacDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

				if (fechaNac.isAfter(LocalDate.now())) {
					JOptionPane.showMessageDialog(this, "Fecha no puede ser futura", "Error",
							JOptionPane.ERROR_MESSAGE);
					return;
				}

				// OBTENER DOCTOR QUE REGISTRA AL PACIENTE
				Doctor doctorRegistrador = Control.getDoctorLogeado();
				String licenciaDoctor = (doctorRegistrador != null) ? doctorRegistrador.getNumeroLicencia() : "Sistema";

				// Crear nuevo paciente
				Paciente nuevoPaciente = new Paciente(cedulaLimpia, txtNombre.getText().trim(),
						txtApellido.getText().trim(), telefonoLimpio, txtCodigo.getText(), licenciaDoctor);

				nuevoPaciente.setDireccion(txtdireccion.getText().trim());
				nuevoPaciente.setFechaNacimiento(fechaNac);
				nuevoPaciente.setSexo(sexo);
				nuevoPaciente.setTipoSangre(cbxTipoSangre.getSelectedItem().toString());
				nuevoPaciente.setPeso(peso);
				nuevoPaciente.setEstatura(estatura);
				nuevoPaciente.setAlergias(alegecitas != null ? alegecitas : new ArrayList<>());

				if (Clinica.getInstance().registrarPaciente(nuevoPaciente)) {
					// Preguntar por vacunas
					int respuesta = JOptionPane.showConfirmDialog(this,
							"¿Desea registrar vacunas previas del paciente?", "Historial de Vacunas",
							JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

					if (respuesta == JOptionPane.YES_OPTION) {
						selecVacunas dialogVacunas = new selecVacunas(nuevoPaciente);
						dialogVacunas.setModal(true);
						dialogVacunas.setVisible(true);
					}

					JOptionPane.showMessageDialog(this,
							"PACIENTE REGISTRADO EXITOSAMENTE\n\n" + "Código: " + txtCodigo.getText() + "\n"
									+ "Nombre: " + txtNombre.getText() + " " + txtApellido.getText() + "\n" + "Cédula: "
									+ cedulaLimpia + "\n" + "Doctor registrador: "
									+ (doctorRegistrador != null ? doctorRegistrador.getNombre() : "Sistema"),
							"Registro Exitoso", JOptionPane.INFORMATION_MESSAGE);

					dispose();
				}
			}
		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	public Paciente getPaciente() {
		return pacienteCar;
	}
}