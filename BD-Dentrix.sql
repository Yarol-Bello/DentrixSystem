--
-- SCRIPT DE BASE DE DATOS - DENTRIX SYSTEM
-- PostgreSQL 15+
--

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;

-- 1. TABLA: usuarios
CREATE TABLE public.usuarios (
    id serial PRIMARY KEY,
    dni character varying(8) NOT NULL UNIQUE,
    username character varying(50) NOT NULL UNIQUE,
    password_hash character varying(255) NOT NULL,
    nombres character varying(100) NOT NULL,
    apellidos character varying(100) NOT NULL,
    rol character varying(20) NOT NULL,
    consultorio_asignado character varying(50),
    activo boolean DEFAULT true,
    fecha_creacion timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT usuarios_rol_check CHECK (((rol)::text = ANY ((ARRAY['ADMIN'::character varying, 'ODONTOLOGO'::character varying, 'RECEPCION'::character varying])::text[])))
);

-- 2. TABLA: pacientes
CREATE TABLE public.pacientes (
    id serial PRIMARY KEY,
    codigo_hc character varying(20) NOT NULL UNIQUE,
    dni character varying(8) NOT NULL UNIQUE,
    apellidos character varying(100) NOT NULL,
    nombres character varying(100) NOT NULL,
    fecha_nacimiento date,
    sexo character varying(10),
    celular character varying(15),
    correo character varying(100),
    direccion character varying(255),
    alergias text,
    fecha_registro timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pacientes_sexo_check CHECK (((sexo)::text = ANY ((ARRAY['Masculino'::character varying, 'Femenino'::character varying])::text[])))
);

-- 3. TABLA: procedimientos
CREATE TABLE public.procedimientos (
    id serial PRIMARY KEY,
    codigo character varying(20) NOT NULL UNIQUE,
    nombre character varying(150) NOT NULL,
    especialidad character varying(100) NOT NULL,
    calculo character varying(50) NOT NULL,
    precio numeric(10,2) NOT NULL,
    CONSTRAINT procedimientos_calculo_check CHECK (((calculo)::text = ANY ((ARRAY['Por Pieza'::character varying, 'Sesión'::character varying, 'Mensual'::character varying])::text[]))),
    CONSTRAINT procedimientos_precio_check CHECK ((precio >= (0)::numeric))
);

-- 4. TABLA: cie10
CREATE TABLE public.cie10 (
    id serial PRIMARY KEY,
    codigo character varying(10) NOT NULL UNIQUE,
    nombre character varying(150) NOT NULL,
    grupo character varying(100) NOT NULL,
    prioridad character varying(20) DEFAULT 'ESTANDAR'::character varying,
    descripcion text,
    CONSTRAINT cie10_prioridad_check CHECK (((prioridad)::text = ANY ((ARRAY['FRECUENTE'::character varying, 'ESTANDAR'::character varying])::text[])))
);

-- 5. TABLA: citas
CREATE TABLE public.citas (
    id serial PRIMARY KEY,
    paciente_id integer NOT NULL REFERENCES public.pacientes(id),
    odontologo_id integer NOT NULL REFERENCES public.usuarios(id),
    procedimiento_id integer REFERENCES public.procedimientos(id),
    consultorio character varying(50) NOT NULL,
    fecha date NOT NULL,
    hora_inicio time without time zone NOT NULL,
    hora_fin time without time zone NOT NULL,
    turno character varying(20) NOT NULL,
    estado character varying(20) DEFAULT 'PENDIENTE'::character varying,
    observaciones text,
    fecha_creacion timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT citas_estado_check CHECK (((estado)::text = ANY ((ARRAY['PENDIENTE'::character varying, 'EN_SALA'::character varying, 'EN_ATENCION'::character varying, 'ATENDIDO'::character varying, 'FALTO'::character varying])::text[]))),
    CONSTRAINT citas_turno_check CHECK (((turno)::text = ANY ((ARRAY['MAÑANA'::character varying, 'TARDE'::character varying])::text[])))
);

-- 6. TABLA: fichas_clinicas
CREATE TABLE public.fichas_clinicas (
    id serial PRIMARY KEY,
    paciente_id integer NOT NULL REFERENCES public.pacientes(id),
    cita_id integer REFERENCES public.citas(id),
    odontologo_id integer NOT NULL REFERENCES public.usuarios(id),
    fecha date NOT NULL,
    anamnesis text,
    diagnostico_cie10 character varying(100),
    plan_tratamiento text,
    prescripcion text,
    total_presupuesto numeric(10,2) DEFAULT 0,
    monto_pagado numeric(10,2) DEFAULT 0,
    saldo numeric(10,2) DEFAULT 0,
    estado_presupuesto character varying(20) DEFAULT 'PENDIENTE'::character varying,
    fecha_creacion timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fichas_clinicas_estado_presupuesto_check CHECK (((estado_presupuesto)::text = ANY ((ARRAY['PENDIENTE'::character varying, 'ACEPTADO'::character varying, 'ABONO'::character varying, 'COBRADO'::character varying])::text[])))
);

-- 7. TABLA: ficha_detalles
CREATE TABLE public.ficha_detalles (
    id serial PRIMARY KEY,
    ficha_id integer NOT NULL REFERENCES public.fichas_clinicas(id) ON DELETE CASCADE,
    pieza_dental character varying(10) NOT NULL,
    cara_dental character varying(20),
    diagnostico character varying(150),
    procedimiento_id integer REFERENCES public.procedimientos(id),
    estado character varying(50),
    precio_aplicado numeric(10,2) DEFAULT 0,
    subtotal numeric(10,2) DEFAULT 0
);

-- 8. TABLA: consultorios
CREATE TABLE public.consultorios (
    id serial PRIMARY KEY,
    nombre character varying(50) NOT NULL UNIQUE,
    especialidad character varying(100) NOT NULL,
    odontologo_id integer REFERENCES public.usuarios(id),
    estado character varying(20) DEFAULT 'OPERATIVO'::character varying,
    CONSTRAINT consultorios_estado_check CHECK (((estado)::text = ANY ((ARRAY['OPERATIVO'::character varying, 'PAUSADO'::character varying])::text[])))
);

-- 9. TABLA: turnos_caja
CREATE TABLE public.turnos_caja (
    id serial PRIMARY KEY,
    fecha date NOT NULL,
    turno character varying(20) NOT NULL,
    cajero_apertura_id integer NOT NULL REFERENCES public.usuarios(id),
    cajero_cierre_id integer REFERENCES public.usuarios(id),
    fondo_inicial numeric(10,2) DEFAULT 0,
    hora_apertura timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    hora_cierre timestamp without time zone,
    estado character varying(20) DEFAULT 'ABIERTO'::character varying,
    CONSTRAINT turnos_caja_estado_check CHECK (((estado)::text = ANY ((ARRAY['ABIERTO'::character varying, 'CERRADO'::character varying])::text[]))),
    CONSTRAINT turnos_caja_turno_check CHECK (((turno)::text = ANY ((ARRAY['MAÑANA'::character varying, 'TARDE'::character varying])::text[])))
);

-- 10. TABLA: pagos
CREATE TABLE public.pagos (
    id serial PRIMARY KEY,
    ficha_id integer REFERENCES public.fichas_clinicas(id),
    paciente_id integer NOT NULL REFERENCES public.pacientes(id),
    turno_id integer REFERENCES public.turnos_caja(id),
    monto numeric(10,2) NOT NULL,
    metodo_pago character varying(20) NOT NULL,
    numero_operacion character varying(50),
    comprobante character varying(20) UNIQUE,
    saldo_restante numeric(10,2) DEFAULT 0,
    fecha_pago timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT pagos_metodo_pago_check CHECK (((metodo_pago)::text = ANY ((ARRAY['EFECTIVO'::character varying, 'YAPE'::character varying, 'PLIN'::character varying])::text[]))),
    CONSTRAINT pagos_monto_check CHECK ((monto > (0)::numeric))
);

-- 11. TABLA: cierres_caja
CREATE TABLE public.cierres_caja (
    id serial PRIMARY KEY,
    turno_id integer UNIQUE REFERENCES public.turnos_caja(id),
    fecha date NOT NULL,
    turno character varying(20) NOT NULL,
    cajero_id integer NOT NULL REFERENCES public.usuarios(id),
    fondo_inicial numeric(10,2) NOT NULL,
    total_efectivo numeric(10,2) NOT NULL,
    total_digital numeric(10,2) NOT NULL,
    esperado_gaveta numeric(10,2) NOT NULL,
    contado numeric(10,2) NOT NULL,
    diferencia numeric(10,2) NOT NULL,
    numero_acta character varying(50) NOT NULL UNIQUE,
    fecha_cierre timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);

-- 12. TABLA: configuracion_clinica
CREATE TABLE public.configuracion_clinica (
    id serial PRIMARY KEY,
    ruc character varying(11) NOT NULL,
    razon_social character varying(150) NOT NULL,
    nombre_comercial character varying(150) NOT NULL,
    direccion_fiscal character varying(255) NOT NULL,
    serie_boleta character varying(10) DEFAULT 'B001'::character varying NOT NULL,
    celular_yape character varying(20),
    titular_yape character varying(100),
    director_medico character varying(100),
    cop character varying(50),
    rne character varying(50),
    licencia_municipal character varying(50)
);

-- 13. TABLA: auditoria
CREATE TABLE public.auditoria (
    id serial PRIMARY KEY,
    usuario_id integer REFERENCES public.usuarios(id),
    accion character varying(100) NOT NULL,
    entidad character varying(50),
    detalle text,
    fecha timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================================
-- DATOS DE PRUEBA (SEED)
-- ============================================================================

-- Usuarios (Contraseña para todos: 123456)
INSERT INTO public.usuarios (dni, username, password_hash, nombres, apellidos, rol, consultorio_asignado, activo) VALUES
('12345678', 'admin', '$2a$10$zPsCabRVju03SEuTPy2LeuTmtJT5FOMAhkx4kLTDyIyajg7taINme', 'Juan', 'Vallejos', 'ADMIN', 'Consultorio 01', true),
('23456789', 'jvallejos', '$2a$10$4A7Q7oqGa0e03/Yqn.OWGOggfatNf5LbudhY6KRBS3Dv/N7i7dDAW', 'Juan', 'Vallejos', 'ODONTOLOGO', 'Consultorio 01', true),
('34567890', 'jperez', '$2a$10$N9qo8uLOickgx2ZMRZoMyeIjZAgcfl7p92ldGxad68LJZdL17lhWy', 'Jesica', 'Perez', 'ODONTOLOGO', 'Consultorio 02', true),
('45678901', 'ccampos', '$2a$10$gZAlu0osnY.v4HLIBYjM1ezgGBsO2WIfZNWnwMEsqtsLIGls0bKZS', 'Cristina', 'Campos', 'RECEPCION', 'Recepción Central', true);

-- Configuración Clínica
INSERT INTO public.configuracion_clinica (ruc, razon_social, nombre_comercial, direccion_fiscal, serie_boleta, director_medico) VALUES
('20608945123', 'CENTRO ODONTOLÓGICO DENTRIX S.A.C.', 'Centro Odontológico Dentrix', 'Av. Huancavelica N° 450, Chilca - Huancayo, Junín', 'B001', 'Dr. Juan Vallejos');

-- Consultorios
INSERT INTO public.consultorios (nombre, especialidad, odontologo_id, estado) VALUES
('Consultorio 01', 'Ortodoncia e Implantes', 2, 'OPERATIVO'),
('Consultorio 02', 'Endodoncia y Estética', 3, 'OPERATIVO'),
('Recepción Central', 'Atención al Cliente', 4, 'OPERATIVO');

-- Procedimientos
INSERT INTO public.procedimientos (codigo, nombre, especialidad, calculo, precio) VALUES
('TRAT-01', 'Curación Resina Oclusal', 'Operatoria', 'Por Pieza', 80.00),
('TRAT-02', 'Profilaxis y Destartraje', 'Periodoncia', 'Sesión', 70.00),
('TRAT-03', 'Control Mensual Ortodoncia', 'Ortodoncia', 'Mensual', 100.00),
('TRAT-04', 'Exodoncia Simple', 'Cirugía', 'Por Pieza', 90.00),
('TRAT-05', 'Endodoncia Unirradicular', 'Endodoncia', 'Por Pieza', 120.00);

-- CIE-10
INSERT INTO public.cie10 (codigo, nombre, grupo, prioridad, descripcion) VALUES
('K02.0', 'Caries limitada al esmalte', 'K02 Caries dental', 'ESTANDAR', 'Lesión inicial en esmalte.'),
('K02.1', 'Caries de la dentina', 'K02 Caries dental', 'FRECUENTE', 'Lesión que compromete la dentina.'),
('K04.0', 'Pulpitis aguda / crónica', 'K04 Pulpar', 'FRECUENTE', 'Inflamación de la pulpa.'),
('K05.1', 'Gingivitis marginal crónica', 'K05 Periodontal', 'FRECUENTE', 'Inflamación de encías.');

-- Pacientes
INSERT INTO public.pacientes (codigo_hc, dni, apellidos, nombres, fecha_nacimiento, sexo, celular, alergias) VALUES
('P-001', '47895612', 'Pérez Alania', 'Juan Carlos', '1994-05-12', 'Masculino', '954128745', 'Hipertensión arterial controlada.'),
('P-002', '70124589', 'Gómez Mendoza', 'María', '1988-08-23', 'Femenino', '987123456', 'Ninguna.'),
('P-003', '19854236', 'Díaz Rojas', 'Pedro', '1979-11-04', 'Masculino', '923456789', 'Alergia lidocaína.');

-- Citas (Hoy)
INSERT INTO public.citas (paciente_id, odontologo_id, procedimiento_id, consultorio, fecha, hora_inicio, hora_fin, turno, estado) VALUES
(1, 2, 3, 'Consultorio 01', CURRENT_DATE, '08:00', '08:45', 'MAÑANA', 'ATENDIDO'),
(2, 3, 1, 'Consultorio 02', CURRENT_DATE, '09:00', '09:45', 'MAÑANA', 'EN_ATENCION'),
(3, 2, 4, 'Consultorio 01', CURRENT_DATE, '10:00', '10:45', 'MAÑANA', 'PENDIENTE');

-- Ficha Clínica de prueba
INSERT INTO public.fichas_clinicas (paciente_id, cita_id, odontologo_id, fecha, anamnesis, diagnostico_cie10, plan_tratamiento, total_presupuesto, estado_presupuesto) VALUES
(1, 1, 2, CURRENT_DATE, 'Dolor al masticar.', 'K02.1 Caries de la dentina', 'TRAT-01 + TRAT-02', 150.00, 'ACEPTADO');

INSERT INTO public.ficha_detalles (ficha_id, pieza_dental, cara_dental, diagnostico, procedimiento_id, estado, precio_aplicado, subtotal) VALUES
(1, '17', 'Oclusal', 'Caries activa', 1, 'Caries', 80.00, 80.00),
(1, 'Gral', 'N/A', 'Profilaxis general', 2, 'Curado', 70.00, 70.00);

-- Turno de caja abierto para cobrar
INSERT INTO public.turnos_caja (fecha, turno, cajero_apertura_id, fondo_inicial, estado) VALUES
(CURRENT_DATE, 'MAÑANA', 4, 200.00, 'ABIERTO');