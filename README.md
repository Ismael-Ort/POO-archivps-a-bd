# Sistema de Clínica - Conversión de Archivos `.dat` a Base de Datos

Este proyecto consiste en adaptar un sistema de gestión clínica desarrollado en Java Swing. El sistema original guarda la información usando archivos `.dat`, y el objetivo actual es migrar progresivamente esa persistencia hacia una base de datos MySQL.

La idea no es rehacer todo el sistema desde cero, sino conservar la mayor parte de las ventanas existentes y reemplazar la lógica de guardado y carga de archivos por operaciones con base de datos.

## Tecnologías utilizadas

- Java
- Java Swing
- MySQL
- JDBC
- MySQL Connector/J
- Aiven MySQL
- Git y GitHub

## Estado actual

Actualmente el sistema utiliza archivos `.dat` para guardar la información principal:

- `control.dat`: usuarios del sistema.
- `clinica.dat`: doctores, pacientes, alergias, enfermedades, vacunas y otros datos.
- `clinica_respaldo.dat`: respaldo.
- `reporteEnfermedades.dat`: reporte de enfermedades bajo vigilancia.

La conexión a MySQL ya fue probada usando una clase llamada `ConexionBD`.

## Alcance de esta conversión

Para que el proyecto sea manejable, se decidió migrar solo los módulos principales.

### Sí se va a convertir

- Login.
- Usuarios administradores.
- Doctores.
- Pacientes.
- Alergias.
- Enfermedades.
- Vacunas del sistema.
- Administración de vacunas a pacientes.
- Vacunas previas del paciente.
- Historial básico del paciente.
- Listar, buscar, modificar y activar/desactivar cuando aplique.

### No se va a convertir en esta etapa

- Agendar citas.
- Listar citas.
- Registrar consultas.
- Listar consultas.
- Detalles de cita.
- Detalles de consulta.
- Tratamientos.
- Medicamentos.
- Gráficas y reportes avanzados.

Estos módulos quedan fuera porque harían que la migración fuera demasiado extensa.

## Documentación del proyecto

La documentación completa se encuentra en la carpeta:

```text
ProyectoFinalICC104/docs
