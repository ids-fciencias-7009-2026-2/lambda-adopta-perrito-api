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
                         token         VARCHAR(100),
                         rol           VARCHAR(20) DEFAULT 'USER'
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
                        estado        VARCHAR(20)  DEFAULT 'DISPONIBLE'
);

-- Datos de prueba: Usuarios
INSERT INTO usuario (nombre, correo, codigo_postal, contrasena, token)
VALUES
    ('Ramon Diaz',         'ramonn.d@ciencias.unam.mx', '04510', 'hash_seguro_123',         'jwt_token_fragment_abc123'),
    ('Benito Hernandez',   'benito.h@ciencias.unam.mx', '04360', 'password_encriptada_456', 'tdz_token_fragment_zdq123'),
    ('Luis Ricardo',       'luis.ricardo@unam.mx',      '09243', 'admin_pass_789',          'token_acceso_rapido_xyz'),
    ('Federica Dominguez', 'test@dominio.com',           '05612', 'test_password_000',       'olg_token_fragment_uas');

-- Datos de prueba: Admin
INSERT INTO usuario (nombre, correo, codigo_postal, contrasena, token, rol)
VALUES
    ('Admin Lambda', 'admin@adopta.com', '04510', 'hash_seguro_123', null, 'ADMIN');

-- Datos de prueba: Animales
INSERT INTO animal (nombre, especie, raza, descripcion, foto_url, codigo_postal)
VALUES
    ('Firulais', 'Perro', 'Beagle', 'Un perrito muy alegre y sociable, ideal para familias.', 'https://images.unsplash.com/photo-1543466835-00a7907e9de1', '04510'),
    ('Michi', 'Gato', 'Siamés', 'Gato elegante y muy tranquilo. Le encanta dormir al sol.', 'https://images.unsplash.com/photo-1514888286974-6c03e2ca1dba', '11000'),
    ('Rex', 'Perro', 'Labrador', 'Excelente compañero para salir a correr, muy obediente.', 'https://images.unsplash.com/photo-1552053831-71594a27632d', '04510'),
    ('Luna', 'Gato', 'Mestizo', 'Gatita rescatada, un poco tímida al principio pero muy cariñosa.', 'https://images.unsplash.com/photo-1533733358354-29973cd6a8b8', '04510'),
    ('Simba', 'Perro', 'Golden Retriever', 'Cachorro lleno de energía, busca un hogar con jardín.', 'https://images.unsplash.com/photo-1552053831-71594a27632d', '06600');