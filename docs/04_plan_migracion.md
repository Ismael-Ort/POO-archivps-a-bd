# 04 - Plan de Migración

## 1. Objetivo del documento

Este documento define el orden recomendado para migrar el sistema desde archivos `.dat` hacia base de datos MySQL.

La migración no debe hacerse toda de una vez. Lo correcto es avanzar módulo por módulo, probando cada parte antes de continuar con la siguiente.

El objetivo es evitar romper el sistema completo y tener una guía clara de trabajo para el equipo.

---

## 2. Estrategia general

La estrategia será una migración gradual.

Esto significa que se irá reemplazando poco a poco el uso de archivos `.dat` por operaciones SQL.

Actualmente muchas ventanas terminan llamando a:
PersistenciaManager.guardarDatos();

Ese método guarda todo el sistema en archivos.

En la nueva versión, cada módulo deberá llamar a clases específicas de base de datos.

Ejemplo:

DoctorBD
PacienteBD
UsuarioBD
AlergiaBD
EnfermedadBD
VacunaBD
RegistroVacunaBD

La idea es que cada clase se encargue de una tabla o grupo de tablas.

## 3. Orden recomendado de migración

El orden recomendado es:

1. Crear base de datos y tablas.
2. Insertar usuario administrador inicial.
3. Migrar login.
4. Migrar usuarios administradores.
5. Migrar doctores.
6. Migrar pacientes.
7. Migrar alergias.
8. Migrar enfermedades.
9. Migrar vacunas del sistema.
10. Migrar administración de vacunas.
11. Migrar vacunas previas.
12. Ajustar historial básico.
13. Eliminar dependencia de archivos en los módulos migrados.

Este orden es importante porque algunos módulos dependen de otros.

Por ejemplo:

No se puede migrar pacientes antes de tener doctores.
No se puede migrar vacunas antes de tener enfermedades.
No se puede administrar vacunas antes de tener pacientes y vacunas.
No se puede probar bien el historial si todavía no existen pacientes, alergias y vacunas.

## 4. Paso 1 - Crear base de datos y tablas

El primer paso es crear la estructura de la base de datos.

Las tablas principales serán:

usuario
doctor
paciente
alergia
paciente_alergia
enfermedad
vacuna
registro_vacuna
vacuna_previa

Estas tablas deben crearse antes de modificar las ventanas del sistema.

También se deben definir:

Claves primarias.
Claves foráneas.
Campos únicos.
Tipos de datos.
Restricciones básicas.

## 5. Paso 2 - Insertar usuario administrador inicial

Antes de migrar el login, debe existir un usuario administrador en la base de datos.

Usuario inicial:

Usuario: Admin
Contraseña: Admin
Tipo: Administrador

Este usuario permitirá entrar al sistema después de que el login deje de usar archivos .dat.

Sin este usuario inicial, no habría forma de acceder al sistema.

## 6. Paso 3 - Migrar login

El login debe ser el primer módulo funcional migrado.

Actualmente el login valida los usuarios usando la clase Control y los datos cargados desde control.dat.

En la nueva versión, el login debe consultar la tabla usuario.

Flujo esperado:

1. El usuario escribe username y password.
2. El sistema busca ese username en la tabla usuario.
3. Si no existe, muestra error.
4. Si existe, compara la contraseña.
5. Si la contraseña es incorrecta, muestra error.
6. Si es correcta, revisa el tipo de usuario.
7. Si es Administrador, abre la ventana principal como administrador.
8. Si es Doctor, carga el doctor relacionado y abre la ventana principal como doctor.

Este paso también debe mantener la lógica de sesión actual, porque Principal.java depende de saber si el usuario es administrador o doctor.

## 7. Paso 4 - Migrar usuarios administradores

Después del login, se debe migrar el registro de usuarios administradores.

La ventana relacionada es:

regUser.java

Actualmente esta ventana registra usuarios usando:

Control.getInstance().regUser(nuevo);

En la nueva versión debe guardar en la tabla:

usuario

Reglas que debe cumplir:

El username no puede estar vacío.
La contraseña no puede estar vacía.
La contraseña debe tener al menos 4 caracteres.
El username no puede repetirse.
El tipo debe ser Administrador.
id_doctor debe quedar nulo.

## 8. Paso 5 - Migrar doctores

Las ventanas relacionadas son:

regDoctor.java
listDoctor.java
modDoctor.java

Este módulo debe permitir:

Registrar doctores.
Listar doctores.
Buscar doctores.
Modificar doctores.
Activar y desactivar doctores.

Al registrar un doctor, también debe crearse un usuario de tipo doctor.

El flujo recomendado es:

1. Validar datos del doctor.
2. Verificar que la cédula no exista.
3. Verificar que la licencia no exista.
4. Verificar que el username no exista.
5. Insertar el doctor en la tabla doctor.
6. Obtener el id del doctor insertado.
7. Insertar el usuario de tipo Doctor en la tabla usuario.
8. Relacionar ese usuario con el doctor.

Esto evita duplicar credenciales en varias partes del sistema.

## 9. Paso 6 - Migrar pacientes

Las ventanas relacionadas son:

regPaciente.java
listPacientes.java

Este módulo debe permitir:

Registrar pacientes.
Listar pacientes.
Modificar datos permitidos.
Relacionar al paciente con el doctor que lo registró.

En el sistema actual, el paciente guarda:

doctorRegistrador

Ese campo contiene la licencia del doctor.

En base de datos se recomienda reemplazar eso por:

id_doctor_registrador

Esto crea una relación directa entre el paciente y el doctor.

## 10. Paso 7 - Migrar alergias

Las ventanas relacionadas son:

regAlergia.java
TomaAlergias.java
verAlergias.java

Este módulo debe permitir:

Registrar alergias generales.
Listar alergias disponibles.
Buscar alergias.
Seleccionar alergias para un paciente.
Ver alergias de un paciente.

La relación entre pacientes y alergias debe guardarse en:

paciente_alergia

Flujo esperado al registrar paciente con alergias:

1. Se registra el paciente.
2. Se seleccionan las alergias.
3. Se guarda cada relación en paciente_alergia.
   
## 11. Paso 8 - Migrar enfermedades

Las ventanas relacionadas son:

regEnfermedades.java
listEnfermedad.java

Este módulo debe permitir:

Registrar enfermedades.
Listar enfermedades.
Filtrar por nombre.
Filtrar por tipo.
Ver detalles.
Cambiar estado de vigilancia.

Las enfermedades son necesarias antes de migrar vacunas, porque cada vacuna debe estar relacionada con una enfermedad.

## 12. Paso 9 - Migrar vacunas del sistema

Las ventanas relacionadas son:

regVacuna.java
listVacuna.java

Este módulo debe permitir:

Registrar vacunas del sistema.
Listar vacunas.
Buscar por nombre.
Buscar por lote.
Buscar por laboratorio.
Mostrar stock.
Mostrar si está caducada.
Mostrar si está inactiva.
Relacionar la vacuna con una enfermedad.

Reglas importantes:

El número de lote no puede repetirse.
La vacuna debe tener una enfermedad.
La cantidad debe ser mayor que cero al registrarse.
La fecha de caducidad debe guardarse.
La vacuna debe tener estado activo.

## 13. Paso 10 - Migrar administración de vacunas a pacientes

La ventana relacionada es:

adminVacuna.java

Este módulo permite aplicar vacunas del sistema a pacientes.

Flujo esperado:

1. Seleccionar paciente.
2. Seleccionar vacuna disponible.
3. Validar que la vacuna esté activa.
4. Validar que la vacuna no esté vencida.
5. Validar que la vacuna tenga stock.
6. Registrar la aplicación en registro_vacuna.
7. Reducir el stock de la vacuna.
8. Actualizar la lista de vacunas disponibles.

La tabla principal usada aquí será:

registro_vacuna

También se actualizará:

vacuna.cantidad

## 14. Paso 11 - Migrar vacunas previas

Las ventanas relacionadas son:

selecVacunas.java
regVacuViea.java

Estas vacunas son las que el paciente ya tenía antes de ser registrado en el sistema.

Deben guardarse en la tabla:

vacuna_previa

Reglas:

No pertenecen al inventario.
No reducen stock.
No tienen lote real.
No tienen laboratorio.
Se muestran en historial con lote desconocido.

## 15. Paso 12 - Ajustar historial básico

La ventana relacionada es:

VerHistorialClinico.java

Solo se tomará la parte de historial básico.

Debe mostrar:

Datos del paciente.
Alergias del paciente.
Vacunas aplicadas desde el sistema.
Vacunas previas.
Vacunas pendientes.

No se migrará:

Consultas.
Detalles de consulta.
Resumen automático.
Marcar consultas para resumen.

## 16. Paso 13 - Eliminar dependencia de archivos

Después de migrar cada módulo, se debe ir eliminando la dependencia de:

PersistenciaManager.guardarDatos();

No se debe eliminar todo de una vez.

La recomendación es hacerlo por módulo.

Ejemplo:

Cuando doctores funcione con BD:
→ quitar guardado por .dat solo en doctores.

Cuando pacientes funcione con BD:
→ quitar guardado por .dat solo en pacientes.

Así se reduce el riesgo de romper el sistema completo.

## 17. Clases de base de datos recomendadas

Se recomienda crear clases separadas para manejar la base de datos.

Ejemplo:

UsuarioBD.java
DoctorBD.java
PacienteBD.java
AlergiaBD.java
EnfermedadBD.java
VacunaBD.java
RegistroVacunaBD.java
VacunaPreviaBD.java

Estas clases deberían estar en un paquete como:

javaBD

Cada clase debe tener métodos para insertar, buscar, listar y actualizar.

## 18. Ejemplo de métodos por clase
    
UsuarioBD
validarLogin()
registrarUsuario()
buscarPorUsername()
DoctorBD
registrarDoctor()
listarDoctores()
buscarPorCodigo()
buscarPorCedula()
modificarDoctor()
cambiarEstado()
PacienteBD
registrarPaciente()
listarPacientes()
buscarPorCedula()
buscarPorCodigo()
modificarPaciente()
AlergiaBD
registrarAlergia()
listarAlergias()
buscarPorNombre()
asignarAlergiaAPaciente()
listarAlergiasDePaciente()
EnfermedadBD
registrarEnfermedad()
listarEnfermedades()
buscarPorCodigo()
buscarPorNombre()
cambiarVigilancia()
VacunaBD
registrarVacuna()
listarVacunas()
buscarPorCodigo()
buscarPorLote()
listarDisponibles()
actualizarStock()
RegistroVacunaBD
registrarAplicacion()
listarVacunasDePaciente()
VacunaPreviaBD
registrarVacunaPrevia()
listarVacunasPreviasDePaciente()
20. Conclusión

La migración debe hacerse de forma ordenada y gradual.

El orden propuesto permite avanzar desde lo más básico hasta lo más dependiente:

Login
→ Usuarios
→ Doctores
→ Pacientes
→ Alergias
→ Enfermedades
→ Vacunas
→ Administración de vacunas
→ Historial básico

Este plan ayuda a que el equipo sepa qué hacer primero, qué depende de qué, y cómo evitar que el proyecto se vuelva desorganizado.
