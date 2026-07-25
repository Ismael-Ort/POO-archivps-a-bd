package visual;

import javaBD.AlergiaBD;
import logico.Alergia;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class TomaAlergias extends JDialog {

	private final JPanel contentPanel =
			new JPanel();

	private JPanel panelChecks;
	private JTextField txtBuscar;

	private ArrayList<Alergia>
			alergiasSeleccionadas = null;

	public static void main(String[] args) {
		try {
			TomaAlergias dialog =
					new TomaAlergias();

			dialog.setDefaultCloseOperation(
					JDialog.DISPOSE_ON_CLOSE
			);

			dialog.setVisible(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public TomaAlergias() {

		setIconImage(
				Toolkit.getDefaultToolkit()
						.getImage(
								TomaAlergias.class
										.getResource(
												"/recursos/enfc.jpg"
										)
						)
		);

		setTitle("Selección de Alergias");
		setModal(true);
		setBounds(100, 100, 450, 500);
		setLocationRelativeTo(null);

		getContentPane().setLayout(
				new BorderLayout()
		);

		contentPanel.setBackground(
				new Color(255, 250, 205)
		);

		contentPanel.setBorder(
				new EmptyBorder(15, 15, 15, 15)
		);

		getContentPane().add(
				contentPanel,
				BorderLayout.CENTER
		);

		contentPanel.setLayout(null);

		JLabel lblTitulo =
				new JLabel(
						"Seleccione las alergias que padece:"
				);

		lblTitulo.setBounds(
				15, 10, 400, 20
		);

		contentPanel.add(lblTitulo);

		JLabel lblBuscar =
				new JLabel("Buscar:");

		lblBuscar.setBounds(
				15, 40, 60, 20
		);

		contentPanel.add(lblBuscar);

		txtBuscar = new JTextField();
		txtBuscar.setBounds(
				80, 38, 250, 26
		);
		contentPanel.add(txtBuscar);

		txtBuscar.addKeyListener(
				new KeyAdapter() {
					@Override
					public void keyReleased(
							KeyEvent e
					) {
						buscarAlergias(
								txtBuscar.getText()
						);
					}
				}
		);

		JButton btnLimpiar =
				new JButton("Limpiar");

		btnLimpiar.addActionListener(
				e -> {
					txtBuscar.setText("");
					cargarAlergias();
				}
		);

		btnLimpiar.setBounds(
				340, 38, 80, 26
		);

		contentPanel.add(btnLimpiar);

		panelChecks = new JPanel();

		panelChecks.setBackground(
				new Color(255, 245, 238)
		);

		panelChecks.setBorder(
				new TitledBorder(
						null,
						"Alergias Disponibles",
						TitledBorder.LEADING,
						TitledBorder.TOP,
						null,
						null
				)
		);

		panelChecks.setLayout(
				new BoxLayout(
						panelChecks,
						BoxLayout.Y_AXIS
				)
		);

		JScrollPane scrollPane =
				new JScrollPane(panelChecks);

		scrollPane.setBounds(
				15, 75, 405, 260
		);

		contentPanel.add(scrollPane);

		JButton btnRegistrarNueva =
				new JButton(
						"+ Registrar Nueva Alergia"
				);

		btnRegistrarNueva.addActionListener(
				e -> {
					regAlergia dialog =
							new regAlergia();

					dialog.setModal(true);
					dialog.setVisible(true);

					/*
					 * Después de cerrar el formulario,
					 * se recarga la lista desde MySQL.
					 */
					cargarAlergias();
				}
		);

		btnRegistrarNueva.setBounds(
				15, 345, 200, 30
		);

		contentPanel.add(btnRegistrarNueva);

		JLabel lblInfo =
				new JLabel(
						"Nota: Si no encuentra una alergia, puede registrarla"
				);

		lblInfo.setBounds(
				15, 385, 400, 20
		);

		contentPanel.add(lblInfo);

		JPanel buttonPane =
				new JPanel();

		buttonPane.setBackground(
				new Color(224, 255, 255)
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

		JButton btnSeleccionar =
				new JButton("Seleccionar");

		btnSeleccionar.addActionListener(
				e -> seleccionarAlergias()
		);

		buttonPane.add(btnSeleccionar);

		getRootPane().setDefaultButton(
				btnSeleccionar
		);

		JButton btnCancelar =
				new JButton("Cancelar");

		btnCancelar.setBackground(
				new Color(240, 248, 255)
		);

		btnCancelar.addActionListener(
				e -> {
					alergiasSeleccionadas = null;
					dispose();
				}
		);

		buttonPane.add(btnCancelar);

		cargarAlergias();
	}

	private void cargarAlergias() {

		panelChecks.removeAll();

		/*
		 * =============================================================
		 * IMPLEMENTACIÓN ANTERIOR MEDIANTE ARCHIVOS / ARRAYLIST
		 * =============================================================
		 *
		 * ArrayList<Alergia> alergias =
		 *         Clinica.getInstance().getAlergias();
		 *
		 * La línea queda documentada, pero ya no se ejecuta.
		 */

		/*
		 * =============================================================
		 * NUEVA IMPLEMENTACIÓN MEDIANTE BASE DE DATOS
		 * =============================================================
		 */
		ArrayList<Alergia> alergias =
				AlergiaBD.obtenerAlergias();

		if (alergias == null
				|| alergias.isEmpty()) {

			JLabel lblVacio =
					new JLabel(
							"  No hay alergias registradas. "
									+ "Use el botón 'Registrar Nueva Alergia'"
					);

			panelChecks.add(lblVacio);

		} else {

			for (Alergia alergia :
					alergias) {

				JCheckBox chk =
						new JCheckBox(
								alergia.getNombre()
										+ " ("
										+ alergia.getTipo()
										+ ")"
						);

				chk.putClientProperty(
						"alergia",
						alergia
				);

				panelChecks.add(chk);
			}
		}

		panelChecks.revalidate();
		panelChecks.repaint();
	}

	private void buscarAlergias(
			String criterio
	) {

		panelChecks.removeAll();

		if (criterio.trim().isEmpty()) {
			cargarAlergias();
			return;
		}

		String criterioLower =
				criterio.toLowerCase().trim();

		/*
		 * IMPLEMENTACIÓN ANTERIOR:
		 *
		 * ArrayList<Alergia> alergias =
		 *         Clinica.getInstance().getAlergias();
		 */

		// NUEVA IMPLEMENTACIÓN CON BASE DE DATOS
		ArrayList<Alergia> alergias =
				AlergiaBD.obtenerAlergias();

		boolean encontrado = false;

		for (Alergia alergia :
				alergias) {

			String nombreLower =
					alergia.getNombre()
							.toLowerCase();

			String tipoLower =
					alergia.getTipo()
							.toLowerCase();

			if (nombreLower.contains(
					criterioLower
			) || tipoLower.contains(
					criterioLower
			)) {

				JCheckBox chk =
						new JCheckBox(
								alergia.getNombre()
										+ " ("
										+ alergia.getTipo()
										+ ")"
						);

				chk.putClientProperty(
						"alergia",
						alergia
				);

				panelChecks.add(chk);
				encontrado = true;
			}
		}

		if (!encontrado) {

			JLabel lblNoEncontrado =
					new JLabel(
							"  No se encontraron alergias "
									+ "con ese criterio"
					);

			panelChecks.add(
					lblNoEncontrado
			);
		}

		panelChecks.revalidate();
		panelChecks.repaint();
	}

	private void seleccionarAlergias() {

		alergiasSeleccionadas =
				new ArrayList<>();

		for (Component comp :
				panelChecks.getComponents()) {

			if (comp instanceof JCheckBox) {

				JCheckBox chk =
						(JCheckBox) comp;

				if (chk.isSelected()) {

					Alergia alergia =
							(Alergia)
									chk.getClientProperty(
											"alergia"
									);

					if (alergia != null) {
						alergiasSeleccionadas.add(
								alergia
						);
					}
				}
			}
		}

		if (alergiasSeleccionadas.isEmpty()) {

			int confirmacion =
					JOptionPane.showConfirmDialog(
							this,
							"No ha seleccionado ninguna alergia.\n"
									+ "¿Confirma que el paciente "
									+ "no tiene alergias?",
							"Sin alergias seleccionadas",
							JOptionPane.YES_NO_OPTION,
							JOptionPane.QUESTION_MESSAGE
					);

			if (confirmacion
					== JOptionPane.YES_OPTION) {

				dispose();
			}

			return;
		}

		JOptionPane.showMessageDialog(
				this,
				"Se seleccionaron "
						+ alergiasSeleccionadas.size()
						+ " alergia(s)",
				"Alergias seleccionadas",
				JOptionPane.INFORMATION_MESSAGE
		);

		dispose();
	}

	public ArrayList<Alergia>
	AlergiasSeleccionadas() {

		return alergiasSeleccionadas;
	}
}
