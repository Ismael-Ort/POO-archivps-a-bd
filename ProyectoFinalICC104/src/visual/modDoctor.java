package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerNumberModel;
import java.util.Date;
import java.util.Calendar;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;
import javax.swing.JFormattedTextField;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.MaskFormatter;
import logico.Clinica;
import logico.Doctor;
import javax.swing.ButtonGroup;
import javax.swing.SpinnerListModel;
import java.awt.Toolkit;
import java.awt.Color;

public class modDoctor extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtCodigo;
	private JTextField txtNombre;
	private JTextField txtApellido;
	private JFormattedTextField txtCedula;
	private JFormattedTextField txtTelefono;
	private JTextArea txtDireccion;
	private JTextField txtNumeroLicencia;
	private JSpinner spnHorarioInicio;
	private JSpinner spnHorarioFin;
	private JRadioButton rdbtnHombre;
	private JRadioButton rdbtnMujer;
	private JComboBox<String> cbxEspecialidad;
	private JSpinner spnCitasPorDia;
	private JSpinner spnFechaNacimiento;
	private JCheckBox chckbxActivo;
	private Doctor doctorAModificar;
	private JButton btnGuardar;

	// Datos originales para detectar cambios
	private String telefonoOriginal = "";
	private String direccionOriginal = "";
	private String nombreOriginal = "";
	private String apellidoOriginal = "";
	private String especialidadOriginal = "";
	private int citasPorDiaOriginal = 0;
	private String horarioInicioOriginal = "";
	private String horarioFinOriginal = "";
	private boolean activoOriginal = false;

	public modDoctor(Doctor doctor) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(modDoctor.class.getResource("/recursos/doc.jpg")));
		this.doctorAModificar = doctor;

		if (doctor == null) {
			JOptionPane.showMessageDialog(this, "No se ha seleccionado ningún doctor", "Error",
					JOptionPane.ERROR_MESSAGE);
			dispose();
			return;
		}

		setTitle("Modificar Doctor - " + doctor.getNombre() + " " + doctor.getApellido());
		setModal(true);
		setBounds(100, 100, 680, 650);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(245, 255, 250));
		contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		crearComponentes();
		cargarDatosDoctor();
		configurarCamposNoEditables();
		crearBotones();
		configurarDeteccionCambios();
	}

	private void crearComponentes() {
		// ========== CÓDIGO (NO EDITABLE) ==========
		JLabel lblCodigo = new JLabel("Código:");
		lblCodigo.setBounds(540, 15, 60, 20);
		contentPanel.add(lblCodigo);

		txtCodigo = new JTextField();
		txtCodigo.setEditable(false);
		txtCodigo.setBounds(540, 38, 100, 26);
		contentPanel.add(txtCodigo);

		// ========== FILA 1: NOMBRE Y APELLIDO ==========
		JLabel lblNombre = new JLabel("Nombre(s):");
		lblNombre.setBounds(15, 15, 100, 20);
		contentPanel.add(lblNombre);

		txtNombre = new JTextField();
		txtNombre.setBounds(15, 38, 250, 26);
		contentPanel.add(txtNombre);

		JLabel lblApellido = new JLabel("Apellido(s):");
		lblApellido.setBounds(280, 15, 100, 20);
		contentPanel.add(lblApellido);

		txtApellido = new JTextField();
		txtApellido.setBounds(280, 38, 245, 26);
		contentPanel.add(txtApellido);

		// ========== FILA 2: CÉDULA (NO EDITABLE) Y TELÉFONO ==========
		JLabel lblCedula = new JLabel("Cédula:");
		lblCedula.setBounds(15, 80, 100, 20);
		contentPanel.add(lblCedula);

		try {
			MaskFormatter cedulaMask = new MaskFormatter("###-#######-#");
			cedulaMask.setPlaceholderCharacter('_');
			txtCedula = new JFormattedTextField(cedulaMask);
		} catch (Exception e) {
			txtCedula = new JFormattedTextField();
		}
		txtCedula.setBounds(15, 103, 180, 26);
		contentPanel.add(txtCedula);

		JLabel lblTelefono = new JLabel("Teléfono:");
		lblTelefono.setBounds(210, 80, 100, 20);
		contentPanel.add(lblTelefono);

		try {
			MaskFormatter telefonoMask = new MaskFormatter("(###) ###-####");
			telefonoMask.setPlaceholderCharacter('_');
			txtTelefono = new JFormattedTextField(telefonoMask);
		} catch (Exception e) {
			txtTelefono = new JFormattedTextField();
		}
		txtTelefono.setBounds(210, 103, 180, 26);
		contentPanel.add(txtTelefono);

		// ========== FILA 3: SEXO Y FECHA DE NACIMIENTO ==========
		JLabel lblSexo = new JLabel("Sexo:");
		lblSexo.setBounds(405, 80, 50, 20);
		contentPanel.add(lblSexo);

		rdbtnHombre = new JRadioButton("M");
		rdbtnHombre.setBounds(405, 100, 60, 29);
		contentPanel.add(rdbtnHombre);

		rdbtnMujer = new JRadioButton("F");
		rdbtnMujer.setBounds(470, 100, 55, 29);
		contentPanel.add(rdbtnMujer);

		ButtonGroup grupoSexo = new ButtonGroup();
		grupoSexo.add(rdbtnHombre);
		grupoSexo.add(rdbtnMujer);

		JLabel lblFechaNacimiento = new JLabel("Fecha de nacimiento:");
		lblFechaNacimiento.setBounds(15, 145, 150, 20);
		contentPanel.add(lblFechaNacimiento);

		spnFechaNacimiento = new JSpinner();
		spnFechaNacimiento.setModel(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_YEAR));
		JSpinner.DateEditor editor = new JSpinner.DateEditor(spnFechaNacimiento, "dd/MM/yyyy");
		spnFechaNacimiento.setEditor(editor);
		spnFechaNacimiento.setBounds(15, 168, 150, 26);
		contentPanel.add(spnFechaNacimiento);

		// ========== DIRECCIÓN (TEXTAREA) ==========
		JLabel lblDireccion = new JLabel("Dirección:");
		lblDireccion.setBounds(180, 145, 100, 20);
		contentPanel.add(lblDireccion);

		txtDireccion = new JTextArea();
		txtDireccion.setLineWrap(true);
		txtDireccion.setWrapStyleWord(true);
		JScrollPane scrollDireccion = new JScrollPane(txtDireccion);
		scrollDireccion.setBounds(180, 168, 460, 60);
		contentPanel.add(scrollDireccion);

		// ========== FILA 4: ESPECIALIDAD Y CITAS POR DÍA ==========
		JLabel lblEspecialidad = new JLabel("Especialidad:");
		lblEspecialidad.setBounds(15, 245, 100, 20);
		contentPanel.add(lblEspecialidad);

		cbxEspecialidad = new JComboBox<String>();
		cbxEspecialidad.setModel(new DefaultComboBoxModel<>(new String[] { "<Seleccione>", "Cardiología", "Pediatría",
				"Dermatología", "Neurología", "Ginecología", "Medicina General", "Traumatología", "Oftalmología",
				"Otorrinolaringología", "Psiquiatría", "Urología", "Endocrinología" }));
		cbxEspecialidad.setBounds(15, 268, 280, 26);
		contentPanel.add(cbxEspecialidad);

		JLabel lblCitasPorDia = new JLabel("Citas por día:");
		lblCitasPorDia.setBounds(310, 245, 100, 20);
		contentPanel.add(lblCitasPorDia);

		spnCitasPorDia = new JSpinner();
		spnCitasPorDia.setModel(new SpinnerNumberModel(5, 1, 20, 1));
		spnCitasPorDia.setBounds(310, 268, 80, 26);
		contentPanel.add(spnCitasPorDia);

		JLabel lblInfoCitas = new JLabel("(Máximo 20)");
		lblInfoCitas.setBounds(400, 245, 100, 20);
		contentPanel.add(lblInfoCitas);

		// ========== FILA 5: NÚMERO DE LICENCIA (NO EDITABLE) ==========
		JLabel lblNumeroLicencia = new JLabel("Número de Licencia:");
		lblNumeroLicencia.setBounds(15, 310, 200, 20);
		contentPanel.add(lblNumeroLicencia);

		txtNumeroLicencia = new JTextField();
		txtNumeroLicencia.setBounds(15, 333, 250, 26);
		contentPanel.add(txtNumeroLicencia);

		JLabel lblInfoLicencia = new JLabel("(Ej: EXQM-12345)");
		lblInfoLicencia.setBounds(270, 336, 200, 20);
		contentPanel.add(lblInfoLicencia);

		// ========== FILA 6: HORARIOS CON SPINNERS ==========
		JLabel lblHorarioInicio = new JLabel("Horario Inicio:");
		lblHorarioInicio.setBounds(15, 375, 120, 20);
		contentPanel.add(lblHorarioInicio);

		String[] horasDisponibles = generarHorasDisponibles();
		spnHorarioInicio = new JSpinner(new SpinnerListModel(horasDisponibles));
		spnHorarioInicio.setBounds(15, 398, 100, 26);
		contentPanel.add(spnHorarioInicio);

		JLabel lblFormatoInicio = new JLabel("(HH:MM)");
		lblFormatoInicio.setBounds(125, 401, 60, 20);
		contentPanel.add(lblFormatoInicio);

		JLabel lblHorarioFin = new JLabel("Horario Fin:");
		lblHorarioFin.setBounds(200, 375, 100, 20);
		contentPanel.add(lblHorarioFin);

		spnHorarioFin = new JSpinner(new SpinnerListModel(horasDisponibles));
		spnHorarioFin.setBounds(200, 398, 100, 26);
		contentPanel.add(spnHorarioFin);

		JLabel lblFormatoFin = new JLabel("(HH:MM)");
		lblFormatoFin.setBounds(310, 401, 60, 20);
		contentPanel.add(lblFormatoFin);

		// ========== ACTIVO ==========
		chckbxActivo = new JCheckBox("Doctor Activo");
		chckbxActivo.setBounds(15, 440, 150, 29);
		contentPanel.add(chckbxActivo);
	}

	private void cargarDatosDoctor() {
		if (doctorAModificar == null)
			return;

		txtCodigo.setText(doctorAModificar.getCodigoDoctor());
		txtNombre.setText(doctorAModificar.getNombre());
		txtApellido.setText(doctorAModificar.getApellido());
		txtCedula.setText(formatearCedula(doctorAModificar.getCedula()));
		txtTelefono.setText(formatearTelefono(doctorAModificar.getTelefono()));
		txtDireccion.setText(doctorAModificar.getDireccion());
		txtNumeroLicencia.setText(doctorAModificar.getNumeroLicencia());

		// Sexo
		if (doctorAModificar.getSexo() == 'M') {
			rdbtnHombre.setSelected(true);
		} else {
			rdbtnMujer.setSelected(true);
		}

		// Fecha de nacimiento
		Date fechaNacDate = Date
				.from(doctorAModificar.getFechaNacimiento().atStartOfDay(ZoneId.systemDefault()).toInstant());
		spnFechaNacimiento.setValue(fechaNacDate);

		// Especialidad
		cbxEspecialidad.setSelectedItem(doctorAModificar.getEspecialidad());

		// Citas por día
		spnCitasPorDia.setValue(doctorAModificar.getCitasPorDia());

		// Horarios
		spnHorarioInicio.setValue(doctorAModificar.getHorarioInicio().toString());
		spnHorarioFin.setValue(doctorAModificar.getHorarioFin().toString());

		// Activo
		chckbxActivo.setSelected(doctorAModificar.isActivo());

		// Guardar datos originales
		telefonoOriginal = doctorAModificar.getTelefono().replaceAll("[^0-9]", "");
		direccionOriginal = doctorAModificar.getDireccion() != null ? doctorAModificar.getDireccion() : "";
		nombreOriginal = doctorAModificar.getNombre();
		apellidoOriginal = doctorAModificar.getApellido();
		especialidadOriginal = doctorAModificar.getEspecialidad();
		citasPorDiaOriginal = doctorAModificar.getCitasPorDia();
		horarioInicioOriginal = doctorAModificar.getHorarioInicio().toString();
		horarioFinOriginal = doctorAModificar.getHorarioFin().toString();
		activoOriginal = doctorAModificar.isActivo();
	}

	private void configurarCamposNoEditables() {
		// Campos que NO se pueden modificar
		txtCodigo.setEnabled(false);
		txtCedula.setEnabled(false);
		txtNumeroLicencia.setEnabled(false);
		spnFechaNacimiento.setEnabled(false);
		rdbtnHombre.setEnabled(false);
		rdbtnMujer.setEnabled(false);
	}

	private void configurarDeteccionCambios() {
		// Deshabilitar botón inicialmente
		btnGuardar.setEnabled(false);

		// Listener para campos de texto
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

		// Aplicar listeners
		txtNombre.getDocument().addDocumentListener(cambioListener);
		txtApellido.getDocument().addDocumentListener(cambioListener);
		txtTelefono.getDocument().addDocumentListener(cambioListener);
		txtDireccion.getDocument().addDocumentListener(cambioListener);

		// Listeners para spinners y combobox
		spnCitasPorDia.addChangeListener(e -> verificarCambios());
		spnHorarioInicio.addChangeListener(e -> verificarCambios());
		spnHorarioFin.addChangeListener(e -> verificarCambios());
		cbxEspecialidad.addActionListener(e -> verificarCambios());
		chckbxActivo.addActionListener(e -> verificarCambios());
	}

	private void verificarCambios() {
		String telefonoActual = txtTelefono.getText().replaceAll("[^0-9]", "");
		String direccionActual = txtDireccion.getText().trim();
		String nombreActual = txtNombre.getText().trim();
		String apellidoActual = txtApellido.getText().trim();
		String especialidadActual = cbxEspecialidad.getSelectedItem() != null
				? cbxEspecialidad.getSelectedItem().toString()
				: "";
		int citasPorDiaActual = (Integer) spnCitasPorDia.getValue();
		String horarioInicioActual = (String) spnHorarioInicio.getValue();
		String horarioFinActual = (String) spnHorarioFin.getValue();
		boolean activoActual = chckbxActivo.isSelected();

		boolean huboCambios = !telefonoActual.equals(telefonoOriginal) || !direccionActual.equals(direccionOriginal)
				|| !nombreActual.equals(nombreOriginal) || !apellidoActual.equals(apellidoOriginal)
				|| !especialidadActual.equals(especialidadOriginal) || citasPorDiaActual != citasPorDiaOriginal
				|| !horarioInicioActual.equals(horarioInicioOriginal) || !horarioFinActual.equals(horarioFinOriginal)
				|| activoActual != activoOriginal;

		btnGuardar.setEnabled(huboCambios);
	}

	private void crearBotones() {
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(224, 255, 255));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		btnGuardar = new JButton("Guardar Cambios");
		btnGuardar.setBackground(new Color(255, 239, 213));
		btnGuardar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				modificarDoctor();
			}
		});
		buttonPane.add(btnGuardar);

		JButton cancelButton = new JButton("Cancelar");
		cancelButton.setBackground(new Color(230, 230, 250));
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPane.add(cancelButton);
	}

	private boolean validarNombre(String texto, String campo) {
		// Solo letras, espacios y tildes
		if (!texto.matches("[a-záéíóúñüA-ZÁÉÍÓÚÑÜ ]+")) {
			JOptionPane.showMessageDialog(this,
					campo + " solo puede contener letras y espacios.\nNo se permiten números ni caracteres especiales.",
					"Nombre inválido", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	private void modificarDoctor() {
		// ========== 1. LIMPIAR DATOS ==========
		String telefonoLimpio = txtTelefono.getText().replaceAll("[^0-9]", "");

		// ========== 2. VALIDAR CAMPOS VACÍOS ==========
		if (txtNombre.getText().trim().isEmpty() || txtApellido.getText().trim().isEmpty()
				|| telefonoLimpio.length() != 10 || txtDireccion.getText().trim().isEmpty()
				|| cbxEspecialidad.getSelectedIndex() == 0) {

			JOptionPane.showMessageDialog(this,
					"Complete todos los campos correctamente.\n\n" + "Verifique:\n" + "• Teléfono: 10 dígitos\n"
							+ "• Dirección completa\n" + "• Especialidad seleccionada",
					"Campos incompletos", JOptionPane.WARNING_MESSAGE);
			return;
		}

		// ========== 2.5. VALIDAR NOMBRE Y APELLIDO ==========
		if (!validarNombre(txtNombre.getText().trim(), "El nombre")) {
			txtNombre.requestFocus();
			return;
		}

		if (!validarNombre(txtApellido.getText().trim(), "El apellido")) {
			txtApellido.requestFocus();
			return;
		}

		// ========== 3. VALIDAR TELÉFONO DUPLICADO (SOLO SI CAMBIÓ) ==========
		if (!telefonoLimpio.equals(telefonoOriginal)) {
			if (Clinica.getInstance().isTelefonoRegistrado(telefonoLimpio, doctorAModificar.getCedula())) {
				JOptionPane.showMessageDialog(this,
						"Este teléfono ya está registrado en el sistema.\n" + "Teléfono: " + txtTelefono.getText()
								+ "\n" + "Por favor ingrese un teléfono diferente.",
						"Teléfono duplicado", JOptionPane.ERROR_MESSAGE);
				return;
			}
		}

		try {
			// ========== 4. FECHA DE NACIMIENTO ==========
			Date fechaNacDate = (Date) spnFechaNacimiento.getValue();
			LocalDate fechaNac = fechaNacDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			if (fechaNac.isAfter(LocalDate.now())) {
				JOptionPane.showMessageDialog(this, "La fecha de nacimiento no puede ser futura", "Fecha inválida",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			// ========== 5. HORARIOS ==========
			String horarioInicioStr = (String) spnHorarioInicio.getValue();
			String horarioFinStr = (String) spnHorarioFin.getValue();

			LocalTime horarioInicio = LocalTime.parse(horarioInicioStr);
			LocalTime horarioFin = LocalTime.parse(horarioFinStr);

			if (!horarioFin.isAfter(horarioInicio)) {
				JOptionPane.showMessageDialog(this, "El horario de fin debe ser posterior al de inicio",
						"Horario inválido", JOptionPane.ERROR_MESSAGE);
				return;
			}

			// Validar que haya al menos 1 hora de diferencia
			if (horarioInicio.plusHours(1).isAfter(horarioFin)) {
				JOptionPane.showMessageDialog(
						this, "El horario laboral debe ser de al menos 1 hora.\n" + "Horario actual: "
								+ horarioInicioStr + " - " + horarioFinStr,
						"Horario muy corto", JOptionPane.WARNING_MESSAGE);
				return;
			}

			// ========== 6. ACTUALIZAR DOCTOR ==========
			doctorAModificar.setNombre(txtNombre.getText().trim());
			doctorAModificar.setApellido(txtApellido.getText().trim());
			doctorAModificar.setTelefono(telefonoLimpio);
			doctorAModificar.setDireccion(txtDireccion.getText().trim());
			doctorAModificar.setFechaNacimiento(fechaNac);
			doctorAModificar.setEspecialidad(cbxEspecialidad.getSelectedItem().toString());
			doctorAModificar.setCitasPorDia((Integer) spnCitasPorDia.getValue());
			doctorAModificar.setHorarioInicio(horarioInicio);
			doctorAModificar.setHorarioFin(horarioFin);
			doctorAModificar.setActivo(chckbxActivo.isSelected());

			// ========== 7. GUARDAR CAMBIOS ==========
			if (Clinica.getInstance().modificarDoctor(doctorAModificar)) {
				JOptionPane.showMessageDialog(this,
						"Doctor modificado exitosamente\n\n" + "Código: " + doctorAModificar.getCodigoDoctor() + "\n"
								+ "Nombre: " + doctorAModificar.getNombre() + " " + doctorAModificar.getApellido()
								+ "\n" + "Horario: " + horarioInicioStr + " - " + horarioFinStr,
						"Modificación Exitosa", JOptionPane.INFORMATION_MESSAGE);
				dispose();
			} else {
				JOptionPane.showMessageDialog(this, "Error al modificar el doctor", "Error", JOptionPane.ERROR_MESSAGE);
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error al modificar:\n" + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	/**
	 * Genera un array de horas disponibles en formato HH:MM desde las 6:00 AM hasta
	 * las 10:00 PM en intervalos de 30 minutos
	 */
	private String[] generarHorasDisponibles() {
		java.util.List<String> horas = new java.util.ArrayList<>();

		for (int hora = 6; hora <= 22; hora++) {
			for (int minuto = 0; minuto < 60; minuto += 30) {
				String horaStr = String.format("%02d:%02d", hora, minuto);
				horas.add(horaStr);
			}
		}

		return horas.toArray(new String[0]);
	}

	// ========== MÉTODOS AUXILIARES PARA FORMATEAR ==========
	private String formatearCedula(String cedula) {
		if (cedula.length() == 11) {
			return cedula.substring(0, 3) + "-" + cedula.substring(3, 10) + "-" + cedula.substring(10);
		}
		return cedula;
	}

	private String formatearTelefono(String telefono) {
		if (telefono.length() == 10) {
			return "(" + telefono.substring(0, 3) + ") " + telefono.substring(3, 6) + "-" + telefono.substring(6);
		}
		return telefono;
	}
}