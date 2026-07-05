package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTabbedPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.MaskFormatter;
import javax.swing.border.TitledBorder;
import logico.Clinica;
import logico.Control;
import logico.Doctor;
import logico.Paciente;
import logico.Consulta;
import logico.Vacuna;
import logico.VacunaVieja;
import logico.RegistroVacuna;
import logico.Alergia;
import java.util.ArrayList;
import java.awt.Toolkit;
import java.awt.Color;

public class VerHistorialClinico extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtBusqueda;
	private JLabel lblNombrePaciente;
	private JLabel lblCedula;
	private JLabel lblTipoSangre;
	private JLabel lblFechaRegistro;
	private JTextArea txtAlergias;

	private JTable tableConsultas;
	private DefaultTableModel modeloConsultas;

	private JTextArea txtResumen;

	private JTable tableVacunasAplicadas;
	private DefaultTableModel modeloVacunasAplicadas;

	private JTable tableVacunasFaltantes;
	private DefaultTableModel modeloVacunasFaltantes;

	private Paciente pacienteActual = null;

	public VerHistorialClinico() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(VerHistorialClinico.class.getResource("/recursos/adm.jpg")));
		setTitle("Historial Clínico");
		setBounds(100, 100, 1000, 700);
		setLocationRelativeTo(null);
		setModal(true);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(240, 248, 255));
		contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		crearPanelBusqueda();
		crearPanelInformacionPaciente();
		crearTabs();
		crearBotonCerrar();
	}

	private void crearPanelBusqueda() {
		JPanel panelBusqueda = new JPanel();
		panelBusqueda.setBackground(new Color(240, 255, 255));
		panelBusqueda.setBorder(
				new TitledBorder(null, "Buscar Paciente", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelBusqueda.setBounds(10, 11, 964, 80);
		panelBusqueda.setLayout(null);
		contentPanel.add(panelBusqueda);

		JLabel lblBuscar = new JLabel("Cédula:");
		lblBuscar.setBackground(new Color(255, 239, 213));
		lblBuscar.setBounds(52, 30, 120, 22);
		panelBusqueda.add(lblBuscar);

		try {
			MaskFormatter cedulaMask = new MaskFormatter("###-#######-#");
			cedulaMask.setPlaceholderCharacter('_');
			txtBusqueda = new JFormattedTextField(cedulaMask);
			txtBusqueda.setBackground(new Color(255, 245, 238));
		} catch (Exception e) {
			txtBusqueda = new JFormattedTextField();
		}
		txtBusqueda.setBounds(140, 30, 200, 22);
		panelBusqueda.add(txtBusqueda);

		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.setBackground(new Color(245, 255, 250));
		btnBuscar.setBounds(350, 29, 100, 24);
		btnBuscar.addActionListener(e -> buscarPaciente());
		panelBusqueda.add(btnBuscar);
	}

	private void crearPanelInformacionPaciente() {
		JPanel panelInfo = new JPanel();
		panelInfo.setBackground(new Color(240, 255, 255));
		panelInfo.setBorder(
				new TitledBorder(null, "Información del Paciente", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelInfo.setBounds(10, 102, 964, 150);
		panelInfo.setLayout(null);
		contentPanel.add(panelInfo);

		JLabel lblNombreLbl = new JLabel("Nombre:");
		lblNombreLbl.setBounds(10, 25, 80, 22);
		panelInfo.add(lblNombreLbl);

		lblNombrePaciente = new JLabel("---");
		lblNombrePaciente.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblNombrePaciente.setBounds(100, 25, 400, 22);
		panelInfo.add(lblNombrePaciente);

		JLabel lblCedulaLbl = new JLabel("Cédula:");
		lblCedulaLbl.setBounds(520, 25, 80, 22);
		panelInfo.add(lblCedulaLbl);

		lblCedula = new JLabel("---");
		lblCedula.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblCedula.setBounds(610, 25, 344, 22);
		panelInfo.add(lblCedula);

		JLabel lblTipoSangreLbl = new JLabel("Tipo Sangre:");
		lblTipoSangreLbl.setBounds(10, 55, 80, 22);
		panelInfo.add(lblTipoSangreLbl);

		lblTipoSangre = new JLabel("---");
		lblTipoSangre.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblTipoSangre.setBounds(100, 55, 100, 22);
		panelInfo.add(lblTipoSangre);

		JLabel lblFechaRegLbl = new JLabel("Registro:");
		lblFechaRegLbl.setBounds(220, 55, 80, 22);
		panelInfo.add(lblFechaRegLbl);

		lblFechaRegistro = new JLabel("---");
		lblFechaRegistro.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblFechaRegistro.setBounds(310, 55, 200, 22);
		panelInfo.add(lblFechaRegistro);

		JLabel lblAlergiasLbl = new JLabel("Alergias:");
		lblAlergiasLbl.setBounds(10, 85, 80, 22);
		panelInfo.add(lblAlergiasLbl);

		JScrollPane spAlergias = new JScrollPane();
		spAlergias.setBounds(100, 85, 854, 55);
		panelInfo.add(spAlergias);

		txtAlergias = new JTextArea();
		txtAlergias.setBackground(new Color(253, 245, 230));
		txtAlergias.setEditable(false);
		txtAlergias.setLineWrap(true);
		txtAlergias.setWrapStyleWord(true);
		spAlergias.setViewportView(txtAlergias);
	}

	private void crearTabs() {
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 263, 964, 340);
		contentPanel.add(tabbedPane);

		crearTabConsultas(tabbedPane);
		crearTabResumen(tabbedPane);
		crearTabVacunacion(tabbedPane);
	}

	private void crearTabConsultas(JTabbedPane tabbedPane) {
		JPanel tabConsultas = new JPanel();
		tabConsultas.setLayout(new BorderLayout(0, 0));
		tabbedPane.addTab("Consultas", null, tabConsultas, null);

		String[] columnasConsultas = { "Código", "Fecha", "Doctor", "Diagnóstico", "En Resumen", "Vigilancia" };
		modeloConsultas = new DefaultTableModel(columnasConsultas, 0) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tableConsultas = new JTable(modeloConsultas);
		tableConsultas.getColumnModel().getColumn(3).setPreferredWidth(200);

		JScrollPane spConsultas = new JScrollPane(tableConsultas);
		tabConsultas.add(spConsultas, BorderLayout.CENTER);

		JPanel panelBotones = new JPanel();
		panelBotones.setBackground(new Color(240, 248, 255));
		panelBotones.setLayout(new FlowLayout(FlowLayout.RIGHT));
		tabConsultas.add(panelBotones, BorderLayout.SOUTH);

		JButton btnVerDetalle = new JButton("Ver Detalles");
		btnVerDetalle.addActionListener(e -> verDetalleConsulta());
		panelBotones.add(btnVerDetalle);

		JButton btnMarcarResumen = new JButton("Marcar para Resumen");
		btnMarcarResumen.addActionListener(e -> marcarParaResumen());
		panelBotones.add(btnMarcarResumen);

		JButton btnDesmarcarResumen = new JButton("Desmarcar de Resumen");
		btnDesmarcarResumen.addActionListener(e -> desmarcarDeResumen());
		panelBotones.add(btnDesmarcarResumen);
	}

	private void crearTabResumen(JTabbedPane tabbedPane) {
		JPanel tabResumen = new JPanel();
		tabResumen.setLayout(new BorderLayout(0, 0));
		tabbedPane.addTab("Resumen Automático", null, tabResumen, null);

		txtResumen = new JTextArea();
		txtResumen.setEditable(false);
		txtResumen.setLineWrap(true);
		txtResumen.setWrapStyleWord(true);
		txtResumen.setFont(new Font("Monospaced", Font.PLAIN, 12));

		JScrollPane spResumen = new JScrollPane(txtResumen);
		tabResumen.add(spResumen, BorderLayout.CENTER);

		JPanel panelBotones = new JPanel();
		panelBotones.setLayout(new FlowLayout(FlowLayout.RIGHT));
		tabResumen.add(panelBotones, BorderLayout.SOUTH);

		JButton btnGenerarResumen = new JButton("Generar Resumen");
		btnGenerarResumen.addActionListener(e -> generarResumen());
		panelBotones.add(btnGenerarResumen);
	}

	private void crearTabVacunacion(JTabbedPane tabbedPane) {
		JPanel tabVacunacion = new JPanel();
		tabVacunacion.setLayout(null);
		tabbedPane.addTab("Vacunación", null, tabVacunacion, null);

		// Vacunas Aplicadas
		JLabel lblVacunasAplicadas = new JLabel("Vacunas Aplicadas:");
		lblVacunasAplicadas.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblVacunasAplicadas.setBounds(10, 10, 200, 22);
		tabVacunacion.add(lblVacunasAplicadas);

		String[] columnasAplicadas = { "Vacuna", "Fecha Aplicación", "Lote" };
		modeloVacunasAplicadas = new DefaultTableModel(columnasAplicadas, 0) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tableVacunasAplicadas = new JTable(modeloVacunasAplicadas);
		JScrollPane spAplicadas = new JScrollPane(tableVacunasAplicadas);
		spAplicadas.setBounds(10, 35, 929, 110);
		tabVacunacion.add(spAplicadas);

		// Vacunas Faltantes
		JLabel lblVacunasFaltantes = new JLabel("Vacunas Pendientes:");
		lblVacunasFaltantes.setFont(new Font("Tahoma", Font.BOLD, 12));
		lblVacunasFaltantes.setBounds(10, 160, 200, 22);
		tabVacunacion.add(lblVacunasFaltantes);

		String[] columnasFaltantes = { "Código", "Nombre", "Enfermedad" };
		modeloVacunasFaltantes = new DefaultTableModel(columnasFaltantes, 0) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tableVacunasFaltantes = new JTable(modeloVacunasFaltantes);
		JScrollPane spFaltantes = new JScrollPane(tableVacunasFaltantes);
		spFaltantes.setBounds(10, 185, 929, 110);
		tabVacunacion.add(spFaltantes);
	}

	private void crearBotonCerrar() {
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(240, 248, 255));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton btnCerrar = new JButton("Cerrar");
		btnCerrar.setBackground(new Color(255, 245, 238));
		btnCerrar.addActionListener(e -> dispose());
		buttonPane.add(btnCerrar);
	}

	private void buscarPaciente() {
		String busqueda = txtBusqueda.getText().trim();

		if (busqueda.isEmpty() || busqueda.contains("_")) {
			JOptionPane.showMessageDialog(this, "Ingrese una cédula completa", "Advertencia",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		String cedulaLimpia = busqueda.replaceAll("[^0-9]", "");
		if (cedulaLimpia.length() != 11) {
			JOptionPane.showMessageDialog(this, "La cédula debe tener 11 dígitos", "Cédula inválida",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		Paciente paciente = Clinica.getInstance().buscarPacientePorCedula(cedulaLimpia);

		if (paciente == null) {
			JOptionPane.showMessageDialog(this, "Paciente no encontrado", "No encontrado", JOptionPane.ERROR_MESSAGE);
			limpiarInformacion();
			return;
		}

		pacienteActual = paciente;
		mostrarInformacionPaciente();
		cargarConsultas();
		cargarVacunacion();
	}

	private void mostrarInformacionPaciente() {
		if (pacienteActual == null)
			return;

		lblNombrePaciente.setText(pacienteActual.getNombre() + " " + pacienteActual.getApellido());
		lblCedula.setText(pacienteActual.getCedula());
		lblTipoSangre.setText(pacienteActual.getTipoSangre());
		lblFechaRegistro.setText(pacienteActual.getFechaRegistro().toString());

		ArrayList<Alergia> alergias = pacienteActual.getAlergias();
		if (alergias == null || alergias.isEmpty()) {
			txtAlergias.setText("No presenta alergias registradas");
		} else {
			StringBuilder sb = new StringBuilder();
			for (Alergia a : alergias) {
				sb.append("- ").append(a.getNombre());
				if (a.getTipo() != null && !a.getTipo().isEmpty()) {
					sb.append(" (").append(a.getTipo()).append(")");
				}
				sb.append("\n");
			}
			txtAlergias.setText(sb.toString());
		}
	}

	// Modifica el método cargarConsultas() para filtrar según el doctor logeado
	private void cargarConsultas() {
		modeloConsultas.setRowCount(0);
		if (pacienteActual == null)
			return;

		ArrayList<Consulta> consultas = pacienteActual.getHistoriaClinica().getConsultas();
		Doctor doctorLogeado = Control.getDoctorLogeado();
		boolean esAdmin = Control.esAdministrador();

		for (Consulta c : consultas) {
			// LÓGICA DE VISIBILIDAD:
			// 1. Admin ve TODO
			// 2. Doctor ve: consultas propias O consultas con enfermedad bajo vigilancia
			// 3. Si no es doctor ni admin, no ve nada (no debería pasar)

			boolean puedeVer = false;

			if (esAdmin) {
				puedeVer = true; // Admin ve todo
			} else if (doctorLogeado != null) {
				// Doctor ve si:
				// a) Es SU consulta (la hizo él)
				// b) Es consulta de enfermedad bajo vigilancia
				boolean esSuConsulta = c.getDoctor().getNumeroLicencia().equals(doctorLogeado.getNumeroLicencia());
				boolean esVigilancia = c.isEsEnfermedadVigilancia();
				puedeVer = esSuConsulta || esVigilancia;
			}

			if (puedeVer) {
				Object[] fila = { c.getCodigoConsulta(), c.getFechaConsulta().toString(),
						c.getDoctor().getNombre() + " " + c.getDoctor().getApellido(), c.getDiagnostico(),
						c.isIncluidaEnResumen() ? "Sí" : "No", c.isEsEnfermedadVigilancia() ? "Sí" : "No" };
				modeloConsultas.addRow(fila);
			}
		}

		// Si no hay consultas visibles, mostrar mensaje
		if (modeloConsultas.getRowCount() == 0 && !esAdmin && doctorLogeado != null) {
			JOptionPane.showMessageDialog(this,
					"No hay consultas registradas\n"
							+ "Solo puede ver consultas que usted realizó o de enfermedades bajo vigilancia.",
					"Información", JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private void cargarVacunacion() {
		modeloVacunasAplicadas.setRowCount(0);
		modeloVacunasFaltantes.setRowCount(0);
		if (pacienteActual == null)
			return;

		// Vacunas Aplicadas
		ArrayList<RegistroVacuna> registros = pacienteActual.getRegistrosVacunas();
		if (registros != null) {
			for (RegistroVacuna r : registros) {
				Object[] fila = { r.getVacuna().getNombre(), r.getFechaAplicacion().toString(),
						r.getVacuna().getNumeroLote() };
				modeloVacunasAplicadas.addRow(fila);
			}
		}

		// Vacunas Viejas
		for (VacunaVieja viejita : pacienteActual.getVacunasViejas()) {
			Object[] fila = { viejita.getEnfermedad(), viejita.getFecha(), "DESCONOCIDO" };
			modeloVacunasAplicadas.addRow(fila);
		}

		// Vacunas Faltantes
		ArrayList<Vacuna> faltantes = Clinica.getInstance().obtenerVacunasFaltantes(pacienteActual.getCedula());
		if (faltantes != null) {
			for (Vacuna v : faltantes) {
				Object[] fila = { v.getCodigoVacuna(), v.getNombre(), v.getEnfermedad() };
				modeloVacunasFaltantes.addRow(fila);
			}
		}
	}

	private void verDetalleConsulta() {
		int filaSeleccionada = tableConsultas.getSelectedRow();
		if (filaSeleccionada == -1) {
			JOptionPane.showMessageDialog(this, "Seleccione una consulta", "Advertencia", JOptionPane.WARNING_MESSAGE);
			return;
		}

		String codigoConsulta = (String) modeloConsultas.getValueAt(filaSeleccionada, 0);
		Consulta consulta = Clinica.getInstance().buscarConsulta(codigoConsulta);

		if (consulta == null) {
			JOptionPane.showMessageDialog(this, "Consulta no encontrada", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		DetalleConsulta detalle = new DetalleConsulta(consulta);
		detalle.setVisible(true);
		cargarConsultas();
	}

	private void marcarParaResumen() {
		int filaSeleccionada = tableConsultas.getSelectedRow();
		if (filaSeleccionada == -1) {
			JOptionPane.showMessageDialog(this, "Seleccione una consulta", "Advertencia", JOptionPane.WARNING_MESSAGE);
			return;
		}

		String codigoConsulta = (String) modeloConsultas.getValueAt(filaSeleccionada, 0);
		if (Clinica.getInstance().marcarConsultaParaResumen(codigoConsulta)) {
			JOptionPane.showMessageDialog(this, "Consulta marcada para resumen", "Éxito",
					JOptionPane.INFORMATION_MESSAGE);
			cargarConsultas();
		} else {
			JOptionPane.showMessageDialog(this, "No se pudo marcar la consulta", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void desmarcarDeResumen() {
		int filaSeleccionada = tableConsultas.getSelectedRow();
		if (filaSeleccionada == -1) {
			JOptionPane.showMessageDialog(this, "Seleccione una consulta", "Advertencia", JOptionPane.WARNING_MESSAGE);
			return;
		}

		String codigoConsulta = (String) modeloConsultas.getValueAt(filaSeleccionada, 0);
		if (Clinica.getInstance().desmarcarConsultaDeResumen(codigoConsulta)) {
			JOptionPane.showMessageDialog(this, "Consulta desmarcada del resumen", "Éxito",
					JOptionPane.INFORMATION_MESSAGE);
			cargarConsultas();
		} else {
			JOptionPane.showMessageDialog(this, "No se pudo desmarcar la consulta", "Error", JOptionPane.ERROR_MESSAGE);
		}
	}

	private void generarResumen() {
		if (pacienteActual == null) {
			JOptionPane.showMessageDialog(this, "Debe buscar un paciente primero", "Advertencia",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		String resumen = pacienteActual.getHistoriaClinica().generarResumen();
		txtResumen.setText(resumen);
		txtResumen.setCaretPosition(0);
	}

	private void limpiarInformacion() {
		pacienteActual = null;
		lblNombrePaciente.setText("---");
		lblCedula.setText("---");
		lblTipoSangre.setText("---");
		lblFechaRegistro.setText("---");
		txtAlergias.setText("");
		modeloConsultas.setRowCount(0);
		txtResumen.setText("");
		modeloVacunasAplicadas.setRowCount(0);
		modeloVacunasFaltantes.setRowCount(0);
	}
}