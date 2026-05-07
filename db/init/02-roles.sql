-- =====================================================================
-- BERMEOKO UDALA - GALDUTAKOAK
-- 02-roles.sql · Datu-baseko rolak eta erabiltzaileak
-- =====================================================================

USE erronka_galduak;

-- ---------------------------------------------------------------------
-- 1. ROLAK SORTU
-- ---------------------------------------------------------------------
CREATE ROLE IF NOT EXISTS 'admin_rola';
CREATE ROLE IF NOT EXISTS 'langile_rola';
CREATE ROLE IF NOT EXISTS 'ikusle_rola';

-- ---------------------------------------------------------------------
-- 2. BAIMENAK ESLEITU
-- ---------------------------------------------------------------------

-- Admin: dena
GRANT ALL PRIVILEGES ON erronka_galduak.* TO 'admin_rola';

-- Langilea: operazional osoa, LANGILEA eta ROLA irakurketa soilik
GRANT SELECT, INSERT, UPDATE, DELETE ON erronka_galduak.ARTIKULUA     TO 'langile_rola';
GRANT SELECT, INSERT, UPDATE, DELETE ON erronka_galduak.KATEGORIA     TO 'langile_rola';
GRANT SELECT, INSERT, UPDATE, DELETE ON erronka_galduak.KOKALEKUA     TO 'langile_rola';
GRANT SELECT, INSERT, UPDATE, DELETE ON erronka_galduak.HARTZAILEA    TO 'langile_rola';
GRANT SELECT, INSERT, UPDATE, DELETE ON erronka_galduak.JABEA         TO 'langile_rola';
GRANT SELECT, INSERT, UPDATE, DELETE ON erronka_galduak.ERAKUNDEA     TO 'langile_rola';
GRANT SELECT, INSERT, UPDATE, DELETE ON erronka_galduak.ERREKLAMAZIOA TO 'langile_rola';
GRANT SELECT, INSERT, UPDATE, DELETE ON erronka_galduak.EMANALDIA     TO 'langile_rola';
GRANT SELECT, INSERT, UPDATE, DELETE ON erronka_galduak.MUGIMENDUA    TO 'langile_rola';
GRANT SELECT, INSERT, UPDATE, DELETE ON erronka_galduak.JAKINARAZPENA TO 'langile_rola';
GRANT SELECT, INSERT, UPDATE, DELETE ON erronka_galduak.AURKITZAILEA  TO 'langile_rola';
GRANT SELECT ON erronka_galduak.LANGILEA TO 'langile_rola';
GRANT SELECT ON erronka_galduak.ROLA     TO 'langile_rola';

-- Ikuslea: artikuluak erregistratu soilik
GRANT SELECT ON erronka_galduak.KATEGORIA  TO 'ikusle_rola';
GRANT SELECT ON erronka_galduak.KOKALEKUA  TO 'ikusle_rola';
GRANT SELECT ON erronka_galduak.ROLA       TO 'ikusle_rola';
GRANT SELECT, INSERT ON erronka_galduak.ARTIKULUA    TO 'ikusle_rola';
GRANT SELECT, INSERT ON erronka_galduak.MUGIMENDUA   TO 'ikusle_rola';
GRANT SELECT, INSERT ON erronka_galduak.AURKITZAILEA TO 'ikusle_rola';

-- ---------------------------------------------------------------------
-- 3. ERABILTZAILEAK SORTU ETA ROLEKIN LOTU
-- ---------------------------------------------------------------------
CREATE USER IF NOT EXISTS 'admin'@'%' IDENTIFIED BY 'admin123';
GRANT 'admin_rola' TO 'admin'@'%';
SET DEFAULT ROLE 'admin_rola' FOR 'admin'@'%';

CREATE USER IF NOT EXISTS 'langile1'@'%' IDENTIFIED BY 'langile123';
GRANT 'langile_rola' TO 'langile1'@'%';
SET DEFAULT ROLE 'langile_rola' FOR 'langile1'@'%';

CREATE USER IF NOT EXISTS 'ikusle1'@'%' IDENTIFIED BY 'ikusle123';
GRANT 'ikusle_rola' TO 'ikusle1'@'%';
SET DEFAULT ROLE 'ikusle_rola' FOR 'ikusle1'@'%';

FLUSH PRIVILEGES;

SELECT '[INIT] Rol eta erabiltzaileak sortuta' AS mezua;
