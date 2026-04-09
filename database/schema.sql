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

-- Eliminar tabla si ya existe (para ejecucion limpia)
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

-- Datos de prueba
INSERT INTO usuario (nombre, correo, codigo_postal, contrasena, token)
VALUES
    ('Ramon Diaz',         'ramonn.d@ciencias.unam.mx', '04510', 'hash_seguro_123',        'jwt_token_fragment_abc123'),
    ('Benito Hernandez',   'benito.h@ciencias.unam.mx', '04360', 'password_encriptada_456', 'tdz_token_fragment_zdq123'),
    ('Luis Ricardo',       'luis.ricardo@unam.mx',      '09243', 'admin_pass_789',          'token_acceso_rapido_xyz'),
    ('Federica Dominguez', 'test@dominio.com',           '05612', 'test_password_000',       'olg_token_fragment_uas');