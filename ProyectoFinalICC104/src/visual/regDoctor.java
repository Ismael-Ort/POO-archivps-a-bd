package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerDateModel;
import javax.swing.SpinnerListModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import javax.swing.text.MaskFormatter;

import logico.Clinica;
import logico.Control;
import logico.Doctor;
import logico.User;
import logico.PersistenciaManager;

public class regDoctor extends JDialog {

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
	private JTextField txtUsuario;
	private JTextField txtContrasena;

	public regDoctor() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(regDoctor.class.getResource("/recursos/doc.jpg")));
		setTitle("Registro de Doctores");
		setModal(true);
		setBounds(100, 100, 680, 650);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(245, 255, 250));
		contentPanel.setBorder(new EmptyBorder(15, 15, 15, 15));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		inicializarComponentes();
		crearPanelBotones();
	}

	private void inicializarComponentes() {

		JLabel lblCodigo = new JLabel("Código:");
		lblCodigo.setBounds(540, 15, 60, 20);
		contentPanel.add(lblCodigo);

		txtCodigo = new JTextField();
		txtCodigo.setBackground(new Color(224, 255, 255));
		txtCodigo.setEditable(false);
		txtCodigo.setBounds(540, 38, 100, 26);
		txtCodigo.setColumns(10);
		txtCodigo.setText("DOC-" + Clinica.getInstance().getContadorDoctores());
		contentPanel.add(txtCodigo);

		JLabel lblNombre = new JLabel("Nombre:");
		lblNombre.setBounds(15, 15, 100, 20);
		contentPanel.add(lblNombre);

		txtNombre = new JTextField();
		txtNombre.setBounds(15, 38, 250, 26);
		txtNombre.setColumns(10);
		contentPanel.add(txtNombre);

		JLabel lblApellido = new JLabel("Apellido:");
		lblApellido.setBounds(280, 15, 100, 20);
		contentPanel.add(lblApellido);

		txtApellido = new JTextField();
		txtApellido.setBounds(280, 38, 250, 26);
		txtApellido.setColumns(10);
		contentPanel.add(txtApellido);

		crearCamposCedulaTelefono();
		crearCamposSexoYNacimiento();
		crearCampoDireccion();
		crearCamposEspecialidadYCitas();
		crearCampoLicencia();
		crearCamposHorarios();
		crearPanelCredenciales();

		chckbxActivo = new JCheckBox("Doctor Activo");
		chckbxActivo.setBackground(new Color(245, 245, 245));
		chckbxActivo.setSelected(true);
		chckbxActivo.setBounds(15, 440, 150, 29);
		contentPanel.add(chckbxActivo);

		JLabel lblInfo = new JLabel("Nota: Todos los campos son obligatorios");
		lblInfo.setBounds(15, 480, 400, 20);
		contentPanel.add(lblInfo);
	}

	private void crearCamposCedulaTelefono() {
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
	}

	private void crearCamposSexoYNacimiento() {
		JLabel lblSexo = new JLabel("Sexo:");
		lblSexo.setBounds(405, 80, 50, 20);
		contentPanel.add(lblSexo);

		rdbtnHombre = new JRadioButton("M");
		rdbtnHombre.setBackground(new Color(240, 248, 255));
		rdbtnHombre.setSelected(true);
		rdbtnHombre.setBounds(405, 100, 60, 29);
		contentPanel.add(rdbtnHombre);

		rdbtnMujer = new JRadioButton("F");
		rdbtnMujer.setBackground(new Color(240, 255, 255));
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
	}

	private void crearCampoDireccion() {
		JLabel lblDireccion = new JLabel("Dirección:");
		lblDireccion.setBounds(180, 145, 100, 20);
		contentPanel.add(lblDireccion);

		txtDireccion = new JTextArea();
		txtDireccion.setLineWrap(true);
		txtDireccion.setWrapStyleWord(true);
		JScrollPane scrollDireccion = new JScrollPane(txtDireccion);
		scrollDireccion.setBounds(180, 168, 460, 60);
		contentPanel.add(scrollDireccion);
	}

	private void crearCamposEspecialidadYCitas() {
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
	}

	private void crearCampoLicencia() {
		JLabel lblNumeroLicencia = new JLabel("Número de Licencia Médica:");
		lblNumeroLicencia.setBounds(15, 310, 200, 20);
		contentPanel.add(lblNumeroLicencia);

		try {
			MaskFormatter licenciaMask = new MaskFormatter("UUUU-#####");
			licenciaMask.setPlaceholderCharacter('_');
			txtNumeroLicencia = new JFormattedTextField(licenciaMask);
		} catch (Exception e) {
			txtNumeroLicencia = new JTextField();
		}

		txtNumeroLicencia.setBounds(15, 333, 250, 26);
		txtNumeroLicencia.setColumns(10);
		contentPanel.add(txtNumeroLicencia);

		JLabel lblInfoLicencia = new JLabel("(Ej: EXQM-12345)");
		lblInfoLicencia.setBounds(270, 336, 200, 20);
		contentPanel.add(lblInfoLicencia);
	}

	private void crearCamposHorarios() {
		JLabel lblHorarioInicio = new JLabel("Horario Inicio:");
		lblHorarioInicio.setBounds(15, 375, 120, 20);
		contentPanel.add(lblHorarioInicio);

		String[] horasDisponibles = generarHorasDisponibles();
		spnHorarioInicio = new JSpinner(new SpinnerListModel(horasDisponibles));
		spnHorarioInicio.setValue("08:00");
		spnHorarioInicio.setBounds(15, 398, 100, 26);
		contentPanel.add(spnHorarioInicio);

		JLabel lblFormatoInicio = new JLabel("(HH:MM)");
		lblFormatoInicio.setBounds(125, 401, 60, 20);
		contentPanel.add(lblFormatoInicio);

		JLabel lblHorarioFin = new JLabel("Horario Fin:");
		lblHorarioFin.setBounds(200, 375, 100, 20);
		contentPanel.add(lblHorarioFin);

		spnHorarioFin = new JSpinner(new SpinnerListModel(horasDisponibles));
		spnHorarioFin.setValue("17:00");
		spnHorarioFin.setBounds(200, 398, 100, 26);
		contentPanel.add(spnHorarioFin);

		JLabel lblFormatoFin = new JLabel("(HH:MM)");
		lblFormatoFin.setBounds(310, 401, 60, 20);
		contentPanel.add(lblFormatoFin);
	}

	private void crearPanelCredenciales() {
		JPanel panel = new JPanel();
		panel.setBackground(new Color(255, 240, 245));
		panel.setBorder(new LineBorder(Color.GRAY, 1, true));
		panel.setBounds(385, 386, 255, 153);
		contentPanel.add(panel);
		panel.setLayout(null);

		JLabel lblCredenciales = new JLabel("Credenciales de acceso:");
		lblCredenciales.setBounds(15, 0, 180, 20);
		panel.add(lblCredenciales);

		JLabel lblUsuario = new JLabel("Usuario:");
		lblUsuario.setBounds(15, 41, 69, 20);
		panel.add(lblUsuario);

		JLabel lblContrasena = new JLabel("Contraseña:");
		lblContrasena.setBounds(15, 78, 85, 20);
		panel.add(lblContrasena);

		txtUsuario = new JTextField();
		txtUsuario.setBounds(106, 38, 139, 26);
		txtUsuario.setColumns(10);
		panel.add(txtUsuario);

		txtContrasena = new JTextField();
		txtContrasena.setBounds(106, 75, 139, 26);
		txtContrasena.setColumns(10);
		panel.add(txtContrasena);

		JLabel lblNota1 = new JLabel("Nota: El usuario y la contraseña ");
		lblNota1.setBounds(15, 114, 230, 20);
		panel.add(lblNota1);

		JLabel lblNota2 = new JLabel("no podrán ser modificados.");
		lblNota2.setBounds(15, 133, 225, 20);
		panel.add(lblNota2);
	}

	private void crearPanelBotones() {
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(224, 255, 255));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton btnRegistrar = new JButton("Registrar");
		btnRegistrar.setBackground(new Color(245, 255, 250));
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				registrarDoctor();
			}
		});
		buttonPane.add(btnRegistrar);
		getRootPane().setDefaultButton(btnRegistrar);

		JButton btnLimpiar = new JButton("Limpiar");
		btnLimpiar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				limpiarCampos();
			}
		});
		buttonPane.add(btnLimpiar);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBackground(new Color(255, 239, 213));
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				dispose();
			}
		});
		buttonPane.add(btnCancelar);
	}

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

	private boolean validarNombre(String texto, String campo) {
		if (!texto.matches("[a-záéíóúñüA-ZÁÉÍÓÚÑÜ ]+")) {
			JOptionPane.showMessageDialog(this,
					campo + " solo puede contener letras y espacios.\nNo se permiten números ni caracteres especiales.",
					"Nombre inválido", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	private void registrarDoctor() {
		String cedulaLimpia = txtCedula.getText().replaceAll("[^0-9]", "");
		String telefonoLimpio = txtTelefono.getText().replaceAll("[^0-9]", "");
		String licenciaTexto = txtNumeroLicencia.getText().trim().replaceAll("_", "");

		if (txtNombre.getText().trim().isEmpty() || txtApellido.getText().trim().isEmpty()
				|| cedulaLimpia.length() != 11 || telefonoLimpio.length() != 10
				|| txtDireccion.getText().trim().isEmpty() || licenciaTexto.isEmpty() || licenciaTexto.length() < 4
				|| cbxEspecialidad.getSelectedIndex() == 0 || txtUsuario.getText().isEmpty()
				|| txtContrasena.getText().isEmpty()) {

			JOptionPane.showMessageDialog(this,
					"Complete todos los campos correctamente.\n\n" + "Verifique:\n" + "• Cédula: 11 dígitos\n"
							+ "• Teléfono: 10 dígitos\n" + "• Licencia: al menos 4 caracteres\n"
							+ "• Usuario y contraseña son obligatorios",
					"Campos incompletos", JOptionPane.WARNING_MESSAGE);
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

		if (Clinica.getInstance().isCedulaRegistrada(cedulaLimpia)) {
			JOptionPane.showMessageDialog(this,
					"Esta cédula ya está registrada en el sistema.\n" + "Cédula: " + txtCedula.getText(),
					"Cédula duplicada", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (Clinica.getInstance().isTelefonoRegistrado(telefonoLimpio)) {
			JOptionPane.showMessageDialog(this,
					"Este teléfono ya está registrado en el sistema.\n" + "Teléfono: " + txtTelefono.getText(),
					"Teléfono duplicado", JOptionPane.WARNING_MESSAGE);
			return;
		}

		String licenciaCompleta = txtNumeroLicencia.getText().trim().toUpperCase();
		if (Clinica.getInstance().isLicenciaRegistrada(licenciaCompleta)) {
			JOptionPane.showMessageDialog(this,
					"Esta licencia médica ya está registrada.\n" + "Licencia: " + licenciaCompleta,
					"Licencia duplicada", JOptionPane.ERROR_MESSAGE);
			return;
		}

		String usuario = txtUsuario.getText().trim();

		if (Control.getInstance().existeUsuario(usuario)) {
			JOptionPane.showMessageDialog(this,
					"El nombre de usuario '" + usuario + "' ya está en uso.\n"
							+ "Por favor, elija otro nombre de usuario para el doctor.",
					"Usuario duplicado", JOptionPane.ERROR_MESSAGE);
			txtUsuario.requestFocus();
			return;
		}

		if (Clinica.getInstance().existeUsuarioDoctor(usuario)) {
			JOptionPane.showMessageDialog(this,
					"El nombre de usuario '" + usuario + "' ya está en uso por otro doctor.\n"
							+ "Por favor, elija otro nombre de usuario.",
					"Usuario duplicado", JOptionPane.ERROR_MESSAGE);
			txtUsuario.requestFocus();
			return;
		}

		String contrasena = txtContrasena.getText().trim();
		if (contrasena.length() < 4) {
			JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 4 caracteres.",
					"Contraseña muy corta", JOptionPane.WARNING_MESSAGE);
			txtContrasena.requestFocus();
			return;
		}

		try {
			Date fechaNacDate = (Date) spnFechaNacimiento.getValue();
			LocalDate fechaNac = fechaNacDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			if (fechaNac.isAfter(LocalDate.now())) {
				JOptionPane.showMessageDialog(this, "La fecha de nacimiento no puede ser futura", "Fecha inválida",
						JOptionPane.ERROR_MESSAGE);
				return;
			}

			String horarioInicioStr = (String) spnHorarioInicio.getValue();
			String horarioFinStr = (String) spnHorarioFin.getValue();

			LocalTime horarioInicio = LocalTime.parse(horarioInicioStr);
			LocalTime horarioFin = LocalTime.parse(horarioFinStr);

			if (!horarioFin.isAfter(horarioInicio)) {
				JOptionPane.showMessageDialog(this, "El horario de fin debe ser posterior al de inicio",
						"Horario inválido", JOptionPane.ERROR_MESSAGE);
				return;
			}

			char sexo = rdbtnHombre.isSelected() ? 'M' : 'F';
			String especialidad = cbxEspecialidad.getSelectedItem().toString();
			int citasPorDia = (Integer) spnCitasPorDia.getValue();
			boolean activo = chckbxActivo.isSelected();

			Doctor nuevoDoctor = new Doctor(cedulaLimpia, txtNombre.getText().trim(), txtApellido.getText().trim(),
					telefonoLimpio, txtDireccion.getText().trim(), fechaNac, sexo, txtCodigo.getText(), especialidad,
					licenciaCompleta, citasPorDia, horarioInicio, horarioFin, activo, usuario, contrasena);

			User usuarioDoctor = new User("Doctor", usuario, contrasena);

			boolean doctorRegistrado = Clinica.getInstance().registrarDoctor(nuevoDoctor);
			boolean usuarioRegistrado = Control.getInstance().regUser(usuarioDoctor);

			if (doctorRegistrado && usuarioRegistrado) {
				PersistenciaManager.guardarDatos();

				JOptionPane.showMessageDialog(this,
						"DOCTOR REGISTRADO EXITOSAMENTE\n\n" + "Código: " + txtCodigo.getText() + "\n" + "Nombre: "
								+ nuevoDoctor.getNombre() + " " + nuevoDoctor.getApellido() + "\n" + "Especialidad: "
								+ especialidad + "\n" + "Licencia: " + licenciaCompleta + "\n" + "Horario: "
								+ horarioInicioStr + " - " + horarioFinStr + "\n" + "Citas por día: " + citasPorDia
								+ "\n" + "Usuario: " + usuario + "\n" + "Contraseña: ******",
						"REGISTRO EXITOSO", JOptionPane.INFORMATION_MESSAGE);
				limpiarCampos();
			} else {
				String mensajeError = "Error al registrar doctor:\n\n";
				if (!doctorRegistrado) {
					mensajeError += "• No se pudo registrar el doctor\n";
				}
				if (!usuarioRegistrado) {
					mensajeError += "• No se pudo registrar el usuario\n";
				}
				mensajeError += "\nVerifique que no existan duplicados.";

				JOptionPane.showMessageDialog(this, mensajeError, "Error de registro", JOptionPane.ERROR_MESSAGE);
				Clinica.getInstance().recalcularContadorDoctores();
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error al registrar doctor:\n" + ex.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
			Clinica.getInstance().recalcularContadorDoctores();
		}
	}

	private void limpiarCampos() {
		txtNombre.setText("");
		txtApellido.setText("");
		txtCedula.setText("");
		txtTelefono.setText("");
		txtDireccion.setText("");
		txtNumeroLicencia.setText("");
		spnHorarioInicio.setValue("08:00");
		spnHorarioFin.setValue("17:00");
		txtCodigo.setText("DOC-" + Clinica.getInstance().getContadorDoctores());
		rdbtnHombre.setSelected(true);
		chckbxActivo.setSelected(true);
		cbxEspecialidad.setSelectedIndex(0);
		spnCitasPorDia.setValue(5);
		spnFechaNacimiento.setValue(new Date());
		txtUsuario.setText("");
		txtContrasena.setText("");
	}
}