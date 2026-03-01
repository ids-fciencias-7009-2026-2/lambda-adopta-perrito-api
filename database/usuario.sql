CREATE TABLE usuario (
    id_usuario SERIAL PRIMARY KEY,
	nombre VARCHAR(100) NOT NULL,
	correo VARCHAR(100) UNIQUE NOT NULL,
	codigo_postal VARCHAR (10),
	contrasena VARCHAR(100) NOT NULL,
    token VARCHAR(100));


-- Agrego algunos ejemplos por si necesitamos hacer pruebas. 

-- 1. Registro completo (Usuario estándar)
INSERT INTO usuario (nombre, correo, codigo_postal, contrasena, token) 
VALUES ('Ramon Diaz', 'ramonn.d@ciencias.unam.mx', '04510', 'hash_seguro_123', 'jwt_token_fragment_abc123');

-- 2. Registro sin token (Usuario que acaba de registrarse pero no ha iniciado sesión)
INSERT INTO usuario (nombre, correo, codigo_postal, contrasena, token) 
VALUES ('Benito Hernandez', 'benito.h@ciencias.unam.mx', '04360', 'password_encriptada_456', 'tdz_token_fragment_zdq123');

-- 3. Registro sin código postal (Campo opcional según tu definición)
INSERT INTO usuario (nombre, correo, codigo_postal, contrasena, token) 
VALUES ('Luis Ricardo', 'luis.ricardo@unam.mx','09243' ,'admin_pass_789', 'token_acceso_rapido_xyz');

-- 4. Registro mínimo (Solo los campos NOT NULL)
INSERT INTO usuario (nombre, correo, codigo_postal ,contrasena, token) 
VALUES ('Federica Dominguez', 'test@dominio.com', '05612','test_password_000', 'olg_token_fragment_uas');