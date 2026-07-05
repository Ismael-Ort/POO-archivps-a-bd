package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import logico.Clinica;
import logico.Doctor;
import java.awt.Toolkit;
import java.awt.Color;

public class listDoctor extends JDialog {
	private final JPanel contentPanel = new JPanel();
	private JTable table;
	private static DefaultTableModel model;
	private static Object[] row;
	private JTextField txtBuscar;
	private JButton btnModificar;
	private JButton btnActivarDesactivar;
	private Doctor doctorSeleccionado = null;

	public static void main(String[] args) {
		try {
			listDoctor dialog = new listDoctor();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public listDoctor() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(listDoctor.class.getResource("/recursos/doc.jpg")));
		setTitle("Listado de Doctores");
		setBounds(100, 100, 950, 500);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(240, 248, 255));
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		
		// ========== PANEL DE BÚSQUEDA ==========
		JPanel panelBusqueda = new JPanel();
		panelBusqueda.setBackground(new Color(240, 248, 255));
		panelBusqueda.setBorder(new TitledBorder(null, "Búsqueda", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		contentPanel.add(panelBusqueda, BorderLayout.NORTH);
		panelBusqueda.setLayout(new FlowLayout(FlowLayout.LEFT));
		
		JLabel lblBuscar = new JLabel("Buscar por nombre o cédula:");
		panelBusqueda.add(lblBuscar);
		
		txtBuscar = new JTextField();
		txtBuscar.setColumns(30);
		panelBusqueda.add(txtBuscar);
		
		// Búsqueda en tiempo real
		txtBuscar.addKeyListener(new KeyAdapter() {
			@Override
			public void keyReleased(KeyEvent e) {
				buscarDoctores(txtBuscar.getText());
			}
		});
		
		JButton btnBuscar = new JButton("Buscar");
		btnBuscar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				buscarDoctores(txtBuscar.getText());
			}
		});
		panelBusqueda.add(btnBuscar);
		
		JButton btnMostrarTodos = new JButton("Mostrar Todos");
		btnMostrarTodos.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				txtBuscar.setText("");
				cargarDoctores();
			}
		});
		panelBusqueda.add(btnMostrarTodos);
		
		// ========== PANEL DE TABLA ==========
		{
			JPanel panel = new JPanel();
			panel.setBackground(new Color(240, 255, 255));
			panel.setBorder(new TitledBorder(null, "Doctores Registrados", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			contentPanel.add(panel, BorderLayout.CENTER);
			panel.setLayout(new BorderLayout(0, 0));
			{
				JScrollPane scrollPane = new JScrollPane();
				panel.add(scrollPane, BorderLayout.CENTER);
				{
					model = new DefaultTableModel() {
						// Hacer que la tabla no sea editable
						@Override
						public boolean isCellEditable(int row, int column) {
							return false;
						}
					};
					table = new JTable();
					table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
					String[] headers = {"Código", "Cédula", "Nombre Completo", "Especialidad", "Citas/Día", "Horario", "Estado"};
					model.setColumnIdentifiers(headers);
					table.setModel(model);
					scrollPane.setViewportView(table);
					
					// Ajustar anchos de columnas
					table.getColumnModel().getColumn(0).setPreferredWidth(80);  // Código
					table.getColumnModel().getColumn(1).setPreferredWidth(110); // Cédula
					table.getColumnModel().getColumn(2).setPreferredWidth(200); // Nombre completo
					table.getColumnModel().getColumn(3).setPreferredWidth(130); // Especialidad
					table.getColumnModel().getColumn(4).setPreferredWidth(70);  // Citas/Día
					table.getColumnModel().getColumn(5).setPreferredWidth(120); // Horario
					table.getColumnModel().getColumn(6).setPreferredWidth(80);  // Estado
					
					// Listener para selección
					table.getSelectionModel().addListSelectionListener(e -> {
						if (!e.getValueIsAdjusting() && table.getSelectedRow() != -1) {
							int index = table.getSelectedRow();
							String codigo = model.getValueAt(index, 0).toString();
							doctorSeleccionado = Clinica.getInstance().buscarDoctorPorCodigo(codigo);
							
							// Habilitar botones
							btnModificar.setEnabled(true);
							btnActivarDesactivar.setEnabled(true);
							
							// Cambiar texto del botón según el estado
							if (doctorSeleccionado != null) {
								if (doctorSeleccionado.isActivo()) {
									btnActivarDesactivar.setText("Desactivar");
								} else {
									btnActivarDesactivar.setText("Activar");
								}
							}
						}
					});
				}
			}
		}
		
		// ========== PANEL DE BOTONES ==========
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBackground(new Color(255, 248, 220));
			buttonPane.setBorder(new TitledBorder(null, "", TitledBorder.LEADING, TitledBorder.TOP, null, null));
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			
			{
				btnModificar = new JButton("Modificar");
				btnModificar.setEnabled(false);
				btnModificar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (doctorSeleccionado != null) {
							modDoctor dialog = new modDoctor(doctorSeleccionado);
							dialog.setModal(true);
							dialog.setVisible(true);
							cargarDoctores(); // Recargar tabla después de modificar
							doctorSeleccionado = null;
							btnModificar.setEnabled(false);
							btnActivarDesactivar.setEnabled(false);
						} else {
							JOptionPane.showMessageDialog(listDoctor.this, 
								"Seleccione un doctor de la lista", 
								"Aviso", 
								JOptionPane.WARNING_MESSAGE);
						}
					}
				});
				buttonPane.add(btnModificar);
			}
			
			{
				btnActivarDesactivar = new JButton("Activar/Desactivar");
				btnActivarDesactivar.setEnabled(false);
				btnActivarDesactivar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if (doctorSeleccionado != null) {
							String accion = doctorSeleccionado.isActivo() ? "desactivar" : "activar";
							int confirmacion = JOptionPane.showConfirmDialog(listDoctor.this, 
								"¿Está seguro que desea " + accion + " al Dr. " + 
								doctorSeleccionado.getNombre() + " " + doctorSeleccionado.getApellido() + "?", 
								"Confirmar " + accion, 
								JOptionPane.YES_NO_OPTION);
							
							if (confirmacion == JOptionPane.YES_OPTION) {
								doctorSeleccionado.setActivo(!doctorSeleccionado.isActivo());
								JOptionPane.showMessageDialog(listDoctor.this, 
									"Doctor " + (doctorSeleccionado.isActivo() ? "activado" : "desactivado") + " exitosamente", 
									"Operación exitosa", 
									JOptionPane.INFORMATION_MESSAGE);
								cargarDoctores();
								doctorSeleccionado = null;
								btnModificar.setEnabled(false);
								btnActivarDesactivar.setEnabled(false);
							}
						}
					}
				});
				buttonPane.add(btnActivarDesactivar);
			}
			
			{
				JButton btnCerrar = new JButton("Cerrar");
				btnCerrar.setBackground(new Color(224, 255, 255));
				btnCerrar.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						dispose();
					}
				});
				btnCerrar.setActionCommand("Cancel");
				buttonPane.add(btnCerrar);
			}
		}
		
		cargarDoctores();
	}
	
	// ========== MÉTODO PARA CARGAR TODOS LOS DOCTORES ==========
	public static void cargarDoctores() {
		model.setRowCount(0);
		row = new Object[model.getColumnCount()];
		for (Doctor doctor : Clinica.getInstance().getDoctores()) {
			row[0] = doctor.getCodigoDoctor();
			row[1] = doctor.getCedula();
			row[2] = doctor.getNombre() + " " + doctor.getApellido();
			row[3] = doctor.getEspecialidad();
			row[4] = doctor.getCitasPorDia();
			row[5] = doctor.getHorarioInicio() + " - " + doctor.getHorarioFin();
			row[6] = doctor.isActivo() ? "Activo" : "Inactivo";
			model.addRow(row);
		}
	}
	
	// ========== MÉTODO PARA BUSCAR DOCTORES ==========
	private void buscarDoctores(String criterio) {
		model.setRowCount(0);
		row = new Object[model.getColumnCount()];
		
		if (criterio.trim().isEmpty()) {
			cargarDoctores();
			return;
		}
		
		String criterioLower = criterio.toLowerCase().trim();
		
		for (Doctor doctor : Clinica.getInstance().getDoctores()) {
			String nombreCompleto = (doctor.getNombre() + " " + doctor.getApellido()).toLowerCase();
			String cedula = doctor.getCedula().toLowerCase();
			
			if (nombreCompleto.contains(criterioLower) || cedula.contains(criterioLower)) {
				row[0] = doctor.getCodigoDoctor();
				row[1] = doctor.getCedula();
				row[2] = doctor.getNombre() + " " + doctor.getApellido();
				row[3] = doctor.getEspecialidad();
				row[4] = doctor.getCitasPorDia();
				row[5] = doctor.getHorarioInicio() + " - " + doctor.getHorarioFin();
				row[6] = doctor.isActivo() ? "Activo" : "Inactivo";
				model.addRow(row);
			}
		}
		
		if (model.getRowCount() == 0) {
			JOptionPane.showMessageDialog(this, 
				"No se encontraron doctores con ese criterio de búsqueda", 
				"Sin resultados", 
				JOptionPane.INFORMATION_MESSAGE);
		}
	}
}