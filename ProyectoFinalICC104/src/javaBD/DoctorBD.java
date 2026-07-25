package javaBD;

import logico.Doctor;
import seguridad.encriptador;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DoctorBD {

    /**
     * Registra de forma transaccional un nuevo doctor y su respectivo usuario en la base de datos.
     * Aplica control transaccional (rollback) para garantizar que, si ocurre un fallo al crear el usuario,
     * se revierta también la inserción del doctor y mantener la integridad referencial.
     */
// =========================================================================
//  MÉTODO PARA REGISTRAR DOCTOR + USUARIO EN BD
// =========================================================================
    public static boolean registrarDoctor(Doctor doctor) {

        String sqlDoctor = "INSERT INTO doctor (codigo_doctor, nombres, apellidos, cedula, telefono, direccion, fecha_nacimiento, sexo, especialidad, numero_licencia ,citas_por_dia, horario_inicio, horario_fin, activo) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String sqlUsuario = "INSERT INTO usuario (username, password_hash, tipo, activo, id_doctor) VALUES (?, ?, 'DOCTOR', TRUE, ?)";

        Connection conexion = null;

        try {
            conexion = ConexionBD.conectar();

            // Iniciar Transacción (para que si falla el usuario, se revierta el doctor)
            conexion.setAutoCommit(false);

            int idDoctorGenerado = -1;

            // -----------------------------------------------------------------
            // PASO 1: Insertar el registro en la tabla 'doctor'
            // -----------------------------------------------------------------
            try (PreparedStatement psDoc = conexion.prepareStatement(sqlDoctor, Statement.RETURN_GENERATED_KEYS)) {

                psDoc.setString(1, doctor.getCodigoDoctor());
                psDoc.setString(2, doctor.getNombre());
                psDoc.setString(3, doctor.getApellido());
                psDoc.setString(4, doctor.getCedula());
                psDoc.setString(5, doctor.getTelefono());
                psDoc.setString(6, doctor.getDireccion());

                // Conversión de LocalDate de Java a java.sql.Date
                psDoc.setDate(7, Date.valueOf(doctor.getFechaNacimiento()));
                psDoc.setString(8, String.valueOf(doctor.getSexo()));

                psDoc.setString(9, doctor.getEspecialidad());
                psDoc.setString(10, doctor.getNumeroLicencia());
                psDoc.setInt(11, doctor.getCitasPorDia());

                // Conversión de LocalTime de Java a java.sql.Time
                psDoc.setTime(12, Time.valueOf(doctor.getHorarioInicio()));
                psDoc.setTime(13, Time.valueOf(doctor.getHorarioFin()));
                psDoc.setBoolean(14, doctor.isActivo());

                int filas = psDoc.executeUpdate();

                if (filas > 0) {
                    // Obtener la Clave Primaria (id_doctor AUTO_INCREMENT) generada en MySQL
                    try (ResultSet rsKey = psDoc.getGeneratedKeys()) {
                        if (rsKey.next()) {
                            idDoctorGenerado = rsKey.getInt(1);
                        }
                    }
                }
            }

            // Si el doctor no se pudo insertar, abortamos
            if (idDoctorGenerado == -1) {
                conexion.rollback();
                return false;
            }

            // -----------------------------------------------------------------
            // PASO 2: Insertar las credenciales en la tabla 'usuario'
            // -----------------------------------------------------------------
            try (PreparedStatement psUser = conexion.prepareStatement(sqlUsuario)) {

                // Encriptar la contraseña que viene en el objeto Doctor
                String hashPass = encriptador.encriptarPassword(doctor.getContrasena());

                psUser.setString(1, doctor.getUsuario());
                psUser.setString(2, hashPass);
                psUser.setInt(3, idDoctorGenerado); // <-- Relación Foreign Key

                psUser.executeUpdate();
            }

            // Si ambos pasos fueron exitosos, confirmamos la transacción
            conexion.commit();
            return true;

        } catch (Exception e) {
            System.out.println("Error al registrar doctor en BD: " + e.getMessage());
            if (conexion != null) {
                try {
                    conexion.rollback(); // Deshacer cambios si algo falló
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
            return false;
        } finally {
            if (conexion != null) {
                try {
                    conexion.setAutoCommit(true);
                    conexion.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }


    /**
     * Consulta y retorna la lista completa de todos los doctores registrados en la base de datos,
     * mapeando cada registro del ResultSet a un objeto de tipo Doctor.
     */
// =========================================================================
//  1. OBTENER TODOS LOS DOCTORES (Para llenar la tabla inicial)
// =========================================================================
    public static List<Doctor> obtenerDoctores() {
        List<Doctor> lista = new ArrayList<>();
        String sql = "SELECT * FROM doctor";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Doctor doc = mapearDoctor(rs);
                lista.add(doc);
            }

        } catch (Exception e) {
            System.out.println("Error al obtener doctores: " + e.getMessage());
        }

        return lista;
    }


    /**
     * Busca y retorna un doctor específico en la base de datos utilizando su código único.
     * Si encuentra el registro, transforma los datos de la consulta en un objeto Doctor.
     */
// =========================================================================
//  2. BUSCAR DOCTOR POR CÓDIGO (Al seleccionar una fila de la tabla)
// =========================================================================
    public static Doctor buscarPorCodigo(String codigoDoctor) {
        Doctor doc = null;
        String sql = "SELECT * FROM doctor WHERE codigo_doctor = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, codigoDoctor);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    doc = mapearDoctor(rs);
                }
            }

        } catch (Exception e) {
            System.out.println("Error al buscar doctor por código: " + e.getMessage());
        }

        return doc;
    }


    /**
     * Busca doctores en la base de datos aplicando un filtro flexible por coincidencia
     * en el nombre, apellido o número de cédula, útil para búsquedas en tiempo real.
     */
// =========================================================================
//  3. BUSCAR DOCTORES EN TIEMPO REAL (Filtro por Nombre, Apellido o Cédula)
// =========================================================================
    public static List<Doctor> buscarDoctores(String criterio) {
        List<Doctor> lista = new ArrayList<>();
        String sql = "SELECT * FROM doctor WHERE nombres LIKE ? OR apellidos LIKE ? OR cedula LIKE ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            // Envuelve el criterio de búsqueda entre comodines para permitir coincidencias parciales en cualquier posición del texto
            String patronBusqueda = "%" + criterio + "%";
            ps.setString(1, patronBusqueda);
            ps.setString(2, patronBusqueda);
            ps.setString(3, patronBusqueda);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearDoctor(rs));
                }
            }

        } catch (Exception e) {
            System.out.println("Error al buscar doctores: " + e.getMessage());
        }

        return lista;
    }




    /**
     * Modifica el estado de actividad (activo/inactivo) de un doctor en la base de datos según su código único.
     * Retorna true si la actualización fue exitosa o false en caso de error o si el doctor no existe.
     */
// =========================================================================
//  4. CAMBIAR ESTADO ACTIVAR / DESACTIVAR EN BD
// =========================================================================
    public static boolean cambiarEstado(String codigoDoctor, boolean nuevoEstado) {
        String sql = "UPDATE doctor SET activo = ? WHERE codigo_doctor = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setBoolean(1, nuevoEstado);
            ps.setString(2, codigoDoctor);

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (Exception e) {
            System.out.println("Error al cambiar estado del doctor: " + e.getMessage());
            return false;
        }
    }




    /**
     * Busca y retorna un doctor en la base de datos utilizando su identificador numérico único (Primary Key).
     * Mapea los resultados obtenidos de la consulta a un objeto de tipo Doctor.
     */
// =========================================================================
//  BUSCAR DOCTOR POR ID PRIMARY KEY (Método corregido)
// =========================================================================
    public static Doctor buscarDoctor(int id_doctor) {
        Doctor doc = null;
        String sql = "SELECT * FROM doctor WHERE id_doctor = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id_doctor); // ps.setInt en lugar de setString

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    doc = mapearDoctor(rs);
                }
            }

        } catch (Exception e) {
            System.out.println("Error al buscar doctor por ID: " + e.getMessage());
        }

        return doc;
    }



    /**
     * Convierte el registro actual de un ResultSet en una instancia de la clase Doctor,
     * asignando cada campo de la base de datos a su respectiva propiedad y realizando
     * las conversiones necesarias de tipos de fecha y hora.
     */
// =========================================================================
//  MÉTODO AUXILIAR PRIVADO PARA MAPEAR REGISTRO RS -> OBJETO DOCTOR
// =========================================================================
    private static Doctor mapearDoctor(ResultSet rs) throws Exception {
        Doctor doc = new Doctor();

        // Extrae los datos básicos de texto y los asigna al objeto Doctor
        doc.setCodigoDoctor(rs.getString("codigo_doctor"));
        doc.setNombre(rs.getString("nombres"));
        doc.setApellido(rs.getString("apellidos"));
        doc.setCedula(rs.getString("cedula"));
        doc.setTelefono(rs.getString("telefono"));
        doc.setDireccion(rs.getString("direccion"));

        // Convierte la fecha de la base de datos (java.sql.Date) al formato moderno de Java (LocalDate)
        Date fechaSql = rs.getDate("fecha_nacimiento");
        if (fechaSql != null) {
            doc.setFechaNacimiento(fechaSql.toLocalDate());
        }

        // Extrae el texto del sexo y lo convierte a un solo carácter ('M' o 'F')
        String sexoStr = rs.getString("sexo");
        if (sexoStr != null && !sexoStr.isEmpty()) {
            doc.setSexo(sexoStr.charAt(0));
        }

        doc.setEspecialidad(rs.getString("especialidad"));
        doc.setNumeroLicencia(rs.getString("numero_licencia"));
        doc.setCitasPorDia(rs.getInt("citas_por_dia"));

        // Convierte la hora de inicio de la base de datos (java.sql.Time) al formato moderno de Java (LocalTime)
        Time inicioSql = rs.getTime("horario_inicio");
        if (inicioSql != null) {
            doc.setHorarioInicio(inicioSql.toLocalTime());
        }

        // Convierte la hora de fin de la base de datos (java.sql.Time) al formato moderno de Java (LocalTime)
        Time finSql = rs.getTime("horario_fin");
        if (finSql != null) {
            doc.setHorarioFin(finSql.toLocalTime());
        }

        doc.setActivo(rs.getBoolean("activo"));

        return doc;
    }

    /**
     * Actualiza la información de un doctor existente en la base de datos utilizando su código único
     * como filtro, transformando las propiedades del objeto Doctor a los tipos de datos compatibles con SQL.
     */
// =========================================================================
//  MÉTODO PARA MODIFICAR/ACTUALIZAR DATOS DE UN DOCTOR EN BD
// =========================================================================
    public static boolean actualizarDoctor(Doctor doctor) {
        String sql = "UPDATE doctor SET nombres = ?, apellidos = ?, telefono = ?, direccion = ?, especialidad = ?, citas_por_dia = ?, horario_inicio = ?, horario_fin = ?, activo = ? WHERE codigo_doctor = ?";

        try (Connection conexion = ConexionBD.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, doctor.getNombre());
            ps.setString(2, doctor.getApellido());
            ps.setString(3, doctor.getTelefono());
            ps.setString(4, doctor.getDireccion());
            ps.setString(5, doctor.getEspecialidad());
            ps.setInt(6, doctor.getCitasPorDia());

            // Conversión de LocalTime (Java) a java.sql.Time (SQL)
            ps.setTime(7, Time.valueOf(doctor.getHorarioInicio()));
            ps.setTime(8, Time.valueOf(doctor.getHorarioFin()));
            ps.setBoolean(9, doctor.isActivo());

            // El WHERE identifica exactamente qué doctor actualizar
            ps.setString(10, doctor.getCodigoDoctor());

            int filasAfectadas = ps.executeUpdate();
            return filasAfectadas > 0;

        } catch (Exception e) {
            System.out.println("Error al actualizar doctor en BD: " + e.getMessage());
            return false;
        }
    }


    /**
     * Genera de forma automática un código único para un nuevo doctor basado en el conteo total
     * de registros existentes en la tabla (ej. "DOC-1", "DOC-2", etc.).
     */
    public static String generarCodigoDoctor() {
        String nuevoCodigo = "DOC-1";
        String sql = "SELECT COUNT(*) FROM doctor";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                int totalDoctores = rs.getInt(1);
                nuevoCodigo = "DOC-" + (totalDoctores + 1);
            }

        } catch (SQLException e) {
            System.err.println("Error al generar código de doctor: " + e.getMessage());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return nuevoCodigo;
    }



}