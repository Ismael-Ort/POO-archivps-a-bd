package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import logico.Clinica;
import logico.Control;
import logico.Cita;
import logico.Doctor;
import logico.User;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.Toolkit;
import java.awt.Color;

public class ListaCitas extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTable tableCitas;
	private DefaultTableModel modelo;
	private JComboBox<String> cbxEstado;
	private JTextField txtBuscarPaciente;
	private JButton btnVerDetalles;
	private JButton btnRealizarConsulta;
	private JButton btnCancelarCita;
	private Cita citaSeleccionada = null;

	public ListaCitas() {
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(ListaCitas.class.getResource("/recursos/resp.jpg")));
		setTitle("Lista de Citas");
		setBounds(100, 100, 900, 600);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(240, 255, 255));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		setLocationRelativeTo(null);

		// PANEL FILTROS
		JPanel panelFiltros = new JPanel();
		panelFiltros.setBackground(new Color(240, 255, 240));
		contentPanel.add(panelFiltros, BorderLayout.NORTH);
		panelFiltros.setLayout(new FlowLayout(FlowLayout.LEFT));

		JLabel lblEstado = new JLabel("Estado:");
		panelFiltros.add(lblEstado);

		cbxEstado = new JComboBox<String>();
		cbxEstado.addItem("Todas");
		cbxEstado.addItem("Pendientes");
		cbxEstado.addItem("Completadas");
		cbxEstado.addItem("Canceladas");
		cbxEstado.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cargarCitas();
				limpiarSeleccion();
			}
		});
		panelFiltros.add(cbxEstado);

		JLabel lblPaciente = new JLabel("  Paciente:");
		panelFiltros.add(lblPaciente);

		txtBuscarPaciente = new JTextField();
		txtBuscarPaciente.setColumns(15);
		panelFiltros.add(txtBuscarPaciente);

		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cargarCitas();
				limpiarSeleccion();
			}
		});
		panelFiltros.add(btnBuscar);

		// TABLA
		JScrollPane scrollPane = new JScrollPane();
		contentPanel.add(scrollPane, BorderLayout.CENTER);

		String[] columnas = { "Código", "Fecha", "Hora", "Paciente", "Doctor", "Motivo", "Estado" };
		modelo = new DefaultTableModel(columnas, 0) {
			public boolean isCellEditable(int row, int column) {
				return false;
			}
		};

		tableCitas = new JTable(modelo);

		// Listener para detectar selección en la tabla
		tableCitas.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				if (!e.getValueIsAdjusting()) {
					manejarSeleccionCita();
				}
			}
		});

		scrollPane.setViewportView(tableCitas);

		// BOTONES
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(240, 255, 255));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		btnVerDetalles = new JButton("Ver Detalles");
		btnVerDetalles.setEnabled(false);
		btnVerDetalles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				verDetalles();
			}
		});
		buttonPane.add(btnVerDetalles);

		btnRealizarConsulta = new JButton("Realizar Consulta");
		btnRealizarConsulta.setEnabled(false);
		btnRealizarConsulta.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				realizarConsulta();
			}
		});
		buttonPane.add(btnRealizarConsulta);

		btnCancelarCita = new JButton("Cancelar Cita");
		btnCancelarCita.setEnabled(false);
		btnCancelarCita.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cancelarCita();
			}
		});
		buttonPane.add(btnCancelarCita);

		JButton btnCerrar = new JButton("Cerrar");
		btnCerrar.setBackground(new Color(255, 239, 213));
		btnCerrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		buttonPane.add(btnCerrar);

		cargarCitas();
	}

	private void cargarCitas() {
		modelo.setRowCount(0);

		// Obtener usuario actual
		User usuarioActual = Control.getLoginUser();
		if (usuarioActual == null) {
			JOptionPane.showMessageDialog(this, "No hay usuario logeado", "Error", JOptionPane.ERROR_MESSAGE);
			dispose();
			return;
		}

		// Verificar si es admin
		boolean esAdmin = Control.esAdministrador();

		// Obtener doctor logeado (si es doctor)
		Doctor doctorLogeado = Control.getDoctorLogeado();
		String licenciaDoctorLogeado = (doctorLogeado != null) ? doctorLogeado.getNumeroLicencia() : "";

		ArrayList<Cita> citas = Clinica.getInstance().getCitas();
		String estadoFiltro = (String) cbxEstado.getSelectedItem();
		String pacienteFiltro = txtBuscarPaciente.getText().trim().toLowerCase();

		for (Cita cita : citas) {
			boolean cumpleFiltros = true;

			// Filtro por estado
			if (!estadoFiltro.equals("Todas")) {
				String estadoBuscado = "";

				if (estadoFiltro.equals("Pendientes")) {
					estadoBuscado = "Pendiente";
				} else if (estadoFiltro.equals("Completadas")) {
					estadoBuscado = "Completada";
				} else if (estadoFiltro.equals("Canceladas")) {
					estadoBuscado = "Cancelada";
				}

				if (!cita.getEstadoCita().equals(estadoBuscado)) {
					cumpleFiltros = false;
				}
			}

			// Filtro por paciente
			if (!pacienteFiltro.isEmpty()) {
				String nombreCompletoPaciente = cita.getPaciente().getNombre() + " " + cita.getPaciente().getApellido();
				if (!nombreCompletoPaciente.toLowerCase().contains(pacienteFiltro)) {
					cumpleFiltros = false;
				}
			}

			// FILTRO POR PERMISOS DE VISIBILIDAD
			if (cumpleFiltros) {
				boolean puedeVer = false;

				// 1. Admin ve todo
				if (esAdmin) {
					puedeVer = true;
				}
				// 2. Doctor solo ve sus propias citas
				else if (doctorLogeado != null && cita.getDoctor() != null) {
					boolean esCitaDelDoctor = cita.getDoctor().getNumeroLicencia().equalsIgnoreCase(doctorLogeado.getNumeroLicencia());
					puedeVer = esCitaDelDoctor;
				}

				if (puedeVer) {
					Object[] fila = { cita.getCodigoCita(), cita.getFechaCita().toString(),
							cita.getHoraCita().toString(),
							cita.getPaciente().getNombre() + " " + cita.getPaciente().getApellido(),
							cita.getDoctor().getNombre() + " " + cita.getDoctor().getApellido(), cita.getMotivoCita(),
							cita.getEstadoCita() };
					modelo.addRow(fila);
				}
			}
		}

		// Mostrar mensaje si no hay citas
		if (modelo.getRowCount() == 0) {
			if (esAdmin) {
				modelo.addRow(new Object[] { "", "No hay citas en el sistema", "", "", "", "", "" });
			} else {
				modelo.addRow(new Object[] { "", "No tiene citas asignadas", "", "", "", "", "" });
			}
		}
	}

	private void manejarSeleccionCita() {
		int filaSeleccionada = tableCitas.getSelectedRow();

		if (filaSeleccionada == -1 || modelo.getRowCount() == 0) {
			limpiarSeleccion();
			return;
		}

		// Verificar que no sea la fila de "no hay citas"
		String codigoCita = (String) modelo.getValueAt(filaSeleccionada, 0);
		if (codigoCita == null || codigoCita.isEmpty()) {
			limpiarSeleccion();
			return;
		}

		citaSeleccionada = Clinica.getInstance().buscarCita(codigoCita);

		if (citaSeleccionada != null) {
			// Habilitar siempre el botón Ver Detalles
			btnVerDetalles.setEnabled(true);

			// Determinar estado de la cita
			String estado = citaSeleccionada.getEstadoCita();

			// Botón Realizar Consulta - solo para citas Pendientes
			btnRealizarConsulta.setEnabled(estado.equals("Pendiente"));

			// Botón Cancelar Cita - solo para citas Pendientes
			btnCancelarCita.setEnabled(estado.equals("Pendiente"));

			// Cambiar texto del botón Cancelar según estado
			if (estado.equals("Cancelada")) {
				btnCancelarCita.setText("Cita Cancelada");
				btnCancelarCita.setEnabled(false);
			} else if (estado.equals("Completada")) {
				btnCancelarCita.setText("Consulta Realizada");
				btnCancelarCita.setEnabled(false);
			} else {
				btnCancelarCita.setText("Cancelar Cita");
			}
		} else {
			limpiarSeleccion();
		}
	}

	private void limpiarSeleccion() {
		citaSeleccionada = null;
		btnVerDetalles.setEnabled(false);
		btnRealizarConsulta.setEnabled(false);
		btnCancelarCita.setEnabled(false);
		btnCancelarCita.setText("Cancelar Cita");
	}

	private void verDetalles() {
		if (citaSeleccionada == null) {
			JOptionPane.showMessageDialog(this, "Seleccione una cita", "Advertencia", JOptionPane.WARNING_MESSAGE);
			return;
		}

		DetalleCita ventana = new DetalleCita(citaSeleccionada);
		ventana.setModal(true);
		ventana.setVisible(true);
	}

	private void realizarConsulta() {
		if (citaSeleccionada == null) {
			JOptionPane.showMessageDialog(this, "Seleccione una cita válida", "Advertencia",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Verificar que el doctor logeado sea el mismo de la cita
		Doctor doctorLogeado = Control.getDoctorLogeado();
		if (doctorLogeado == null) {
			JOptionPane.showMessageDialog(this, "No hay doctor logeado", "Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		if (!citaSeleccionada.estaPendiente()) {
			JOptionPane.showMessageDialog(this, "Solo se pueden realizar consultas a citas pendientes", "Error",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Verificar que el doctor logeado sea el asignado a la cita
		if (!citaSeleccionada.getDoctor().getNumeroLicencia().equals(doctorLogeado.getNumeroLicencia())) {
			JOptionPane.showMessageDialog(this,
					"No puede realizar esta consulta.\n" + "Esta cita está asignada a otro doctor.", "No autorizado",
					JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Verificar que la cita sea para hoy o una fecha pasada
		if (citaSeleccionada.getFechaCita().isAfter(LocalDate.now())) {
			int respuesta = JOptionPane.showConfirmDialog(this,
					"La cita seleccionada es para una fecha futura (" + citaSeleccionada.getFechaCita() + ").\n"
							+ "¿Desea realizar la consulta de todas formas?",
					"Cita Futura", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			if (respuesta != JOptionPane.YES_OPTION) {
				return;
			}
		}

		// Abrir ventana de registro de consulta
		regConsulta dialog = new regConsulta(citaSeleccionada);
		dialog.setLocationRelativeTo(this);
		dialog.setVisible(true);

		// Recargar citas después de cerrar la consulta
		cargarCitas();
		limpiarSeleccion();
	}

	private void cancelarCita() {
		if (citaSeleccionada == null) {
			JOptionPane.showMessageDialog(this, "Seleccione una cita", "Advertencia", JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Verificar que la cita esté pendiente
		if (!citaSeleccionada.estaPendiente()) {
			JOptionPane.showMessageDialog(this,
					"No se puede cancelar una cita que ya está " + citaSeleccionada.getEstadoCita().toLowerCase(),
					"Error", JOptionPane.ERROR_MESSAGE);
			return;
		}

		int confirmacion = JOptionPane.showConfirmDialog(this,
				"¿Está seguro de cancelar esta cita?\n\n" + "Código: " + citaSeleccionada.getCodigoCita() + "\n"
						+ "Paciente: " + citaSeleccionada.getPaciente().getNombre() + " "
						+ citaSeleccionada.getPaciente().getApellido() + "\n" + "Fecha: "
						+ citaSeleccionada.getFechaCita() + "\n" + "Hora: " + citaSeleccionada.getHoraCita(),
				"Confirmar Cancelación", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

		if (confirmacion == JOptionPane.YES_OPTION) {
			boolean cancelada = Clinica.getInstance().cancelarCita(citaSeleccionada.getCodigoCita());

			if (cancelada) {
				JOptionPane.showMessageDialog(this, "Cita cancelada exitosamente", "Éxito",
						JOptionPane.INFORMATION_MESSAGE);
				cargarCitas();
				limpiarSeleccion();
			} else {
				JOptionPane.showMessageDialog(this, "No se pudo cancelar la cita", "Error", JOptionPane.ERROR_MESSAGE);
			}
		}
	}
}