package visual;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import logico.Clinica;
import logico.Control;
import logico.PersistenciaManager;
import Server.Servidor;

import java.awt.Color;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import javax.swing.Timer;

public class Principal extends JFrame {

	private JPanel contentPane;
	private Timer autoGuardado;

	public static void main(String[] args) {
		// Iniciar servidor en thread separado
		Thread servidorThread = new Thread(() -> {
			Servidor.main(new String[] {});
		}, "ServidorAutoStarter");
		servidorThread.setDaemon(true);
		servidorThread.start();

		EventQueue.invokeLater(() -> {
			try {
				Principal frame = new Principal();
				frame.setVisible(true);
			} catch (Exception e) {
				e.printStackTrace();
			}
		});
	}

	public Principal() {
		setIconImage(Toolkit.getDefaultToolkit()
				.getImage(Principal.class.getResource("/recursos/adm.jpg")));

		setTitle(
				"Clínica - Principal ("
						+ (Control.esAdministrador() ? "ADMINISTRADOR"
								: Control.esDoctor() ? "DOCTOR: " + Control.getDoctorLogeado().getNombre() : "USUARIO")
						+ ")");

		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setExtendedState(JFrame.MAXIMIZED_BOTH);
		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		setSize(screen);
		setLocationRelativeTo(null);
		setResizable(true);

		// ===== CONFIGURAR GUARDADO AUTOMATICO =====
		configurarAutoGuardado();

		// ===== CONFIGURAR CIERRE DE VENTANA =====
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				cerrarAplicacion();
			}
		});

		JMenuBar menuBar = new JMenuBar();
		menuBar.setBackground(new Color(220, 220, 220));
		setJMenuBar(menuBar);

		// ========== MENÚ PACIENTES ==========
		JMenu mnPacientes = new JMenu("Pacientes");
		mnPacientes.setBackground(new Color(220, 220, 220));
		menuBar.add(mnPacientes);

		JMenuItem mntmRegPac = new JMenuItem("Registrar");
		mntmRegPac.addActionListener(e -> {
			regPaciente dialog = new regPaciente(null);
			dialog.setModal(true);
			dialog.setVisible(true);
			guardarDatosDespuesDeAccion();
		});
		mnPacientes.add(mntmRegPac);

		JMenuItem mntmListPac = new JMenuItem("Listar");
		mntmListPac.addActionListener(e -> {
			listPacientes dialog = new listPacientes();
			dialog.setModal(true);
			dialog.setVisible(true);
		});
		mnPacientes.add(mntmListPac);

		JMenuItem mntmVerHistorial = new JMenuItem("Ver Historial Clínico");
		mntmVerHistorial.addActionListener(e -> {
			VerHistorialClinico dialog = new VerHistorialClinico();
			dialog.setModal(true);
			dialog.setLocationRelativeTo(Principal.this);
			dialog.setVisible(true);
		});
		mnPacientes.add(mntmVerHistorial);

		// ========== MENÚ DOCTORES ==========
		JMenu mnDoctores = new JMenu("Doctores");
		mnDoctores.setBackground(new Color(220, 220, 220));
		menuBar.add(mnDoctores);

		JMenuItem mntmRegDoc = new JMenuItem("Registrar");
		mntmRegDoc.addActionListener(e -> {
			regDoctor dialog = new regDoctor();
			dialog.setModal(true);
			dialog.setVisible(true);
			guardarDatosDespuesDeAccion();
		});
		mnDoctores.add(mntmRegDoc);

		JMenuItem mntmListDoc = new JMenuItem("Listar");
		mntmListDoc.addActionListener(e -> {
			listDoctor dialog = new listDoctor();
			dialog.setModal(true);
			dialog.setVisible(true);
		});
		mnDoctores.add(mntmListDoc);

		// ========== MENÚ ENFERMEDADES ==========
		JMenu mnEnfermedades = new JMenu("Enfermedades");
		mnEnfermedades.setBackground(new Color(220, 220, 220));
		menuBar.add(mnEnfermedades);

		JMenuItem mntmRegEnf = new JMenuItem("Registrar");
		mntmRegEnf.addActionListener(e -> {
			regEnfermedades dialog = new regEnfermedades();
			dialog.setModal(true);
			dialog.setLocationRelativeTo(Principal.this);
			dialog.setVisible(true);
			guardarDatosDespuesDeAccion();
		});
		mnEnfermedades.add(mntmRegEnf);

		JMenuItem mntmListEnf = new JMenuItem("Listar");
		mntmListEnf.addActionListener(e -> {
			listEnfermedad dialog = new listEnfermedad();
			dialog.setModal(true);
			dialog.setLocationRelativeTo(Principal.this);
			dialog.setVisible(true);
		});
		mnEnfermedades.add(mntmListEnf);

		// ========== MENÚ VACUNAS ==========
		JMenu mnVacunas = new JMenu("Vacunas");
		mnVacunas.setBackground(new Color(220, 220, 220));
		menuBar.add(mnVacunas);

		JMenuItem mntmRegVac = new JMenuItem("Registrar");
		mntmRegVac.addActionListener(e -> {
			regVacuna vacunacion = new regVacuna();
			vacunacion.setModal(true);
			vacunacion.setVisible(true);
			guardarDatosDespuesDeAccion();
		});
		mnVacunas.add(mntmRegVac);

		JMenuItem mntmListVac = new JMenuItem("Listar");
		mntmListVac.addActionListener(e -> {
			listVacuna verVacuna = new listVacuna();
			verVacuna.setModal(true);
			verVacuna.setVisible(true);
		});
		mnVacunas.add(mntmListVac);

		JMenuItem mntmAdminVac = new JMenuItem("Administrar a paciente");
		mntmAdminVac.addActionListener(e -> {
			adminVacuna vacu = new adminVacuna();
			vacu.setModal(true);
			vacu.setVisible(true);
			guardarDatosDespuesDeAccion();
		});
		mnVacunas.add(mntmAdminVac);

		// ========== MENÚ CITAS ==========
		JMenu mnCitas = new JMenu("Citas");
		mnCitas.setBackground(new Color(220, 220, 220));
		menuBar.add(mnCitas);

		JMenuItem mntmAgendarCita = new JMenuItem("Agendar Cita");
		mntmAgendarCita.addActionListener(e -> {
			AgendarCita dialog = new AgendarCita();
			dialog.setModal(true);
			dialog.setVisible(true);
			guardarDatosDespuesDeAccion();
		});
		mnCitas.add(mntmAgendarCita);

		JMenuItem mntmListaCitas = new JMenuItem("Lista de Citas");
		mntmListaCitas.addActionListener(e -> {
			ListaCitas dialog = new ListaCitas();
			dialog.setModal(true);
			dialog.setVisible(true);
		});
		mnCitas.add(mntmListaCitas);

		// ========== MENÚ CONSULTAS ==========
		JMenu mnConsultas = new JMenu("Consultas");
		mnConsultas.setBackground(new Color(192, 192, 192));
		menuBar.add(mnConsultas);

		JMenuItem mntmRegConsulta = new JMenuItem("Registrar consulta");
		mntmRegConsulta.addActionListener(e -> {
			regConsulta dialog = new regConsulta();
			dialog.setLocationRelativeTo(Principal.this);
			dialog.setVisible(true);
			guardarDatosDespuesDeAccion();
		});
		mnConsultas.add(mntmRegConsulta);

		JMenuItem mntmListarConsultas = new JMenuItem("Listar consultas");
		mntmListarConsultas.addActionListener(e -> {
			listConsulta dialog = new listConsulta();
			dialog.setLocationRelativeTo(Principal.this);
			dialog.setVisible(true);
		});
		mnConsultas.add(mntmListarConsultas);

		// ========== MENÚ ADMINISTRACIÓN ==========
		JMenu mnAdministracion = new JMenu("Administración");
		mnAdministracion.setBackground(new Color(220, 220, 220));
		menuBar.add(mnAdministracion);

		JMenuItem mntmReportes = new JMenuItem("Reportes");
		mntmReportes.addActionListener(e -> {
			Reportes ventana = new Reportes();
			ventana.setVisible(true);
		});
		mnAdministracion.add(mntmReportes);

		JMenuItem mntmRegistrarUsuarios = new JMenuItem("Registrar usuarios");
		mntmRegistrarUsuarios.addActionListener(e -> {
			regUser dialog = new regUser();
			dialog.setModal(true);
			dialog.setVisible(true);
			guardarDatosDespuesDeAccion();
		});
		mnAdministracion.add(mntmRegistrarUsuarios);

		// ===== CONFIGURAR VISIBILIDAD SEGÚN USUARIO =====
		// Después de crear todos los menús y sus items, llamar:
		configurarMenusSegunUsuario(mnDoctores, mnAdministracion, mnPacientes, mnEnfermedades, mnVacunas, mnCitas,
				mnConsultas);

		// Panel de fondo
		contentPane = new PanelFondo();
		contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		// Botón de cerrar sesión
		JButton btnCerrarSesion = new JButton("Cerrar sesión");
		btnCerrarSesion.setBounds(1718, 937, 139, 43);
		contentPane.add(btnCerrarSesion);
		btnCerrarSesion.setBackground(new Color(240, 230, 140));
		btnCerrarSesion.addActionListener(e -> cerrarSesion());
	}

	// ===== MÉTODOS DE GUARDADO =====
	private void configurarAutoGuardado() {
		// Guardar automáticamente cada 2 minutos
		autoGuardado = new Timer(120000, e -> {
			PersistenciaManager.guardarDatos();
			System.out.println("[AUTO-GUARDADO] Datos guardados automáticamente");
		});
		autoGuardado.start();
	}

	private void guardarDatosDespuesDeAccion() {
		PersistenciaManager.guardarDatos();
		System.out.println("[GUARDADO] Datos guardados después de acción");
	}

	private void configurarMenusSegunUsuario(JMenu mnDoctores, JMenu mnAdministracion, JMenu mnPacientes,
			JMenu mnEnfermedades, JMenu mnVacunas, JMenu mnCitas, JMenu mnConsultas) {

		if (Control.getLoginUser() != null) {
			if (Control.esAdministrador()) {
				// ADMINISTRADOR: Solo ve Doctores y Administración
				mnDoctores.setVisible(true);
				mnAdministracion.setVisible(true);

				// Ocultar menús que el admin NO debe ver
				mnPacientes.setVisible(false);
				mnEnfermedades.setVisible(false);
				mnVacunas.setVisible(false);
				mnCitas.setVisible(false);
				mnConsultas.setVisible(false);

				System.out.println("Usuario ADMINISTRADOR logeado");

			} else if (Control.esDoctor()) {
				// DOCTOR: Solo ve lo necesario para su trabajo
				mnPacientes.setVisible(true);
				mnEnfermedades.setVisible(true);
				mnVacunas.setVisible(true);
				mnCitas.setVisible(true);
				mnConsultas.setVisible(true);

				// Ocultar menús de administración
				mnDoctores.setVisible(false);
				mnAdministracion.setVisible(false);

				System.out.println("Usuario DOCTOR logeado");

			} else {
				JOptionPane.showMessageDialog(this, "Tipo de usuario no reconocido", "Error",
						JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	private void cerrarSesion() {
		int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de cerrar sesión?",
				"Confirmar cierre de sesión", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

		if (confirmacion == JOptionPane.YES_OPTION) {
			// Detener auto-guardado
			if (autoGuardado != null) {
				autoGuardado.stop();
			}

			// Guardar datos
			PersistenciaManager.guardarDatos();
			System.out.println("[CIERRE SESIÓN] Datos guardados");

			// Limpiar usuario
			Control.clearLoginUser();

			// Cerrar ventana
			dispose();

			// Volver a login
			login inicio = new login();
			inicio.setVisible(true);
		}
	}

	private void cerrarAplicacion() {
		int confirmacion = JOptionPane.showConfirmDialog(this, "¿Está seguro de salir de la aplicación?",
				"Confirmar salida", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

		if (confirmacion == JOptionPane.YES_OPTION) {
			// Detener auto-guardado
			if (autoGuardado != null) {
				autoGuardado.stop();
			}

			// Guardar datos
			PersistenciaManager.guardarDatos();
			System.out.println("[CIERRE] Datos guardados");

			// Realizar respaldo
			realizarRespaldo();

			// Detener servidor
			Servidor.detenerServidor();

			// Salir
			System.exit(0);
		}
	}

	private void realizarRespaldo() {
		try {
			java.net.Socket sfd = new java.net.Socket("127.0.0.1", 7000);
			java.io.DataInputStream aux = new java.io.DataInputStream(
					new java.io.FileInputStream(new java.io.File("control.dat")));
			java.io.DataOutputStream salidaSocket = new java.io.DataOutputStream(sfd.getOutputStream());

			int unByte;
			while ((unByte = aux.read()) != -1) {
				salidaSocket.write(unByte);
				salidaSocket.flush();
			}

			aux.close();
			salidaSocket.close();
			sfd.close();
			System.out.println("[RESPALDO] Respaldo completado");
		} catch (Exception e) {
			System.err.println("Error en respaldo: " + e.getMessage());
		}
	}
}