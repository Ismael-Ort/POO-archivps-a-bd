package visual;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import logico.Control;
import logico.User;

public class regUser extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField txtUserName;
	private JPasswordField txtPass;

	public regUser() {
		getContentPane().setBackground(new Color(224, 255, 255));
		configurarVentana();
		crearComponentes();
	}

	private void configurarVentana() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(regUser.class.getResource("/recursos/pac.jpg")));
		setTitle("Registrar Usuario");
		setBounds(100, 100, 550, 400);
		setLocationRelativeTo(null);
		setResizable(false);
		getContentPane().setLayout(new BorderLayout(10, 10));
	}

	private void crearComponentes() {
		contentPanel.setBackground(new Color(224, 255, 255));
		// Panel principal con borde
		contentPanel.setBorder(new EmptyBorder(20, 30, 20, 30));
		contentPanel.setLayout(null);
		getContentPane().add(contentPanel, BorderLayout.CENTER);

		// ===== SECCIÓN DATOS DEL USUARIO =====
		JPanel panelDatos = new JPanel();
		panelDatos.setBackground(new Color(240, 255, 255));
		panelDatos.setBorder(new TitledBorder(null, "Datos del Usuario", TitledBorder.LEADING, TitledBorder.TOP,
				new Font("Tahoma", Font.BOLD, 13), new Color(0, 0, 0)));
		panelDatos.setBounds(20, 20, 490, 140);
		panelDatos.setLayout(null);
		contentPanel.add(panelDatos);

		// Nombre de usuario
		JLabel lblNombreUsuario = new JLabel("Nombre de usuario:");
		lblNombreUsuario.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblNombreUsuario.setBounds(30, 35, 160, 25);
		panelDatos.add(lblNombreUsuario);

		txtUserName = new JTextField();
		txtUserName.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtUserName.setBounds(30, 65, 200, 30);
		panelDatos.add(txtUserName);

		// Contraseña
		JLabel lblPass = new JLabel("Contraseña:");
		lblPass.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblPass.setBounds(270, 35, 160, 25);
		panelDatos.add(lblPass);

		txtPass = new JPasswordField();
		txtPass.setFont(new Font("Tahoma", Font.PLAIN, 13));
		txtPass.setBounds(270, 65, 200, 30);
		panelDatos.add(txtPass);

		// ===== SECCIÓN TIPO DE USUARIO =====
		JPanel panelTipo = new JPanel();
		panelTipo.setBackground(new Color(240, 255, 255));
		panelTipo.setBorder(new TitledBorder(null, "Tipo de Usuario", TitledBorder.LEADING, TitledBorder.TOP,
				new Font("Tahoma", Font.BOLD, 13), new Color(0, 0, 0)));
		panelTipo.setBounds(20, 180, 230, 90);
		panelTipo.setLayout(null);
		contentPanel.add(panelTipo);

		JLabel lblAdmin = new JLabel("ADMINISTRADOR");
		lblAdmin.setFont(new Font("Tahoma", Font.BOLD, 16));
		lblAdmin.setForeground(new Color(0, 102, 204));
		lblAdmin.setBounds(40, 35, 180, 30);
		panelTipo.add(lblAdmin);

		// ===== SECCIÓN INFORMACIÓN =====
		JPanel panelInfo = new JPanel();
		panelInfo.setBackground(new Color(240, 255, 255));
		panelInfo.setBorder(new TitledBorder(null, "Información", TitledBorder.LEADING, TitledBorder.TOP,
				new Font("Tahoma", Font.BOLD, 13), new Color(0, 0, 0)));
		panelInfo.setBounds(270, 180, 240, 90);
		panelInfo.setLayout(null);
		contentPanel.add(panelInfo);

		JLabel lblInfo1 = new JLabel("Para registrar un doctor");
		lblInfo1.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblInfo1.setBounds(20, 25, 200, 20);
		panelInfo.add(lblInfo1);

		JLabel lblInfo2 = new JLabel("diríjase al menú:");
		lblInfo2.setFont(new Font("Tahoma", Font.PLAIN, 12));
		lblInfo2.setBounds(20, 45, 200, 20);
		panelInfo.add(lblInfo2);

		JLabel lblDoctores = new JLabel("\"DOCTORES\"");
		lblDoctores.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblDoctores.setForeground(new Color(0, 102, 204));
		lblDoctores.setBounds(60, 65, 150, 20);
		panelInfo.add(lblDoctores);

		// Panel de botones
		crearBotones();
	}

	private void crearBotones() {
		JPanel buttonPane = new JPanel();
		buttonPane.setBackground(new Color(224, 255, 255));
		buttonPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT, 15, 10));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton btnRegistrar = new JButton("Registrar");
		btnRegistrar.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnRegistrar.setBackground(new Color(0, 153, 76));
		btnRegistrar.setForeground(Color.WHITE);
		btnRegistrar.setFocusPainted(false);
		btnRegistrar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				registrarUsuario();
			}
		});
		buttonPane.add(btnRegistrar);

		JButton btnCancelar = new JButton("Cancelar");
		btnCancelar.setFont(new Font("Tahoma", Font.BOLD, 13));
		btnCancelar.setBackground(new Color(220, 53, 69));
		btnCancelar.setForeground(Color.WHITE);
		btnCancelar.setFocusPainted(false);
		btnCancelar.addActionListener(e -> dispose());
		buttonPane.add(btnCancelar);
	}

	private void registrarUsuario() {
		String user = txtUserName.getText().trim();
		String pass = new String(txtPass.getPassword()).trim();

		// Validar campos vacíos
		if (user.isEmpty() || pass.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Complete todos los campos.", "Datos incompletos",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Validar si el usuario ya existe
		if (Control.getInstance().existeUsuario(user)) {
			JOptionPane.showMessageDialog(this,
					"El nombre de usuario '" + user + "' ya está en uso.\n"
							+ "Por favor, elija otro nombre de usuario.",
					"Usuario duplicado", JOptionPane.ERROR_MESSAGE);
			return;
		}

		// Validar longitud mínima de contraseña
		if (pass.length() < 4) {
			JOptionPane.showMessageDialog(this, "La contraseña debe tener al menos 4 caracteres.",
					"Contraseña muy corta", JOptionPane.WARNING_MESSAGE);
			return;
		}

		// Crear y registrar usuario
		User nuevo = new User("Administrador", user, pass);
		boolean registrado = Control.getInstance().regUser(nuevo);

		if (registrado) {
			// Guardar los datos
			logico.PersistenciaManager.guardarDatos();

			JOptionPane.showMessageDialog(this, "Usuario administrador registrado exitosamente.", "Registro exitoso",
					JOptionPane.INFORMATION_MESSAGE);
			dispose();
		} else {
			JOptionPane.showMessageDialog(this,
					"No se pudo registrar el usuario.\n" + "El nombre de usuario ya existe.", "Error de registro",
					JOptionPane.ERROR_MESSAGE);
		}
	}

	private void limpiarCampos() {
		txtUserName.setText("");
		txtPass.setText("");
	}
}