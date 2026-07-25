package visual;

import javaBD.PacienteBD;
import logico.Alergia;
import logico.Control;
import logico.Doctor;
import logico.Paciente;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.MaskFormatter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class regPaciente extends JDialog {

	private final JPanel contentPanel =
			new JPanel();

	private JTextField txtCodigo;
	private JTextField txtNombre;
	private JTextField txtApellido;
	private JFormattedTextField txtCedula;
	private JFormattedTextField txtTelefono;
	private JTextArea txtdireccion;

	private ArrayList<Alergia> alegecitas;
	private Paciente pacienteCar;

	private JRadioButton rdbtnHombre;
	private JRadioButton rdbtnMujer;
	private JSpinner spnFechaNacimiento;
	private JComboBox<String> cbxTipoSangre;
	private JSpinner spnFechaActual;
	private JSpinner spnEstatura;
	private JSpinner spnPeso;
	private JCheckBox chckbxAlergias;
	private JButton btnModificar;

	private boolean esModificacion;

	private String telefonoOriginal = "";
	private String direccionOriginal = "";
	private float pesoOriginal = 0;
	private float estaturaOriginal = 0;

	public static void main(String[] args) {
		try {
			regPaciente dialog =
					new regPaciente(null);

			dialog.setDefaultCloseOperation(
					JDialog.DISPOSE_ON_CLOSE
			);

			dialog.setVisible(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public regPaciente(Paciente elpaci) {

		setIconImage(
				Toolkit.getDefaultToolkit()
						.getImage(
								regPaciente.class
										.getResource(
												"/recursos/pac.jpg"
										)
						)
		);

		pacienteCar = elpaci;

		/*
		 * =============================================================
		 * FUNCIONALIDAD ANTERIOR DE INTERESADOS
		 * =============================================================
		 *
		 * La detección de códigos INT- y XX, la conversión de interesados
		 * y la actualización de citas no forman parte de esta migración.
		 */

		esModificacion =
				pacienteCar != null
						&& pacienteCar
						.getCodigoPaciente()
						!= null
						&& !pacienteCar
						.getCodigoPaciente()
						.equals("XX")
						&& !pacienteCar
						.getCodigoPaciente()
						.startsWith("INT-");

		setTitle(
				esModificacion
						? "Modificar Paciente"
						: "Registro de Pacientes"
		);

		setBounds(100, 100, 600, 558);

		getContentPane().setLayout(
				new BorderLayout()
		);

		contentPanel.setBackground(
				new Color(220, 220, 220)
		);

		contentPanel.setBorder(
				new EmptyBorder(10, 10, 10, 10)
		);

		contentPanel.setLayout(null);

		getContentPane().add(
				contentPanel,
				BorderLayout.CENTER
		);

		setLocationRelativeTo(null);

		crearComponentes();
		configurarModoEdicion();

		if (pacienteCar != null
				&& esModificacion) {

			cargarPaciente();
			configurarDeteccionCambios();
		}
	}

	private void crearComponentes() {

		JLabel lblCodigo =
				new JLabel("Código");

		lblCodigo.setBounds(
				15, 23, 69, 20
		);

		contentPanel.add(lblCodigo);

		txtCodigo = new JTextField();

		txtCodigo.setBackground(
				new Color(224, 255, 255)
		);

		txtCodigo.setEnabled(false);

		txtCodigo.setBounds(
				111, 21, 120, 25
		);

		contentPanel.add(txtCodigo);

		/*
		 * =============================================================
		 * IMPLEMENTACIÓN ANTERIOR MEDIANTE ARCHIVOS .DAT
		 * =============================================================
		 *
		 * txtCodigo.setText(
		 *         "PAC-"
		 *         + Clinica.getInstance().contadorPacientes
		 * );
		 */

		/*
		 * =============================================================
		 * NUEVA IMPLEMENTACIÓN MEDIANTE BASE DE DATOS
		 * =============================================================
		 */
		if (!esModificacion) {
			txtCodigo.setText(
					PacienteBD.generarCodigoPaciente()
			);
		}

		JLabel lblNombre =
				new JLabel("Nombre(s)");

		lblNombre.setBounds(
				15, 57, 80, 20
		);

		contentPanel.add(lblNombre);

		txtNombre = new JTextField();

		txtNombre.setBounds(
				111, 55, 296, 25
		);

		contentPanel.add(txtNombre);

		JLabel lblApellido =
				new JLabel("Apellido(s)");

		lblApellido.setBounds(
				15, 95, 80, 20
		);

		contentPanel.add(lblApellido);

		txtApellido = new JTextField();

		txtApellido.setBounds(
				111, 93, 296, 25
		);

		contentPanel.add(txtApellido);

		JLabel lblCedula =
				new JLabel("Cédula");

		lblCedula.setBounds(
				15, 133, 69, 20
		);

		contentPanel.add(lblCedula);

		try {
			MaskFormatter cedulaMask =
					new MaskFormatter(
							"###-#######-#"
					);

			cedulaMask.setPlaceholderCharacter(
					'_'
			);

			txtCedula =
					new JFormattedTextField(
							cedulaMask
					);

		} catch (Exception e) {
			txtCedula =
					new JFormattedTextField();
		}

		txtCedula.setBounds(
				111, 131, 196, 25
		);

		contentPanel.add(txtCedula);

		JLabel lblTipoSangre =
				new JLabel("Tipo de sangre");

		lblTipoSangre.setBounds(
				322, 133, 100, 20
		);

		contentPanel.add(lblTipoSangre);

		cbxTipoSangre =
				new JComboBox<>();

		cbxTipoSangre.setModel(
				new DefaultComboBoxModel<>(
						new String[]{
								"<Tipo>",
								"A+", "A-",
								"B+", "B-",
								"AB+", "AB-",
								"O+", "O-"
						}
				)
		);

		cbxTipoSangre.setBounds(
				450, 128, 100, 25
		);

		contentPanel.add(cbxTipoSangre);

		JLabel lblEstatura =
				new JLabel("Estatura(cm)");

		lblEstatura.setBounds(
				450, 57, 99, 20
		);

		contentPanel.add(lblEstatura);

		spnEstatura = new JSpinner();

		spnEstatura.setModel(
				new SpinnerNumberModel(
						150.0,
						1.0,
						300.0,
						1.0
				)
		);

		spnEstatura.setBounds(
				450, 79, 80, 25
		);

		contentPanel.add(spnEstatura);

		JLabel lblPeso =
				new JLabel("Peso(lb)");

		lblPeso.setBounds(
				15, 166, 69, 20
		);

		contentPanel.add(lblPeso);

		spnPeso = new JSpinner();

		spnPeso.setModel(
				new SpinnerNumberModel(
						150.0,
						1.0,
						500.0,
						1.0
				)
		);

		spnPeso.setBounds(
				111, 164, 94, 25
		);

		contentPanel.add(spnPeso);

		JLabel lblFechaNacimiento =
				new JLabel(
						"Fecha de nacimiento"
				);

		lblFechaNacimiento.setBounds(
				287, 169, 120, 20
		);

		contentPanel.add(
				lblFechaNacimiento
		);

		spnFechaNacimiento =
				new JSpinner();

		spnFechaNacimiento.setModel(
				new SpinnerDateModel(
						new Date(),
						null,
						new Date(),
						Calendar.DAY_OF_YEAR
				)
		);

		JSpinner.DateEditor
				editorNacimiento =
				new JSpinner.DateEditor(
						spnFechaNacimiento,
						"dd/MM/yyyy"
				);

		spnFechaNacimiento.setEditor(
				editorNacimiento
		);

		spnFechaNacimiento.setBounds(
				414, 166, 136, 25
		);

		contentPanel.add(
				spnFechaNacimiento
		);

		JLabel lblTelefono =
				new JLabel("Teléfono");

		lblTelefono.setBounds(
				15, 214, 69, 20
		);

		contentPanel.add(lblTelefono);

		try {
			MaskFormatter telefonoMask =
					new MaskFormatter(
							"(###) ###-####"
					);

			telefonoMask.setPlaceholderCharacter(
					'_'
			);

			txtTelefono =
					new JFormattedTextField(
							telefonoMask
					);

		} catch (Exception e) {
			txtTelefono =
					new JFormattedTextField();
		}

		txtTelefono.setBounds(
				111, 212, 196, 25
		);

		contentPanel.add(txtTelefono);

		JLabel lblSexo =
				new JLabel("Sexo");

		lblSexo.setBounds(
				336, 214, 69, 20
		);

		contentPanel.add(lblSexo);

		rdbtnHombre =
				new JRadioButton(
						"Masculino"
				);

		rdbtnHombre.setBackground(
				new Color(220, 220, 220)
		);

		rdbtnHombre.setSelected(true);

		rdbtnHombre.setBounds(
				388, 212, 94, 25
		);

		contentPanel.add(rdbtnHombre);

		rdbtnMujer =
				new JRadioButton(
						"Femenino"
				);

		rdbtnMujer.setBackground(
				new Color(220, 220, 220)
		);

		rdbtnMujer.setBounds(
				483, 212, 91, 25
		);

		contentPanel.add(rdbtnMujer);

		ButtonGroup grupoSexo =
				new ButtonGroup();

		grupoSexo.add(rdbtnHombre);
		grupoSexo.add(rdbtnMujer);

		JLabel lblDireccion =
				new JLabel("Dirección");

		lblDireccion.setBounds(
				15, 287, 69, 20
		);

		contentPanel.add(lblDireccion);

		JScrollPane scrollDireccion =
				new JScrollPane();

		scrollDireccion.setBounds(
				100, 258, 454, 80
		);

		contentPanel.add(scrollDireccion);

		txtdireccion = new JTextArea();

		txtdireccion.setLineWrap(true);
		txtdireccion.setWrapStyleWord(true);

		scrollDireccion.setViewportView(
				txtdireccion
		);

		JLabel lblAlergias =
				new JLabel(
						"¿Padece de alguna alergia?"
				);

		lblAlergias.setBounds(
				15, 351, 200, 20
		);

		contentPanel.add(lblAlergias);

		chckbxAlergias =
				new JCheckBox(
						"Sí, padezco de alergias"
				);

		chckbxAlergias.setBackground(
				new Color(220, 220, 220)
		);

		chckbxAlergias.setBounds(
				222, 349, 200, 25
		);

		contentPanel.add(
				chckbxAlergias
		);

		JLabel lblFecha =
				new JLabel("Fecha Registro");

		lblFecha.setBounds(
				15, 400, 100, 20
		);

		contentPanel.add(lblFecha);

		spnFechaActual =
				new JSpinner();

		spnFechaActual.setEnabled(false);

		spnFechaActual.setModel(
				new SpinnerDateModel(
						new Date(),
						null,
						null,
						Calendar.DAY_OF_YEAR
				)
		);

		JSpinner.DateEditor
				editorActual =
				new JSpinner.DateEditor(
						spnFechaActual,
						"dd/MM/yyyy"
				);

		spnFechaActual.setEditor(
				editorActual
		);

		spnFechaActual.setBounds(
				111, 398, 120, 25
		);

		contentPanel.add(spnFechaActual);

		JPanel buttonPane =
				new JPanel();

		buttonPane.setBackground(
				new Color(220, 220, 220)
		);

		buttonPane.setLayout(
				new FlowLayout(
						FlowLayout.RIGHT
				)
		);

		getContentPane().add(
				buttonPane,
				BorderLayout.SOUTH
		);

		btnModificar =
				new JButton(
						esModificacion
								? "Modificar"
								: "Registrar"
				);

		btnModificar.setBackground(
				new Color(224, 255, 255)
		);

		btnModificar.addActionListener(
				this::procesarPaciente
		);

		buttonPane.add(btnModificar);

		getRootPane().setDefaultButton(
				btnModificar
		);

		JButton cancelButton =
				new JButton("Cancelar");

		cancelButton.setBackground(
				new Color(255, 228, 181)
		);

		cancelButton.addActionListener(
				e -> dispose()
		);

		buttonPane.add(cancelButton);
	}

	private void configurarModoEdicion() {

		if (esModificacion) {

			setTitle("Modificar Paciente");
			btnModificar.setText("Modificar");

			txtCedula.setEnabled(false);
			txtNombre.setEnabled(false);
			txtApellido.setEnabled(false);
			spnFechaNacimiento.setEnabled(false);
			rdbtnHombre.setEnabled(false);
			rdbtnMujer.setEnabled(false);
			cbxTipoSangre.setEnabled(false);
			chckbxAlergias.setEnabled(false);
			chckbxAlergias.setVisible(false);

			JOptionPane.showMessageDialog(
					this,
					"Para pacientes ya registrados "
							+ "solo puede modificar:\n"
							+ "• Teléfono\n"
							+ "• Dirección\n"
							+ "• Peso\n"
							+ "• Estatura",
					"Modificación Limitada",
					JOptionPane.INFORMATION_MESSAGE
			);

		} else {

			setTitle(
					"Registro de Nuevo Paciente"
			);

			btnModificar.setText("Registrar");

			chckbxAlergias.setEnabled(true);
			chckbxAlergias.setVisible(true);
		}
	}

	private void configurarDeteccionCambios() {

		btnModificar.setEnabled(false);

		DocumentListener cambioListener =
				new DocumentListener() {

					@Override
					public void insertUpdate(
							DocumentEvent e
					) {
						verificarCambios();
					}

					@Override
					public void removeUpdate(
							DocumentEvent e
					) {
						verificarCambios();
					}

					@Override
					public void changedUpdate(
							DocumentEvent e
					) {
						verificarCambios();
					}
				};

		txtTelefono.getDocument()
				.addDocumentListener(
						cambioListener
				);

		txtdireccion.getDocument()
				.addDocumentListener(
						cambioListener
				);

		spnPeso.addChangeListener(
				e -> verificarCambios()
		);

		spnEstatura.addChangeListener(
				e -> verificarCambios()
		);
	}

	private void verificarCambios() {

		if (!esModificacion) {
			return;
		}

		String telefonoActual =
				txtTelefono.getText()
						.replaceAll(
								"[^0-9]",
								""
						);

		String direccionActual =
				txtdireccion.getText().trim();

		float pesoActual =
				((Number)
						spnPeso.getValue())
						.floatValue();

		float estaturaActual =
				((Number)
						spnEstatura.getValue())
						.floatValue();

		boolean huboCambios =
				!telefonoActual.equals(
						telefonoOriginal
				)
						|| !direccionActual.equals(
						direccionOriginal
				)
						|| pesoActual != pesoOriginal
						|| estaturaActual
						!= estaturaOriginal;

		btnModificar.setEnabled(
				huboCambios
		);
	}

	private void cargarPaciente() {

		if (pacienteCar == null) {
			return;
		}

		txtCodigo.setText(
				pacienteCar.getCodigoPaciente()
		);

		txtdireccion.setText(
				pacienteCar.getDireccion()
						!= null
						? pacienteCar.getDireccion()
						: ""
		);

		txtApellido.setText(
				pacienteCar.getApellido()
						!= null
						? pacienteCar.getApellido()
						: ""
		);

		txtCedula.setText(
				pacienteCar.getCedula()
						!= null
						? pacienteCar.getCedula()
						: ""
		);

		txtNombre.setText(
				pacienteCar.getNombre()
						!= null
						? pacienteCar.getNombre()
						: ""
		);

		txtTelefono.setText(
				pacienteCar.getTelefono()
						!= null
						? pacienteCar.getTelefono()
						: ""
		);

		if (pacienteCar.getEstatura() > 0) {
			spnEstatura.setValue(
					(double)
							pacienteCar.getEstatura()
			);
		}

		if (pacienteCar.getPeso() > 0) {
			spnPeso.setValue(
					(double)
							pacienteCar.getPeso()
			);
		}

		if (pacienteCar
				.getFechaNacimiento()
				!= null) {

			Date fechaNacDate =
					Date.from(
							pacienteCar
									.getFechaNacimiento()
									.atStartOfDay(
											ZoneId.systemDefault()
									)
									.toInstant()
					);

			spnFechaNacimiento.setValue(
					fechaNacDate
			);
		}

		if (pacienteCar
				.getFechaRegistro()
				!= null) {

			Date fechaRegDate =
					Date.from(
							pacienteCar
									.getFechaRegistro()
									.atStartOfDay(
											ZoneId.systemDefault()
									)
									.toInstant()
					);

			spnFechaActual.setValue(
					fechaRegDate
			);
		}

		if (pacienteCar.getSexo() == 'M') {
			rdbtnHombre.setSelected(true);

		} else if (
				pacienteCar.getSexo() == 'F'
		) {
			rdbtnMujer.setSelected(true);
		}

		if (pacienteCar.getTipoSangre()
				!= null) {

			cbxTipoSangre.setSelectedItem(
					pacienteCar.getTipoSangre()
			);
		}

		telefonoOriginal =
				pacienteCar.getTelefono()
						!= null
						? pacienteCar.getTelefono()
						  .replaceAll(
								  "[^0-9]",
								  ""
						  )
						: "";

		direccionOriginal =
				pacienteCar.getDireccion()
						!= null
						? pacienteCar.getDireccion()
						: "";

		pesoOriginal =
				pacienteCar.getPeso();

		estaturaOriginal =
				pacienteCar.getEstatura();
	}

	private boolean validarNombre(
			String texto,
			String campo
	) {

		if (!texto.matches(
				"[a-záéíóúñüA-ZÁÉÍÓÚÑÜ ]+"
		)) {

			JOptionPane.showMessageDialog(
					this,
					campo
							+ " solo puede contener letras",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);

			return false;
		}

		return true;
	}

	private void tomarAlergias() {

		TomaAlergias dialogAlergias =
				new TomaAlergias();

		dialogAlergias.setModal(true);
		dialogAlergias.setVisible(true);

		alegecitas =
				dialogAlergias
						.AlergiasSeleccionadas();

		if (alegecitas == null
				|| alegecitas.isEmpty()) {

			chckbxAlergias
					.setSelected(false);

			alegecitas = null;

		} else {

			chckbxAlergias
					.setSelected(true);
		}

		dialogAlergias.dispose();
	}

	private void procesarPaciente(
			ActionEvent evento
	) {

		String cedulaLimpia =
				txtCedula.getText()
						.replaceAll(
								"[^0-9]",
								""
						);

		String telefonoLimpio =
				txtTelefono.getText()
						.replaceAll(
								"[^0-9]",
								""
						);

		/*
		 * =============================================================
		 * VALIDACIÓN DEL TELÉFONO AL MODIFICAR EN BASE DE DATOS
		 * =============================================================
		 *
		 * Solo se consulta si el teléfono cambió.
		 * El código del paciente se excluye de la búsqueda para que
		 * su propio teléfono actual no aparezca como duplicado.
		 */
		if (esModificacion) {

			if (!telefonoLimpio.equals(telefonoOriginal)
					&& PacienteBD.existeTelefonoEnSistema(
					telefonoLimpio,
					pacienteCar.getCodigoPaciente()
			)) {

				JOptionPane.showMessageDialog(
						this,
						"El teléfono ya está registrado en el sistema.",
						"Teléfono duplicado",
						JOptionPane.ERROR_MESSAGE
				);

				return;
			}
		}


		if (txtNombre.getText()
				.trim().isEmpty()
				|| txtApellido.getText()
				.trim().isEmpty()
				|| cedulaLimpia.length() != 11
				|| telefonoLimpio.length() != 10
				|| txtdireccion.getText()
				.trim().isEmpty()
				|| cbxTipoSangre
				.getSelectedIndex() == 0) {

			JOptionPane.showMessageDialog(
					this,
					"Complete todos los campos",
					"Incompleto",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (!validarNombre(
				txtNombre.getText().trim(),
				"El nombre"
		)) {

			txtNombre.requestFocus();
			return;
		}

		if (!validarNombre(
				txtApellido.getText().trim(),
				"El apellido"
		)) {

			txtApellido.requestFocus();
			return;
		}

		/*
		 * =============================================================
		 * VALIDACIONES ANTERIORES MEDIANTE ARCHIVOS / ARRAYLIST
		 * =============================================================
		 *
		 * Clinica.getInstance().isCedulaRegistrada(...)
		 * Clinica.getInstance().isTelefonoRegistrado(...)
		 *
		 * Esas validaciones recorrían las listas cargadas desde .dat.
		 */

		/*
		 * =============================================================
		 * NUEVAS VALIDACIONES MEDIANTE BASE DE DATOS
		 * =============================================================
		 *
		 * Para un registro nuevo se revisan las tablas paciente y doctor.
		 */
		if (!esModificacion) {

			if (PacienteBD
					.existeCedulaEnSistema(
							cedulaLimpia
					)) {

				JOptionPane.showMessageDialog(
						this,
						"La cédula ya está registrada "
								+ "en el sistema.",
						"Cédula duplicada",
						JOptionPane.ERROR_MESSAGE
				);

				return;
			}

			if (PacienteBD
					.existeTelefonoEnSistema(
							telefonoLimpio
					)) {

				JOptionPane.showMessageDialog(
						this,
						"El teléfono ya está registrado "
								+ "en el sistema.",
						"Teléfono duplicado",
						JOptionPane.ERROR_MESSAGE
				);

				return;
			}
		}

		float peso =
				((Number)
						spnPeso.getValue())
						.floatValue();

		float estatura =
				((Number)
						spnEstatura.getValue())
						.floatValue();

		if (peso < 1 || peso > 500) {

			JOptionPane.showMessageDialog(
					this,
					"Peso: 1-500 libras",
					"Inválido",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		if (estatura < 1
				|| estatura > 300) {

			JOptionPane.showMessageDialog(
					this,
					"Estatura: 1-300 cm",
					"Inválido",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		char sexo =
				rdbtnHombre.isSelected()
						? 'M'
						: 'F';

		if (!esModificacion
				&& chckbxAlergias.isSelected()
				&& alegecitas == null) {

			tomarAlergias();

			if (alegecitas == null) {

				int confirmacion =
						JOptionPane
								.showConfirmDialog(
										this,
										"¿Continuar sin alergias?",
										"Sin alergias",
										JOptionPane.YES_NO_OPTION,
										JOptionPane.QUESTION_MESSAGE
								);

				if (confirmacion
						== JOptionPane.NO_OPTION) {

					return;
				}
			}
		}

		try {

			if (esModificacion) {

				/*
				 * =============================================================
				 * NUEVA MODIFICACIÓN MEDIANTE BASE DE DATOS MYSQL
				 * =============================================================
				 */
				modificarPacienteEnBaseDeDatos(
						telefonoLimpio,
						peso,
						estatura
				);

			} else {

				registrarPacienteEnBaseDeDatos(
						cedulaLimpia,
						telefonoLimpio,
						peso,
						estatura,
						sexo
				);
			}

		} catch (Exception ex) {

			JOptionPane.showMessageDialog(
					this,
					"Ocurrió un error al procesar "
							+ "el paciente.\n"
							+ ex.getMessage(),
					"Error",
					JOptionPane.ERROR_MESSAGE
			);

			ex.printStackTrace();
		}
	}

	private void registrarPacienteEnBaseDeDatos(
			String cedulaLimpia,
			String telefonoLimpio,
			float peso,
			float estatura,
			char sexo
	) {

		Date fechaNacDate =
				(Date)
						spnFechaNacimiento
								.getValue();

		LocalDate fechaNacimiento =
				fechaNacDate.toInstant()
						.atZone(
								ZoneId.systemDefault()
						)
						.toLocalDate();

		if (fechaNacimiento.isAfter(
				LocalDate.now()
		)) {

			JOptionPane.showMessageDialog(
					this,
					"La fecha de nacimiento "
							+ "no puede ser futura",
					"Fecha inválida",
					JOptionPane.ERROR_MESSAGE
			);

			return;
		}

		Doctor doctorRegistrador =
				Control.getDoctorLogeado();

		if (doctorRegistrador == null) {

			JOptionPane.showMessageDialog(
					this,
					"No se pudo registrar el paciente "
							+ "porque no hay un doctor "
							+ "con sesión iniciada.",
					"Doctor no identificado",
					JOptionPane.ERROR_MESSAGE
			);

			return;
		}

		String codigoDoctorRegistrador =
				doctorRegistrador
						.getCodigoDoctor();

		String licenciaDoctor =
				doctorRegistrador
						.getNumeroLicencia();

		Paciente nuevoPaciente =
				new Paciente(
						cedulaLimpia,
						txtNombre.getText().trim(),
						txtApellido.getText().trim(),
						telefonoLimpio,
						txtCodigo.getText(),
						licenciaDoctor
				);

		nuevoPaciente.setDireccion(
				txtdireccion.getText().trim()
		);

		nuevoPaciente.setFechaNacimiento(
				fechaNacimiento
		);

		nuevoPaciente.setSexo(sexo);

		nuevoPaciente.setTipoSangre(
				cbxTipoSangre
						.getSelectedItem()
						.toString()
		);

		nuevoPaciente.setPeso(peso);
		nuevoPaciente.setEstatura(estatura);

		nuevoPaciente.setAlergias(
				alegecitas != null
						? alegecitas
						: new ArrayList<>()
		);

		/*
		 * =============================================================
		 * IMPLEMENTACIÓN ANTERIOR MEDIANTE ARCHIVOS .DAT
		 * =============================================================
		 *
		 * Clinica.getInstance()
		 *         .registrarPaciente(nuevoPaciente);
		 *
		 * contadorPacientes++;
		 * PersistenciaManager.guardarDatos();
		 *
		 * Este bloque queda documentado, pero no se ejecuta.
		 */

		/*
		 * =============================================================
		 * NUEVA IMPLEMENTACIÓN MEDIANTE BASE DE DATOS
		 * =============================================================
		 *
		 * PacienteBD registra:
		 * 1. Paciente.
		 * 2. Relaciones paciente_alergia.
		 * Todo dentro de una transacción.
		 */
		boolean registrado =
				PacienteBD.registrarPaciente(
						nuevoPaciente,
						codigoDoctorRegistrador
				);

		if (!registrado) {

			String mensaje =
					PacienteBD.getUltimoError();

			if (mensaje == null
					|| mensaje.trim().isEmpty()) {

				mensaje =
						"No se pudo registrar "
								+ "el paciente.";
			}

			JOptionPane.showMessageDialog(
					this,
					mensaje,
					"Registro no realizado",
					JOptionPane.ERROR_MESSAGE
			);

			return;
		}

		/*
		 * Después de guardar el paciente y sus alergias,
		 * se permite registrar las vacunas que ya tenía antes
		 * de entrar al sistema.
		 */
		int respuesta =
				JOptionPane.showConfirmDialog(
						this,
						"¿Desea registrar vacunas previas "
								+ "del paciente?",
						"Historial de Vacunas",
						JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE
				);

		if (respuesta
				== JOptionPane.YES_OPTION) {

			selecVacunas dialogVacunas =
					new selecVacunas(
							nuevoPaciente
					);

			dialogVacunas.setModal(true);
			dialogVacunas.setVisible(true);
		}

		JOptionPane.showMessageDialog(
				this,
				"PACIENTE REGISTRADO "
						+ "EXITOSAMENTE\n\n"
						+ "Código: "
						+ txtCodigo.getText()
						+ "\nNombre: "
						+ txtNombre.getText()
						+ " "
						+ txtApellido.getText()
						+ "\nCédula: "
						+ cedulaLimpia
						+ "\nDoctor registrador: "
						+ doctorRegistrador
						.getNombre(),
				"Registro Exitoso",
				JOptionPane.INFORMATION_MESSAGE
		);

		dispose();
	}

	private void modificarPacienteEnBaseDeDatos(
			String telefonoLimpio,
			float peso,
			float estatura
	) {

		pacienteCar.setDireccion(
				txtdireccion.getText().trim()
		);

		pacienteCar.setTelefono(
				telefonoLimpio
		);

		pacienteCar.setEstatura(
				estatura
		);

		pacienteCar.setPeso(
				peso
		);

		/*
		 * =============================================================
		 * IMPLEMENTACIÓN ANTERIOR MEDIANTE ARCHIVOS / ARRAYLIST
		 * =============================================================
		 *
		 * if (Clinica.getInstance()
		 *         .modificarPaciente(pacienteCar)) {
		 *
		 *     JOptionPane.showMessageDialog(
		 *             this,
		 *             "Paciente modificado",
		 *             "Éxito",
		 *             JOptionPane.INFORMATION_MESSAGE
		 *     );
		 *
		 *     dispose();
		 * }
		 *
		 * Esta implementación ya no se ejecuta porque la modificación
		 * del paciente se guarda directamente en MySQL.
		 */

		/*
		 * =============================================================
		 * NUEVA IMPLEMENTACIÓN MEDIANTE BASE DE DATOS MYSQL
		 * =============================================================
		 */
		boolean modificado =
				PacienteBD.modificarPaciente(
						pacienteCar
				);

		if (!modificado) {

			String mensaje =
					PacienteBD.getUltimoError();

			if (mensaje == null
					|| mensaje.trim().isEmpty()) {

				mensaje =
						"No se pudo modificar el paciente.";
			}

			JOptionPane.showMessageDialog(
					this,
					mensaje,
					"Modificación no realizada",
					JOptionPane.ERROR_MESSAGE
			);

			return;
		}

		JOptionPane.showMessageDialog(
				this,
				"Paciente modificado correctamente.",
				"Éxito",
				JOptionPane.INFORMATION_MESSAGE
		);

		dispose();
	}

	public Paciente getPaciente() {
		return pacienteCar;
	}
}
