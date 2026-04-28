-- =====================================================================
-- BERMEOKO UDALA - OBJEKTU GALDUAK
-- Script osoa - 5. Mugarria
-- Diseinuarekin (kontzeptuala + logikoa) eta Java aplikazioarekin bat
-- =====================================================================

DROP DATABASE IF EXISTS erronka_galduak;
CREATE DATABASE erronka_galduak CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE erronka_galduak;

-- =====================================================================
-- 1. TAULEN ERAKETA
-- =====================================================================

-- ---------------------------------------------------------------------
-- ROLA
-- Oharra: zutabe izena 'deskribapena' da (LangileaDAO: r.deskribapena)
-- ---------------------------------------------------------------------
CREATE TABLE ROLA (
    id_rola      INT          AUTO_INCREMENT PRIMARY KEY,
    deskribapena VARCHAR(100) NOT NULL
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- LANGILEA
-- Oharra: id_rola FK zuzenean (LangileaDAO: l.id_rola = r.id_rola)
-- LANGILEA_ROLA taula ere badago asignazio_data gordetzeko
-- ---------------------------------------------------------------------
CREATE TABLE LANGILEA (
    id_langile     INT          AUTO_INCREMENT PRIMARY KEY,
    izena          VARCHAR(50)  NOT NULL,
    abizena        VARCHAR(50)  NOT NULL,
    erabiltzailea  VARCHAR(50)  UNIQUE NOT NULL,
    pasahitza_hash VARCHAR(255) NOT NULL,
    id_rola        INT,
    FOREIGN KEY (id_rola) REFERENCES ROLA(id_rola) ON DELETE RESTRICT
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- LANGILEA_ROLA  (diseinu logikotik: asignazio_data gordetzeko)
-- ---------------------------------------------------------------------
CREATE TABLE LANGILEA_ROLA (
    id_langile     INT  NOT NULL,
    id_rola        INT  NOT NULL,
    asignazio_data DATE NOT NULL DEFAULT (CURRENT_DATE),
    PRIMARY KEY (id_langile, id_rola),
    FOREIGN KEY (id_langile) REFERENCES LANGILEA(id_langile) ON DELETE CASCADE,
    FOREIGN KEY (id_rola)    REFERENCES ROLA(id_rola)        ON DELETE CASCADE
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- KATEGORIA
-- ---------------------------------------------------------------------
CREATE TABLE KATEGORIA (
    id_kategoria INT         AUTO_INCREMENT PRIMARY KEY,
    izena        VARCHAR(50) NOT NULL
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- KOKALEKUA  (bha_da = Bolumen Handikoen Armairua)
-- ---------------------------------------------------------------------
CREATE TABLE KOKALEKUA (
    id_kokalekua INT         AUTO_INCREMENT PRIMARY KEY,
    armairua     VARCHAR(50),
    apala        VARCHAR(50),
    bha_da       BOOLEAN     DEFAULT FALSE
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- HARTZAILEA (klase abstraktua)
-- JABEA eta ERAKUNDEA azpiklaseak dira
-- ---------------------------------------------------------------------
CREATE TABLE HARTZAILEA (
    id_hartzailea INT          AUTO_INCREMENT PRIMARY KEY,
    helbidea      VARCHAR(200),
    telefonoa     VARCHAR(20),
    emaila        VARCHAR(100)
) ENGINE=InnoDB;

-- JABEA: PK = nan (diseinu logikotik), id_hartzailea FK HARTZAILEA-ra
CREATE TABLE JABEA (
    nan           VARCHAR(15)  PRIMARY KEY,
    jabe_izena    VARCHAR(100) NOT NULL,
    jabe_abizena  VARCHAR(100) NOT NULL,
    herria        VARCHAR(100),
    pk            VARCHAR(10),
    probintzia    VARCHAR(100),
    id_hartzailea INT          UNIQUE NOT NULL,
    FOREIGN KEY (id_hartzailea) REFERENCES HARTZAILEA(id_hartzailea) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ERAKUNDEA: PK = ifz (diseinu logikotik), id_hartzailea FK HARTZAILEA-ra
-- Oharra: zutabe izena 'ift' → 'ifz' (Erakundea.java: getIfz())
CREATE TABLE ERAKUNDEA (
    ifz                  VARCHAR(20)  PRIMARY KEY,
    erakunde_izen_fiskala VARCHAR(150) NOT NULL,
    id_hartzailea        INT          UNIQUE NOT NULL,
    FOREIGN KEY (id_hartzailea) REFERENCES HARTZAILEA(id_hartzailea) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- ARTIKULUA
-- egoera ENUM ArtikuluaDAO switch-ekin bat dator:
--   aurkitua    → EgoeraArtikulua.BILTEGIAN
--   bueltatua   → EgoeraArtikulua.ITZULITA
--   artxibatua  → EgoeraArtikulua.IRAUNGITA
--   iraungita   → EgoeraArtikulua.IRAUNGITA
-- ---------------------------------------------------------------------
CREATE TABLE ARTIKULUA (
    id_artikulua    VARCHAR(15)  PRIMARY KEY,
    a_izena         VARCHAR(200) NOT NULL,
    a_deskribapena  TEXT         NOT NULL,
    egoera          ENUM('aurkitua','bueltatua','artxibatua','iraungita')
                    NOT NULL DEFAULT 'aurkitua',
    iragankorra     BOOLEAN      DEFAULT FALSE,
    sarrera_data    DATE         NOT NULL,
    iraungitze_data DATE,
    argazkia        VARCHAR(255),
    id_kategoria    INT,
    id_kokalekua    INT,
    FOREIGN KEY (id_kategoria) REFERENCES KATEGORIA(id_kategoria) ON DELETE SET NULL,
    FOREIGN KEY (id_kokalekua) REFERENCES KOKALEKUA(id_kokalekua) ON DELETE SET NULL
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- ERREKLAMAZIOA
-- Oharra: 'errek_egoera' (EstadistikaDAO: errek_egoera = 'irekita')
-- ---------------------------------------------------------------------
CREATE TABLE ERREKLAMAZIOA (
    id_erreklamazio     INT  AUTO_INCREMENT PRIMARY KEY,
    erreklamazio_data   DATE NOT NULL,
    errek_egoera        ENUM('irekita','ebatzita','baztertuta') NOT NULL DEFAULT 'irekita',
    deskribapen_bilatua TEXT NOT NULL,
    id_hartzailea       INT,
    id_langile          INT,
    id_kategoria        INT,
    FOREIGN KEY (id_hartzailea) REFERENCES HARTZAILEA(id_hartzailea) ON DELETE CASCADE,
    FOREIGN KEY (id_langile)    REFERENCES LANGILEA(id_langile)       ON DELETE SET NULL,
    FOREIGN KEY (id_kategoria)  REFERENCES KATEGORIA(id_kategoria)    ON DELETE SET NULL
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- JAKINARAZPENA
-- kanala ENUM Kanala.java-rekin bat dator: SMS, EMAIL, APP
-- ---------------------------------------------------------------------
CREATE TABLE JAKINARAZPENA (
    id_jakinarazpena INT  AUTO_INCREMENT PRIMARY KEY,
    bidaltze_data    DATE NOT NULL,
    kanala           ENUM('SMS','EMAIL','APP'),
    mezua            TEXT NOT NULL,
    irakurrita       BOOLEAN DEFAULT FALSE,
    id_erreklamazio  INT,
    id_artikulua     VARCHAR(15),
    FOREIGN KEY (id_erreklamazio) REFERENCES ERREKLAMAZIOA(id_erreklamazio) ON DELETE CASCADE,
    FOREIGN KEY (id_artikulua)    REFERENCES ARTIKULUA(id_artikulua)        ON DELETE CASCADE
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- EMANALDIA
-- ---------------------------------------------------------------------
CREATE TABLE EMANALDIA (
    id_emanaldia    INT         AUTO_INCREMENT PRIMARY KEY,
    emate_data      DATE        NOT NULL,
    oharrak         TEXT,
    dokumentu_bidea VARCHAR(255),
    id_artikulua    VARCHAR(15) UNIQUE,
    id_hartzailea   INT,
    id_langile      INT,
    FOREIGN KEY (id_artikulua)  REFERENCES ARTIKULUA(id_artikulua)   ON DELETE RESTRICT,
    FOREIGN KEY (id_hartzailea) REFERENCES HARTZAILEA(id_hartzailea) ON DELETE RESTRICT,
    FOREIGN KEY (id_langile)    REFERENCES LANGILEA(id_langile)       ON DELETE SET NULL
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- MUGIMENDUA  (trazabilitate-loga)
-- mota ENUM MugimenduMota.java + diseinu logikoarekin bat dator
-- Oharra: 'data' (EstadistikaDAO: m.data), 'deskribapena' (m.deskribapena)
-- ---------------------------------------------------------------------
CREATE TABLE MUGIMENDUA (
    id_mugimendua INT      AUTO_INCREMENT PRIMARY KEY,
    data          DATETIME DEFAULT CURRENT_TIMESTAMP,
    mota          ENUM('sarrera','barne_mugimendua','irteera_jabea',
                       'irteera_erakundea','iraungitze_alerta'),
    deskribapena  TEXT     NOT NULL,
    id_artikulua  VARCHAR(15),
    id_langile    INT,
    FOREIGN KEY (id_artikulua) REFERENCES ARTIKULUA(id_artikulua) ON DELETE CASCADE,
    FOREIGN KEY (id_langile)   REFERENCES LANGILEA(id_langile)    ON DELETE SET NULL
) ENGINE=InnoDB;


-- =====================================================================
-- 2. ROLAK ETA DATU-BASEKO ERABILTZAILEAK
-- =====================================================================

CREATE ROLE IF NOT EXISTS 'admin_rola';
CREATE ROLE IF NOT EXISTS 'udaltzain_rola';
CREATE ROLE IF NOT EXISTS 'bezero_rola';

-- Admin: dena
GRANT ALL PRIVILEGES ON erronka_galduak.* TO 'admin_rola';

-- Udaltzaina: dena LANGILEA eta ROLA izan ezik (irakurketa soilik)
GRANT SELECT, INSERT, UPDATE, DELETE ON erronka_galduak.ARTIKULUA     TO 'udaltzain_rola';
GRANT SELECT, INSERT, UPDATE, DELETE ON erronka_galduak.KATEGORIA     TO 'udaltzain_rola';
GRANT SELECT, INSERT, UPDATE, DELETE ON erronka_galduak.KOKALEKUA     TO 'udaltzain_rola';
GRANT SELECT, INSERT, UPDATE, DELETE ON erronka_galduak.HARTZAILEA    TO 'udaltzain_rola';
GRANT SELECT, INSERT, UPDATE, DELETE ON erronka_galduak.JABEA         TO 'udaltzain_rola';
GRANT SELECT, INSERT, UPDATE, DELETE ON erronka_galduak.ERAKUNDEA     TO 'udaltzain_rola';
GRANT SELECT, INSERT, UPDATE, DELETE ON erronka_galduak.ERREKLAMAZIOA TO 'udaltzain_rola';
GRANT SELECT, INSERT, UPDATE, DELETE ON erronka_galduak.EMANALDIA     TO 'udaltzain_rola';
GRANT SELECT, INSERT, UPDATE, DELETE ON erronka_galduak.MUGIMENDUA    TO 'udaltzain_rola';
GRANT SELECT, INSERT, UPDATE, DELETE ON erronka_galduak.JAKINARAZPENA TO 'udaltzain_rola';
GRANT SELECT ON erronka_galduak.LANGILEA     TO 'udaltzain_rola';
GRANT SELECT ON erronka_galduak.ROLA         TO 'udaltzain_rola';
GRANT SELECT ON erronka_galduak.LANGILEA_ROLA TO 'udaltzain_rola';

-- Bezeroa: web atari publikoa
GRANT SELECT ON erronka_galduak.ARTIKULUA  TO 'bezero_rola';
GRANT SELECT ON erronka_galduak.KATEGORIA  TO 'bezero_rola';
GRANT SELECT ON erronka_galduak.KOKALEKUA  TO 'bezero_rola';
GRANT INSERT ON erronka_galduak.HARTZAILEA    TO 'bezero_rola';
GRANT INSERT ON erronka_galduak.JABEA         TO 'bezero_rola';
GRANT INSERT ON erronka_galduak.ERREKLAMAZIOA TO 'bezero_rola';

-- Erabiltzaileak
CREATE USER IF NOT EXISTS 'admin'@'%' IDENTIFIED BY 'admin123';
GRANT 'admin_rola' TO 'admin'@'%';
SET DEFAULT ROLE 'admin_rola' FOR 'admin'@'%';

CREATE USER IF NOT EXISTS 'udaltzain1'@'%' IDENTIFIED BY 'udal123';
GRANT 'udaltzain_rola' TO 'udaltzain1'@'%';
SET DEFAULT ROLE 'udaltzain_rola' FOR 'udaltzain1'@'%';

CREATE USER IF NOT EXISTS 'udaltzain2'@'%' IDENTIFIED BY 'udal123';
GRANT 'udaltzain_rola' TO 'udaltzain2'@'%';
SET DEFAULT ROLE 'udaltzain_rola' FOR 'udaltzain2'@'%';

CREATE USER IF NOT EXISTS 'bezero_web'@'%' IDENTIFIED BY 'bezeropw';
GRANT 'bezero_rola' TO 'bezero_web'@'%';
SET DEFAULT ROLE 'bezero_rola' FOR 'bezero_web'@'%';

FLUSH PRIVILEGES;

SELECT '[INIT] Rol eta erabiltzaileak sortuta' AS mezua;
SELECT User, Host FROM mysql.user
 WHERE User IN ('admin','udaltzain1','udaltzain2','bezero_web')
 ORDER BY User;


-- =====================================================================
-- 3. DATU HASIERAKOAK (seed)
-- =====================================================================

-- ---------------------------------------------------------------------
-- ROLA
-- ---------------------------------------------------------------------
INSERT INTO ROLA (deskribapena) VALUES
    ('Administratzailea'),
    ('Udaltzaina');

-- ---------------------------------------------------------------------
-- LANGILEA
-- Pasahitza: '1234' (BCrypt cost 10)
-- ---------------------------------------------------------------------
INSERT INTO LANGILEA (izena, abizena, erabiltzailea, pasahitza_hash, id_rola) VALUES
    ('Miren', 'Agirre',  'admin',
     '$2a$10$oFl.yJsTOmztwUCT0ov7K.VoNDHioYD/wGpqbNP2U3kTwgB4B7Ica', 1),
    ('Jon',   'Zabala',  'udaltzain1',
     '$2a$10$oFl.yJsTOmztwUCT0ov7K.VoNDHioYD/wGpqbNP2U3kTwgB4B7Ica', 2);

-- LANGILEA_ROLA (asignazio_data gordetzeko)
INSERT INTO LANGILEA_ROLA (id_langile, id_rola, asignazio_data) VALUES
    (1, 1, '2026-01-01'),
    (2, 2, '2026-01-01');

-- ---------------------------------------------------------------------
-- KATEGORIA
-- ---------------------------------------------------------------------
INSERT INTO KATEGORIA (izena) VALUES
    ('Osagarri pertsonalak'),
    ('Betaurrekoak'),
    ('Giltzak'),
    ('Bestelakoak');

-- ---------------------------------------------------------------------
-- KOKALEKUA  (bha_da = Bolumen Handikoen Armairua)
-- ---------------------------------------------------------------------
INSERT INTO KOKALEKUA (armairua, apala, bha_da) VALUES
    ('F',   '004', FALSE),
    ('A',   '001', FALSE),
    ('BHA', '1',   TRUE);

-- ---------------------------------------------------------------------
-- HARTZAILEA + JABEA
-- ---------------------------------------------------------------------
INSERT INTO HARTZAILEA (id_hartzailea, telefonoa) VALUES
    (1, '688729149'),
    (2, '664146219');

INSERT INTO JABEA (nan, jabe_izena, jabe_abizena, id_hartzailea) VALUES
    ('11111111A', 'RAMON',  'INFANTE', 1),
    ('22222222B', 'ISMAEL', 'CORTES',  2);

-- ---------------------------------------------------------------------
-- ARTIKULUA
-- ---------------------------------------------------------------------
INSERT INTO ARTIKULUA
    (id_artikulua, a_izena, a_deskribapena, egoera,
     sarrera_data, iraungitze_data, id_kategoria, id_kokalekua)
VALUES
    ('G-092-26', 'Kartera oria',
     'Osagarri pertsonalak, kartera larruzkoa.',
     'aurkitua', '2026-04-01', '2028-04-01', 1, 1),
    ('G-093-26', 'Umearen betaurrekoak',
     'Betaurrekoak umearentzat, koloretsuak.',
     'aurkitua', '2026-04-07', '2028-04-07', 2, 1);

-- ---------------------------------------------------------------------
-- MUGIMENDUA  (mota ENUM balioarekin)
-- ---------------------------------------------------------------------
INSERT INTO MUGIMENDUA (mota, deskribapena, id_artikulua) VALUES
    ('sarrera', 'Artikulua sisteman erregistratu da F-004 kokalekuan', 'G-092-26'),
    ('sarrera', 'Artikulua sisteman erregistratu da A-001 kokalekuan', 'G-093-26');

SELECT '[INIT] Probetarako datuak txertatuta' AS mezua;
SELECT COUNT(*) AS langile_kop  FROM LANGILEA;
SELECT COUNT(*) AS artikulu_kop FROM ARTIKULUA;
