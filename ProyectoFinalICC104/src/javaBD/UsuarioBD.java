package javaBD;

import seguridad.encriptador;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioBD {

    // --- ATRIBUTOS ---
    private int id_usuario;
    private String tipo;
    private String username;
    private String password_hash;
    private boolean activo;
    private Integer id_doctor; // Se usa Integer porque permite guardar 'null'

    // --- CONSTRUCTOR ---
    public UsuarioBD(int id_usuario, String tipo, String username, String password_hash, boolean activo, Integer id_doctor) {
        this.id_usuario = id_usuario;
        this.tipo = tipo;
        this.username = username;
        this.password_hash = password_hash;
        this.activo = activo;
        this.id_doctor = id_doctor;
    }

    // --- GETTERS ---
    public int getId_usuario() {
        return id_usuario;
    }

    public String getTipo() {
        return tipo;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword_hash() {
        return password_hash;
    }

    public boolean isActivo() {
        return activo;
    }

    public Integer getId_doctor() {
        return id_doctor;
    }

    /**
     * Valida las credenciales de acceso de un usuario consultando la base de datos.
     * Realiza una búsqueda por nombre de usuario de forma estricta (distinguiendo mayúsculas/minúsculas)
     * y verifica la contraseña ingresada contra el hash de seguridad almacenado.
     */
    public static UsuarioBD login(String username, String passwordIngresada) {

        // 1. Buscamos en la BD ÚNICAMENTE por el username (y que esté activo)
        String sql = "SELECT * FROM usuario WHERE BINARY username = ? AND activo = TRUE";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {

                    // 2. Traemos el Hash de 60 caracteres guardado en MySQL
                    String hashBD = rs.getString("password_hash");

                    // 3. Verificamos la contraseña en texto plano contra el Hash
                    if (encriptador.verificarPassword(passwordIngresada, hashBD)) {

                        // Verificar si id_doctor viene nulo en MySQL
                        Integer idDocFinal = null;
                        if (rs.getObject("id_doctor") != null) {
                            idDocFinal = rs.getInt("id_doctor");
                        }

                        // ¡Contraseña correcta! Retornamos el objeto con sus datos
                        return new UsuarioBD(
                                rs.getInt("id_usuario"),
                                rs.getString("tipo"),
                                rs.getString("username"),
                                hashBD,
                                rs.getBoolean("activo"),
                                idDocFinal
                        );
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error al intentar iniciar sesión: " + e.getMessage());
        }

        // Si el usuario no existe o la contraseña no coincidió
        return null;
    }

    /**
     * Registra un nuevo usuario en la base de datos aplicando un hash seguro a su contraseña.
     * Retorna true si la inserción es exitosa o false en caso de error o duplicidad.
     */
    public static boolean registrar(String username, String password, String tipo) {
        String sql = "INSERT INTO usuario (username, password_hash, tipo, activo) VALUES (?, ?, ?, TRUE)";

        String passwordHash = encriptador.encriptarPassword(password);

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, passwordHash);
            ps.setString(3, tipo);

            // Guardamos cuántas filas se insertaron
            int filasAfectadas = ps.executeUpdate();

            // Evaluamos claramente con if/else
            if (filasAfectadas > 0) {
                return true;  // Se guardó exitosamente
            } else {
                return false; // No se modificó ninguna fila
            }

        } catch (Exception e) {
            System.out.println("Error al registrar en BD: " + e.getMessage());
            return false; // Si ocurre una excepción (ej: usuario duplicado), devuelve false
        }
    }


    /**
     * Consulta y retorna el tipo o rol asociado a un nombre de usuario específico en la base de datos.
     */
    public static String tipoUsuario(String username) {

        String tipo = null;
        String sql = "SELECT tipo FROM usuario WHERE username = ?";

        try(Connection conexion = ConexionBD.conectar(); PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, username);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    tipo = rs.getString("tipo");
                }

            }

        } catch (SQLException e) {
            System.err.println("Error al obtener el tipo de usuario: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return tipo;

    }


    }