CREATE TABLE doctor (
                        id_doctor INT AUTO_INCREMENT,
                        codigo_doctor VARCHAR(20) NOT NULL,
                        cedula VARCHAR(11) NOT NULL,
                        nombres VARCHAR(100) NOT NULL,
                        apellidos VARCHAR(100) NOT NULL,
                        telefono VARCHAR(10) NOT NULL,
                        direccion VARCHAR(255) NOT NULL,
                        fecha_nacimiento DATE NOT NULL,
                        sexo CHAR(1) NOT NULL,
                        especialidad VARCHAR(100) NOT NULL,
                        numero_licencia VARCHAR(50) NOT NULL,
                        citas_por_dia INT NOT NULL,
                        horario_inicio TIME NOT NULL,
                        horario_fin TIME NOT NULL,
                        activo BOOLEAN NOT NULL DEFAULT TRUE,

                        CONSTRAINT pk_doctor PRIMARY KEY (id_doctor),
                        CONSTRAINT uq_doctor_codigo UNIQUE (codigo_doctor),
                        CONSTRAINT uq_doctor_cedula UNIQUE (cedula),
                        CONSTRAINT uq_doctor_telefono UNIQUE (telefono),
                        CONSTRAINT uq_doctor_numero_licencia UNIQUE (numero_licencia),
                        CONSTRAINT chk_doctor_sexo CHECK (sexo IN ('M', 'F')),
                        CONSTRAINT chk_doctor_citas_por_dia CHECK (citas_por_dia BETWEEN 1 AND 20),
                        CONSTRAINT chk_doctor_horario CHECK (horario_fin > horario_inicio)
);


CREATE TABLE usuario (
                         id_usuario INT AUTO_INCREMENT,
                         tipo VARCHAR(30) NOT NULL,
                         username VARCHAR(50) NOT NULL,
                         password_hash VARCHAR(255) NOT NULL,
                         activo BOOLEAN NOT NULL DEFAULT TRUE,
                         id_doctor INT NULL,

                         CONSTRAINT pk_usuario PRIMARY KEY (id_usuario),
                         CONSTRAINT uq_usuario_username UNIQUE (username),
                         CONSTRAINT uq_usuario_doctor UNIQUE (id_doctor),
                         CONSTRAINT chk_usuario_tipo CHECK (tipo IN ('Administrador', 'Doctor')),
                         CONSTRAINT chk_usuario_doctor_tipo CHECK (
                             (tipo = 'Administrador' AND id_doctor IS NULL)
                                 OR
                             (tipo = 'Doctor' AND id_doctor IS NOT NULL)
                             ),
                         CONSTRAINT fk_usuario_doctor FOREIGN KEY (id_doctor)
                             REFERENCES doctor(id_doctor)
);


CREATE TABLE paciente (
                          id_paciente INT AUTO_INCREMENT,
                          codigo_paciente VARCHAR(20) NOT NULL,
                          cedula VARCHAR(11) NOT NULL,
                          nombres VARCHAR(100) NOT NULL,
                          apellidos VARCHAR(100) NOT NULL,
                          telefono VARCHAR(10) NOT NULL,
                          direccion VARCHAR(255) NOT NULL,
                          fecha_nacimiento DATE NOT NULL,
                          sexo CHAR(1) NOT NULL,
                          tipo_sangre VARCHAR(5) NOT NULL,
                          fecha_registro DATE NOT NULL DEFAULT (CURRENT_DATE),
                          activo BOOLEAN NOT NULL DEFAULT TRUE,
                          peso DECIMAL(6,2) NOT NULL,
                          estatura DECIMAL(5,2) NOT NULL,
                          id_doctor_registrador INT NULL,

                          CONSTRAINT pk_paciente PRIMARY KEY (id_paciente),
                          CONSTRAINT uq_paciente_codigo UNIQUE (codigo_paciente),
                          CONSTRAINT uq_paciente_cedula UNIQUE (cedula),
                          CONSTRAINT uq_paciente_telefono UNIQUE (telefono),
                          CONSTRAINT chk_paciente_sexo CHECK (sexo IN ('M', 'F')),
                          CONSTRAINT chk_paciente_tipo_sangre CHECK (
                              tipo_sangre IN ('A+', 'A-', 'B+', 'B-', 'AB+', 'AB-', 'O+', 'O-')
                              ),
                          CONSTRAINT chk_paciente_peso CHECK (peso > 0),
                          CONSTRAINT chk_paciente_estatura CHECK (estatura > 0),
                          CONSTRAINT fk_paciente_doctor FOREIGN KEY (id_doctor_registrador)
                              REFERENCES doctor(id_doctor)
);


CREATE TABLE alergia (
                         id_alergia INT AUTO_INCREMENT,
                         nombre VARCHAR(100) NOT NULL,
                         tipo VARCHAR(50) NOT NULL,

                         CONSTRAINT pk_alergia PRIMARY KEY (id_alergia),
                         CONSTRAINT uq_alergia_nombre UNIQUE (nombre),
                         CONSTRAINT chk_alergia_tipo CHECK (
                             tipo IN ('Alimento', 'Medicamento', 'Ambiental', 'Animal', 'Contacto')
                             )
);


CREATE TABLE paciente_alergia (
                                  id_paciente INT NOT NULL,
                                  id_alergia INT NOT NULL,

                                  CONSTRAINT pk_paciente_alergia PRIMARY KEY (id_paciente, id_alergia),
                                  CONSTRAINT fk_paciente_alergia_paciente FOREIGN KEY (id_paciente)
                                      REFERENCES paciente(id_paciente),
                                  CONSTRAINT fk_paciente_alergia_alergia FOREIGN KEY (id_alergia)
                                      REFERENCES alergia(id_alergia)
);



CREATE TABLE enfermedad (
                            id_enfermedad INT AUTO_INCREMENT,
                            codigo_enfermedad VARCHAR(20) NOT NULL,
                            nombre VARCHAR(100) NOT NULL,
                            bajo_vigilancia BOOLEAN NOT NULL DEFAULT FALSE,
                            sintomas_y_signos TEXT NOT NULL,
                            nivel_gravedad INT NOT NULL,
                            potencial_propagacion VARCHAR(30) NOT NULL,
                            tipo VARCHAR(100) NOT NULL,

                            CONSTRAINT pk_enfermedad PRIMARY KEY (id_enfermedad),
                            CONSTRAINT uq_enfermedad_codigo UNIQUE (codigo_enfermedad),
                            CONSTRAINT uq_enfermedad_nombre UNIQUE (nombre),
                            CONSTRAINT chk_enfermedad_nivel CHECK (nivel_gravedad BETWEEN 1 AND 4),
                            CONSTRAINT chk_enfermedad_propagacion CHECK (
                                potencial_propagacion IN ('Bajo', 'Leve', 'Medio', 'Elevado', 'Alto')
                                )
);


CREATE TABLE vacuna (
                        id_vacuna INT AUTO_INCREMENT,
                        codigo_vacuna VARCHAR(20) NOT NULL,
                        numero_lote VARCHAR(50) NOT NULL,
                        nombre VARCHAR(100) NOT NULL,
                        cantidad INT NOT NULL DEFAULT 0,
                        fecha_caducidad DATE NOT NULL,
                        laboratorio VARCHAR(100) NOT NULL,
                        activa BOOLEAN NOT NULL DEFAULT TRUE,
                        id_enfermedad INT NOT NULL,

                        CONSTRAINT pk_vacuna PRIMARY KEY (id_vacuna),
                        CONSTRAINT uq_vacuna_codigo UNIQUE (codigo_vacuna),
                        CONSTRAINT uq_vacuna_lote UNIQUE (numero_lote),
                        CONSTRAINT chk_vacuna_cantidad CHECK (cantidad >= 0),
                        CONSTRAINT fk_vacuna_enfermedad FOREIGN KEY (id_enfermedad)
                            REFERENCES enfermedad(id_enfermedad)
);




CREATE TABLE registro_vacuna (
                                 id_registro_vacuna INT AUTO_INCREMENT,
                                 id_paciente INT NOT NULL,
                                 id_vacuna INT NOT NULL,
                                 fecha_aplicacion DATE NOT NULL,
                                 aplicada_por VARCHAR(100) NOT NULL,

                                 CONSTRAINT pk_registro_vacuna PRIMARY KEY (id_registro_vacuna),
                                 CONSTRAINT fk_registro_vacuna_paciente FOREIGN KEY (id_paciente)
                                     REFERENCES paciente(id_paciente),
                                 CONSTRAINT fk_registro_vacuna_vacuna FOREIGN KEY (id_vacuna)
                                     REFERENCES vacuna(id_vacuna)
);



CREATE TABLE vacuna_previa (
                               id_vacuna_previa INT AUTO_INCREMENT,
                               id_paciente INT NOT NULL,
                               enfermedad VARCHAR(100) NOT NULL,
                               fecha_aplicacion DATE NOT NULL,

                               CONSTRAINT pk_vacuna_previa PRIMARY KEY (id_vacuna_previa),
                               CONSTRAINT fk_vacuna_previa_paciente FOREIGN KEY (id_paciente)
                                   REFERENCES paciente(id_paciente)
);
