package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import logico.Clinica;
import logico.Paciente;
import logico.RegistroVacuna;
import logico.Vacuna;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.ZoneId;
import java.awt.event.ActionEvent;
import javax.swing.JSpinner;
import javax.swing.SpinnerDateModel;
import java.util.Date;
import java.util.Calendar;
import javax.swing.SpinnerNumberModel;
import javax.swing.DefaultComboBoxModel;
import java.awt.Color;
import javax.swing.JScrollPane;
import java.awt.Toolkit;

public class adminVacuna extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtEnfermera;
	private JButton btnVerAlergias;
	private JTextField txtCedula;
	private JTextField txtSangre;
	private JTextField txtLote;
	private JTextField txtEnfermedad;
	private JTextField txtLaboratorio;

	// Variables de instancia para los componentes
	private JComboBox<String> cbxPaciente;
	private JComboBox<String> cbxVacuna;
	private JSpinner spnPeso;
	private JSpinner spnEstatura;
	private JSpinner spnCantidad;
	private JSpinner spnVencimiento;
	private JSpinner spnFecha;

	private Vacuna vacunaSeleccionada = null;
	private Paciente pacienteSeleccionado = null;
	private JLabel lblInfoPaciente;
	private JLabel lblInfoVacuna;

	public static void main(String[] args) {
		try {
			adminVacuna dialog = new adminVacuna();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public adminVacuna() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(adminVacuna.class.getResource("/recursos/agu.jpg")));
		setTitle("Administrar Vacuna");
		setBounds(100, 100, 700, 550);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(255, 235, 205));
		contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		setLocationRelativeTo(null);
		setResizable(false);

		Font labelFont = new Font("Tahoma", Font.PLAIN, 12);
		Font titleFont = new Font("Tahoma", Font.BOLD, 13);

		JLabel lblTitulo = new JLabel("ADMINISTRAR VACUNA");
		lblTitulo.setFont(titleFont);
		lblTitulo.setBounds(250, 10, 200, 20);
		contentPanel.add(lblTitulo);

		// PACIENTE
		JLabel lblSeccionPaciente = new JLabel("Paciente:");
		lblSeccionPaciente.setFont(labelFont);
		lblSeccionPaciente.setBounds(15, 40, 200, 20);
		contentPanel.add(lblSeccionPaciente);

		cbxPaciente = new JComboBox<String>();
		cbxPaciente.setModel(new DefaultComboBoxModel(new String[] { "<Seleccione>" }));
		for (Paciente paciente : Clinica.getInstance().getPacientes()) {
			if (paciente != null && paciente.isActivo()) {
				cbxPaciente.addItem(
						paciente.getCodigoPaciente() + ": " + paciente.getNombre() + " " + paciente.getApellido());
			}
		}
		cbxPaciente.setBounds(15, 65, 250, 26);
		contentPanel.add(cbxPaciente);

		btnVerAlergias = new JButton("Alergias");
		btnVerAlergias.setEnabled(false);
		btnVerAlergias.setBounds(280, 65, 100, 26);
		contentPanel.add(btnVerAlergias);

		JButton btnRegistrarPaciente = new JButton("Nuevo Paciente");
		btnRegistrarPaciente.setBounds(395, 65, 150, 26);
		contentPanel.add(btnRegistrarPaciente);

		lblInfoPaciente = new JLabel("Seleccione un paciente");
		lblInfoPaciente.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblInfoPaciente.setBounds(15, 100, 400, 15);
		contentPanel.add(lblInfoPaciente);

		JLabel lblCedula = new JLabel("Cédula:");
		lblCedula.setFont(labelFont);
		lblCedula.setBounds(15, 125, 80, 20);
		contentPanel.add(lblCedula);

		txtCedula = new JTextField();
		txtCedula.setEditable(false);
		txtCedula.setBounds(15, 145, 150, 25);
		contentPanel.add(txtCedula);

		JLabel lblSangre = new JLabel("Tipo Sangre:");
		lblSangre.setFont(labelFont);
		lblSangre.setBounds(180, 125, 80, 20);
		contentPanel.add(lblSangre);

		txtSangre = new JTextField();
		txtSangre.setEditable(false);
		txtSangre.setBounds(180, 145, 80, 25);
		contentPanel.add(txtSangre);

		JLabel lblPeso = new JLabel("Peso (kg):");
		lblPeso.setFont(labelFont);
		lblPeso.setBounds(280, 125, 80, 20);
		contentPanel.add(lblPeso);

		spnPeso = new JSpinner();
		spnPeso.setModel(new SpinnerNumberModel(0.0, 0.0, 300.0, 0.1));
		spnPeso.setEnabled(false);
		spnPeso.setBounds(280, 145, 80, 25);
		contentPanel.add(spnPeso);

		JLabel lblEstatura = new JLabel("Estatura (cm):");
		lblEstatura.setFont(labelFont);
		lblEstatura.setBounds(380, 125, 100, 20);
		contentPanel.add(lblEstatura);

		spnEstatura = new JSpinner();
		spnEstatura.setModel(new SpinnerNumberModel(0.0, 0.0, 250.0, 0.1));
		spnEstatura.setEnabled(false);
		spnEstatura.setBounds(380, 145, 80, 25);
		contentPanel.add(spnEstatura);

		// VACUNA
		JLabel lblSeccionVacuna = new JLabel("Vacuna:");
		lblSeccionVacuna.setFont(labelFont);
		lblSeccionVacuna.setBounds(15, 185, 200, 20);
		contentPanel.add(lblSeccionVacuna);

		cbxVacuna = new JComboBox<String>();
		cbxVacuna.setModel(new DefaultComboBoxModel(new String[] { "<Seleccione>" }));
		cargarVacunasDisponibles();
		cbxVacuna.setBounds(15, 210, 250, 26);
		contentPanel.add(cbxVacuna);

		JButton btnRegistrarVacuna = new JButton("Nueva Vacuna");
		btnRegistrarVacuna.setBounds(280, 210, 150, 26);
		contentPanel.add(btnRegistrarVacuna);

		lblInfoVacuna = new JLabel("Seleccione una vacuna");
		lblInfoVacuna.setFont(new Font("Tahoma", Font.ITALIC, 11));
		lblInfoVacuna.setBounds(15, 245, 400, 15);
		contentPanel.add(lblInfoVacuna);

		JLabel lblLote = new JLabel("Lote:");
		lblLote.setFont(labelFont);
		lblLote.setBounds(15, 270, 80, 20);
		contentPanel.add(lblLote);

		txtLote = new JTextField();
		txtLote.setEditable(false);
		txtLote.setBounds(15, 290, 120, 25);
		contentPanel.add(txtLote);

		JLabel lblEnfermedad = new JLabel("Enfermedad:");
		lblEnfermedad.setFont(labelFont);
		lblEnfermedad.setBounds(150, 270, 100, 20);
		contentPanel.add(lblEnfermedad);

		txtEnfermedad = new JTextField();
		txtEnfermedad.setEditable(false);
		txtEnfermedad.setBounds(150, 290, 180, 25);
		contentPanel.add(txtEnfermedad);

		JLabel lblLaboratorio = new JLabel("Laboratorio:");
		lblLaboratorio.setFont(labelFont);
		lblLaboratorio.setBounds(340, 270, 100, 20);
		contentPanel.add(lblLaboratorio);

		txtLaboratorio = new JTextField();
		txtLaboratorio.setEditable(false);
		txtLaboratorio.setBounds(340, 290, 150, 25);
		contentPanel.add(txtLaboratorio);

		JLabel lblCantidad = new JLabel("Stock:");
		lblCantidad.setFont(labelFont);
		lblCantidad.setBounds(500, 270, 80, 20);
		contentPanel.add(lblCantidad);

		spnCantidad = new JSpinner();
		spnCantidad.setEnabled(false);
		spnCantidad.setBounds(500, 290, 60, 25);
		contentPanel.add(spnCantidad);

		JLabel lblVencimiento = new JLabel("Vencimiento:");
		lblVencimiento.setFont(labelFont);
		lblVencimiento.setBounds(15, 325, 80, 20);
		contentPanel.add(lblVencimiento);

		spnVencimiento = new JSpinner();
		spnVencimiento.setEnabled(false);
		spnVencimiento.setModel(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_YEAR));
		JSpinner.DateEditor editorVencimiento = new JSpinner.DateEditor(spnVencimiento, "dd/MM/yyyy");
		spnVencimiento.setEditor(editorVencimiento);
		spnVencimiento.setBounds(15, 345, 120, 25);
		contentPanel.add(spnVencimiento);

		// ADMINISTRACIÓN
		JLabel lblSeccionAdmin = new JLabel("Aplicación:");
		lblSeccionAdmin.setFont(labelFont);
		lblSeccionAdmin.setBounds(15, 385, 250, 20);
		contentPanel.add(lblSeccionAdmin);

		JLabel lblEnfermera = new JLabel("Administrada por:");
		lblEnfermera.setFont(labelFont);
		lblEnfermera.setBounds(15, 410, 150, 20);
		contentPanel.add(lblEnfermera);

		txtEnfermera = new JTextField();
		txtEnfermera.setBounds(15, 430, 250, 25);
		contentPanel.add(txtEnfermera);

		JLabel lblFecha = new JLabel("Fecha:");
		lblFecha.setFont(labelFont);
		lblFecha.setBounds(280, 410, 150, 20);
		contentPanel.add(lblFecha);

		spnFecha = new JSpinner();
		spnFecha.setModel(new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_YEAR));
		JSpinner.DateEditor editorFecha = new JSpinner.DateEditor(spnFecha, "dd/MM/yyyy");
		spnFecha.setEditor(editorFecha);
		spnFecha.setBounds(280, 430, 120, 25);
		contentPanel.add(spnFecha);

		// LISTENERS
		cbxPaciente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cbxPaciente.getSelectedIndex() <= 0 || cbxPaciente.getSelectedItem() == null) {
					limpiarDatosPaciente();
					btnVerAlergias.setEnabled(false);
					pacienteSeleccionado = null;
					return;
				}

				try {
					String itemSeleccionado = cbxPaciente.getSelectedItem().toString();
					String[] partes = itemSeleccionado.split(":");
					if (partes.length > 0) {
						String codigoPaciente = partes[0].trim();
						pacienteSeleccionado = Clinica.getInstance().buscarPacientePorCodigo(codigoPaciente);

						if (pacienteSeleccionado != null) {
							cargarDatosPaciente();
							btnVerAlergias.setEnabled(true);
							lblInfoPaciente.setText("Paciente: " + pacienteSeleccionado.getNombre() + " "
									+ pacienteSeleccionado.getApellido());
						}
					}
				} catch (Exception ex) {
					// Ignorar errores al limpiar el combo
				}
			}
		});

		cbxVacuna.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (cbxVacuna.getSelectedIndex() <= 0 || cbxVacuna.getSelectedItem() == null) {
					limpiarDatosVacuna();
					vacunaSeleccionada = null;
					return;
				}

				try {
					String itemSeleccionado = cbxVacuna.getSelectedItem().toString();
					String[] partes = itemSeleccionado.split(":");
					if (partes.length > 0) {
						String codigoVacuna = partes[0].trim();
						vacunaSeleccionada = Clinica.getInstance().buscarVacunaPorCodigo(codigoVacuna);

						if (vacunaSeleccionada != null) {
							cargarDatosVacuna();
						}
					}
				} catch (Exception ex) {
					// Ignorar errores al limpiar el combo
				}
			}
		});

		btnVerAlergias.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (pacienteSeleccionado != null) {
					verAlergias dialog = new verAlergias(pacienteSeleccionado);
					dialog.setModal(true);
					dialog.setVisible(true);
				}
			}
		});

		btnRegistrarPaciente.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				regPaciente dialog = new regPaciente(null);
				dialog.setModal(true);
				dialog.setVisible(true);
				refrescarComboPacientes();
			}
		});

		btnRegistrarVacuna.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				regVacuna dialog = new regVacuna();
				dialog.setModal(true);
				dialog.setVisible(true);
				cargarVacunasDisponibles();
			}
		});

		// BOTONES
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(224, 255, 255));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton btnAdministrar = new JButton("Administrar");
		btnAdministrar.setBackground(new Color(211, 211, 211));
		btnAdministrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				administrarVacuna();
			}
		});
		buttonPane.add(btnAdministrar);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setBackground(new Color(255, 245, 238));
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPane.add(btnCancelar);
	}

	private void cargarVacunasDisponibles() {
		cbxVacuna.removeAllItems();
		cbxVacuna.addItem("<Seleccione>");

		for (Vacuna vacuna : Clinica.getInstance().getVacunas()) {
			if (vacuna != null && vacuna.isActiva() && vacuna.getCantidad() > 0 && !vacuna.estaCaducada()) {
				String texto = vacuna.getCodigoVacuna() + ": " + vacuna.getNombre();
				if (vacuna.getCantidad() <= 5) {
					texto += " (Stock: " + vacuna.getCantidad() + ")";
				}
				cbxVacuna.addItem(texto);
			}
		}
	}

	private void cargarDatosPaciente() {
		if (pacienteSeleccionado == null)
			return;

		txtCedula.setText(pacienteSeleccionado.getCedula());
		txtSangre.setText(pacienteSeleccionado.getTipoSangre());
		spnPeso.setValue(pacienteSeleccionado.getPeso());
		spnEstatura.setValue(pacienteSeleccionado.getEstatura());
	}

	private void cargarDatosVacuna() {
		if (vacunaSeleccionada == null)
			return;

		txtLote.setText(vacunaSeleccionada.getNumeroLote());
		txtEnfermedad.setText(vacunaSeleccionada.getEnfermedad());
		txtLaboratorio.setText(vacunaSeleccionada.getLaboratorio());
		spnCantidad.setValue(vacunaSeleccionada.getCantidad());
		spnVencimiento.setValue(vacunaSeleccionada.getFechaCaducidad());

		if (vacunaSeleccionada.estaCaducada()) {
			lblInfoVacuna.setText("VACUNA CADUCADA");
			lblInfoVacuna.setForeground(Color.RED);
		} else {
			lblInfoVacuna.setText("Vacuna: " + vacunaSeleccionada.getNombre());
			lblInfoVacuna.setForeground(Color.BLUE);
		}
	}

	private void limpiarDatosPaciente() {
		txtCedula.setText("");
		txtSangre.setText("");
		spnPeso.setValue(0.0);
		spnEstatura.setValue(0.0);
		lblInfoPaciente.setText("Seleccione un paciente");
		lblInfoPaciente.setForeground(Color.BLACK);
	}

	private void limpiarDatosVacuna() {
		txtLote.setText("");
		txtEnfermedad.setText("");
		txtLaboratorio.setText("");
		spnCantidad.setValue(0);
		spnVencimiento.setValue(new Date());
		lblInfoVacuna.setText("Seleccione una vacuna");
		lblInfoVacuna.setForeground(Color.BLACK);
	}

	private void administrarVacuna() {
		if (!validarDatos()) {
			return;
		}

		try {
			Date fechaDate = (Date) spnFecha.getValue();
			LocalDate fechaAplicacion = fechaDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

			if (fechaAplicacion.isAfter(LocalDate.now())) {
				int respuesta = JOptionPane.showConfirmDialog(this, "La fecha es futura. ¿Continuar?", "Confirmar",
						JOptionPane.YES_NO_OPTION);
				if (respuesta != JOptionPane.YES_OPTION) {
					return;
				}
			}

			if (vacunaSeleccionada.estaCaducada()) {
				JOptionPane.showMessageDialog(this, "Esta vacuna está caducada", "Error", JOptionPane.ERROR_MESSAGE);
				return;
			}

			if (pacienteSeleccionado.tieneVacuna(vacunaSeleccionada)) {
				int respuesta = JOptionPane.showConfirmDialog(this,
						"El paciente ya tiene esta vacuna. ¿Registrar otra dosis?", "Confirmar",
						JOptionPane.YES_NO_OPTION);
				if (respuesta != JOptionPane.YES_OPTION) {
					return;
				}
			}

			RegistroVacuna registro = new RegistroVacuna(vacunaSeleccionada, fechaAplicacion,
					vacunaSeleccionada.getNumeroLote(), txtEnfermera.getText().trim());

			boolean exito = Clinica.getInstance().registrarVacunaPaciente(pacienteSeleccionado, registro);

			if (exito) {
				JOptionPane.showMessageDialog(this, "Vacuna administrada correctamente", "Éxito",
						JOptionPane.INFORMATION_MESSAGE);
				limpiarFormulario();
				cargarVacunasDisponibles();
			} else {
				JOptionPane.showMessageDialog(this, "Error al administrar la vacuna", "Error",
						JOptionPane.ERROR_MESSAGE);
			}

		} catch (Exception ex) {
			JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
			ex.printStackTrace();
		}
	}

	private boolean validarDatos() {
		if (pacienteSeleccionado == null) {
			JOptionPane.showMessageDialog(this, "Seleccione un paciente", "Error", JOptionPane.WARNING_MESSAGE);
			return false;
		}

		if (vacunaSeleccionada == null) {
			JOptionPane.showMessageDialog(this, "Seleccione una vacuna", "Error", JOptionPane.WARNING_MESSAGE);
			return false;
		}

		if (txtEnfermera.getText().trim().isEmpty()) {
			JOptionPane.showMessageDialog(this, "Ingrese quién administra la vacuna", "Error",
					JOptionPane.WARNING_MESSAGE);
			txtEnfermera.requestFocus();
			return false;
		}

		if (vacunaSeleccionada.getCantidad() <= 0) {
			JOptionPane.showMessageDialog(this, "No hay stock disponible", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (vacunaSeleccionada.estaCaducada()) {
			JOptionPane.showMessageDialog(this, "Esta vacuna está caducada", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		if (!vacunaSeleccionada.isActiva()) {
			JOptionPane.showMessageDialog(this, "Esta vacuna está inactiva", "Error", JOptionPane.ERROR_MESSAGE);
			return false;
		}

		return true;
	}

	private void limpiarFormulario() {
		if (cbxPaciente != null) {
			cbxPaciente.setSelectedIndex(0);
		}

		if (cbxVacuna != null) {
			cbxVacuna.setSelectedIndex(0);
		}

		txtEnfermera.setText("");
		limpiarDatosPaciente();
		limpiarDatosVacuna();
		btnVerAlergias.setEnabled(false);
		pacienteSeleccionado = null;
		vacunaSeleccionada = null;
	}

	private void refrescarComboPacientes() {
		cbxPaciente.removeAllItems();
		cbxPaciente.addItem("<Seleccione>");
		for (Paciente paciente : Clinica.getInstance().getPacientes()) {
			if (paciente != null && paciente.isActivo()) {
				cbxPaciente.addItem(
						paciente.getCodigoPaciente() + ": " + paciente.getNombre() + " " + paciente.getApellido());
			}
		}
	}
}