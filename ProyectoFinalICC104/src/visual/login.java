package visual;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import logico.Control;
import logico.Clinica;
import logico.PersistenciaManager;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.ObjectInputStream;
import java.awt.event.ActionEvent;
import java.awt.Toolkit;
import java.awt.Color;

public class login extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JPasswordField textField_1;

	public static void main(String[] args) {
		// Iniciar servidor de respaldo
		new Thread(() -> {
			Server.Servidor.main(new String[] {});
		}, "ServidorAutoStarter").start();

		EventQueue.invokeLater(() -> {
			try {
				login frame = new login();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public login() {
		setIconImage(Toolkit.getDefaultToolkit().getImage(login.class.getResource("/recursos/doc.jpg")));
		setTitle("Login - Cl?nica");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 340, 260);
		setLocationRelativeTo(null);

		contentPane = new JPanel();
		contentPane.setBackground(new Color(211, 211, 211));
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JLabel lblUsuario = new JLabel("Usuario:");
		lblUsuario.setBounds(20, 30, 100, 20);
		contentPane.add(lblUsuario);

		textField = new JTextField();
		textField.setBounds(20, 55, 280, 24);
		contentPane.add(textField);

		JLabel lblPass = new JLabel("Contrasena:");
		lblPass.setBounds(20, 90, 100, 20);
		contentPane.add(lblPass);

		textField_1 = new JPasswordField();
		textField_1.setBounds(20, 115, 280, 24);
		contentPane.add(textField_1);

		JButton btnLogin = new JButton("Iniciar sesion");
		btnLogin.setBackground(new Color(240, 230, 140));
		btnLogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				realizarLogin();
			}
		});
		btnLogin.setBounds(20, 160, 135, 28);
		contentPane.add(btnLogin);

		JButton btnSalir = new JButton("Salir");
		btnSalir.setBackground(new Color(255, 239, 213));
		btnSalir.addActionListener(e -> System.exit(0));
		btnSalir.setBounds(165, 160, 135, 28);
		contentPane.add(btnSalir);

		// Permitir enter para login
		getRootPane().setDefaultButton(btnLogin);

		// ===== CARGAR DATOS AL INICIAR =====
		cargarSistema();
	}

	private void cargarSistema() {
		System.out.println("=== INICIANDO CARGA DEL SISTEMA ===");

		File archivoControl = new File("control.dat");
		File archivoClinica = new File("clinica.dat");

		boolean cargaExitosa = false;

		// Intentar cargar Control
		if (archivoControl.exists()) {
			System.out.println("Archivo control.dat encontrado. Cargando...");
			try (FileInputStream fis = new FileInputStream(archivoControl);
					ObjectInputStream ois = new ObjectInputStream(fis)) {

				Control temp = (Control) ois.readObject();
				Control.setControl(temp);

				System.out.println("Control cargado exitosamente");
				System.out.println("Usuarios en sistema: " + Control.getInstance().getMisUsers().size());
				cargaExitosa = true;

			} catch (Exception ex) {
				System.err.println("Error cargando control.dat: " + ex.getMessage());
				ex.printStackTrace();
			}
		}

		// Intentar cargar Cl?nica
		if (archivoClinica.exists()) {
			System.out.println("Archivo clinica.dat encontrado. Cargando...");
			try (FileInputStream fis = new FileInputStream(archivoClinica);
					ObjectInputStream ois = new ObjectInputStream(fis)) {

				Clinica temp = (Clinica) ois.readObject();
				Clinica.setInstance(temp);

				System.out.println("Cl?nica cargada exitosamente");
				System.out.println("Pacientes: " + Clinica.getInstance().getPacientes().size());
				System.out.println("Doctores: " + Clinica.getInstance().getDoctores().size());

			} catch (Exception ex) {
				System.err.println("Error cargando clinica.dat: " + ex.getMessage());
				ex.printStackTrace();
			}
		}

		// Si no se carg? nada, crear admin por defecto
		if (!cargaExitosa) {
			System.out.println("No se encontraron archivos. Creando admin por defecto...");
			PersistenciaManager.crearAdminPorDefecto();
		}

		System.out.println("=== SISTEMA CARGADO ===");
		mostrarEstadisticasSistema();
	}

	private void mostrarEstadisticasSistema() {
		System.out.println("\n--- ESTAD?STICAS DEL SISTEMA ---");
		System.out.println("Usuarios: " + Control.getInstance().getMisUsers().size());
		System.out.println("Doctores: " + Clinica.getInstance().getDoctores().size());
		System.out.println("Pacientes: " + Clinica.getInstance().getPacientes().size());
		System.out.println("Citas: " + Clinica.getInstance().getCitas().size());
		System.out.println("--------------------------------\n");
	}

	private void realizarLogin() {
		String usuario = textField.getText().trim();
		String password = new String(textField_1.getPassword()).trim();

		if (usuario.isEmpty() || password.isEmpty()) {
			JOptionPane.showMessageDialog(this, "Ingrese usuario y contrase?a", "Campos vac?os",
					JOptionPane.WARNING_MESSAGE);
			return;
		}

		System.out.println("Intentando login: " + usuario);

		if (Control.getInstance().confirmLogin(usuario, password)) {
			System.out.println("Login exitoso: " + usuario);

			// Mostrar informaci?n de sesi?n
			if (Control.esAdministrador()) {
				System.out.println("TIPO: Administrador");
			} else if (Control.esDoctor()) {
				System.out.println("TIPO: Doctor");
				System.out.println("Doctor: " + Control.getDoctorLogeado().getNombre());
			}

			// Abrir ventana principal
			Principal frame = new Principal();
			dispose();
			frame.setVisible(true);

		} else {
			System.out.println("Login fallido: " + usuario);
			JOptionPane.showMessageDialog(this, "Usuario o contrase?a incorrectos", "Error de autenticaci?n",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}