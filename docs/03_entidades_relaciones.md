# 03 - Entidades y Relaciones Propuestas

## 1. Objetivo del documento

Este documento presenta las entidades principales que se proponen para la base de datos del sistema de clínica.

Las entidades fueron definidas a partir del análisis del sistema original, sus clases Java y las ventanas que forman parte del alcance de la conversión.

El objetivo es dejar claro:

- Qué tablas se necesitan.
- Qué representa cada tabla.
- Qué atributos debe tener cada una.
- Qué relaciones existen entre ellas.
- Por qué se necesita cada relación.

---

## 2. Entidades principales propuestas

Las entidades principales para esta etapa son:

usuario
doctor
paciente
alergia
paciente_alergia
enfermedad
vacuna
registro_vacuna
vacuna_previa

Estas tablas cubren los módulos definidos en el alcance:

Login.
Usuarios administradores.
Doctores.
Pacientes.
Alergias.
Enfermedades.
Vacunas.
Administración de vacunas.
Historial básico.

## 3. Tabla usuario
3.1 Descripción

La tabla usuario almacena las credenciales de acceso al sistema.

En el sistema original, esta información está en la clase User, la cual tiene:

tipo
userName
pass

En base de datos, esta tabla permitirá validar el login.

3.2 Campos propuestos
id_usuario
tipo
username
password
id_doctor
activo

3.3 Explicación de campos
id_usuario

Identificador único del usuario en la base de datos.

Será la clave primaria.

tipo

Indica el tipo de usuario.

Valores esperados:

Administrador
Doctor
username

Nombre de usuario utilizado para iniciar sesión.

Debe ser único.

password

Contraseña del usuario.

En el sistema original se guarda como texto normal. Para esta etapa se puede mantener así por simplicidad, aunque en un sistema real debería guardarse cifrada o encriptada.

id_doctor

Relaciona el usuario con un doctor.

Este campo será nulo cuando el usuario sea administrador.

Cuando el usuario sea doctor, este campo apuntará al doctor correspondiente.

activo

Indica si el usuario está activo.

Esto permite desactivar usuarios sin eliminarlos.

3.4 Reglas
Un username no puede repetirse.
Un administrador no necesita estar relacionado con un doctor.
Un usuario de tipo doctor sí debe relacionarse con un doctor.
El login debe consultar esta tabla.

## 4. Tabla doctor
4.1 Descripción

La tabla doctor almacena la información de los doctores registrados.

Proviene de la clase Doctor, que hereda de Persona.

4.2 Campos propuestos
id_doctor
codigo_doctor
cedula
nombre
apellido
telefono
direccion
fecha_nacimiento
sexo
especialidad
numero_licencia
citas_por_dia
horario_inicio
horario_fin
activo
4.3 Explicación de campos
id_doctor

Identificador interno de la base de datos.

codigo_doctor

Código usado por el sistema, por ejemplo:

DOC-1
DOC-2

Debe ser único.

cedula

Cédula del doctor.

Debe ser única.

nombre y apellido

Datos personales del doctor.

telefono

Teléfono del doctor.

Puede usarse para contacto y también se valida para evitar duplicados.

direccion

Dirección del doctor.

fecha_nacimiento

Fecha de nacimiento del doctor.

sexo

Sexo del doctor.

Valores esperados:

M
F
especialidad

Especialidad médica del doctor.

Ejemplos:

Cardiología
Pediatría
Medicina General
Neurología
numero_licencia

Número de licencia médica.

Debe ser único porque identifica legalmente al doctor.

citas_por_dia

Cantidad máxima de citas que el doctor puede atender por día.

Aunque las citas no serán migradas, este campo se mantiene porque forma parte del registro del doctor.

horario_inicio y horario_fin

Horario laboral del doctor.

activo

Indica si el doctor está activo o inactivo.

4.4 Reglas
No puede haber dos doctores con la misma cédula.
No puede haber dos doctores con la misma licencia.
No puede haber dos doctores con el mismo código.
Un doctor puede activarse o desactivarse.
Un doctor puede tener un usuario para iniciar sesión.

## 5. Tabla paciente
5.1 Descripción

La tabla paciente almacena los datos de los pacientes registrados en el sistema.

Proviene de la clase Paciente, que también hereda de Persona.

5.2 Campos propuestos
id_paciente
codigo_paciente
cedula
nombre
apellido
telefono
direccion
fecha_nacimiento
sexo
tipo_sangre
fecha_registro
activo
peso
estatura
id_doctor_registrador
5.3 Explicación de campos
id_paciente

Identificador interno del paciente en la base de datos.

codigo_paciente

Código usado por el sistema, por ejemplo:

PAC-1
PAC-2

Debe ser único.

cedula

Cédula del paciente.

Debe ser única.

nombre y apellido

Datos personales del paciente.

telefono

Teléfono del paciente.

direccion

Dirección del paciente.

fecha_nacimiento

Fecha de nacimiento del paciente.

sexo

Sexo del paciente.

Valores esperados:

M
F
tipo_sangre

Tipo de sangre del paciente.

Ejemplos:

A+
A-
B+
B-
AB+
AB-
O+
O-
fecha_registro

Fecha en que el paciente fue registrado en el sistema.

activo

Indica si el paciente está activo.

peso

Peso del paciente.

estatura

Estatura del paciente.

id_doctor_registrador

Doctor que registró al paciente.

En el sistema original se guarda como doctorRegistrador, usando el número de licencia del doctor. En base de datos se recomienda guardar la relación directa con la tabla doctor.

5.4 Reglas
No puede haber dos pacientes con la misma cédula.
No puede haber dos pacientes con el mismo código.
Un paciente puede estar activo o inactivo.
Un paciente puede tener muchas alergias.
Un paciente puede tener muchas vacunas aplicadas.
Un paciente puede tener muchas vacunas previas.
Un paciente debe estar relacionado con el doctor que lo registró, cuando aplique.

## 6. Tabla alergia
6.1 Descripción

La tabla alergia almacena las alergias generales registradas en el sistema.

Una alergia registrada puede luego asignarse a uno o varios pacientes.

6.2 Campos propuestos
id_alergia
nombre
tipo
6.3 Explicación de campos
id_alergia

Identificador único de la alergia.

nombre

Nombre del alérgeno.

Ejemplos:

Polen
Penicilina
Maní
Polvo
Mariscos
tipo

Tipo de alergia.

Ejemplos:

Alimento
Medicamento
Ambiental
Animal
Contacto
6.4 Reglas
Una alergia no debería repetirse con el mismo nombre.
Una alergia puede estar asociada a varios pacientes.
Un paciente puede tener varias alergias.

## 7. Tabla paciente_alergia
7.1 Descripción

Esta tabla representa la relación entre pacientes y alergias.

Se necesita porque la relación es muchos a muchos.

Es decir:

Un paciente puede tener muchas alergias.
Una alergia puede estar registrada en muchos pacientes.
7.2 Campos propuestos
id_paciente
id_alergia
7.3 Reglas
No se debe repetir la misma alergia para el mismo paciente.
Cada registro debe apuntar a un paciente existente.
Cada registro debe apuntar a una alergia existente.

## 8. Tabla enfermedad
8.1 Descripción

La tabla enfermedad almacena las enfermedades registradas en el sistema.

Las enfermedades son importantes porque las vacunas del sistema se relacionan con ellas.

8.2 Campos propuestos
id_enfermedad
codigo_enfermedad
nombre
bajo_vigilancia
sintomas_y_signos
nivel_gravedad
potencial_propagacion
tipo
8.3 Explicación de campos
id_enfermedad

Identificador interno.

codigo_enfermedad

Código usado por el sistema, por ejemplo:

ENF-1
ENF-2

Debe ser único.

nombre

Nombre de la enfermedad.

bajo_vigilancia

Indica si la enfermedad está bajo vigilancia.

sintomas_y_signos

Descripción de síntomas y signos asociados a la enfermedad.

nivel_gravedad

Nivel de gravedad de la enfermedad.

En el sistema se maneja de 1 a 4.

potencial_propagacion

Indica el potencial de propagación.

Ejemplos:

Bajo
Leve
Medio
Elevado
Alto
tipo

Tipo de enfermedad.

Ejemplos:

Infecciosa
Cardiovascular
Respiratoria
Neurologica
Dermatologica
8.4 Reglas
No debe repetirse el código de enfermedad.
No debería repetirse el nombre de enfermedad.
Una enfermedad puede estar bajo vigilancia.
Una enfermedad puede tener varias vacunas asociadas.

## 9. Tabla vacuna
9.1 Descripción

La tabla vacuna almacena las vacunas del sistema.

Estas vacunas representan el inventario disponible en la clínica.

9.2 Campos propuestos
id_vacuna
codigo_vacuna
numero_lote
nombre
cantidad
fecha_caducidad
laboratorio
id_enfermedad
activa
9.3 Explicación de campos
id_vacuna

Identificador interno de la vacuna.

codigo_vacuna

Código usado por el sistema.

Ejemplo:

VAC-1
VAC-2

Debe ser único.

numero_lote

Número de lote de la vacuna.

Debe ser único.

nombre

Nombre de la vacuna.

cantidad

Stock disponible.

Cuando una vacuna se administra a un paciente, esta cantidad debe disminuir.

fecha_caducidad

Fecha de vencimiento de la vacuna.

laboratorio

Laboratorio que produjo la vacuna.

id_enfermedad

Enfermedad que previene la vacuna.

Este campo reemplaza el texto simple usado en la clase Vacuna.

activa

Indica si la vacuna está activa.

9.4 Reglas
No puede repetirse el código de vacuna.
No puede repetirse el número de lote.
La vacuna debe tener una enfermedad asociada.
La cantidad no puede ser negativa.
Una vacuna vencida no debe administrarse.
Una vacuna sin stock no debe aparecer como disponible.
Una vacuna inactiva no debe administrarse.

## 10. Tabla registro_vacuna
10.1 Descripción

La tabla registro_vacuna almacena las vacunas del sistema que han sido aplicadas a pacientes.

Representa la relación entre:

Paciente
Vacuna
Fecha de aplicación
Persona que la aplicó
10.2 Campos propuestos
id_registro_vacuna
id_paciente
id_vacuna
fecha_aplicacion
aplicada_por
10.3 Explicación de campos
id_registro_vacuna

Identificador único del registro.

id_paciente

Paciente que recibió la vacuna.

id_vacuna

Vacuna aplicada.

fecha_aplicacion

Fecha en que se aplicó la vacuna.

aplicada_por

Nombre de la persona que administró la vacuna.

En el sistema actual este dato se escribe manualmente.

10.4 Reglas
Debe existir el paciente.
Debe existir la vacuna.
La vacuna debe estar activa.
La vacuna no debe estar caducada.
La vacuna debe tener stock.
Después de registrar la aplicación, se debe descontar una unidad del stock.

## 11. Tabla vacuna_previa
11.1 Descripción

La tabla vacuna_previa almacena las vacunas que el paciente ya tenía antes de ser registrado en el sistema.

Estas vacunas no pertenecen al inventario de la clínica.

11.2 Campos propuestos
id_vacuna_previa
id_paciente
enfermedad
fecha_aplicacion
11.3 Explicación de campos
id_vacuna_previa

Identificador único.

id_paciente

Paciente al que pertenece la vacuna previa.

enfermedad

Enfermedad que prevenía la vacuna.

En este caso se guarda como texto porque no necesariamente corresponde a una vacuna registrada en el sistema.

fecha_aplicacion

Fecha en que el paciente recibió esa vacuna.

11.4 Reglas
La vacuna previa no reduce stock.
La vacuna previa no pertenece al inventario.
La vacuna previa no necesita lote.
La vacuna previa se muestra en historial con lote desconocido.
Un paciente puede tener varias vacunas previas.

## 12. Relaciones principales
12.1 Doctor y Usuario
Doctor 1 ─── 0..1 Usuario

Un doctor puede tener un usuario para iniciar sesión.

Un usuario administrador no pertenece a ningún doctor, por eso id_doctor puede ser nulo en la tabla usuario.

12.2 Doctor y Paciente
Doctor 1 ─── N Paciente

Un doctor puede registrar muchos pacientes.

Un paciente puede tener un doctor registrador.

12.3 Paciente y Alergia
Paciente N ─── M Alergia

Un paciente puede tener varias alergias.

Una alergia puede pertenecer a varios pacientes.

Se resuelve con la tabla intermedia:

paciente_alergia
12.4 Enfermedad y Vacuna
Enfermedad 1 ─── N Vacuna

Una enfermedad puede tener varias vacunas asociadas.

Una vacuna pertenece a una enfermedad.

12.5 Paciente y Registro_Vacuna
Paciente 1 ─── N Registro_Vacuna

Un paciente puede recibir varias vacunas del sistema.

12.6 Vacuna y Registro_Vacuna
Vacuna 1 ─── N Registro_Vacuna

Una vacuna puede ser aplicada a muchos pacientes.

12.7 Paciente y Vacuna_Previa
Paciente 1 ─── N Vacuna_Previa

Un paciente puede tener muchas vacunas previas registradas.

## 13. Conclusión

Las entidades propuestas permiten convertir los módulos principales del sistema a una base de datos relacional.

El diseño separa claramente:

Usuarios.
Doctores.
Pacientes.
Alergias.
Enfermedades.
Vacunas del inventario.
Vacunas aplicadas.
Vacunas previas.

Esta separación permite reemplazar las listas internas del sistema por tablas organizadas en MySQL.
