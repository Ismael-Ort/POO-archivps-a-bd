\# Análisis inicial de conversión del proyecto POO a base de datos



\## Descripción del proyecto original



El proyecto original corresponde a un sistema de clínica desarrollado en Programación Orientada a Objetos. En su versión inicial, el sistema almacena información utilizando archivos locales, principalmente archivos con extensión `.dat`.



Dentro del proyecto se observan clases relacionadas con diferentes áreas del sistema, como pacientes, doctores, citas, consultas, usuarios, enfermedades, vacunas, tratamientos, historias clínicas y reportes. También existen archivos de datos como `clinica.dat`, `control.dat`, `clinica\_respaldo.dat` y otros archivos usados por el sistema.



\## Objetivo de la conversión



El objetivo de esta asignación es adaptar una parte del proyecto original para que utilice una base de datos relacional en lugar de depender únicamente de archivos.



Antes de modificar el código, es necesario revisar la estructura actual del proyecto para determinar cuál es la forma más conveniente de realizar la conversión.



\## Análisis pendiente del código



Todavía no se ha seleccionado de manera definitiva el módulo que será convertido. Primero se revisarán las clases principales del sistema para entender cómo se guarda, se carga y se manipula la información.



Esta revisión permitirá identificar si el manejo de archivos está centralizado en una sola clase o si está distribuido en varias partes del proyecto.



\## Opciones posibles



Para realizar la conversión se consideran varias alternativas:



\### Opción 1: Modificar la persistencia existente



Esta opción consiste en mantener la mayor parte del sistema igual y cambiar solamente la parte encargada de guardar y cargar los datos. En lugar de usar archivos `.dat`, esa parte pasaría a comunicarse con MariaDB.



Esta opción sería conveniente si el guardado de información está centralizado en una clase específica.



\### Opción 2: Convertir un módulo específico



Esta opción consiste en escoger una parte del sistema, por ejemplo pacientes, citas, doctores o consultas, y adaptar solamente ese módulo para que trabaje con base de datos.



Esta opción puede ser más manejable si el proyecto completo es muy grande o si modificar todo el sistema requiere demasiado tiempo.



\### Opción 3: Crear una parte nueva desde cero



Esta opción consiste en desarrollar un módulo nuevo conectado directamente a la base de datos, aprovechando la idea del proyecto original, pero sin depender totalmente del código viejo.



Esta alternativa puede ser útil si el código original resulta difícil de modificar o si la lógica de archivos está muy mezclada con las pantallas y clases del sistema.



\## Criterios para tomar la decisión



Para decidir qué opción conviene más, se tomarán en cuenta los siguientes criterios:



\- Qué tan centralizado está el manejo de archivos.

\- Qué clases dependen directamente de los archivos `.dat`.

\- Qué módulo es más fácil de aislar.

\- Qué parte permite demostrar mejor el uso de base de datos.

\- Qué cambios se pueden realizar dentro del tiempo disponible.

\- Qué opción permite mantener el sistema funcionando con menos errores.



\## Próximos pasos



1\. Revisar las clases principales del proyecto.

2\. Identificar dónde se guardan y cargan los datos.

3\. Determinar si conviene modificar la persistencia o trabajar un módulo aparte.

4\. Seleccionar el módulo o parte del sistema que será convertido.

5\. Diseñar las tablas necesarias en MariaDB.

6\. Crear la conexión entre Java y la base de datos.

7\. Probar la conversión realizada.



\## Conclusión inicial



En esta etapa todavía no se tomará una decisión definitiva sobre qué parte del sistema será convertida. Primero se analizará el código para elegir la opción más conveniente. La finalidad es realizar una conversión ordenada, funcional y posible de completar, sin modificar el sistema completo sin antes entender su estructura.





























\------------------------------------------------------------------------------------------------------------------------------------------------------------

CONTINUAMOS AQUI YA CON LA DECICIÓN....

