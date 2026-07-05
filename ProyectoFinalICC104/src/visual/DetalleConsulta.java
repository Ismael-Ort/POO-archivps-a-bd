package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;
import javax.swing.border.TitledBorder;

import logico.Clinica;
import logico.Consulta;
import logico.Tratamiento;
import logico.Medicamento;
import java.util.ArrayList;
import java.awt.Toolkit;
import java.awt.Color;

public class DetalleConsulta extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtCodigo;
	private JTextField txtFecha;
	private JTextField txtPaciente;
	private JTextField txtDoctor;
	private JTextField txtEspecialidad;
	private JTextArea txtSintomas;
	private JTextArea txtDiagnostico;
	private JTextArea txtTratamiento;
	private JTextArea txtNotas;
	private JCheckBox chkIncluidaResumen;
	private JCheckBox chkEnfermedadVigilada;

	private Consulta consultaActual;
	private boolean esDoctor = false;
	private String numeroLicenciaDoctor = null;

	public DetalleConsulta(Consulta consulta) {
		setIconImage(Toolkit.getDefaultToolkit().getImage(DetalleConsulta.class.getResource("/recursos/doc.jpg")));
		this.consultaActual = consulta;
		// verificarPermisos();
		initialize();
		cargarDatosConsulta();
	}

	/*
	 * private void verificarPermisos() { // Aquí integrarás con tu sistema de login
	 * más adelante // Por ahora, simulamos permisos if (Control.getLoginUser() !=
	 * null) { String tipoUsuario = Control.getLoginUser().getTipo(); if
	 * ("Doctor".equalsIgnoreCase(tipoUsuario)) { esDoctor = true; // Obtener número
	 * de licencia del doctor logueado // numeroLicenciaDoctor = ... (se
	 * implementará con el login) } } }
	 * 
	 */

	private void initialize() {
		setTitle("Detalle de Consulta");
		setBounds(100, 100, 900, 800);
		setLocationRelativeTo(null);
		setModal(true);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(255, 228, 225));
		contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		Font labelFont = new Font("Tahoma", Font.BOLD, 12);
		Font valueFont = new Font("Tahoma", Font.PLAIN, 12);

		// ========== INFORMACIÓN BÁSICA ==========
		JPanel panelBasico = new JPanel();
		panelBasico.setBackground(new Color(250, 240, 230));
		panelBasico.setBorder(
				new TitledBorder(null, "Información Básica", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelBasico.setBounds(10, 10, 864, 150);
		panelBasico.setLayout(null);
		contentPanel.add(panelBasico);

		// Fila 1: Código y Fecha
		JLabel lblCodigo = new JLabel("Código Consulta:");
		lblCodigo.setFont(labelFont);
		lblCodigo.setBounds(10, 25, 120, 20);
		panelBasico.add(lblCodigo);

		txtCodigo = new JTextField();
		txtCodigo.setFont(valueFont);
		txtCodigo.setEditable(false);
		txtCodigo.setBounds(140, 25, 200, 20);
		panelBasico.add(txtCodigo);
		txtCodigo.setColumns(10);

		JLabel lblFecha = new JLabel("Fecha:");
		lblFecha.setFont(labelFont);
		lblFecha.setBounds(400, 25, 80, 20);
		panelBasico.add(lblFecha);

		txtFecha = new JTextField();
		txtFecha.setFont(valueFont);
		txtFecha.setEditable(false);
		txtFecha.setBounds(480, 25, 374, 20);
		panelBasico.add(txtFecha);
		txtFecha.setColumns(10);

		// Fila 2: Paciente
		JLabel lblPaciente = new JLabel("Paciente:");
		lblPaciente.setFont(labelFont);
		lblPaciente.setBounds(10, 55, 80, 20);
		panelBasico.add(lblPaciente);

		txtPaciente = new JTextField();
		txtPaciente.setFont(valueFont);
		txtPaciente.setEditable(false);
		txtPaciente.setBounds(140, 55, 714, 20);
		panelBasico.add(txtPaciente);
		txtPaciente.setColumns(10);

		// Fila 3: Doctor y Especialidad
		JLabel lblDoctor = new JLabel("Doctor:");
		lblDoctor.setFont(labelFont);
		lblDoctor.setBounds(10, 85, 80, 20);
		panelBasico.add(lblDoctor);

		txtDoctor = new JTextField();
		txtDoctor.setFont(valueFont);
		txtDoctor.setEditable(false);
		txtDoctor.setBounds(140, 85, 300, 20);
		panelBasico.add(txtDoctor);
		txtDoctor.setColumns(10);

		JLabel lblEspecialidad = new JLabel("Especialidad:");
		lblEspecialidad.setFont(labelFont);
		lblEspecialidad.setBounds(450, 85, 100, 20);
		panelBasico.add(lblEspecialidad);

		txtEspecialidad = new JTextField();
		txtEspecialidad.setFont(valueFont);
		txtEspecialidad.setEditable(false);
		txtEspecialidad.setBounds(550, 85, 304, 20);
		panelBasico.add(txtEspecialidad);
		txtEspecialidad.setColumns(10);

		// Fila 4: Información de Resumen
		JLabel lblInfoResumen = new JLabel("Información de Resumen:");
		lblInfoResumen.setFont(labelFont);
		lblInfoResumen.setBounds(10, 115, 180, 20);
		panelBasico.add(lblInfoResumen);

		chkIncluidaResumen = new JCheckBox("Incluida en Resumen");
		chkIncluidaResumen.setBackground(new Color(250, 240, 230));
		chkIncluidaResumen.setFont(valueFont);
		chkIncluidaResumen.setEnabled(false);
		chkIncluidaResumen.setBounds(200, 115, 150, 20);
		panelBasico.add(chkIncluidaResumen);

		chkEnfermedadVigilada = new JCheckBox("Enfermedad Vigilada");
		chkEnfermedadVigilada.setBackground(new Color(250, 240, 230));
		chkEnfermedadVigilada.setFont(valueFont);
		chkEnfermedadVigilada.setEnabled(false);
		chkEnfermedadVigilada.setBounds(360, 115, 150, 20);
		panelBasico.add(chkEnfermedadVigilada);

		// ========== SÍNTOMAS ==========
		JPanel panelSintomas = new JPanel();
		panelSintomas.setBackground(new Color(250, 240, 230));
		panelSintomas.setBorder(
				new TitledBorder(null, "Síntomas Reportados", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelSintomas.setBounds(10, 170, 864, 100);
		panelSintomas.setLayout(new BorderLayout(0, 0));
		contentPanel.add(panelSintomas);

		txtSintomas = new JTextArea();
		txtSintomas.setFont(valueFont);
		txtSintomas.setEditable(false);
		txtSintomas.setLineWrap(true);
		txtSintomas.setWrapStyleWord(true);
		JScrollPane scrollSintomas = new JScrollPane(txtSintomas);
		panelSintomas.add(scrollSintomas, BorderLayout.CENTER);

		// ========== DIAGNÓSTICO ==========
		JPanel panelDiagnostico = new JPanel();
		panelDiagnostico.setBackground(new Color(250, 240, 230));
		panelDiagnostico
				.setBorder(new TitledBorder(null, "Diagnóstico", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelDiagnostico.setBounds(10, 280, 864, 100);
		panelDiagnostico.setLayout(new BorderLayout(0, 0));
		contentPanel.add(panelDiagnostico);

		txtDiagnostico = new JTextArea();
		txtDiagnostico.setFont(valueFont);
		txtDiagnostico.setEditable(false);
		txtDiagnostico.setLineWrap(true);
		txtDiagnostico.setWrapStyleWord(true);
		JScrollPane scrollDiagnostico = new JScrollPane(txtDiagnostico);
		panelDiagnostico.add(scrollDiagnostico, BorderLayout.CENTER);

		// ========== TRATAMIENTO ==========
		JPanel panelTratamiento = new JPanel();
		panelTratamiento.setBackground(new Color(250, 240, 230));
		panelTratamiento.setBorder(
				new TitledBorder(null, "Tratamiento Prescrito", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panelTratamiento.setBounds(10, 390, 864, 200);
		panelTratamiento.setLayout(new BorderLayout(0, 0));
		contentPanel.add(panelTratamiento);

		txtTratamiento = new JTextArea();
		txtTratamiento.setFont(valueFont);
		txtTratamiento.setEditable(false);
		txtTratamiento.setLineWrap(true);
		txtTratamiento.setWrapStyleWord(true);
		JScrollPane scrollTratamiento = new JScrollPane(txtTratamiento);
		panelTratamiento.add(scrollTratamiento, BorderLayout.CENTER);

		// ========== NOTAS MÉDICAS ==========
		JPanel panelNotas = new JPanel();
		panelNotas.setBackground(new Color(250, 240, 230));
		panelNotas.setBorder(new TitledBorder(null, "Notas Médicas Adicionales", TitledBorder.LEADING, TitledBorder.TOP,
				null, null));
		panelNotas.setBounds(10, 600, 864, 100);
		panelNotas.setLayout(new BorderLayout(0, 0));
		contentPanel.add(panelNotas);

		txtNotas = new JTextArea();
		txtNotas.setFont(valueFont);
		txtNotas.setEditable(false);
		txtNotas.setLineWrap(true);
		txtNotas.setWrapStyleWord(true);
		JScrollPane scrollNotas = new JScrollPane(txtNotas);
		panelNotas.add(scrollNotas, BorderLayout.CENTER);

		// ========== BOTONES ==========
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(255, 228, 225));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		// Botón para marcar/desmarcar resumen (solo para doctores)
		JButton btnGestionResumen = new JButton("Gestionar Resumen");
		btnGestionResumen.setVisible(esDoctor); // Solo visible para doctores
		btnGestionResumen.addActionListener(e -> gestionarResumen());
		buttonPane.add(btnGestionResumen);

		JButton btnCerrar = new JButton("Cerrar");
		btnCerrar.setBackground(new Color(224, 255, 255));
		btnCerrar.addActionListener(e -> dispose());
		buttonPane.add(btnCerrar);
	}

	private void cargarDatosConsulta() {
		if (consultaActual == null)
			return;

		// Información básica
		txtCodigo.setText(consultaActual.getCodigoConsulta());
		txtFecha.setText(consultaActual.getFechaConsulta().toString());
		txtPaciente.setText(consultaActual.getPaciente().getNombre() + " " + consultaActual.getPaciente().getApellido()
				+ " (" + consultaActual.getPaciente().getCedula() + ")");
		txtDoctor.setText(consultaActual.getDoctor().getNombre() + " " + consultaActual.getDoctor().getApellido());
		txtEspecialidad.setText(consultaActual.getDoctor().getEspecialidad());

		// Síntomas y diagnóstico
		txtSintomas.setText(
				consultaActual.getSintomas() != null ? consultaActual.getSintomas() : "No se registraron síntomas");
		txtDiagnostico.setText(consultaActual.getDiagnostico() != null ? consultaActual.getDiagnostico()
				: "No se registró diagnóstico");

		// Tratamiento
		txtTratamiento.setText(formatearTratamiento(consultaActual.getTratamiento()));

		// Información adicional
		txtNotas.setText(consultaActual.getNotasMedicas() != null ? consultaActual.getNotasMedicas()
				: "No hay notas médicas adicionales");

		chkIncluidaResumen.setSelected(consultaActual.isIncluidaEnResumen());
		chkEnfermedadVigilada.setSelected(consultaActual.isEsEnfermedadVigilancia());
	}

	private String formatearTratamiento(Tratamiento tratamiento) {
		if (tratamiento == null) {
			return "No se especificó tratamiento para esta consulta.";
		}

		StringBuilder sb = new StringBuilder();

		// Información básica del tratamiento
		sb.append(" INFORMACIÓN DEL TRATAMIENTO\n");
		sb.append("  Código: ").append(tratamiento.getCodigoTratamiento()).append("\n");
		sb.append("  Nombre: ").append(tratamiento.getNombreTratamiento()).append("\n");
		sb.append("  Duración: ").append(tratamiento.getDuracion()).append("\n");

		if (tratamiento.getDescripcion() != null && !tratamiento.getDescripcion().isEmpty()) {
			sb.append("  Descripción: ").append(tratamiento.getDescripcion()).append("\n");
		}

		if (tratamiento.getIndicaciones() != null && !tratamiento.getIndicaciones().isEmpty()) {
			sb.append("  Indicaciones: ").append(tratamiento.getIndicaciones()).append("\n");
		}

		sb.append("\n");

		// Medicamentos
		ArrayList<Medicamento> medicamentos = tratamiento.getMedicamentos();
		if (medicamentos != null && !medicamentos.isEmpty()) {
			sb.append(" MEDICAMENTOS PRESCRITOS\n");
			for (int i = 0; i < medicamentos.size(); i++) {
				Medicamento m = medicamentos.get(i);
				sb.append("  ").append(i + 1).append(". ").append(m.getNombre()).append("\n");
				sb.append("     Dosis: ").append(m.getDosisMg()).append(" mg\n");
				sb.append("     Frecuencia: Cada ").append(m.getFrecuenciaHoras()).append(" horas\n");
				sb.append("     Duración: ").append(m.getDuracionDias()).append(" días\n");
				sb.append("     Vía: ").append(m.getVia()).append("\n");

				if (m.getIndicaciones() != null && !m.getIndicaciones().isEmpty()) {
					sb.append("     Indicaciones: ").append(m.getIndicaciones()).append("\n");
				}
				sb.append("\n");
			}
		} else {
			sb.append(" MEDICAMENTOS: No se prescribieron medicamentos para este tratamiento.\n");
		}

		return sb.toString();
	}

	private void gestionarResumen() {
		if (consultaActual == null)
			return;

		boolean actualmenteEnResumen = consultaActual.isIncluidaEnResumen();
		boolean nuevoEstado = !actualmenteEnResumen;

		if (nuevoEstado) {
			if (Clinica.getInstance().marcarConsultaParaResumen(consultaActual.getCodigoConsulta())) {
				JOptionPane.showMessageDialog(this, "Consulta marcada para resumen", "Éxito",
						JOptionPane.INFORMATION_MESSAGE);
				chkIncluidaResumen.setSelected(true);
				consultaActual.marcarParaResumen();
			} else {
				JOptionPane.showMessageDialog(this, "Error al marcar la consulta", "Error", JOptionPane.ERROR_MESSAGE);
			}
		} else {
			if (Clinica.getInstance().desmarcarConsultaDeResumen(consultaActual.getCodigoConsulta())) {
				JOptionPane.showMessageDialog(this, "Consulta desmarcada del resumen", "Éxito",
						JOptionPane.INFORMATION_MESSAGE);
				chkIncluidaResumen.setSelected(false);
				consultaActual.desmarcarParaResumen();
			} else {
				JOptionPane.showMessageDialog(this, "Error al desmarcar la consulta", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}