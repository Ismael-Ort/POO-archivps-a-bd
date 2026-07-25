package javaBD;

import logico.Alergia;
import logico.Paciente;
import logico.VacunaVieja;

import java.sql.*;
import java.util.ArrayList;

public class PacienteBD {

    private static String ultimoError = "";

    public static String getUltimoError() {
        return ultimoError;
    }

    /** Genera de forma automática un código único para un nuevo paciente contando los registros actuales y sumándole uno (ej. "PAC-1"). */
// =========================================================================
// GENERAR CÓDIGO DEL PACIENTE
// =========================================================================
    public static String generarCodigoPaciente() {
        String nuevoCodigo = "PAC-1";
        String sql = "SELECT COUNT(*) FROM paciente";

        try (Connection con = ConexionBD.conectar(); PreparedStatement ps = con.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                nuevoCodigo = "PAC-" + (rs.getInt(1) + 1);
            }
        } catch (Exception e) {
            System.err.println("Error al generar código de paciente: " + e.getMessage());
        }

        return nuevoCodigo;
    }



    /** Valida si una cédula ya se encuentra registrada en las tablas de pacientes o doctores del sistema. */
// =========================================================================
// VALIDAR CÉDULA EN PACIENTE Y DOCTOR
// =========================================================================
    public static boolean existeCedulaEnSistema(String cedula) {
        String sqlPaciente = "SELECT cedula FROM paciente WHERE cedula = ?";
        String sqlDoctor = "SELECT cedula FROM doctor WHERE cedula = ?";

        try (Connection con = ConexionBD.conectar()) {
            try (PreparedStatement psPaciente = con.prepareStatement(sqlPaciente)) {
                psPaciente.setString(1, cedula);
                try (ResultSet rs = psPaciente.executeQuery()) {
                    if (rs.next()) {
                        return true;
                    }
                }
            }

            try (PreparedStatement psDoctor = con.prepareStatement(sqlDoctor)) {
                psDoctor.setString(1, cedula);
                try (ResultSet rs = psDoctor.executeQuery()) {
                    if (rs.next()) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al verificar la cédula: " + e.getMessage());
        }

        return false;
    }





    /** Valida si un número de teléfono ya se encuentra registrado en las tablas de pacientes o doctores del sistema. */
// =========================================================================
// VALIDAR TELÉFONO EN PACIENTE Y DOCTOR
// =========================================================================
    public static boolean existeTelefonoEnSistema(String telefono) {
        String sqlPaciente = "SELECT telefono FROM paciente WHERE telefono = ?";
        String sqlDoctor = "SELECT telefono FROM doctor WHERE telefono = ?";

        try (Connection con = ConexionBD.conectar()) {
            try (PreparedStatement psPaciente = con.prepareStatement(sqlPaciente)) {
                psPaciente.setString(1, telefono);
                try (ResultSet rs = psPaciente.executeQuery()) {
                    if (rs.next()) {
                        return true;
                    }
                }
            }

            try (PreparedStatement psDoctor = con.prepareStatement(sqlDoctor)) {
                psDoctor.setString(1, telefono);
                try (ResultSet rs = psDoctor.executeQuery()) {
                    if (rs.next()) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al verificar el teléfono: " + e.getMessage());
        }

        return false;
    }

    /** Valida si un número de teléfono ya está registrado en el sistema durante una modificación, excluyendo al paciente actual. */
// =========================================================================
// VALIDAR TELÉFONO DURANTE UNA MODIFICACIÓN
// Excluye al paciente que se está modificando.
// =========================================================================
    public static boolean existeTelefonoEnSistema(String telefono, String codigoPacienteExcluir) {
        String sqlPaciente = "SELECT telefono FROM paciente WHERE telefono = ? AND codigo_paciente <> ?";
        String sqlDoctor = "SELECT telefono FROM doctor WHERE telefono = ?";

        try (Connection con = ConexionBD.conectar()) {
            try (PreparedStatement psPaciente = con.prepareStatement(sqlPaciente)) {
                psPaciente.setString(1, telefono);
                psPaciente.setString(2, codigoPacienteExcluir);
                try (ResultSet rs = psPaciente.executeQuery()) {
                    if (rs.next()) {
                        return true;
                    }
                }
            }

            try (PreparedStatement psDoctor = con.prepareStatement(sqlDoctor)) {
                psDoctor.setString(1, telefono);
                try (ResultSet rs = psDoctor.executeQuery()) {
                    if (rs.next()) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            System.err.println("Error al verificar el teléfono en modificación: " + e.getMessage());
        }

        return false;
    }

    /**
     * Registra un nuevo paciente y sus alergias asociadas.
     * Primero busca el ID del doctor, luego inserta el paciente obteniendo su ID generado,
     * y finalmente recorre la lista de alergias buscándolas individualmente para relacionarlas.
     */
// =========================================================================
// REGISTRAR PACIENTE Y ALERGIAS EN UNA TRANSACCIÓN
// =========================================================================
    public static boolean registrarPaciente(Paciente paciente, String codigoDoctor) {
        ultimoError = "";
        String sqlBuscarDoctor = "SELECT id_doctor FROM doctor WHERE codigo_doctor = ? AND activo = TRUE";
        String sqlPaciente = "INSERT INTO paciente (codigo_paciente, cedula, nombres, apellidos, telefono, direccion, fecha_nacimiento, sexo, tipo_sangre, peso, estatura, id_doctor_registrador) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        // Consulta normal y corriente para buscar el ID de la alergia por su nombre antes de relacionarla
        String sqlBuscarAlergia = "SELECT id_alergia FROM alergia WHERE nombre = ?";
        String sqlPacienteAlergia = "INSERT INTO paciente_alergia (id_paciente, id_alergia) VALUES (?, ?)";

        Connection con = null;

        try {
            con = ConexionBD.conectar();
            // Desactivamos el auto-commit para controlar la transacción manualmente
            con.setAutoCommit(false);

            int idDoctor;
            int idPaciente;

            // 1. Verificamos que el doctor exista y esté activo en el sistema
            try (PreparedStatement ps = con.prepareStatement(sqlBuscarDoctor)) {
                ps.setString(1, codigoDoctor);
                try (ResultSet rs = ps.executeQuery()) {
                    if (!rs.next()) {
                        ultimoError = "No se encontró un doctor activo asociado a la sesión.";
                        con.rollback();
                        return false;
                    }
                    idDoctor = rs.getInt("id_doctor");
                }
            }

            // 2. Insertamos al paciente y pedimos que nos devuelva el ID autoincremental generado
            try (PreparedStatement ps = con.prepareStatement(sqlPaciente, Statement.RETURN_GENERATED_KEYS)) {
                ps.setString(1, paciente.getCodigoPaciente());
                ps.setString(2, paciente.getCedula());
                ps.setString(3, paciente.getNombre());
                ps.setString(4, paciente.getApellido());
                ps.setString(5, paciente.getTelefono());
                ps.setString(6, paciente.getDireccion());
                ps.setDate(7, java.sql.Date.valueOf(paciente.getFechaNacimiento()));
                ps.setString(8, String.valueOf(paciente.getSexo()));
                ps.setString(9, paciente.getTipoSangre());
                ps.setFloat(10, paciente.getPeso());
                ps.setFloat(11, paciente.getEstatura());
                ps.setInt(12, idDoctor);

                if (ps.executeUpdate() <= 0) {
                    ultimoError = "No se pudo insertar el paciente.";
                    con.rollback();
                    return false;
                }

                try (ResultSet keys = ps.getGeneratedKeys()) {
                    if (!keys.next()) {
                        ultimoError = "No se pudo obtener el ID del paciente.";
                        con.rollback();
                        return false;
                    }
                    idPaciente = keys.getInt(1);
                }
            }

            // 3. Si el paciente tiene alergias, buscamos el ID de cada una y luego las insertamos paso a paso
            if (paciente.getAlergias() != null && !paciente.getAlergias().isEmpty()) {
                for (Alergia alergia : paciente.getAlergias()) {
                    int idAlergia = -1;

                    // Primero buscamos el ID que le corresponde a esta alergia por su nombre
                    try (PreparedStatement psAlergia = con.prepareStatement(sqlBuscarAlergia)) {
                        psAlergia.setString(1, alergia.getNombre());
                        try (ResultSet rsAlergia = psAlergia.executeQuery()) {
                            if (rsAlergia.next()) {
                                idAlergia = rsAlergia.getInt("id_alergia");
                            }
                        }
                    }

                    // Si encontramos la alergia, procedemos a hacer el INSERT en la tabla puente
                    if (idAlergia != -1) {
                        try (PreparedStatement psRelacion = con.prepareStatement(sqlPacienteAlergia)) {
                            psRelacion.setInt(1, idPaciente);
                            psRelacion.setInt(2, idAlergia);

                            if (psRelacion.executeUpdate() <= 0) {
                                ultimoError = "No se pudo relacionar la alergia " + alergia.getNombre() + " con el paciente.";
                                con.rollback();
                                return false;
                            }
                        }
                    }
                }
            }

            // Si todo salió bien, guardamos los cambios definitivamente
            con.commit();
            return true;

        } catch (SQLException e) {
            ultimoError = "No se pudo registrar el paciente. Revise que el código, la cédula y el teléfono no estén repetidos y que los datos cumplan las reglas establecidas.";
            System.err.println(ultimoError);
            System.err.println("Detalle técnico: " + e.getMessage());
            rollback(con);
            return false;
        } catch (Exception e) {
            ultimoError = "No se pudo registrar el paciente. Revise la conexión con la base de datos.";
            System.err.println(ultimoError);
            System.err.println("Detalle técnico: " + e.getMessage());
            rollback(con);
            return false;
        } finally {
            cerrarConexion(con);
        }
    }

    /**
     * Obtiene un listado de todos los pacientes registrados ordenados por su código,
     * mapeando cada registro con su información completa.
     */
// =========================================================================
// LISTAR TODOS LOS PACIENTES
// =========================================================================
    public static ArrayList<Paciente> listarTodos() {
        ArrayList<Paciente> pacientes = new ArrayList<>();
        String sql = consultaBasePaciente() + " ORDER BY p.codigo_paciente";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                pacientes.add(mapearPacienteCompleto(con, rs));
            }

        } catch (Exception e) {
            System.err.println("Error al listar pacientes: " + e.getMessage());
        }

        return pacientes;
    }



    /**
     * Devuelve la lista de pacientes registrados por un doctor específico,
     * ordenados por su código.
     */
// =========================================================================
// LISTAR PACIENTES REGISTRADOS POR UN DOCTOR
// =========================================================================
    public static ArrayList<Paciente> listarPorDoctor(String codigoDoctor) {
        ArrayList<Paciente> pacientes = new ArrayList<>();
        String sql = consultaBasePaciente() + " WHERE d.codigo_doctor = ? ORDER BY p.codigo_paciente";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, codigoDoctor);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    pacientes.add(mapearPacienteCompleto(con, rs));
                }
            }

        } catch (Exception e) {
            System.err.println("Error al listar pacientes del doctor: " + e.getMessage());
        }

        return pacientes;
    }


    /**
     * Busca un paciente en la base de datos utilizando su código único.
     */
// =========================================================================
// BUSCAR PACIENTE POR CÓDIGO
// =========================================================================
    public static Paciente buscarPorCodigo(String codigoPaciente) {
        String sql = consultaBasePaciente() + " WHERE p.codigo_paciente = ?";

        return buscarPaciente(sql, codigoPaciente);
    }


    /**
     * Busca un paciente en el sistema utilizando su número de cédula.
     */
// =========================================================================
// BUSCAR PACIENTE POR CÉDULA
// =========================================================================
    public static Paciente buscarPorCedula(String cedula) {
        String sql = consultaBasePaciente() + " WHERE p.cedula = ?";

        return buscarPaciente(sql, cedula);
    }



    /**
     * Realiza la búsqueda de un paciente ejecutando una consulta SQL y un parámetro dinámico.
     */
// =========================================================================
// BUSCAR PACIENTE (MÉTODO AUXILIAR)
// =========================================================================
    private static Paciente buscarPaciente(String sql, String valor) {
        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, valor);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearPacienteCompleto(con, rs);
                }
            }

        } catch (Exception e) {
            System.err.println("Error al buscar paciente: " + e.getMessage());
        }

        return null;
    }



    /**
     * Modifica los campos permitidos de un paciente existente como el teléfono,
     * la dirección, el peso y la estatura, utilizando su código como referencia.
     */
// =========================================================================
// MODIFICAR CAMPOS PERMITIDOS DEL PACIENTE
// =========================================================================
    public static boolean modificarPaciente(Paciente paciente) {
        ultimoError = "";
        String sql = "UPDATE paciente SET telefono = ?, direccion = ?, peso = ?, estatura = ? WHERE codigo_paciente = ?";

        try (Connection con = ConexionBD.conectar();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, paciente.getTelefono());
            ps.setString(2, paciente.getDireccion());
            ps.setFloat(3, paciente.getPeso());
            ps.setFloat(4, paciente.getEstatura());
            ps.setString(5, paciente.getCodigoPaciente());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            ultimoError = "No se pudo modificar el paciente. Revise que el teléfono no esté registrado y que los valores sean válidos.";
            System.err.println(ultimoError);
            System.err.println("Detalle técnico: " + e.getMessage());
            return false;
        } catch (Exception e) {
            ultimoError = "No se pudo modificar el paciente. Revise la conexión con la base de datos.";
            System.err.println(ultimoError);
            System.err.println("Detalle técnico: " + e.getMessage());
            return false;
        }
    }

    /**
     * Devuelve la consulta SQL base que une la información de los pacientes
     * con los datos del doctor que los registró mediante un LEFT JOIN.
     */
// =========================================================================
// CONSULTA BASE CON EL DOCTOR REGISTRADOR
// =========================================================================
    private static String consultaBasePaciente() {
        return "SELECT p.codigo_paciente, p.cedula, p.nombres, p.apellidos, p.telefono, p.direccion, p.fecha_nacimiento, p.sexo, p.tipo_sangre, p.fecha_registro, p.activo, p.peso, p.estatura, d.codigo_doctor, d.numero_licencia, d.nombres AS nombres_doctor, d.apellidos AS apellidos_doctor FROM paciente p LEFT JOIN doctor d ON p.id_doctor_registrador = d.id_doctor";
    }


    /**
     * Mapea una fila del resultado de la base de datos a un objeto Paciente completo,
     * asignando sus datos personales, el doctor registrador y cargando sus listas
     * de alergias y vacunas previas.
     */
// =========================================================================
// MAPEAR FILA DE MYSQL A PACIENTE
// =========================================================================
    private static Paciente mapearPacienteCompleto(Connection con, ResultSet rs) throws Exception {
        String licenciaDoctor = rs.getString("numero_licencia");
        Paciente paciente = new Paciente(
                rs.getString("cedula"),
                rs.getString("nombres"),
                rs.getString("apellidos"),
                rs.getString("telefono"),
                rs.getString("codigo_paciente"),
                licenciaDoctor
        );

        paciente.setDireccion(rs.getString("direccion"));
        java.sql.Date fechaNacimiento = rs.getDate("fecha_nacimiento");

        if (fechaNacimiento != null) {
            paciente.setFechaNacimiento(fechaNacimiento.toLocalDate());
        }

        String sexo = rs.getString("sexo");

        if (sexo != null && !sexo.isEmpty()) {
            paciente.setSexo(sexo.charAt(0));
        }

        paciente.setTipoSangre(rs.getString("tipo_sangre"));
        paciente.setActivo(rs.getBoolean("activo"));
        paciente.setPeso(rs.getFloat("peso"));
        paciente.setEstatura(rs.getFloat("estatura"));
        paciente.setAlergias(listarAlergiasPaciente(con, paciente.getCodigoPaciente()));
        paciente.setVacunasViejas(listarVacunasPreviasPaciente(con, paciente.getCodigoPaciente()));

        return paciente;
    }



    /**
     * Carga y devuelve la lista de alergias asociadas a un paciente específico
     * utilizando una única consulta con comparación de IDs en el WHERE.
     */
// =========================================================================
// CARGAR ALERGIAS DEL PACIENTE
// =========================================================================
    private static ArrayList<Alergia> listarAlergiasPaciente(Connection con, String codigoPaciente) throws SQLException {
        ArrayList<Alergia> alergias = new ArrayList<>();
        String sql = "SELECT a.nombre, a.tipo FROM alergia a, paciente_alergia pa, paciente p WHERE a.id_alergia = pa.id_alergia AND pa.id_paciente = p.id_paciente AND p.codigo_paciente = ? ORDER BY a.nombre";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigoPaciente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    alergias.add(new Alergia(rs.getString("nombre"), rs.getString("tipo")));
                }
            }
        }

        return alergias;
    }


    /**
     * Carga y devuelve la lista de vacunas previas asociadas a un paciente específico
     * utilizando una única consulta con comparación de IDs en el WHERE.
     */
// =========================================================================
// CARGAR VACUNAS PREVIAS DEL PACIENTE
// =========================================================================
    private static ArrayList<VacunaVieja> listarVacunasPreviasPaciente(Connection con, String codigoPaciente) throws SQLException {
        ArrayList<VacunaVieja> vacunas = new ArrayList<>();
        String sql = "SELECT vp.enfermedad, vp.fecha_aplicacion FROM vacuna_previa vp, paciente p WHERE vp.id_paciente = p.id_paciente AND p.codigo_paciente = ? ORDER BY vp.fecha_aplicacion";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, codigoPaciente);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    vacunas.add(new VacunaVieja(rs.getString("enfermedad"), rs.getDate("fecha_aplicacion").toLocalDate()));
                }
            }
        }

        return vacunas;
    }


    /**
     * Método auxiliar privado que revierte los cambios de la transacción actual
     * de manera segura, encapsulando el manejo de excepciones de SQL.
     */
// =========================================================================
// MÉTODO AUXILIAR ROLLBACK
// =========================================================================
    private static void rollback(Connection con) {
        if (con != null) {
            try {
                con.rollback();
            } catch (SQLException e) {
                System.err.println("Error al deshacer transacción: " + e.getMessage());
            }
        }
    }

    /**
     * Método auxiliar privado que restaura el modo autocommit de la conexión
     * y la cierra de forma segura, manejando cualquier excepción de SQL.
     */
// =========================================================================
// MÉTODO AUXILIAR CERRAR CONEXIÓN
// =========================================================================
    private static void cerrarConexion(Connection con) {
        if (con != null) {
            try {
                con.setAutoCommit(true);
                con.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
}
