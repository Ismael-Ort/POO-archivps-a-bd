# 01 - Alcance de la Conversión

## 1. Objetivo del documento

Este documento define el alcance de la conversión del sistema de clínica de archivos `.dat` a base de datos MySQL.

El sistema original fue desarrollado en Java utilizando ventanas Swing y almacenamiento por archivos serializados. Aunque el sistema contiene varios módulos, no todos serán migrados en esta etapa, ya que convertir todo el proyecto completo haría que el trabajo fuera demasiado extenso y difícil de manejar.

Por esa razón, en este documento se deja claro:

- Qué módulos sí forman parte de la conversión.
- Qué módulos quedan fuera.
- Por qué algunos módulos fueron excluidos.
- Qué flujo principal será tomado en cuenta.
- Qué roles del sistema se manejarán.
- Qué operaciones básicas deberá soportar la base de datos.

La finalidad es que cualquier integrante del equipo pueda entender rápidamente qué se va a trabajar y qué no se va a trabajar en esta primera migración.

---

## 2. Descripción general del sistema original

El sistema original es una aplicación de gestión clínica desarrollada en Java Swing.

El programa permite manejar diferentes áreas de una clínica, como:

- Inicio de sesión.
- Administración de usuarios.
- Registro de doctores.
- Registro de pacientes.
- Registro de alergias.
- Registro de enfermedades.
- Registro de vacunas.
- Administración de vacunas a pacientes.
- Historial clínico.
- Citas.
- Consultas.
- Tratamientos.
- Medicamentos.
- Reportes y gráficas.

Sin embargo, el sistema original guarda la información en archivos `.dat`, usando serialización de objetos. Esto significa que los datos no están almacenados en tablas, sino dentro de objetos completos guardados en archivos.

La conversión busca reemplazar gradualmente esa forma de guardar información por una base de datos MySQL.

---

## 3. Objetivo de la conversión

El objetivo principal de esta conversión es migrar la persistencia de datos desde archivos `.dat` hacia una base de datos MySQL.

Esto significa que el sistema debe dejar de depender de archivos como:

```text
control.dat
clinica.dat y comenzar a guardar, buscar, actualizar y eliminar información usando tablas de base de datos.

La intención no es rehacer el sistema completo desde cero. La idea es mantener las ventanas existentes en la medida de lo posible y modificar la parte interna que guarda y carga los datos.

En otras palabras:

Antes:
Ventanas Java Swing → Objetos en memoria → Archivos .dat

Después:
Ventanas Java Swing → Clases de base de datos → MySQL

## 4. Criterio usado para definir el alcance

Para decidir qué módulos entrarían en esta etapa, se tomaron en cuenta tres criterios principales:

4.1 Importancia del módulo

Se eligieron los módulos más importantes para que el sistema pueda funcionar de forma básica:

Login.
Usuarios.
Doctores.
Pacientes.
Alergias.
Enfermedades.
Vacunas.

Estos módulos forman la base del sistema clínico.

4.2 Relación directa con los datos principales

Se priorizaron los módulos que manejan información clara y estructurada, fácil de convertir a tablas.

Por ejemplo:

Doctor se puede convertir en una tabla doctor.
Paciente se puede convertir en una tabla paciente.
Vacuna se puede convertir en una tabla vacuna.
Enfermedad se puede convertir en una tabla enfermedad.

4.3 Tamaño del proyecto

El sistema tiene módulos adicionales como citas, consultas, tratamientos y reportes. Aunque son importantes en el sistema original, migrarlos en esta primera etapa haría que el trabajo fuera demasiado largo.

Por esa razón, se decidió excluir esos módulos para que la conversión sea más realista y manejable.

## 5. Módulos incluidos en la conversión

Los módulos que sí serán migrados a base de datos son los siguientes:

5.1 Login

El login será migrado para que la validación de usuario y contraseña se realice contra la base de datos.

Actualmente el sistema valida el login usando información cargada desde archivos .dat.

En la nueva versión, el flujo será:

Usuario escribe username y password
→ El sistema consulta la tabla usuario
→ Si los datos son correctos, permite entrar
→ Si el usuario es administrador, abre el menú de administrador
→ Si el usuario es doctor, abre el menú de doctor

Este módulo es obligatorio porque sin login no se puede acceder al sistema.

5.2 Usuarios administradores

El sistema permite registrar usuarios administradores.

Estos usuarios no están asociados a un doctor. Su función es administrar áreas generales del sistema, especialmente el registro de doctores y otros usuarios administradores.

En base de datos, estos usuarios se guardarán en la tabla usuario.

Ejemplo:

tipo = Administrador
username = Admin
password = Admin
id_doctor = NULL

5.3 Doctores

El módulo de doctores sí será migrado.

Incluye:

Registrar doctores.
Listar doctores.
Buscar doctores.
Modificar doctores.
Activar o desactivar doctores.

El doctor tiene datos personales y datos laborales, como:

Código.
Cédula.
Nombre.
Apellido.
Teléfono.
Dirección.
Fecha de nacimiento.
Sexo.
Especialidad.
Número de licencia médica.
Citas por día.
Horario de inicio.
Horario de fin.
Estado activo o inactivo.

Además, cada doctor puede tener un usuario para iniciar sesión en el sistema.

5.4 Pacientes

El módulo de pacientes sí será migrado.

Incluye:

Registrar pacientes.
Listar pacientes.
Buscar pacientes.
Modificar datos permitidos.
Relacionar pacientes con el doctor que los registró.
Relacionar pacientes con alergias.
Relacionar pacientes con vacunas aplicadas.
Relacionar pacientes con vacunas previas.

El paciente tiene datos como:

Código.
Cédula.
Nombre.
Apellido.
Teléfono.
Dirección.
Fecha de nacimiento.
Sexo.
Tipo de sangre.
Fecha de registro.
Peso.
Estatura.
Estado activo.
Doctor registrador.

5.5 Alergias

El módulo de alergias sí será migrado.

El sistema permite registrar alergias generales y luego asignarlas a los pacientes.

Por ejemplo, se pueden registrar alergias como:

Polen
Penicilina
Maní
Polvo
Mariscos

Cada alergia tiene:

Nombre.
Tipo.

El tipo puede ser:

Alimento.
Medicamento.
Ambiental.
Animal.
Contacto.

Un paciente puede tener muchas alergias, y una misma alergia puede pertenecer a varios pacientes.

Por eso se necesita una relación muchos a muchos entre pacientes y alergias.

5.6 Enfermedades

El módulo de enfermedades sí será migrado.

El sistema permite registrar enfermedades y marcar si están bajo vigilancia.

Cada enfermedad tiene:

Código.
Nombre.
Estado de vigilancia.
Síntomas y signos.
Nivel de gravedad.
Potencial de propagación.
Tipo de enfermedad.

Las enfermedades son importantes porque las vacunas del sistema se relacionan con una enfermedad.

Por ejemplo:

Vacuna: Hepatitis B
Enfermedad que previene: Hepatitis B

5.7 Vacunas del sistema

El módulo de vacunas del sistema sí será migrado.

Estas vacunas representan el inventario de vacunas disponibles en la clínica.

Cada vacuna tiene:

Código.
Número de lote.
Nombre.
Cantidad disponible.
Fecha de caducidad.
Laboratorio.
Enfermedad que previene.
Estado activo o inactivo.

Estas vacunas sí pertenecen al inventario de la clínica.

Cuando una vacuna se administra a un paciente, se debe reducir su cantidad disponible.

5.8 Administración de vacunas a pacientes

Este módulo sí será migrado.

Permite seleccionar un paciente y una vacuna disponible para registrar que esa vacuna fue aplicada.

El sistema debe validar que:

El paciente exista.
La vacuna exista.
La vacuna esté activa.
La vacuna no esté caducada.
La vacuna tenga stock disponible.
Se indique quién aplicó la vacuna.
Se indique la fecha de aplicación.

Cuando se administra una vacuna:

1. Se crea un registro de vacuna aplicada.
2. Se relaciona el paciente con la vacuna.
3. Se guarda la fecha de aplicación.
4. Se guarda quién la aplicó.
5. Se reduce el stock de la vacuna.

5.9 Vacunas previas del paciente

Este módulo sí será migrado.

Las vacunas previas son vacunas que el paciente ya tenía aplicadas antes de ser registrado en el sistema.

Estas vacunas son diferentes a las vacunas del sistema.

Una vacuna previa:

No pertenece al inventario.
No tiene código de vacuna del sistema.
No tiene lote real.
No tiene laboratorio.
No reduce stock.
Solo se guarda como información histórica del paciente.

Ejemplo:

Paciente: Juan Pérez
Vacuna previa: Sarampión
Fecha de aplicación: 2020-05-10
Lote: Desconocido

En base de datos se manejarán en una tabla separada llamada vacuna_previa.

5.10 Historial básico del paciente

El historial básico sí será tomado en cuenta, pero solamente para mostrar información relacionada con:

Datos generales del paciente.
Alergias.
Vacunas aplicadas desde el sistema.
Vacunas previas.
Vacunas pendientes.

Aunque la clase VerHistorialClinico.java contiene partes relacionadas con consultas médicas y resumen automático, esas secciones no serán migradas en esta etapa.

## 6. Operaciones incluidas

La base de datos debe permitir las siguientes operaciones:

Registrar
Listar
Buscar
Modificar
Activar
Desactivar
Consultar historial básico

Estas operaciones aplican según el módulo.

Por ejemplo:

Doctores: registrar, listar, buscar, modificar, activar/desactivar.
Pacientes: registrar, listar, modificar, consultar historial.
Enfermedades: registrar, listar, filtrar, modificar vigilancia.
Vacunas: registrar, listar, buscar, validar stock.
Alergias: registrar, listar, asignar a pacientes.

## 7. Módulos excluidos de la conversión

Los siguientes módulos no serán migrados en esta etapa:

Agendar cita.
Listar cita.
Registrar consulta.
Listar consulta.
Detalle de cita.
Detalle de consulta.
Tratamientos.
Medicamentos.
Gestión de tratamientos.
Reportes avanzados.
Gráficas.

## 8. Razón por la cual se excluyen citas y consultas

Los módulos de citas y consultas hacen que el proyecto sea mucho más grande.

Para migrar consultas correctamente habría que incluir:

Citas.
Consultas.
Diagnósticos.
Doctores que atienden consultas.
Historial clínico completo.
Enfermedades bajo vigilancia dentro de consultas.
Resumen automático.
Detalles de consulta.
Tratamientos.
Medicamentos.

Eso aumentaría demasiado el tamaño del proyecto.

Por esta razón, se decidió que en esta etapa solo se trabajará con el historial básico relacionado con pacientes, alergias y vacunas.

## 9. Roles del sistema

El sistema manejará dos roles principales:

9.1 Administrador

El administrador puede acceder a:

Registro de doctores.
Listado de doctores.
Modificación de doctores.
Activación y desactivación de doctores.
Registro de usuarios administradores.

El administrador no se enfoca en registrar pacientes ni administrar vacunas.

9.2 Doctor

El doctor puede acceder a:

Registro de pacientes.
Listado de pacientes.
Registro de alergias.
Selección de alergias para pacientes.
Registro de enfermedades.
Listado de enfermedades.
Registro de vacunas.
Listado de vacunas.
Administración de vacunas a pacientes.
Historial básico del paciente.

## 10. Flujo principal incluido

El flujo principal del sistema que será migrado es:

Login
→ Principal
→ Según el tipo de usuario:
   → Administrador
      → Doctores
      → Usuarios administradores
   → Doctor
      → Pacientes
      → Alergias
      → Enfermedades
      → Vacunas
      → Administración de vacunas
      → Historial básico
      
## 11. Resultado esperado del alcance

Al terminar esta etapa, el sistema debe poder funcionar usando base de datos para los módulos principales.

El resultado esperado es:

Poder iniciar sesión con usuarios guardados en MySQL.
Poder registrar administradores.
Poder registrar doctores.
Poder listar y modificar doctores.
Poder activar o desactivar doctores.
Poder registrar pacientes.
Poder asignar alergias a pacientes.
Poder registrar enfermedades.
Poder registrar vacunas del sistema.
Poder administrar vacunas a pacientes.
Poder registrar vacunas previas del paciente.
Poder consultar un historial básico del paciente.

## 12. Conclusión

El alcance de esta conversión fue definido para mantener el proyecto claro y manejable.

Se decidió trabajar con los módulos principales del sistema y dejar fuera las áreas de citas, consultas, tratamientos, medicamentos y reportes avanzados.

Esta decisión permite enfocar el trabajo en la conversión real de archivos .dat a base de datos MySQL, sin convertir el proyecto en un sistema demasiado grande para esta etapa.
