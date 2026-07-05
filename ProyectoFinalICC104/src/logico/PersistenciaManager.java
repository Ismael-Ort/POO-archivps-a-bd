package logico;

import java.io.*;

public class PersistenciaManager {

	/**
	 * Guarda TODOS los datos del sistema (Control + Clínica)
	 */
	public static void guardarDatos() {
		guardarControl();
		guardarClinica();
		System.out.println("[PERSISTENCIA] Datos guardados completamente");
	}

	/**
	 * Guarda el objeto Control (usuarios, login)
	 */
	private static void guardarControl() {
		try (FileOutputStream fos = new FileOutputStream("control.dat");
				ObjectOutputStream oos = new ObjectOutputStream(fos)) {

			oos.writeObject(Control.getInstance());
			System.out.println("[GUARDADO] control.dat - Usuarios: " + Control.getInstance().getMisUsers().size());

		} catch (Exception e) {
			System.err.println("Error guardando control.dat: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Guarda el objeto Clínica (pacientes, doctores, citas, etc.)
	 */
	private static void guardarClinica() {
		try (FileOutputStream fos = new FileOutputStream("clinica.dat");
				ObjectOutputStream oos = new ObjectOutputStream(fos)) {

			oos.writeObject(Clinica.getInstance());
			System.out.println("[GUARDADO] clinica.dat - Pacientes: " + Clinica.getInstance().getPacientes().size()
					+ ", Doctores: " + Clinica.getInstance().getDoctores().size());

		} catch (Exception e) {
			System.err.println("Error guardando clinica.dat: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Carga TODOS los datos del sistema
	 */
	public static boolean cargarDatos() {
		boolean controlCargado = cargarControl();
		boolean clinicaCargada = cargarClinica();

		return controlCargado || clinicaCargada;
	}

	/**
	 * Carga el objeto Control
	 */
	private static boolean cargarControl() {
		File archivo = new File("control.dat");

		if (!archivo.exists()) {
			System.out.println("Archivo control.dat no existe");
			return false;
		}

		try (FileInputStream fis = new FileInputStream(archivo); ObjectInputStream ois = new ObjectInputStream(fis)) {

			Control temp = (Control) ois.readObject();
			Control.setControl(temp);

			System.out.println("[CARGA] control.dat - Usuarios: " + Control.getInstance().getMisUsers().size());
			return true;

		} catch (IOException | ClassNotFoundException ex) {
			System.err.println("Error cargando control.dat: " + ex.getMessage());
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * Carga el objeto Clínica
	 */
	private static boolean cargarClinica() {
		File archivo = new File("clinica.dat");

		if (!archivo.exists()) {
			System.out.println("Archivo clinica.dat no existe");
			return false;
		}

		try (FileInputStream fis = new FileInputStream(archivo); ObjectInputStream ois = new ObjectInputStream(fis)) {

			Clinica temp = (Clinica) ois.readObject();
			Clinica.setInstance(temp);

			System.out.println("[CARGA] clinica.dat - Pacientes: " + Clinica.getInstance().getPacientes().size()
					+ ", Doctores: " + Clinica.getInstance().getDoctores().size());
			return true;

		} catch (IOException | ClassNotFoundException ex) {
			System.err.println("Error cargando clinica.dat: " + ex.getMessage());
			ex.printStackTrace();
			return false;
		}
	}

	/**
	 * Crea admin por defecto y guarda
	 */
	public static void crearAdminPorDefecto() {
		try {
			// Crear admin por defecto
			User admin = new User("Administrador", "Admin", "Admin");
			Control.getInstance().regUser(admin);

			// Guardar inmediatamente
			guardarDatos();

			System.out.println("[SISTEMA] Admin por defecto creado y guardado");

		} catch (Exception e) {
			System.err.println("Error creando admin: " + e.getMessage());
			e.printStackTrace();
		}
	}

	/**
	 * Verifica la integridad de los archivos
	 */
	public static void verificarIntegridad() {
		File control = new File("control.dat");
		File clinica = new File("clinica.dat");

		System.out.println("\n=== VERIFICACIÓN DE ARCHIVOS ===");
		System.out.println(
				"control.dat: " + (control.exists() ? "EXISTE (" + control.length() + " bytes)" : "NO EXISTE"));
		System.out.println(
				"clinica.dat: " + (clinica.exists() ? "EXISTE (" + clinica.length() + " bytes)" : "NO EXISTE"));
		System.out.println("================================\n");
	}
}