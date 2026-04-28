-- =====================================================================
-- BERMEOKO UDALA - GALDUTAKOAK
-- 02-roles.sql · Datu-baseko rolak eta erabiltzaileak
-- =====================================================================

USE erronka_galduak;

-- ---------------------------------------------------------------------
-- 1. ROLAK SORTU
-- ---------------------------------------------------------------------
CREATE ROLE IF NOT EXISTS 'admin_rola';
CREATE ROLE IF NOT EXISTS 'udaltzain_rola';
CREATE ROLE IF NOT EXISTS 'bezero_rola';

-- ---------------------------------------------------------------------
-- 2. BAIMENAK ESLEITU
-- ---------------------------------------------------------------------

-- Admin: dena
GRANT ALL PRIVILEGES ON erronka_galduak.* TO 'admin_rola';

-- Udaltzaina: dena LANGILEA eta ROLA izan ezik (irakurketa soilik)
GRANT SELECT, INSERT, UPDATE, DELETE ON erronka_galduak.ARTIKULUA    TO 'udaltzain_rola';
GRANT SELECT, INSERT, UPDATE, DELETE ON erronka_galduak.KATEGORIA    TO 'udaltzain_rola';
GRANT SELECT, INSERT, UPDATE, DELETE ON erronka_galduak.KOKALEKUA    TO 'udaltzain_rola';
GRANT SELECT, INSERT, UPDATE, DELETE ON erronka_galduak.HARTZAILEA   TO 'udaltzain_rola';
GRANT SELECT, INSERT, UPDATE, DELETE ON erronka_galduak.JABEA        TO 'udaltzain_rola';
GRANT SELECT, INSERT, UPDATE, DELETE ON erronka_galduak.ERAKUNDEA    TO 'udaltzain_rola';
GRANT SELECT, INSERT, UPDATE, DELETE ON erronka_galduak.ERREKLAMAZIOA TO 'udaltzain_rola';
GRANT SELECT, INSERT, UPDATE, DELETE ON erronka_galduak.EMANALDIA    TO 'udaltzain_rola';
GRANT SELECT, INSERT, UPDATE, DELETE ON erronka_galduak.MUGIMENDUA   TO 'udaltzain_rola';
GRANT SELECT, INSERT, UPDATE, DELETE ON erronka_galduak.JAKINARAZPENA TO 'udaltzain_rola';
GRANT SELECT ON erronka_galduak.LANGILEA TO 'udaltzain_rola';
GRANT SELECT ON erronka_galduak.ROLA     TO 'udaltzain_rola';

-- Bezeroa: web atari publikoa 
GRANT SELECT ON erronka_galduak.ARTIKULUA  TO 'bezero_rola';
GRANT SELECT ON erronka_galduak.KATEGORIA  TO 'bezero_rola';
GRANT SELECT ON erronka_galduak.KOKALEKUA  TO 'bezero_rola';
-- Erreklamazioa BIDALI ahal izateko herritarrek
GRANT INSERT ON erronka_galduak.HARTZAILEA     TO 'bezero_rola';
GRANT INSERT ON erronka_galduak.JABEA          TO 'bezero_rola';
GRANT INSERT ON erronka_galduak.ERREKLAMAZIOA  TO 'bezero_rola';

-- ---------------------------------------------------------------------
-- 3. ERABILTZAILEAK SORTU ETA ROLEKIN LOTU
-- ---------------------------------------------------------------------
-- Admin
CREATE USER IF NOT EXISTS 'admin'@'%' IDENTIFIED BY 'admin123';
GRANT 'admin_rola' TO 'admin'@'%';
SET DEFAULT ROLE 'admin_rola' FOR 'admin'@'%';

-- Udaltzaingoa 
CREATE USER IF NOT EXISTS 'udaltzain1'@'%' IDENTIFIED BY 'udal123';
GRANT 'udaltzain_rola' TO 'udaltzain1'@'%';
SET DEFAULT ROLE 'udaltzain_rola' FOR 'udaltzain1'@'%';

CREATE USER IF NOT EXISTS 'udaltzain2'@'%' IDENTIFIED BY 'udal123';
GRANT 'udaltzain_rola' TO 'udaltzain2'@'%';
SET DEFAULT ROLE 'udaltzain_rola' FOR 'udaltzain2'@'%';

-- Web bezeroa 
CREATE USER IF NOT EXISTS 'bezero_web'@'%' IDENTIFIED BY 'bezeropw';
GRANT 'bezero_rola' TO 'bezero_web'@'%';
SET DEFAULT ROLE 'bezero_rola' FOR 'bezero_web'@'%';

FLUSH PRIVILEGES;

-- ---------------------------------------------------------------------
-- Egiaztapena
-- ---------------------------------------------------------------------
SELECT '[INIT] Rol eta erabiltzaileak sortuta' AS mezua;
SELECT User, Host FROM mysql.user
 WHERE User IN ('admin','udaltzain1','udaltzain2','bezero_web')
 ORDER BY User;