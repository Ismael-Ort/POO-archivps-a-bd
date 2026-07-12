# 02 - Análisis del Sistema Actual

## 1. Objetivo del documento

Este documento explica cómo funciona actualmente el sistema antes de la conversión a base de datos.

El propósito es dejar claro:

- Cómo se guardan los datos actualmente.
- Qué archivos `.dat` utiliza el sistema.
- Qué clases controlan la persistencia.
- Qué clases funcionan como base de datos en memoria.
- Qué ventanas se conectan con los módulos que serán migrados.
- Qué partes del sistema se tomarán en cuenta y cuáles no.

Este análisis es importante porque antes de crear la base de datos hay que entender cómo el sistema guarda y maneja la información actualmente.

---

## 2. Forma actual de almacenamiento

El sistema actual no usa base de datos.

En lugar de eso, guarda objetos completos en archivos `.dat` mediante serialización.

Esto significa que el sistema crea objetos Java, los guarda en memoria y luego escribe esos objetos dentro de archivos binarios.

La idea general es:

text
Objetos Java
→ Se guardan con ObjectOutputStream
→ Se almacenan en archivos .dat

Cuando el programa se abre nuevamente:

Archivos .dat
→ Se leen con ObjectInputStream
→ Se reconstruyen los objetos Java
→ El sistema vuelve a trabajar con esos datos en memoria

## 3. Problema de usar archivos .dat

Usar archivos .dat funciona para proyectos pequeños, pero tiene varias limitaciones:

No permite consultas complejas fácilmente.
No permite relaciones claras entre datos.
Es difícil compartir información entre varios usuarios.
Es más complicado validar duplicados.
Es más difícil hacer respaldos organizados.
No permite trabajar con tablas.
No es ideal para sistemas reales o multiusuario.
Si el archivo se daña, se puede perder mucha información.

Por eso se busca migrar a MySQL.

## 4. Clase PersistenciaManager

La clase principal encargada de guardar y cargar datos es:

PersistenciaManager

Esta clase maneja métodos como:

guardarDatos()
guardarControl()
guardarClinica()
cargarDatos()
cargarControl()
cargarClinica()
crearAdminPorDefecto()

Su función principal es guardar y cargar los objetos principales del sistema.

Actualmente, cuando se realiza una acción importante en el sistema, se llama a:

PersistenciaManager.guardarDatos();

Esto guarda nuevamente los datos en los archivos .dat.

En la conversión a base de datos, esta lógica debe ser reemplazada poco a poco por clases que usen SQL.

## 5. Archivo control.dat

El archivo control.dat guarda el objeto Control.

La clase Control contiene principalmente la lista de usuarios del sistema.

En el código existe una lista como:

private ArrayList<User> misUsers;

Esta lista guarda usuarios administradores y también usuarios de tipo doctor.

Además, Control mantiene información del usuario que ha iniciado sesión:

private static User loginUser;
private static Doctor doctorLogeado;

Esto permite saber si el usuario actual es administrador o doctor.

## 6. Conversión de control.dat a base de datos

Lo que actualmente se guarda en control.dat debe migrarse principalmente a la tabla:

usuario

La tabla usuario debe guardar:

Tipo de usuario.
Nombre de usuario.
Contraseña.
Relación con doctor, si aplica.
Estado activo.

La información de sesión, como loginUser o doctorLogeado, no necesariamente se guarda en base de datos. Esa información puede seguir existiendo en memoria mientras el programa esté abierto.

## 7. Archivo clinica.dat

El archivo clinica.dat guarda el objeto completo Clinica.

La clase Clinica funciona como una base de datos en memoria, porque contiene listas de las entidades principales del sistema.

Entre sus listas principales están:

private ArrayList<Paciente> pacientes;
private ArrayList<Doctor> doctores;
private ArrayList<Enfermedad> enfermedades;
private ArrayList<Vacuna> vacunas;
private ArrayList<Alergia> alergias;

También existen otras listas relacionadas con módulos que no serán migrados en esta etapa, como:

private ArrayList<Cita> citas;
private ArrayList<Consulta> consultas;
private ArrayList<Tratamiento> tratamientos;
private ArrayList<Medicamento> medicamentos;

Estas últimas no forman parte del alcance actual.

## 8. Conversión de clinica.dat a base de datos

La información de clinica.dat será separada en varias tablas.

Por ejemplo:

Lista de doctores        → tabla doctor
Lista de pacientes       → tabla paciente
Lista de alergias        → tabla alergia
Lista de enfermedades    → tabla enfermedad
Lista de vacunas         → tabla vacuna
Vacunas aplicadas        → tabla registro_vacuna
Vacunas previas          → tabla vacuna_previa

Esto permitirá que cada tipo de dato esté organizado en su propia tabla.

## 9. Clase Principal.java

La clase Principal.java representa el menú principal del sistema.

Esta clase es importante porque muestra qué módulos se abren dependiendo del tipo de usuario.

El sistema distingue principalmente entre:

Administrador
Doctor

Cuando el usuario es administrador, se muestran principalmente los menús de:

Doctores
Administración

Cuando el usuario es doctor, se muestran principalmente los menús de:

Pacientes
Enfermedades
Vacunas
Citas
Consultas

Sin embargo, para esta conversión los menús de citas y consultas quedan fuera.

## 10. Módulos del administrador

Según Principal.java, el administrador puede acceder a:

Registrar doctores.
Listar doctores.
Registrar usuarios administradores.
Reportes.

Para esta conversión se tomarán:

Registrar doctores.
Listar doctores.
Modificar doctores.
Activar/desactivar doctores.
Registrar usuarios administradores.

Los reportes no serán migrados en esta etapa.

## 11. Módulos del doctor

Según Principal.java, el doctor puede acceder a:

Pacientes.
Enfermedades.
Vacunas.
Citas.
Consultas.

Para esta conversión se tomarán:

Pacientes.
Enfermedades.
Vacunas.
Historial básico del paciente.

No se tomarán:

Citas.
Consultas.

## 12. Clase Persona

La clase Persona es una clase abstracta que contiene datos comunes de doctores y pacientes.

Sus atributos son:

cedula
nombre
apellido
telefono
direccion
fechaNacimiento
sexo

Las clases Doctor y Paciente heredan de Persona.

Aunque existe esta clase, para simplificar la conversión no se propone crear una tabla persona.

En lugar de eso, los datos personales estarán directamente dentro de las tablas:

doctor
paciente

Esto se hace porque el sistema original maneja doctores y pacientes como entidades separadas.

## 13. Clase User

La clase User representa las credenciales de acceso.

Tiene los atributos:

tipo
userName
pass

Esta clase se convertirá principalmente en la tabla:

usuario

El tipo de usuario puede ser:

Administrador
Doctor

## 14. Clase Doctor

La clase Doctor representa a los doctores registrados en el sistema.

Sus atributos principales son:

codigoDoctor
cedula
nombre
apellido
telefono
direccion
fechaNacimiento
sexo
especialidad
numeroLicencia
citasPorDia
horarioInicio
horarioFin
activo
usuario
contrasena

El doctor también puede iniciar sesión en el sistema.

Actualmente, cuando se registra un doctor, se crea un objeto Doctor y también un objeto User.

En base de datos, se evitará duplicar la información de usuario y contraseña dentro de varias tablas. Lo más conveniente es que las credenciales estén en la tabla usuario.

## 15. Clase Paciente

La clase Paciente representa a los pacientes registrados.

Sus atributos principales son:

codigoPaciente
cedula
nombre
apellido
telefono
direccion
fechaNacimiento
sexo
tipoSangre
fechaRegistro
activo
peso
estatura
doctorRegistrador
alergias
registrosVacunas
vacunasViejas

El paciente se relaciona con:

Un doctor registrador.
Varias alergias.
Varias vacunas aplicadas.
Varias vacunas previas.

## 16. Clase Alergia

La clase Alergia representa una alergia registrada en el sistema.

Sus atributos son:

nombre
tipo

Estas alergias se guardan de forma general y luego se pueden asignar a pacientes.

La ventana TomaAlergias.java permite seleccionar una o varias alergias para un paciente.

## 17. Clase Enfermedad

La clase Enfermedad representa una enfermedad registrada.

Sus atributos son:

codigoEnfermedad
nombre
bajoVigilancia
sintomasYSignos
nivelGravedad
potencialPropagacion
tipo

Esta clase se relaciona con las vacunas porque cada vacuna protege contra una enfermedad.

## 18. Clase Vacuna

La clase Vacuna representa las vacunas del inventario del sistema.

Sus atributos principales son:

codigoVacuna
numeroLote
nombre
cantidad
fechaCaducidad
laboratorio
enfermedad
activa

Estas vacunas sí pertenecen a la clínica y pueden administrarse a pacientes.

## 19. Clase RegistroVacuna

La clase RegistroVacuna representa una vacuna del sistema que fue aplicada a un paciente.

Tiene:

vacuna
fechaAplicacion
aplicadaPor

Actualmente, cada paciente tiene una lista de registros de vacunas.

En base de datos, esto se convertirá en una tabla llamada:

registro_vacuna

## 20. Clase VacunaVieja

La clase VacunaVieja representa vacunas previas del paciente.

Tiene:

enfermedad
fecha

Estas vacunas no pertenecen al inventario. Solo se usan como historial.

En base de datos, se propone una tabla:

vacuna_previa

## 21. Ventanas principales analizadas

Las ventanas principales que forman parte del alcance son:

regDoctor.java
modDoctor.java
listDoctor.java
regUser.java
regPaciente.java
listPacientes.java
regAlergia.java
TomaAlergias.java
verAlergias.java
regEnfermedades.java
listEnfermedad.java
regVacuna.java
listVacuna.java
adminVacuna.java
selecVacunas.java
regVacuViea.java
VerHistorialClinico.java

Estas ventanas son importantes porque muestran cómo el usuario interactúa con los datos.

## 22. Ventanas excluidas

Las ventanas excluidas son:

AgendarCita.java
ListaCitas.java
regConsulta.java
listConsulta.java
DetalleCita.java
DetalleConsulta.java
GestionTratamientos.java
regTratamiento.java
regMedicamento.java
Reportes.java
Gráficas

Estas quedan fuera por el tamaño del proyecto.

## 23. Conclusión

El sistema actual usa archivos .dat como si fueran una base de datos completa. La clase Clinica guarda listas de objetos y PersistenciaManager se encarga de escribir y leer esas listas desde archivos.

La conversión a MySQL requiere separar esas listas en tablas y reemplazar los métodos de guardado en archivos por operaciones SQL.

Este análisis permite entender qué datos existen, dónde se guardan actualmente y cómo se pueden transformar en tablas dentro de una base de datos relacional.
