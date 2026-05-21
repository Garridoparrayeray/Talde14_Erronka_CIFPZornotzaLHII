-- =====================================================================
-- BERMEOKO UDALA - GALDUTAKOAK
-- 03-seed.sql · Hasierako datuak
-- Pasahitza guztientzat: '1234' (BCrypt cost 10)
-- =====================================================================

USE erronka_galduak;

-- ---------------------------------------------------------------------
-- ROLA
-- ---------------------------------------------------------------------
INSERT INTO ROLA (deskribapena) VALUES
    ('Administratzailea'),
    ('Langilea'),
    ('Ikuslea');

-- ---------------------------------------------------------------------
-- LANGILEA
-- ---------------------------------------------------------------------
INSERT INTO LANGILEA (izena, abizena, erabiltzailea, pasahitza_hash, id_rola) VALUES
    ('Miren', 'Agirre',  'admin',
     '$2a$10$oFl.yJsTOmztwUCT0ov7K.VoNDHioYD/wGpqbNP2U3kTwgB4B7Ica', 1),
    ('Jon',   'Zabala',  'langile1',
     '$2a$10$oFl.yJsTOmztwUCT0ov7K.VoNDHioYD/wGpqbNP2U3kTwgB4B7Ica', 2),
    ('Ander', 'Txurru',  'ikusle1',
     '$2a$10$oFl.yJsTOmztwUCT0ov7K.VoNDHioYD/wGpqbNP2U3kTwgB4B7Ica', 3);

-- ---------------------------------------------------------------------
-- KATEGORIA
-- ---------------------------------------------------------------------
INSERT INTO KATEGORIA (izena) VALUES
    ('Osagarri pertsonalak'),
    ('Betaurrekoak'),
    ('Giltzak'),
    ('Teknologia'),
    ('Arropa eta osagarriak'),
    ('Bestelakoak');

-- ---------------------------------------------------------------------
-- KOKALEKUA · 6 zutabe (A B D E F G) × 6 apala (1-6) + Logela (BHA)
-- ---------------------------------------------------------------------
INSERT INTO KOKALEKUA (armairua, apala, bha_da) VALUES
    ('A','1',FALSE),('A','2',FALSE),('A','3',FALSE),('A','4',FALSE),('A','5',FALSE),('A','6',FALSE),
    ('B','1',FALSE),('B','2',FALSE),('B','3',FALSE),('B','4',FALSE),('B','5',FALSE),('B','6',FALSE),
    ('D','1',FALSE),('D','2',FALSE),('D','3',FALSE),('D','4',FALSE),('D','5',FALSE),('D','6',FALSE),
    ('E','1',FALSE),('E','2',FALSE),('E','3',FALSE),('E','4',FALSE),('E','5',FALSE),('E','6',FALSE),
    ('F','1',FALSE),('F','2',FALSE),('F','3',FALSE),('F','4',FALSE),('F','5',FALSE),('F','6',FALSE),
    ('G','1',FALSE),('G','2',FALSE),('G','3',FALSE),('G','4',FALSE),('G','5',FALSE),('G','6',FALSE),
    ('Logela', NULL, TRUE);

-- ---------------------------------------------------------------------
-- HARTZAILEA + JABEA
-- ---------------------------------------------------------------------
INSERT INTO HARTZAILEA (id_hartzailea, telefonoa, emaila) VALUES
    (1, '688729149', 'ramon.infante@email.com'),
    (2, '664146219', 'ismael.cortes@email.com'),
    (3, '612345678', NULL);

INSERT INTO JABEA (id_hartzailea, nan, izena, abizena) VALUES
    (1, '11111111A', 'RAMON',   'INFANTE'),
    (2, '22222222B', 'ISMAEL',  'CORTES'),
    (3, '33333333C', 'AMAIA',   'ETXEBARRIA');

-- ---------------------------------------------------------------------
-- ARTIKULUA (7 artikulu)
-- id: G-NNN-AA formatua · kokalekua: id 1-6 = A1-A6, 7-12 = B1-B6, etc.
-- ---------------------------------------------------------------------
INSERT INTO ARTIKULUA
    (id_artikulua, a_izena, a_deskribapena, egoera,
     sarrera_data, iraungitze_data, id_kategoria, id_kokalekua)
VALUES
    ('G-001-26',
     'Kartera oria',
     'Kartera larruzkoa, oria kolorekoa. Barruan txartel batzuk ditu.',
     'aurkitua', '2026-01-15', '2028-01-15', 1, 5),

    ('G-002-26',
     'Umearen betaurrekoak',
     'Betaurrekoak umearentzat, plastiko koloretsuak, markoa urdina.',
     'aurkitua', '2026-01-22', '2028-01-22', 2, 2),

    ('G-003-26',
     'Giltzak Honda giltza batekin',
     'Hiru etxe giltza eta Honda motako auto giltza bat, giltzetako metalezko orraztekin.',
     'aurkitua', '2026-02-03', '2028-02-03', 3, 8),

    ('G-004-26',
     'Samsung Galaxy telefonoa',
     'Samsung Galaxy serie bat, pantaila pitzatua, kargagailurik gabe aurkitu da.',
     'aurkitua', '2026-02-18', '2028-02-18', 4, 14),

    ('G-005-26',
     'Bufanda grisa artilezkoa',
     'Bufanda grisa, artile naturalez egina, mutur batean korapilo bat du.',
     'aurkitua', '2026-03-05', '2028-03-05', 5, 20),

    ('G-006-26',
     'Zorro berdea dokumentuekin',
     'Plastikozko zorro berdea, barruan dokumentu batzuk eta folio batzuk ditu.',
     'aurkitua', '2026-03-20', '2028-03-20', 6, 26),

    ('G-007-26',
     'Aterkia beltza',
     'Aterkia beltza, tamaina ertainekoa, helduleku makurtua du.',
     'bueltatua', '2026-04-02', '2028-04-02', 6, 32);

-- ---------------------------------------------------------------------
-- MUGIMENDUA (sarrera-erregistroak)
-- ---------------------------------------------------------------------
INSERT INTO MUGIMENDUA (deskribapena, id_artikulua, id_langile) VALUES
    ('Artikulua sisteman erregistratu da A-5 kokalekuan', 'G-001-26', 2),
    ('Artikulua sisteman erregistratu da A-2 kokalekuan', 'G-002-26', 2),
    ('Artikulua sisteman erregistratu da B-2 kokalekuan', 'G-003-26', 3),
    ('Artikulua sisteman erregistratu da D-2 kokalekuan', 'G-004-26', 2),
    ('Artikulua sisteman erregistratu da E-2 kokalekuan', 'G-005-26', 3),
    ('Artikulua sisteman erregistratu da F-2 kokalekuan', 'G-006-26', 2),
    ('Artikulua sisteman erregistratu da G-2 kokalekuan', 'G-007-26', 2);

-- ---------------------------------------------------------------------
-- HARTZAILEA + EMANALDIA (G-007-26 bueltatua da)
-- ---------------------------------------------------------------------
INSERT INTO HARTZAILEA (id_hartzailea, telefonoa) VALUES (4, '699887766');
INSERT INTO JABEA (id_hartzailea, nan, izena, abizena) VALUES
    (4, '44444444D', 'LEIRE', 'MENDIZABAL');

INSERT INTO EMANALDIA (emate_data, oharrak, id_artikulua, id_hartzailea, id_langile) VALUES
    ('2026-04-10', 'Jabeak ekarri du NAN jatorrizkoa', 'G-007-26', 4, 2);

-- ---------------------------------------------------------------------
-- ERREKLAMAZIOA
-- ---------------------------------------------------------------------
INSERT INTO ERREKLAMAZIOA
    (erreklamazio_data, errek_egoera, deskribapen_bilatua, id_hartzailea, id_kategoria, id_langile)
VALUES
    ('2026-02-20', 'irekita',  'Kartera beltza txartelekin eta diru pixka batekin', 1, 1, 2),
    ('2026-03-10', 'irekita',  'Giltzak, etxekoak eta Volkswagen auto giltza bat',  2, 3, 2),
    ('2026-04-05', 'ebatzita', 'Betaurrekoak graduatuak, urrezko markoa',            3, 2, 2);

-- ---------------------------------------------------------------------
SELECT '[INIT] Seed txertatuta' AS mezua;
SELECT COUNT(*) AS langile_kop      FROM LANGILEA;
SELECT COUNT(*) AS kokalekua_kop    FROM KOKALEKUA;
SELECT COUNT(*) AS artikulu_kop     FROM ARTIKULUA;
SELECT COUNT(*) AS erreklamazio_kop FROM ERREKLAMAZIOA;
