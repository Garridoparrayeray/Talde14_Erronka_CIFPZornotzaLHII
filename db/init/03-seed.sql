-- =====================================================================
-- BERMEOKO UDALA - GALDUTAKOAK
-- 03-seed.sql · Probetarako datuak
-- =====================================================================

USE erronka_galduak;

-- ---------------------------------------------------------------------
-- ROLA 
-- ---------------------------------------------------------------------
INSERT INTO ROLA (deskribapena) VALUES
    ('Administratzailea'),
    ('Udaltzaina');

-- ---------------------------------------------------------------------
-- LANGILEA
-- ---------------------------------------------------------------------
INSERT INTO LANGILEA (izena, abizena, erabiltzailea, pasahitza_hash, id_rola) VALUES
    ('Miren', 'Agirre', 'admin',
     '1234', 1),
    ('Jon',   'Zabala', 'udaltzain1',
     '1234', 2);

-- ---------------------------------------------------------------------
-- KATEGORIA
-- ---------------------------------------------------------------------
INSERT INTO KATEGORIA (izena) VALUES
    ('Osagarri pertsonalak'),
    ('Betaurrekoak'),
    ('Giltzak'),
    ('Bestelakoak');

-- ---------------------------------------------------------------------
-- KOKALEKUA · bka_da = Bolumen Handikoen Armairua
-- ---------------------------------------------------------------------
INSERT INTO KOKALEKUA (armairua, apala, bka_da) VALUES
    ('F',   '004', FALSE),
    ('A',   '001', FALSE),
    ('BHA', '1',   TRUE);

-- ---------------------------------------------------------------------
-- HARTZAILEA + JABEA (herentzia)
-- ---------------------------------------------------------------------
INSERT INTO HARTZAILEA (id_hartzailea, telefonoa) VALUES
    (1, '688729149'),
    (2, '664146219');

INSERT INTO JABEA (id_hartzailea, nan, izena, abizena) VALUES
    (1, '11111111A', 'RAMON',  'INFANTE'),
    (2, '22222222B', 'ISMAEL', 'CORTES');

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
-- MUGIMENDUA 
-- ---------------------------------------------------------------------
INSERT INTO MUGIMENDUA (deskribapena, id_artikulua) VALUES
    ('Artikulua sisteman erregistratu da F-004 kokalekuan', 'G-092-26'),
    ('Artikulua sisteman erregistratu da A-001 kokalekuan', 'G-093-26');

SELECT '[INIT] Probetarako datuak txertatuta' AS mezua;
SELECT COUNT(*) AS langile_kop FROM LANGILEA;
SELECT COUNT(*) AS artikulu_kop FROM ARTIKULUA;