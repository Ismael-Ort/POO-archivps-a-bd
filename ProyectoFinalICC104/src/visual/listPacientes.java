package visual;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;
import logico.*;

public class listPacientes extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private DefaultTableModel modelo;
	private JButton btnModificar;
	private JButton btnVerHistorial;

	public listPacientes() {
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(listPacientes.class.getResource("/recursos/pac.jpg")));

		setTitle("Lista de Pacientes - " + (Control.esAdministrador() ? "TODOS" : "MIS PACIENTES"));

		setBounds(100, 100, 1000, 600);
		setLocationRelativeTo(null);
		setModal(true);

		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(255, 250, 205));
		contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));

		// ===== PANEL INFORMACIÓN =====
		JPanel panelInfo = new JPanel();
		panelInfo.setBackground(new Color(255, 250, 205));
		panelInfo.setLayout(new FlowLayout(FlowLayout.LEFT));
		contentPanel.add(panelInfo, BorderLayout.NORTH);

		JLabel lblInfo = new JLabel();
		if (Control.esAdministrador()) {
			lblInfo.setText("Mostrando TODOS los pacientes del sistema");
			lblInfo.setForeground(new Color(0, 102, 204));
		} else if (Control.esDoctor()) {
			Doctor doctor = Control.getDoctorLogeado();
			lblInfo.setText("Mostrando pacientes registrados por: "
					+ (doctor != null ? doctor.getNombre() + " " + doctor.getApellido() : "Usted"));
			lblInfo.setForeground(new Color(0, 128, 0));
		}
		lblInfo.setFont(new Font("Tahoma", Font.BOLD, 12));
		panelInfo.add(lblInfo);

		// ===== TABLA =====
		String[] columnas = { "Código", "Cédula", "Nombre", "Apellido", "Teléfono", "Tipo Sangre", "Estado",
				"Registrado por" };

		modelo = new DefaultTableModel(columnas, 0) {
			@Override
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		table = new JTable(modelo);
		table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		JScrollPane scrollPane = new JScrollPane(table);
		contentPanel.add(scrollPane, BorderLayout.CENTER);

		// ===== BOTONES =====
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(220, 220, 220));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 10));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		btnModificar = new JButton("Modificar");
		btnModificar.setEnabled(false);
		btnModificar.addActionListener(e -> modificarPaciente());
		buttonPane.add(btnModificar);

		btnVerHistorial = new JButton("Ver Historial");
		btnVerHistorial.setEnabled(false);
		btnVerHistorial.addActionListener(e -> verHistorial());
		buttonPane.add(btnVerHistorial);

		JButton btnActualizar = new JButton("Actualizar");
		btnActualizar.addActionListener(e -> cargarPacientes());
		buttonPane.add(btnActualizar);

		JButton btnCerrar = new JButton("Cerrar");
		btnCerrar.setBackground(new Color(255, 239, 213));
		btnCerrar.addActionListener(e -> dispose());
		buttonPane.add(btnCerrar);

		// Listener para habilitar botones
		table.getSelectionModel().addListSelectionListener(e -> {
			boolean seleccionado = table.getSelectedRow() != -1;
			btnModificar.setEnabled(seleccionado && puedeModificarSeleccion());
			btnVerHistorial.setEnabled(seleccionado);
		});

		// ===== CARGAR DATOS =====
		cargarPacientes();
	}

	private void cargarPacientes() {
		modelo.setRowCount(0);

		// Obtener pacientes visibles según permisos
		ArrayList<Paciente> pacientesVisibles = obtenerPacientesVisibles();

		if (pacientesVisibles == null || pacientesVisibles.isEmpty()) {
			String mensaje = Control.esAdministrador() ? "No hay pacientes registrados en el sistema"
					: "No ha registrado pacientes aún";

			modelo.addRow(new Object[] { "", mensaje, "", "", "", "", "", "" });
			return;
		}

		// Cargar pacientes en la tabla
		for (Paciente p : pacientesVisibles) {
			String estado = p.isActivo() ? "Activo" : "Inactivo";
			String registrador = obtenerNombreRegistrador(p.getDoctorRegistrador());

			Object[] fila = { p.getCodigoPaciente(), p.getCedula(), p.getNombre(), p.getApellido(), p.getTelefono(),
					p.getTipoSangre(), estado, registrador };
			modelo.addRow(fila);
		}
	}

	private ArrayList<Paciente> obtenerPacientesVisibles() {
		ArrayList<Paciente> resultado = new ArrayList<>();
		Clinica clinica = Clinica.getInstance();

		if (Control.esAdministrador()) {
			// ADMIN ve TODOS los pacientes
			resultado.addAll(clinica.getPacientes());
		} else if (Control.esDoctor()) {
			// DOCTOR ve pacientes que ÉL registró (no importa si los atendió o no)
			String licenciaDoctorLogeado = Control.getLicenciaDoctorLogeado();

			if (licenciaDoctorLogeado == null || licenciaDoctorLogeado.isEmpty()) {
				JOptionPane.showMessageDialog(this, "Error: No se pudo identificar al doctor logeado",
						"Error de sesión", JOptionPane.ERROR_MESSAGE);
				return resultado;
			}

			// CORRECCIÓN: Buscar por doctor registrador en el paciente
			for (Paciente paciente : clinica.getPacientes()) {
				String doctorRegistrador = paciente.getDoctorRegistrador();

				// Si el paciente fue registrado por este doctor, agregarlo
				if (doctorRegistrador != null && doctorRegistrador.equals(licenciaDoctorLogeado)) {
					resultado.add(paciente);
				}
				for (Consulta consultas : paciente.getHistoriaClinica().getConsultas()) {
					if (consultas.getDoctor().equals(Control.getDoctorLogeado())) {
						if (!resultado.contains(paciente)) {
							resultado.add(paciente);
						}
					}
				}
			}
		}

		return resultado;
	}

	private String obtenerNombreRegistrador(String licencia) {
		if (licencia == null || licencia.equals("Sistema")) {
			return "Sistema";
		}

		Doctor doctor = Clinica.getInstance().buscarDoctorPorNumeroLicencia(licencia);
		if (doctor != null) {
			return "Dr. " + doctor.getNombre() + " " + doctor.getApellido();
		}

		return "Desconocido";
	}

	private boolean puedeModificarSeleccion() {
		int filaSeleccionada = table.getSelectedRow();
		if (filaSeleccionada == -1) {
			return false;
		}

		String codigo = (String) modelo.getValueAt(filaSeleccionada, 0);
		if (codigo == null || codigo.isEmpty()) {
			return false;
		}

		Paciente paciente = Clinica.getInstance().buscarPacientePorCodigo(codigo);
		if (paciente == null) {
			return false;
		}

		// ADMIN puede modificar todos
		if (Control.esAdministrador()) {
			return true;
		}

		// DOCTOR solo puede modificar sus pacientes
		if (Control.esDoctor()) {
			Doctor doctorLogeado = Control.getDoctorLogeado();
			if (doctorLogeado != null && paciente.getDoctorRegistrador() != null) {
				return true;
			}
		}

		return false;
	}

	private void modificarPaciente() {
		int filaSeleccionada = table.getSelectedRow();
		if (filaSeleccionada == -1) {
			JOptionPane.showMessageDialog(this, "Seleccione un paciente", "Advertencia", JOptionPane.WARNING_MESSAGE);
			return;
		}

		String codigo = (String) modelo.getValueAt(filaSeleccionada, 0);
		Paciente paciente = Clinica.getInstance().buscarPacientePorCodigo(codigo);

		if (paciente == null) {
			JOptionPane.showMessageDialog(this, "Paciente no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Verificar permisos
		if (!puedeModificarSeleccion()) {
			JOptionPane.showMessageDialog(this,
					"No tiene permisos para modificar este paciente.\n"
							+ "Solo puede modificar pacientes que usted registró.",
					"Acceso denegado", JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Abrir ventana de modificación
		regPaciente dialog = new regPaciente(paciente);
		dialog.setModal(true);
		dialog.setVisible(true);

		// Recargar lista
		cargarPacientes();
	}

	private void verHistorial() {
		int filaSeleccionada = table.getSelectedRow();
		if (filaSeleccionada == -1) {
			JOptionPane.showMessageDialog(this, "Seleccione un paciente", "Advertencia", JOptionPane.WARNING_MESSAGE);
			return;
		}

		String codigo = (String) modelo.getValueAt(filaSeleccionada, 0);
		Paciente paciente = Clinica.getInstance().buscarPacientePorCodigo(codigo);

		if (paciente == null) {
			JOptionPane.showMessageDialog(this, "Paciente no encontrado", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Abrir historial (con filtros de consultas según permisos)
		VerHistorialClinico dialog = new VerHistorialClinico();
		dialog.setModal(true);
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);
	}
}