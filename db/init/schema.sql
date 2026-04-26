-- =====================================================================
-- BERMEOKO UDALA - OBJEKTU GALDUAK
-- =====================================================================

DROP DATABASE IF EXISTS erronka_galduak;
CREATE DATABASE erronka_galduak CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE erronka_galduak;
/* TAULEN ERAKETA */
CREATE TABLE ROLA (
    id_rola INT AUTO_INCREMENT PRIMARY KEY,
    deskribapena VARCHAR(100) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE LANGILEA (
    id_langile INT AUTO_INCREMENT PRIMARY KEY,
    izena VARCHAR(50) NOT NULL,
    abizena VARCHAR(50) NOT NULL,
    erabiltzailea VARCHAR(50) UNIQUE NOT NULL,
    pasahitza_hash VARCHAR(255) NOT NULL,
    id_rola INT,
    FOREIGN KEY (id_rola) REFERENCES ROLA(id_rola) ON DELETE RESTRICT
) ENGINE=InnoDB;

CREATE TABLE KATEGORIA (
    id_kategoria INT AUTO_INCREMENT PRIMARY KEY,
    izena VARCHAR(50) NOT NULL
) ENGINE=InnoDB;

CREATE TABLE KOKALEKUA (
    id_kokalekua INT AUTO_INCREMENT PRIMARY KEY,
    armairua VARCHAR(50),
    apala VARCHAR(50),
    bha_da BOOLEAN DEFAULT FALSE -- Bolumen Handikoen Armairua
) ENGINE=InnoDB;

-- HERENTZIA HASIERA ----
CREATE TABLE HARTZAILEA (
    id_hartzailea INT AUTO_INCREMENT PRIMARY KEY,
    helbidea VARCHAR(200),
    telefonoa VARCHAR(20),
    emaila VARCHAR(100)
) ENGINE=InnoDB;
-- AZPIKLASEAK --
CREATE TABLE JABEA (
    id_hartzailea INT PRIMARY KEY,
    nan VARCHAR(15) UNIQUE NOT NULL,
    izena VARCHAR(100) NOT NULL,
    abizena VARCHAR(100) NOT NULL,
    FOREIGN KEY (id_hartzailea) REFERENCES HARTZAILEA(id_hartzailea) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE ERAKUNDEA (
    id_hartzailea INT PRIMARY KEY,
    ift VARCHAR(20) UNIQUE NOT NULL,
    izen_ofiziala VARCHAR(150) NOT NULL,
    FOREIGN KEY (id_hartzailea) REFERENCES HARTZAILEA(id_hartzailea) ON DELETE CASCADE
) ENGINE=InnoDB;

-- HERENTZIA BUKAERA --


CREATE TABLE ARTIKULUA (
    id_artikulua VARCHAR(15) PRIMARY KEY,
    a_izena VARCHAR(200) NOT NULL,
    a_deskribapena TEXT NOT NULL,
    egoera ENUM('aurkitua', 'bueltatua', 'artxibatua', 'iraungita') NOT NULL DEFAULT 'aurkitua',
    sarrera_data DATE NOT NULL,
    iraungitze_data DATE, -- 2 urte igarota abisua emateko
    argazkia VARCHAR(255),
    id_kategoria INT,
    id_kokalekua INT,
    FOREIGN KEY (id_kategoria) REFERENCES KATEGORIA(id_kategoria) ON DELETE SET NULL,
    FOREIGN KEY (id_kokalekua) REFERENCES KOKALEKUA(id_kokalekua) ON DELETE SET NULL
) ENGINE=InnoDB;


CREATE TABLE ERREKLAMAZIOA (
    id_erreklamazio INT AUTO_INCREMENT PRIMARY KEY,
    erreklamazio_data DATE NOT NULL,
    errek_egoera ENUM('irekita', 'ebatzita', 'baztertuta') NOT NULL DEFAULT 'irekita',
    deskribapen_bilatua TEXT NOT NULL,
    id_hartzailea INT,
    id_langile INT,
    id_kategoria INT,
    FOREIGN KEY (id_hartzailea) REFERENCES HARTZAILEA(id_hartzailea) ON DELETE CASCADE,
    FOREIGN KEY (id_langile) REFERENCES LANGILEA(id_langile) ON DELETE SET NULL,
    FOREIGN KEY (id_kategoria) REFERENCES KATEGORIA(id_kategoria) ON DELETE SET NULL
) ENGINE=InnoDB;


CREATE TABLE JAKINARAZPENA (
    id_jakinarazpena INT AUTO_INCREMENT PRIMARY KEY,
    bidaltze_data DATE NOT NULL,
    kanala VARCHAR(50),
    mezua TEXT NOT NULL,
    irakurrita BOOLEAN DEFAULT FALSE,
    id_erreklamazio INT,
    id_artikulua VARCHAR(15),
    FOREIGN KEY (id_erreklamazio) REFERENCES ERREKLAMAZIOA(id_erreklamazio) ON DELETE CASCADE,
    FOREIGN KEY (id_artikulua) REFERENCES ARTIKULUA(id_artikulua) ON DELETE CASCADE
) ENGINE=InnoDB;

CREATE TABLE EMANALDIA (
    id_emanaldia INT AUTO_INCREMENT PRIMARY KEY,
    emate_data DATE NOT NULL,
    oharrak TEXT,
    dokumentu_bidea VARCHAR(255),
    id_artikulua VARCHAR(15) UNIQUE,
    id_hartzailea INT,
    id_langile INT,
    FOREIGN KEY (id_artikulua) REFERENCES ARTIKULUA(id_artikulua) ON DELETE RESTRICT,
    FOREIGN KEY (id_hartzailea) REFERENCES HARTZAILEA(id_hartzailea) ON DELETE RESTRICT,
    FOREIGN KEY (id_langile) REFERENCES LANGILEA(id_langile) ON DELETE SET NULL
) ENGINE=InnoDB;

CREATE TABLE MUGIMENDUA (
    id_mugimendua INT AUTO_INCREMENT PRIMARY KEY,
    data DATETIME DEFAULT CURRENT_TIMESTAMP,
    deskribapena TEXT NOT NULL,
    id_artikulua VARCHAR(15),
    FOREIGN KEY (id_artikulua) REFERENCES ARTIKULUA(id_artikulua) ON DELETE CASCADE
) ENGINE=InnoDB;


/* ROL ETA ERABILTZAILEEN TXERTAKETAK */
CREATE ROLE IF NOT EXISTS 'admin_rola';
CREATE ROLE IF NOT EXISTS 'udaltzain_rola';

GRANT ALL PRIVILEGES ON erronka_galduak.* TO 'admin_rola';
GRANT SELECT, INSERT, UPDATE ON erronka_galduak.* TO 'udaltzain_rola';
REVOKE INSERT, UPDATE, DELETE ON erronka_galduak.LANGILEA FROM 'udaltzain_rola';
REVOKE INSERT, UPDATE, DELETE ON erronka_galduak.ROLA FROM 'udaltzain_rola';

CREATE USER IF NOT EXISTS 'admin'@'%' IDENTIFIED BY 'admin123';
GRANT 'admin_rola' TO 'admin'@'%';
SET DEFAULT ROLE 'admin_rola' FOR 'admin'@'%';

CREATE USER IF NOT EXISTS 'udaltzain1'@'%' IDENTIFIED BY 'udal123';
GRANT 'udaltzain_rola' TO 'udaltzain1'@'%';
SET DEFAULT ROLE 'udaltzain_rola' FOR 'udaltzain1'@'%';

CREATE USER IF NOT EXISTS 'udaltzain2'@'%' IDENTIFIED BY 'udal123';
GRANT 'udaltzain_rola' TO 'udaltzain2'@'%';
SET DEFAULT ROLE 'udaltzain_rola' FOR 'udaltzain2'@'%';

FLUSH PRIVILEGES;

-- ========================================== --
--------------- DATUEN TXERTAKETA -------------
-- ========================================== --
INSERT INTO ROLA (deskribapena) VALUES ('Administratzailea'), ('Udaltzaina');

INSERT INTO LANGILEA (izena, abizena, erabiltzailea, pasahitza_hash, id_rola) VALUES 
('Miren', 'Agirre', 'admin', SHA2('admin123', 256), 1),
('Jon', 'Zabala', 'udaltzain1', SHA2('udal123', 256), 2);

INSERT INTO KATEGORIA (izena) VALUES ('Osagarri pertsonalak'), ('Betaurrekoak'), ('Giltzak'), ('Bestelakoak');

-- bHa_da = Bolumen Handikoen Armairua da
INSERT INTO KOKALEKUA (armairua, apala, bka_da) VALUES 
('F', '004', FALSE),
('A', '001', FALSE),
('BHA', '1', TRUE); 

INSERT INTO HARTZAILEA (id_hartzailea, telefonoa) VALUES 
(1, '688729149'), (2, '664146219');

¡INSERT INTO JABEA (id_hartzailea, nan, izena, abizena) VALUES 
(1, '11111111A', 'RAMON', 'INFANTE'),
(2, '22222222B', 'ISMAEL', 'CORTES');

INSERT INTO ARTIKULUA (id_artikulua, a_izena, a_deskribapena, egoera, sarrera_data, iraungitze_data, id_kategoria, id_kokalekua) VALUES 
('G-092-26', 'Kartera oria', 'Osagarri pertsonalak, kartera...', 'aurkitua', '2026-04-01', '2028-04-01', 1, 1),
('G-093-26', 'Umearen betaurrekoak', 'Betaurrekoak umearentzat', 'aurkitua', '2026-04-07', '2028-04-07', 2, 1);

-- Mugimendu baten adibidea trazabilitaterako, TRIGGER BIDEZ
INSERT INTO MUGIMENDUA (deskribapena, id_artikulua) VALUES 
('Artikulua sisteman erregistratu da F-004 kokalekuan', 'G-092-26');