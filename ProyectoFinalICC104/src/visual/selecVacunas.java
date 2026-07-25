package visual;

import javaBD.VacunaPreviaBD;
import logico.Paciente;
import logico.Vacuna;
import logico.VacunaVieja;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.ArrayList;

public class selecVacunas extends JDialog {

	private final JPanel contentPanel =
			new JPanel();

	private JPanel panelChecks;
	private Paciente paciente;

	public static void main(String[] args) {
		try {
			selecVacunas dialog =
					new selecVacunas(null);

			dialog.setDefaultCloseOperation(
					JDialog.DISPOSE_ON_CLOSE
			);

			dialog.setVisible(true);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public selecVacunas(
			Paciente elPaciente
	) {

		setIconImage(
				Toolkit.getDefaultToolkit()
						.getImage(
								selecVacunas.class
										.getResource(
												"/recursos/agu.jpg"
										)
						)
		);

		this.paciente = elPaciente;

		setTitle(
				"Registro de Vacunas - "
						+ (elPaciente != null
						? elPaciente.getNombre()
						: "")
		);

		setModal(true);
		setBounds(100, 100, 550, 500);
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
						"Registro de Vacunas Aplicadas Previamente"
				);

		lblTitulo.setFont(
				new Font(
						"Tahoma",
						Font.BOLD,
						13
				)
		);

		lblTitulo.setBounds(
				15, 10, 500, 25
		);

		contentPanel.add(lblTitulo);

		JLabel lblInfo1 =
				new JLabel(
						"¿El paciente tiene vacunas "
								+ "aplicadas antes de hoy?"
				);

		lblInfo1.setBounds(
				15, 40, 500, 20
		);

		contentPanel.add(lblInfo1);

		JLabel lblInfo2 =
				new JLabel("Regístrelas aquí.");

		lblInfo2.setBounds(
				15, 60, 500, 20
		);

		contentPanel.add(lblInfo2);

		JLabel lblVacunasPrevias =
				new JLabel(
						"Registrar Vacunas:"
				);

		lblVacunasPrevias.setFont(
				new Font(
						"Tahoma",
						Font.BOLD,
						12
				)
		);

		lblVacunasPrevias.setBounds(
				15, 95, 250, 20
		);

		contentPanel.add(lblVacunasPrevias);

		JButton btnRegistrarPrevia =
				new JButton(
						"+ Registrar Vacuna"
				);

		btnRegistrarPrevia.addActionListener(
				e -> registrarVacunaPrevia()
		);

		btnRegistrarPrevia.setBounds(
				15, 120, 180, 30
		);

		contentPanel.add(btnRegistrarPrevia);

		JLabel lblInfoPrevia =
				new JLabel(
						"(Ejemplo: Sarampión, "
								+ "Hepatitis B, etc.)"
				);

		lblInfoPrevia.setBounds(
				205, 125, 300, 20
		);

		contentPanel.add(lblInfoPrevia);

		panelChecks = new JPanel();

		panelChecks.setBackground(
				new Color(240, 255, 240)
		);

		panelChecks.setBorder(
				new TitledBorder(
						null,
						"Vacunas Registradas",
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
				15, 160, 500, 200
		);

		contentPanel.add(scrollPane);

		JPanel buttonPane =
				new JPanel();

		buttonPane.setBackground(
				new Color(255, 228, 225)
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

		JButton btnOmitir =
				new JButton("Omitir");

		btnOmitir.addActionListener(
				e -> {
					int confirmacion =
							JOptionPane.showConfirmDialog(
									selecVacunas.this,
									"¿Omitir el registro "
											+ "de vacunas previas?",
									"Confirmar",
									JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE
							);

					if (confirmacion
							== JOptionPane.YES_OPTION) {

						dispose();
					}
				}
		);

		buttonPane.add(btnOmitir);

		JButton btnFinalizar =
				new JButton("Finalizar");

		btnFinalizar.setBackground(
				new Color(245, 255, 250)
		);

		btnFinalizar.addActionListener(
				e -> finalizarRegistro()
		);

		buttonPane.add(btnFinalizar);

		getRootPane().setDefaultButton(
				btnFinalizar
		);

		cargarVacunas();
	}

	private void registrarVacunaPrevia() {

		/*
		 * La ventana regVacuViea continúa recopilando:
		 * - enfermedad
		 * - fecha de aplicación
		 *
		 * No representa una vacuna del inventario.
		 */
		regVacuViea dialog =
				new regVacuViea(null);

		dialog.setModal(true);
		dialog.setVisible(true);

		VacunaVieja vacunaRegistrada =
				dialog.mandarLaVacu();

		if (vacunaRegistrada != null) {
			anadirVacunaAlPaciente(
					vacunaRegistrada
			);
		}
	}

	private void cargarVacunas() {

		panelChecks.removeAll();

		if (paciente != null
				&& paciente.getVacunasViejas() != null
				&& !paciente.getVacunasViejas()
				.isEmpty()) {

			for (VacunaVieja vacuna :
					paciente.getVacunasViejas()) {

				JCheckBox chk =
						new JCheckBox(
								vacuna.getEnfermedad()
										+ " - "
										+ vacuna.getFecha()
						);

				chk.setSelected(true);
				chk.setEnabled(false);

				panelChecks.add(chk);
			}

		} else {

			JLabel lblVacio =
					new JLabel(
							"  Sin vacunas registradas"
					);

			panelChecks.add(lblVacio);
		}

		panelChecks.revalidate();
		panelChecks.repaint();
	}

	private void anadirVacunaAlPaciente(
			VacunaVieja vacuna
	) {

		if (vacuna == null) {
			return;
		}

		if (paciente == null) {

			JOptionPane.showMessageDialog(
					this,
					"Error: No hay paciente",
					"Error",
					JOptionPane.ERROR_MESSAGE
			);

			return;
		}

		if (paciente.getVacunasViejas()
				== null) {

			paciente.setVacunasViejas(
					new ArrayList<>()
			);
		}

		/*
		 * =============================================================
		 * IMPLEMENTACIÓN ANTERIOR MEDIANTE ARCHIVOS / OBJETOS
		 * =============================================================
		 *
		 * paciente.getVacunasViejas().add(vacuna);
		 * Clinica.getInstance().modificarPaciente(paciente);
		 *
		 * Este bloque queda documentado, pero la persistencia ya no
		 * se realizará mediante el ArrayList de Clinica.
		 */

		/*
		 * =============================================================
		 * NUEVA IMPLEMENTACIÓN MEDIANTE BASE DE DATOS
		 * =============================================================
		 */
		boolean registrada =
				VacunaPreviaBD.registrarVacunaPrevia(
						paciente.getCodigoPaciente(),
						vacuna
				);

		if (!registrada) {

			JOptionPane.showMessageDialog(
					this,
					"No se pudo registrar la vacuna previa.",
					"Registro no realizado",
					JOptionPane.ERROR_MESSAGE
			);

			return;
		}

		/*
		 * Se agrega al objeto solamente para actualizar visualmente
		 * esta ventana durante la sesión actual.
		 * La persistencia real ya se realizó en MySQL.
		 */
		paciente.getVacunasViejas().add(
				vacuna
		);

		JOptionPane.showMessageDialog(
				this,
				"Vacuna agregada: "
						+ vacuna.getEnfermedad(),
				"Éxito",
				JOptionPane.INFORMATION_MESSAGE
		);

		cargarVacunas();
	}

	private void finalizarRegistro() {

		int cantidadVacunas = 0;

		if (paciente != null
				&& paciente.getVacunasViejas()
				!= null) {

			cantidadVacunas =
					paciente.getVacunasViejas()
							.size();
		}

		if (cantidadVacunas > 0) {

			JOptionPane.showMessageDialog(
					this,
					cantidadVacunas
							+ " vacuna(s) registrada(s)",
					"Completado",
					JOptionPane.INFORMATION_MESSAGE
			);
		}

		dispose();
	}

	/*
	 * Método conservado por compatibilidad con la clase original.
	 * Esta ventana trabaja con VacunaVieja, no con Vacuna de inventario.
	 */
	public ArrayList<Vacuna>
	VacunasSeleccionadas() {

		return new ArrayList<>();
	}
}
