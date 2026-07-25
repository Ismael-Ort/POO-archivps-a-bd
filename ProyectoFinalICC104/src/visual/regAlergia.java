package visual;

import javaBD.AlergiaBD;
import logico.Alergia;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class regAlergia extends JDialog {

	private final JPanel contentPanel =
			new JPanel();

	private JTextField txtNombre;
	private JComboBox<String> cbxTipo;

	public regAlergia() {

		setTitle("Registrar Nueva Alergia");
		setModal(true);
		setBounds(100, 100, 420, 240);
		setLocationRelativeTo(null);

		getContentPane().setLayout(
				new BorderLayout()
		);

		contentPanel.setBorder(
				new EmptyBorder(15, 15, 15, 15)
		);

		contentPanel.setLayout(null);
		getContentPane().add(
				contentPanel,
				BorderLayout.CENTER
		);

		JLabel lblNombre =
				new JLabel("Nombre:");

		lblNombre.setBounds(
				20, 25, 100, 25
		);

		contentPanel.add(lblNombre);

		txtNombre = new JTextField();
		txtNombre.setBounds(
				120, 25, 245, 25
		);
		contentPanel.add(txtNombre);

		JLabel lblTipo =
				new JLabel("Tipo:");

		lblTipo.setBounds(
				20, 70, 100, 25
		);

		contentPanel.add(lblTipo);

		cbxTipo = new JComboBox<>(
				new String[]{
						"<Seleccione>",
						"Alimento",
						"Medicamento",
						"Ambiental",
						"Animal",
						"Contacto"
				}
		);

		cbxTipo.setBounds(
				120, 70, 245, 25
		);

		contentPanel.add(cbxTipo);

		JPanel buttonPane =
				new JPanel(
						new FlowLayout(
								FlowLayout.RIGHT
						)
				);

		getContentPane().add(
				buttonPane,
				BorderLayout.SOUTH
		);

		JButton btnRegistrar =
				new JButton("Registrar");

		btnRegistrar.addActionListener(
				e -> registrarAlergia()
		);

		buttonPane.add(btnRegistrar);

		JButton btnCancelar =
				new JButton("Cancelar");

		btnCancelar.addActionListener(
				e -> dispose()
		);

		buttonPane.add(btnCancelar);

		getRootPane().setDefaultButton(
				btnRegistrar
		);
	}

	private void registrarAlergia() {

		String nombre =
				txtNombre.getText().trim();

		String tipo =
				cbxTipo.getSelectedItem()
						.toString();

		if (nombre.isEmpty()
				|| cbxTipo.getSelectedIndex() == 0) {

			JOptionPane.showMessageDialog(
					this,
					"Complete el nombre y seleccione el tipo.",
					"Campos incompletos",
					JOptionPane.WARNING_MESSAGE
			);

			return;
		}

		Alergia nuevaAlergia =
				new Alergia(nombre, tipo);

		/*
		 * =============================================================
		 * IMPLEMENTACIÓN ANTERIOR MEDIANTE ARCHIVOS / ARRAYLIST
		 * =============================================================
		 *
		 * Clinica.getInstance()
		 *         .registrarAlergias(nuevaAlergia);
		 *
		 * PersistenciaManager.guardarDatos();
		 *
		 * Este bloque queda documentado, pero no se ejecuta durante
		 * la migración del módulo a MySQL.
		 */

		/*
		 * =============================================================
		 * NUEVA IMPLEMENTACIÓN MEDIANTE BASE DE DATOS
		 * =============================================================
		 */
		boolean registrada =
				AlergiaBD.registrarAlergia(
						nuevaAlergia
				);

		if (!registrada) {

			JOptionPane.showMessageDialog(
					this,
					"No se pudo registrar la alergia.\n"
							+ "Revise que el nombre no esté repetido "
							+ "y que el tipo sea válido.",
					"Registro no realizado",
					JOptionPane.ERROR_MESSAGE
			);

			return;
		}

		JOptionPane.showMessageDialog(
				this,
				"Alergia registrada correctamente.",
				"Registro exitoso",
				JOptionPane.INFORMATION_MESSAGE
		);

		dispose();
	}
}
