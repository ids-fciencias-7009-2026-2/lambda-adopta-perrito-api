-- =============================================
-- AdoptaPerrito - Schema completo de la BD
-- PostgreSQL
-- =============================================

-- Crear la base de datos si no existe
SELECT 'CREATE DATABASE adopta_perrito_db'
    WHERE NOT EXISTS (
    SELECT FROM pg_database WHERE datname = 'adopta_perrito_db'
)\gexec

-- Conectarse a la base de datos
    \c adopta_perrito_db;

-- Eliminar tablas si ya existen (para ejecucion limpia)
DROP TABLE IF EXISTS animal;
DROP TABLE IF EXISTS usuario;

-- Crear tabla usuario
CREATE TABLE usuario (
                         id_usuario    SERIAL PRIMARY KEY,
                         nombre        VARCHAR(100) NOT NULL,
                         correo        VARCHAR(100) NOT NULL UNIQUE,
                         codigo_postal VARCHAR(10),
                         contrasena    VARCHAR(100) NOT NULL,
                         token         VARCHAR(100)
);

-- Crear tabla animal (CU4: Buscar mascota con filtros)
CREATE TABLE animal (
                        id_animal     SERIAL PRIMARY KEY,
                        nombre        VARCHAR(100) NOT NULL,
                        especie       VARCHAR(50) NOT NULL,
                        raza          VARCHAR(100),
                        descripcion   TEXT,
                        foto_url      VARCHAR(255),
                        codigo_postal VARCHAR(10) NOT NULL,
                        estado        VARCHAR(20)  DEFAULT 'DISPONIBLE', -- LO TUYO
                        id_usuario    INT REFERENCES usuario(id_usuario) -- LO DE TU COMPA
);