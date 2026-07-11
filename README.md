# Propuesta de Conversión del Proyecto POO a Base de Datos

## Título de la propuesta

Conversión del módulo de pacientes, doctores, usuarios y datos clínicos del proyecto POO de archivos `.dat` a base de datos relacional.

---

## Descripción general

El proyecto base ya cuenta con una aplicación funcional desarrollada en Java utilizando Programación Orientada a Objetos. Actualmente, el sistema almacena la información mediante archivos `.dat`, lo cual permite guardar datos, pero dificulta la organización, consulta, mantenimiento y relación entre la información.

La propuesta consiste en adaptar una parte específica del sistema para que deje de depender de archivos `.dat` y pase a utilizar una base de datos relacional. El objetivo no es rehacer todo el programa desde cero, sino conservar las ventanas y funcionalidades existentes que sean útiles, reemplazando principalmente la lógica de almacenamiento y consulta de datos.

En los casos donde la lógica actual esté demasiado acoplada a archivos o sea difícil de adaptar, se desarrollará una nueva lógica de persistencia desde cero utilizando conexión a base de datos.

---

## Alcance de la conversión

La conversión se enfocará en los módulos relacionados con:

- Login del sistema.
- Usuarios administradores.
- Doctores.
- Pacientes.
- Alergias.
- Vacunas.
- Enfermedades.
- Relación entre pacientes y alergias.
- Relación entre pacientes y vacunas.

---

## Funcionalidades principales

El sistema adaptado deberá permitir:

1. Iniciar sesión con usuario y contraseña.
2. Tener un usuario predeterminado:
   - Usuario: `Admin`
   - Contraseña: `Admin`
3. Abrir la ventana principal si el login es correcto.
4. Gestionar doctores desde la ventana principal.
5. Registrar y listar pacientes.
6. Asociar alergias a un paciente.
7. Asociar vacunas a un paciente.
8. Relacionar cada vacuna con una enfermedad.
9. Crear nuevos usuarios administradores desde una ventana de administración.

---

## Tablas propuestas para la base de datos

Para organizar la información, se propone utilizar las siguientes tablas:

- Persona.
- Doctor.
- Paciente.
- Usuario.
- Alergia.
- Vacuna.
- Enfermedad.
- Paciente_Alergia.
- Paciente_Vacuna.

---

## Justificación de las tablas

### Persona

La tabla Persona almacenará los datos generales que pueden compartir doctores y pacientes, como nombre, apellido, cédula, teléfono, sexo, fecha de nacimiento u otros datos personales.

Se utiliza para evitar repetir la misma información en las tablas Doctor y Paciente.

### Doctor

La tabla Doctor almacenará la información específica de los doctores. Cada doctor estará asociado a una persona.

### Paciente

La tabla Paciente almacenará la información específica de los pacientes. Cada paciente estará asociado a una persona.

### Usuario

La tabla Usuario controlará el acceso al sistema mediante usuario y contraseña. Permitirá mantener el usuario predeterminado `Admin / Admin` y crear nuevos usuarios administradores.

### Alergia

La tabla Alergia almacenará los tipos de alergias que pueden ser asignadas a los pacientes.

### Vacuna

La tabla Vacuna almacenará las vacunas disponibles en el sistema.

### Enfermedad

La tabla Enfermedad almacenará las enfermedades relacionadas con las vacunas.

### Paciente_Alergia

Esta tabla intermedia permitirá asociar pacientes con alergias, ya que un paciente puede tener varias alergias y una alergia puede aparecer en varios pacientes.

### Paciente_Vacuna

Esta tabla intermedia permitirá asociar pacientes con vacunas, ya que un paciente puede tener varias vacunas y una vacuna puede ser aplicada a muchos pacientes.

---

## Relaciones principales

Las relaciones principales del diseño serán:

```text
Persona 1 ─── 0..1 Doctor
Persona 1 ─── 0..1 Paciente

Paciente 1 ─── N Paciente_Alergia
Alergia 1 ─── N Paciente_Alergia

Paciente 1 ─── N Paciente_Vacuna
Vacuna 1 ─── N Paciente_Vacuna

Enfermedad 1 ─── N Vacuna

Usuario controla el login del sistema
