# 05 - Configuración de Base de Datos

## 1. Objetivo del documento

Este documento explica cómo debe configurarse la conexión a la base de datos MySQL del proyecto.

La finalidad es que cualquier integrante del equipo pueda clonar el repositorio, crear su archivo de configuración local y probar la conexión sin subir credenciales sensibles a GitHub.

---

## 2. Base de datos utilizada

El proyecto utilizará MySQL como sistema de base de datos.

La conexión se realiza mediante JDBC y MySQL Connector/J.

Actualmente la conexión fue probada usando una base de datos MySQL alojada en Aiven.

---

## 3. Clase de conexión

La clase encargada de conectarse a la base de datos es:
ConexionBD.java

Esta clase se encuentra en el paquete:

javaBD

Su función es:

1. Leer el archivo db.properties.
2. Obtener host, puerto, base de datos, usuario, contraseña y modo SSL.
3. Construir la URL de conexión JDBC.
4. Cargar el driver de MySQL.
5. Retornar un objeto Connection.

La conexión no debe tener la contraseña escrita directamente dentro del código Java.

## 4. Archivo db.properties

El archivo real de conexión debe llamarse:

db.properties

Debe estar ubicado en:

ProyectoFinalICC104/config/db.properties

Este archivo contiene información sensible, por ejemplo:

Host.
Puerto.
Nombre de la base de datos.
Usuario.
Contraseña.
Modo SSL.

Por esa razón, este archivo no debe subirse a GitHub.

## 5. Formato del archivo db.properties

El archivo debe tener esta estructura:

db.host=HOST_DE_LA_BASE_DE_DATOS
db.port=PUERTO
db.database=NOMBRE_BASE_DE_DATOS
db.user=USUARIO
db.password=PASSWORD_REAL
db.sslMode=REQUIRED

Ejemplo de estructura:

db.host=mi-host.aivencloud.com
db.port=12345
db.database=defaultdb
db.user=avnadmin
db.password=mi-password-real
db.sslMode=REQUIRED

No se deben usar comillas.

Incorrecto:

db.user="avnadmin"

Correcto:

db.user=avnadmin

## 6. Archivo db.example.properties

Como db.properties no se sube a GitHub, se debe incluir un archivo de ejemplo llamado:

db.example.properties

Ubicación:

ProyectoFinalICC104/config/db.example.properties

Contenido recomendado:

db.host=HOST_DE_LA_BASE_DE_DATOS
db.port=PUERTO
db.database=NOMBRE_BASE_DE_DATOS
db.user=USUARIO
db.password=CAMBIAR_POR_PASSWORD_REAL
db.sslMode=REQUIRED

Este archivo sí se puede subir al repositorio porque no contiene credenciales reales.

## 7. Cómo debe configurarlo cada integrante

Cada integrante del equipo debe hacer lo siguiente:

1. Clonar el repositorio.
2. Entrar a ProyectoFinalICC104/config.
3. Copiar db.example.properties.
4. Renombrar la copia como db.properties.
5. Completar los datos reales de conexión.
6. Ejecutar la prueba de conexión.

Ejemplo:

db.example.properties → db.properties

El archivo real db.properties queda solamente en la computadora de cada integrante.

## 8. .gitignore

El archivo .gitignore debe ignorar el archivo real de configuración.

Debe contener:

ProyectoFinalICC104/config/db.properties
.env

Esto evita que se suban contraseñas al repositorio.

El archivo que sí debe subirse es:

ProyectoFinalICC104/config/db.example.properties

## 9. MySQL Connector/J

Para que Java pueda conectarse a MySQL, el proyecto necesita MySQL Connector/J.

El archivo usado es:

mysql-connector-j-9.7.0.jar

Este archivo debe estar agregado como librería del proyecto.

En IntelliJ IDEA, se agrega desde:

File
→ Project Structure
→ Modules
→ Dependencies
→ +
→ JARs or Directories

Luego se selecciona el archivo .jar.

## 10. Prueba de conexión

Existe una clase de prueba llamada:

PruebaConexionAiven.java

Esta clase permite verificar si la conexión funciona.

La salida esperada es:

Conexión exitosa.
Versión MySQL: 8.4.8

Si aparece ese mensaje, significa que:

El archivo db.properties fue leído correctamente.
El driver de MySQL está funcionando.
Las credenciales son correctas.
La base de datos acepta la conexión.
El proyecto puede comunicarse con MySQL.

## 11. Errores comunes
11.1 Archivo db.properties no encontrado

Puede pasar si el archivo no está en la ruta correcta.

Ruta esperada:

ProyectoFinalICC104/config/db.properties
11.2 Contraseña incorrecta

Si la contraseña está mal, la conexión fallará.

Hay que revisar que no tenga espacios extra, comillas o caracteres copiados incorrectamente.

11.3 Puerto incorrecto

El puerto debe ser exactamente el que indica el servicio de base de datos.

11.4 Driver no agregado

Si el .jar de MySQL Connector/J no está agregado, puede aparecer un error relacionado con:

com.mysql.cj.jdbc.Driver
11.5 SSL requerido

Como la base de datos usa SSL, debe mantenerse:

db.sslMode=REQUIRED

## 12. Seguridad

No se deben subir contraseñas reales al repositorio.

Si por error se sube una contraseña a GitHub, lo recomendable es:

1. Cambiar la contraseña en el servicio de base de datos.
2. Actualizar el db.properties local.
3. Avisar al equipo.
4. Verificar que el archivo quede ignorado por Git.

Esto es importante porque aunque después se borre el archivo, puede quedar registrado en el historial de Git.

## 13. Qué debe hacer un compañero para ejecutar el proyecto

Un compañero que clone el proyecto debe hacer esto:

1. Hacer git pull o clonar el repositorio.
2. Verificar que tiene el conector MySQL en el proyecto.
3. Crear su archivo db.properties a partir de db.example.properties.
4. Completar las credenciales reales.
5. Ejecutar PruebaConexionAiven.java.
6. Si la conexión es exitosa, continuar con las pruebas del sistema.
   
## 14. Conclusión

La configuración de base de datos debe manejarse de forma segura.

El repositorio debe contener instrucciones y un archivo de ejemplo, pero nunca debe contener contraseñas reales.

Con esta configuración, cada integrante puede trabajar en su computadora conectándose a la misma base de datos sin exponer información sensible en GitHub.
