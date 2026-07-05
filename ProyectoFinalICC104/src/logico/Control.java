package logico;

import java.io.Serializable;
import java.util.ArrayList;

public class Control implements Serializable {
	private static final long serialVersionUID = 1L;
	private ArrayList<User> misUsers;
	private static Control control;
	private static User loginUser;
	private static Doctor doctorLogeado; // NUEVO: Referencia al doctor logeado

	private Control() {
		misUsers = new ArrayList<>();
		doctorLogeado = null;
	}

	public static Control getInstance() {
		if (control == null) {
			control = new Control();
		}
		return control;
	}

	public static void setControl(Control control) {
		Control.control = control;
	}

	public static User getLoginUser() {
		return loginUser;
	}

	public static void clearLoginUser() {
		loginUser = null;
		doctorLogeado = null; // Limpiar tambi?n al doctor
	}

	// ========== REGISTRO DE USUARIOS ==========

	public boolean regUser(User user) {
		if (user == null) {
			return false;
		}

		// Validar que el nombre de usuario no exista SOLO EN USUARIOS NORMALES
		for (User u : misUsers) {
			if (u.getUserName().equalsIgnoreCase(user.getUserName().trim())) {
				return false;
			}
		}

		misUsers.add(user);
		return true;
	}

	public boolean existeUsuario(String nombreUsuario) {
		if (nombreUsuario == null || nombreUsuario.trim().isEmpty()) {
			return false;
		}

		String usuarioBuscado = nombreUsuario.trim();

		// Buscar en usuarios normales
		for (User user : misUsers) {
			if (user.getUserName().equalsIgnoreCase(usuarioBuscado)) {
				return true;
			}
		}

		// Buscar en doctores
		for (Doctor doctor : Clinica.getInstance().getDoctores()) {
			if (doctor.getUsuario() != null && doctor.getUsuario().equalsIgnoreCase(usuarioBuscado)) {
				return true;
			}
		}

		return false;
	}

	// ========== LOGIN MEJORADO ==========

	/**
	 * Login mejorado que almacena tanto el User como el Doctor (si aplica)
	 */
	public boolean confirmLogin(String username, String password) {
		// Resetear usuario anterior
		loginUser = null;
		doctorLogeado = null;

		if (username == null || password == null) {
			return false;
		}

		String user = username.trim();
		String pass = password.trim();

		// 1. Buscar en usuarios normales (Admins, etc.)
		for (User u : misUsers) {
			if (u.getUserName().equals(user) && u.getPass().equals(pass)) {
				loginUser = u;
				System.out.println("Login exitoso: " + user + " (tipo: " + u.getTipo() + ")");
				return true;
			}
		}

		// 2. Buscar en doctores registrados
		for (Doctor doctor : Clinica.getInstance().getDoctores()) {
			if (doctor.getUsuario() != null && doctor.getUsuario().equals(user) && doctor.getContrasena() != null
					&& doctor.getContrasena().equals(pass)) {

				// Crear un User temporal tipo "Doctor"
				loginUser = new User("Doctor", user, pass);
				doctorLogeado = doctor; // ALMACENAR LA REFERENCIA AL DOCTOR

				System.out.println("Login exitoso: " + user + " (Doctor: " + doctor.getNombre() + ")");
				System.out.println("Licencia: " + doctor.getNumeroLicencia());
				return true;
			}
		}

		System.out.println("Login fallido para: " + user);
		return false;
	}

	public boolean confirmLoginAdmin(String username, String password) {
		for (User user : misUsers) {
			if (user.getUserName().equals(username) && user.getPass().equals(password)
					&& user.getTipo().equals("Administrador")) {
				loginUser = user;
				doctorLogeado = null;
				return true;
			}
		}
		return false;
	}

	// ========== M?TODOS DE VERIFICACI?N DE TIPO ==========

	public static boolean esAdministrador() {
		return loginUser != null && "Administrador".equals(loginUser.getTipo());
	}

	public static boolean esDoctor() {
		return loginUser != null && "Doctor".equals(loginUser.getTipo());
	}

	// ========== NUEVOS M?TODOS MEJORADOS ==========

	/**
	 * Obtiene el objeto Doctor completo del usuario logeado Versi?n mejorada: Usa
	 * la referencia almacenada
	 */
	public static Doctor getDoctorLogeado() {
		// Si ya tenemos la referencia almacenada, devolverla
		if (doctorLogeado != null) {
			return doctorLogeado;
		}

		// Si no, buscarlo
		if (!esDoctor()) {
			return null;
		}

		String usuario = loginUser.getUserName();

		// Buscar doctor que coincida con el usuario logeado
		for (Doctor doctor : Clinica.getInstance().getDoctores()) {
			if (doctor != null && doctor.getUsuario() != null && usuario.equals(doctor.getUsuario())) {
				doctorLogeado = doctor; // Almacenar para futuras consultas
				return doctor;
			}
		}

		System.err.println("ERROR: No se encontr? doctor para usuario: " + usuario);
		return null;
	}

	/**
	 * Obtiene la licencia del doctor logeado
	 */
	public static String getLicenciaDoctorLogeado() {
		Doctor doctor = getDoctorLogeado();
		if (doctor != null) {
			return doctor.getNumeroLicencia();
		}
		return null;
	}

	/**
	 * Verifica si el usuario actual puede ver un paciente - Admin: ve todos -
	 * Doctor: solo ve pacientes que ?l registr?
	 */
	public static boolean puedeVerPaciente(Paciente paciente) {
		if (paciente == null)
			return false;

		// Admin ve todo
		if (esAdministrador()) {
			return true;
		}

		// Doctor solo ve sus pacientes
		if (esDoctor()) {
			Doctor doctor = getDoctorLogeado();
			if (doctor != null && paciente.getDoctorRegistrador() != null) {
				return paciente.getDoctorRegistrador().equals(doctor.getNumeroLicencia());
			}
		}

		return false;
	}

	/**
	 * Verifica si el usuario actual puede ver una consulta - Admin: ve todo -
	 * Doctor: ve sus consultas + consultas de enfermedades vigiladas
	 */
	public static boolean puedeVerConsulta(Consulta consulta) {
		if (consulta == null)
			return false;

		// Admin ve todo
		if (esAdministrador()) {
			return true;
		}

		// Doctor
		if (esDoctor()) {
			Doctor doctor = getDoctorLogeado();
			if (doctor != null && consulta.getDoctor() != null) {
				// 1. Si es consulta propia
				boolean esPropia = consulta.getDoctor().getNumeroLicencia().equals(doctor.getNumeroLicencia());

				// 2. Si es enfermedad bajo vigilancia
				boolean esVigilancia = consulta.isEsEnfermedadVigilancia();

				// 3. Si est? marcada para resumen (opcional)
				boolean enResumen = consulta.isIncluidaEnResumen();

				return esPropia || esVigilancia || enResumen;
			}
		}

		return false;
	}

	// ========== GETTERS Y SETTERS ==========

	public ArrayList<User> getMisUsers() {
		return misUsers;
	}

	public void setMisUsers(ArrayList<User> misUsers) {
		this.misUsers = misUsers;
	}
}