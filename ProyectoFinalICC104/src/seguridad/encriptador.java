package seguridad;

import org.mindrot.jbcrypt.BCrypt;

public class encriptador {

    // 1. Método para HASHEAR la contraseña antes de guardarla en MySQL
    public static String encriptarPassword(String passwordEnTextoPlano) {
        // BCrypt.gensalt() genera un salt aleatorio automático
        return BCrypt.hashpw(passwordEnTextoPlano, BCrypt.gensalt());
    }

    // 2. Método para VERIFICAR la contraseña durante el Login
    public static boolean verificarPassword(String passwordIngresada, String hashGuardadoEnBD) {
        if (hashGuardadoEnBD == null || !hashGuardadoEnBD.startsWith("$2a$")) {
            return false; // Por seguridad si el hash en la BD está corrupto
        }
        return BCrypt.checkpw(passwordIngresada, hashGuardadoEnBD);
    }
}