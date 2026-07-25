package visual;

import javaBD.PacienteBD;
import logico.Control;
import logico.Doctor;
import logico.Paciente;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

public class listPacientes extends JDialog {

	private final JPanel contentPanel =
			new JPanel();

	private JTable table;
	private DefaultTableModel modelo;
	private JButton btnModificar;
	private JButton btnVerHistorial;

	public listPacientes() {

		setIconImage(
				Toolkit.getDefaultToolkit()
						.getImage(
								listPacientes.class
										.getResource(
												"/recursos/pac.jpg"
										)
						)
		);

		setTitle(
				"Lista de Pacientes - "
						+ (Control.esAdministrador()
						? "TODOS"
						: "MIS PACIENTES")
		);

		setBounds(100, 100, 1000, 600);
		setLocationRelativeTo(null);
		setModal(true);

		getContentPane().setLayout(
				new BorderLayout()
		);

		contentPanel.setBackground(
				new Color(255, 250, 205)
		);

		contentPanel.setBorder(
				new EmptyBorder(10, 10, 10, 10)
		);

		getContentPane().add(
				contentPanel,
				BorderLayout.CENTER
		);

		contentPanel.setLayout(
				new BorderLayout(0, 0)
		);

		JPanel panelInfo =
				new JPanel();

		panelInfo.setBackground(
				new Color(255, 250, 205)
		);

		panelInfo.setLayout(
				new FlowLayout(
						FlowLayout.LEFT
				)
		);

		contentPanel.add(
				panelInfo,
				BorderLayout.NORTH
		);

		JLabel lblInfo =
				new JLabel();

		if (Control.esAdministrador()) {

			lblInfo.setText(
					"Mostrando TODOS los pacientes "
							+ "registrados en MySQL"
			);

			lblInfo.setForeground(
					new Color(0, 102, 204)
			);

		} else if (Control.esDoctor()) {

			Doctor doctor =
					Control.getDoctorLogeado();

			lblInfo.setText(
					"Mostrando pacientes registrados por: "
							+ (doctor != null
							? doctor.getNombre()
							  + " "
							  + doctor.getApellido()
							: "Usted")
			);

			lblInfo.setForeground(
					new Color(0, 128, 0)
			);
		}

		lblInfo.setFont(
				new Font(
						"Tahoma",
						Font.BOLD,
						12
				)
		);

		panelInfo.add(lblInfo);

		String[] columnas = {
				"Código",
				"Cédula",
				"Nombre",
				"Apellido",
				"Teléfono",
				"Tipo Sangre",
				"Estado",
				"Registrado por"
		};

		modelo =
				new DefaultTableModel(
						columnas,
						0
				) {
					@Override
					public boolean isCellEditable(
							int row,
							int column
					) {
						return false;
					}
				};

		table = new JTable(modelo);

		table.setSelectionMode(
				ListSelectionModel
						.SINGLE_SELECTION
		);

		JScrollPane scrollPane =
				new JScrollPane(table);

		contentPanel.add(
				scrollPane,
				BorderLayout.CENTER
		);

		JPanel buttonPane =
				new JPanel();

		buttonPane.setBackground(
				new Color(220, 220, 220)
		);

		buttonPane.setLayout(
				new FlowLayout(
						FlowLayout.RIGHT,
						10,
						10
				)
		);

		getContentPane().add(
				buttonPane,
				BorderLayout.SOUTH
		);

		btnModificar =
				new JButton("Modificar");

		btnModificar.setEnabled(false);

		btnModificar.addActionListener(
				e -> modificarPaciente()
		);

		buttonPane.add(btnModificar);

		btnVerHistorial =
				new JButton("Ver Historial");

		btnVerHistorial.setEnabled(false);

		btnVerHistorial.addActionListener(
				e -> mostrarHistorialPendiente()
		);

		buttonPane.add(btnVerHistorial);

		JButton btnActualizar =
				new JButton("Actualizar");

		btnActualizar.addActionListener(
				e -> cargarPacientes()
		);

		buttonPane.add(btnActualizar);

		JButton btnCerrar =
				new JButton("Cerrar");

		btnCerrar.setBackground(
				new Color(255, 239, 213)
		);

		btnCerrar.addActionListener(
				e -> dispose()
		);

		buttonPane.add(btnCerrar);

		table.getSelectionModel()
				.addListSelectionListener(
						e -> {
							boolean seleccionado =
									table.getSelectedRow()
											!= -1;

							btnModificar.setEnabled(
									seleccionado
											&& puedeModificarSeleccion()
							);

							/*
							 * El historial se mantiene pendiente
							 * hasta migrar su versión básica.
							 */
							btnVerHistorial.setEnabled(
									seleccionado
							);
						}
				);

		cargarPacientes();
	}

	private void cargarPacientes() {

		modelo.setRowCount(0);

		ArrayList<Paciente>
				pacientesVisibles =
				obtenerPacientesVisibles();

		if (pacientesVisibles == null
				|| pacientesVisibles.isEmpty()) {

			String mensaje =
					Control.esAdministrador()
							? "No hay pacientes registrados "
							  + "en la base de datos"
							: "No ha registrado pacientes aún";

			modelo.addRow(
					new Object[]{
							"",
							mensaje,
							"",
							"",
							"",
							"",
							"",
							""
					}
			);

			return;
		}

		for (Paciente paciente :
				pacientesVisibles) {

			String estado =
					paciente.isActivo()
							? "Activo"
							: "Inactivo";

			String registrador =
					obtenerNombreRegistrador(
							paciente
									.getDoctorRegistrador()
					);

			modelo.addRow(
					new Object[]{
							paciente
									.getCodigoPaciente(),
							paciente.getCedula(),
							paciente.getNombre(),
							paciente.getApellido(),
							paciente.getTelefono(),
							paciente.getTipoSangre(),
							estado,
							registrador
					}
			);
		}
	}

	private ArrayList<Paciente>
	obtenerPacientesVisibles() {

		/*
		 * =============================================================
		 * IMPLEMENTACIÓN ANTERIOR MEDIANTE ARCHIVOS / ARRAYLIST
		 * =============================================================
		 *
		 * Clinica clinica = Clinica.getInstance();
		 *
		 * if (Control.esAdministrador()) {
		 *     resultado.addAll(
		 *             clinica.getPacientes()
		 *     );
		 * }
		 *
		 * Para doctores se recorría:
		 *
		 * clinica.getPacientes()
		 *
		 * y también se revisaban consultas guardadas en los objetos.
		 *
		 * Esa implementación queda documentada y deja de ejecutarse.
		 */

		/*
		 * =============================================================
		 * NUEVA IMPLEMENTACIÓN MEDIANTE BASE DE DATOS MYSQL
		 * =============================================================
		 */
		if (Control.esAdministrador()) {
			return PacienteBD.listarTodos();
		}

		if (Control.esDoctor()) {

			Doctor doctor =
					Control.getDoctorLogeado();

			if (doctor == null
					|| doctor.getCodigoDoctor() == null) {

				JOptionPane.showMessageDialog(
						this,
						"No se pudo identificar "
								+ "al doctor logueado.",
						"Error de sesión",
						JOptionPane.ERROR_MESSAGE
				);

				return new ArrayList<>();
			}

			/*
			 * El doctor ve únicamente pacientes cuyo
			 * id_doctor_registrador corresponde a él.
			 *
			 * No se revisan consultas porque ese módulo
			 * no será migrado.
			 */
			return PacienteBD.listarPorDoctor(
					doctor.getCodigoDoctor()
			);
		}

		return new ArrayList<>();
	}

	private String obtenerNombreRegistrador(
			String licencia
	) {

		if (licencia == null
				|| licencia.trim().isEmpty()) {

			return "Sin doctor";
		}

		/*
		 * La lista ya fue filtrada y cargada mediante JOIN.
		 * En el objeto Paciente se conserva la licencia
		 * por compatibilidad con el modelo Java anterior.
		 *
		 * Para no hacer una consulta adicional por cada fila,
		 * se muestra la licencia del registrador.
		 */
		return "Lic. " + licencia;
	}

	private boolean puedeModificarSeleccion() {

		int fila =
				table.getSelectedRow();

		if (fila == -1) {
			return false;
		}

		String codigo =
				(String)
						modelo.getValueAt(
								fila,
								0
						);

		if (codigo == null
				|| codigo.isEmpty()) {

			return false;
		}

		Paciente paciente =
				PacienteBD.buscarPorCodigo(
						codigo
				);

		if (paciente == null) {
			return false;
		}

		if (Control.esAdministrador()) {
			return true;
		}

		if (Control.esDoctor()) {

			Doctor doctor =
					Control.getDoctorLogeado();

			return doctor != null
					&& paciente
					.getDoctorRegistrador()
					!= null
					&& paciente
					.getDoctorRegistrador()
					.equalsIgnoreCase(
							doctor
									.getNumeroLicencia()
					);
		}

		return false;
	}

	private void modificarPaciente() {

		int fila =
				table.getSelectedRow();

		if (fila == -1) {

			JOptionPane.showMessageDialog(
					this,
					"Seleccione un paciente",
					"Advertencia",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		String codigo =
				(String)
						modelo.getValueAt(
								fila,
								0
						);

		/*
		 * IMPLEMENTACIÓN ANTERIOR:
		 *
		 * Clinica.getInstance()
		 *         .buscarPacientePorCodigo(codigo);
		 */

		// NUEVA IMPLEMENTACIÓN CON MYSQL
		Paciente paciente =
				PacienteBD.buscarPorCodigo(
						codigo
				);

		if (paciente == null) {

			JOptionPane.showMessageDialog(
					this,
					"Paciente no encontrado",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);

			return;
		}

		if (!puedeModificarSeleccion()) {

			JOptionPane.showMessageDialog(
					this,
					"No tiene permisos para modificar "
							+ "este paciente.\n"
							+ "Solo puede modificar pacientes "
							+ "que usted registró.",
					"Acceso denegado",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		regPaciente dialog =
				new regPaciente(paciente);

		dialog.setModal(true);
		dialog.setVisible(true);

		cargarPacientes();
	}

	private void mostrarHistorialPendiente() {

		JOptionPane.showMessageDialog(
				this,
				"El historial básico todavía no ha sido migrado.\n"
						+ "Esta etapa se realizará después de completar "
						+ "pacientes, alergias y vacunas previas.",
				"Historial pendiente",
				JOptionPane.INFORMATION_MESSAGE
		);

		/*
		 * =============================================================
		 * IMPLEMENTACIÓN ANTERIOR MEDIANTE ARCHIVOS
		 * =============================================================
		 *
		 * VerHistorialClinico dialog =
		 *         new VerHistorialClinico();
		 *
		 * dialog.setModal(true);
		 * dialog.setLocationRelativeTo(this);
		 * dialog.setVisible(true);
		 */
	}
}
