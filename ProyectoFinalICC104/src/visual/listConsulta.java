package visual;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;
import java.awt.FlowLayout;

import logico.Clinica;
import logico.Consulta;
import logico.Control;
import logico.Paciente;
import logico.Doctor;
import java.awt.Toolkit;
import java.util.ArrayList;
import java.awt.Color;

public class listConsulta extends JDialog {

	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel = new JPanel();
	private JTable tableConsultas;
	private DefaultTableModel modelo;

	public listConsulta() {
		getContentPane().setBackground(new Color(250, 240, 230));
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(listConsulta.class.getResource("/com/sun/java/swing/plaf/windows/icons/DetailsView.gif")));
		setTitle("Listado de consultas");
		setBounds(100, 100, 1000, 550);
		setLocationRelativeTo(null);
		setModal(true);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(250, 240, 230));
		contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(contentPanel, BorderLayout.NORTH);
		contentPanel.setLayout(null);

		// ====== TABLA ======
		String[] columnas = { "Código", "Paciente", "Doctor", "Fecha", "Diagnóstico", "Vigilancia" };

		modelo = new DefaultTableModel(columnas, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tableConsultas = new JTable(modelo);
		tableConsultas.getColumnModel().getColumn(4).setPreferredWidth(200); // Diagnóstico más ancho

		contentPanel.setLayout(new BorderLayout());
		JScrollPane scrollPane = new JScrollPane(tableConsultas);
		contentPanel.add(scrollPane, BorderLayout.CENTER);
		scrollPane.setBounds(0, 0, 980, 400);

		// ===== BOTONES =====
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(255, 228, 181));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton btnVerDetalles = new JButton("Ver Detalles");
		btnVerDetalles.addActionListener(e -> verDetallesConsulta());
		buttonPane.add(btnVerDetalles);

		JButton btnActualizar = new JButton("Actualizar");
		btnActualizar.addActionListener(e -> cargarConsultas());
		buttonPane.add(btnActualizar);

		JButton btnCerrar = new JButton("Cerrar");
		btnCerrar.setBackground(new Color(240, 248, 255));
		btnCerrar.addActionListener(e -> dispose());
		buttonPane.add(btnCerrar);

		// ===== CARGAR DATOS =====
		cargarConsultas();
	}

	private void cargarConsultas() {
		modelo.setRowCount(0); // limpiar tabla

		// Verificar que haya un usuario logeado
		if (Control.getLoginUser() == null) {
			JOptionPane.showMessageDialog(this, "No hay usuario logeado. Por favor, inicie sesión.", "Error de sesión",
					JOptionPane.ERROR_MESSAGE);
			dispose();
			return;
		}

		// Obtener consultas visibles según tipo de usuario
		ArrayList<Consulta> consultasVisibles = obtenerConsultasVisibles();

		if (consultasVisibles == null) {
			return; // Ya se mostró mensaje de error
		}

		// Mostrar consultas visibles
		for (Consulta c : consultasVisibles) {
			Paciente p = c.getPaciente();
			String nombrePaciente = p.getNombre() + " " + p.getApellido();

			String nombreDoctor = "N/A";
			if (c.getDoctor() != null) {
				nombreDoctor = c.getDoctor().getNombre() + " " + c.getDoctor().getApellido();
			}

			String fecha = (c.getFechaConsulta() != null) ? c.getFechaConsulta().toString() : "";
			String diagnostico = (c.getDiagnostico() != null) ? c.getDiagnostico() : "";
			String vigilancia = c.isEsEnfermedadVigilancia() ? "SÍ" : "No";

			Object[] fila = { c.getCodigoConsulta(), nombrePaciente, nombreDoctor, fecha, diagnostico, vigilancia };

			modelo.addRow(fila);
		}

		// Mostrar mensaje si no hay consultas
		if (modelo.getRowCount() == 0) {
			JOptionPane.showMessageDialog(this, "No hay consultas para mostrar con sus permisos actuales.", "Sin datos",
					JOptionPane.INFORMATION_MESSAGE);
		}
	}

	private ArrayList<Consulta> obtenerConsultasVisibles() {
		ArrayList<Consulta> consultasVisibles = new ArrayList<>();

		if (Control.esAdministrador()) {
			// ADMINISTRADOR: Ve TODAS las consultas
			for (Paciente p : Clinica.getInstance().getPacientes()) {
				if (p.getHistoriaClinica() != null) {
					consultasVisibles.addAll(p.getHistoriaClinica().getConsultas());
				}
			}

			// Mostrar mensaje informativo
			JOptionPane.showMessageDialog(this, "Mostrando TODAS las consultas del sistema (modo Administrador).",
					"Modo Administrador", JOptionPane.INFORMATION_MESSAGE);

		} else if (Control.esDoctor()) {
			// DOCTOR: Ve consultas visibles para él
			Doctor doctorLogeado = Control.getDoctorLogeado();
			if (doctorLogeado != null) {
				for (Paciente pax : Clinica.getInstance().getPacientes()) {
					for (Consulta consultas : pax.getHistoriaClinica().getConsultas()) {
						if (consultas.getDoctor().equals(doctorLogeado) || consultas.getEnfermedad().isBajoVigilancia()) {
							consultasVisibles.add(consultas);
						}
					}
				}

				// Mostrar estadísticas
				int totalConsultas = consultasVisibles.size();
				int consultasPropias = Clinica.getInstance().listarConsultasPorDoctor(doctorLogeado.getNumeroLicencia()).size();
				int consultasVigilancia = totalConsultas - consultasPropias;

				String mensaje = String.format(
						"Consultas visibles: %d total\n- Sus consultas: %d\n- Consultas de vigilancia: %d",
						totalConsultas, consultasPropias, consultasVigilancia);

				JOptionPane.showMessageDialog(this, mensaje, "Consultas visibles", JOptionPane.INFORMATION_MESSAGE);

			} else {
				JOptionPane.showMessageDialog(this,
						"No se pudo identificar al doctor logeado.\n"
								+ "Asegúrese de que su usuario de doctor esté correctamente registrado.",
						"Error", JOptionPane.ERROR_MESSAGE);
				return null;
			}
		} else {
			// Usuario no reconocido
			JOptionPane.showMessageDialog(this, "Tipo de usuario no reconocido o sin permisos para ver consultas.",
					"Acceso denegado", JOptionPane.WARNING_MESSAGE);
			return null;
		}

		return consultasVisibles;
	}

	private void verDetallesConsulta() {
		int filaSeleccionada = tableConsultas.getSelectedRow();
		if (filaSeleccionada == -1) {
			JOptionPane.showMessageDialog(this, "Seleccione una consulta para ver los detalles.", "Advertencia",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		String codigoConsulta = (String) modelo.getValueAt(filaSeleccionada, 0);
		Consulta consulta = Clinica.getInstance().buscarConsulta(codigoConsulta);

		if (consulta != null) {
			// Verificar permisos antes de mostrar
			if (tienePermisoParaVerConsulta(consulta)) {
				DetalleConsulta ventana = new DetalleConsulta(consulta);
				ventana.setModal(true);
				ventana.setLocationRelativeTo(this);
				ventana.setVisible(true);
			} else {
				JOptionPane.showMessageDialog(this, "No tiene permisos para ver los detalles de esta consulta.",
						"Acceso denegado", JOptionPane.WARNING_MESSAGE);
			}
		} else {
			JOptionPane.showMessageDialog(this, "No se pudo encontrar la consulta seleccionada.", "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private boolean tienePermisoParaVerConsulta(Consulta consulta) {
		if (Control.esAdministrador()) {
			return true; // Admin ve todo
		}

		if (Control.esDoctor()) {
			Doctor doctorLogeado = Control.getDoctorLogeado();
			if (doctorLogeado != null) {
				// Doctor ve si:
				// 1. Es SU consulta
				// 2. Es consulta de enfermedad bajo vigilancia
				boolean esSuConsulta = consulta.getDoctor() != null
						&& consulta.getDoctor().getNumeroLicencia().equals(doctorLogeado.getNumeroLicencia());
				boolean esVigilancia = consulta.isEsEnfermedadVigilancia();

				return esSuConsulta || esVigilancia;
			}
		}

		return false;
	}
}