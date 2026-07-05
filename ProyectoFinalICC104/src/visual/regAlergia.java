package visual;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import logico.Alergia;
import logico.Clinica;
import javax.swing.JComboBox;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.DefaultComboBoxModel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.awt.Color;

public class regAlergia extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtNombre;
	private JComboBox<String> cbxTipo;

	public static void main(String[] args) {
		try {
			regAlergia dialog = new regAlergia();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public regAlergia() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(regAlergia.class.getResource("/recursos/enfc.jpg")));
		setTitle("Registro de Alergia");
		setModal(true);
		setBounds(100, 100, 400, 250);
		setLocationRelativeTo(null);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBackground(new Color(255, 235, 205));
		contentPanel.setBorder(new EmptyBorder(20, 20, 20, 20));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);

		// ========== ALÉRGENO ==========
		JLabel lblAlergeno = new JLabel("Alérgeno:");
		lblAlergeno.setBounds(15, 15, 100, 20);
		contentPanel.add(lblAlergeno);

		txtNombre = new JTextField();
		txtNombre.setBounds(15, 38, 340, 26);
		contentPanel.add(txtNombre);
		txtNombre.setColumns(10);

		JLabel lblInfo = new JLabel("(Ejemplo: Polen, Penicilina, Maní, etc.)");
		lblInfo.setBounds(15, 67, 300, 20);
		contentPanel.add(lblInfo);

		// ========== TIPO ==========
		JLabel lblTipo = new JLabel("Tipo de alergia:");
		lblTipo.setBounds(15, 100, 150, 20);
		contentPanel.add(lblTipo);

		cbxTipo = new JComboBox<String>();
		cbxTipo.setModel(new DefaultComboBoxModel<String>(new String[] { 
			"<Seleccione>", 
			"Alimento", 
			"Medicamento", 
			"Ambiental", 
			"Animal", 
			"Contacto" 
		}));
		cbxTipo.setBounds(15, 123, 340, 26);
		contentPanel.add(cbxTipo);

		// ========== PANEL DE BOTONES ==========
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(240, 248, 255));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton btnRegistrar = new JButton("Registrar");
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				registrarAlergia();
			}
		});
		btnRegistrar.setActionCommand("OK");
		buttonPane.add(btnRegistrar);
		getRootPane().setDefaultButton(btnRegistrar);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}
		});
		btnCancelar.setActionCommand("Cancel");
		buttonPane.add(btnCancelar);
	}

	private boolean validarNombre(String texto) {
		// Solo letras, espacios y tildes
		if (!texto.matches("[a-záéíóúñüA-ZÁÉÍÓÚÑÜ ]+")) {
			JOptionPane.showMessageDialog(this,
					"El alérgeno solo puede contener letras y espacios.\nNo se permiten números ni caracteres especiales.",
					"Nombre inválido", JOptionPane.ERROR_MESSAGE);
			return false;
		}
		return true;
	}

	private void registrarAlergia() {
		// Validar campos vacíos
		if (txtNombre.getText().trim().isEmpty() || cbxTipo.getSelectedIndex() == 0) {
			JOptionPane.showMessageDialog(this, 
				"Complete todos los campos:\n\n• Alérgeno\n• Tipo de alergia",
				"Campos incompletos", 
				JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Validar nombre
		if (!validarNombre(txtNombre.getText().trim())) {
			txtNombre.requestFocus();
			return;
		}

		String nombreAlergia = txtNombre.getText().trim();

		// Validar duplicado
		if (!Clinica.getInstance().validarExistenciaAlergia(nombreAlergia)) {
			JOptionPane.showMessageDialog(this,
				"Esta alergia ya está registrada en el sistema.\n" + 
				"Alérgeno: " + nombreAlergia,
				"Alergia duplicada", 
				JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Registrar
		Alergia nuevaAlergia = new Alergia(nombreAlergia, cbxTipo.getSelectedItem().toString());
		Clinica.getInstance().registrarAlergias(nuevaAlergia);

		JOptionPane.showMessageDialog(this,
			"Alergia registrada exitosamente\n\n" +
			"Alérgeno: " + nombreAlergia + "\n" +
			"Tipo: " + cbxTipo.getSelectedItem().toString(),
			"Registro Exitoso", 
			JOptionPane.INFORMATION_MESSAGE);

		limpiarCampos();
		dispose();
	}

	private void limpiarCampos() {
		txtNombre.setText("");
		cbxTipo.setSelectedIndex(0);
	}
}