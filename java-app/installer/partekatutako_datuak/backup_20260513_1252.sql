-- Babes-kopia: 20260513_1252
USE erronka_galduak;

-- ROLA
DELETE FROM ROLA;
INSERT INTO ROLA VALUES ('1', 'Administratzailea');
INSERT INTO ROLA VALUES ('2', 'Langilea');
INSERT INTO ROLA VALUES ('3', 'Ikuslea');

-- LANGILEA
DELETE FROM LANGILEA;
INSERT INTO LANGILEA VALUES ('1', 'Miren', 'Agirre', 'admin', '$2a$10$oFl.yJsTOmztwUCT0ov7K.VoNDHioYD/wGpqbNP2U3kTwgB4B7Ica', '1');
INSERT INTO LANGILEA VALUES ('2', 'Jon', 'Zabala', 'langile1', '$2a$10$oFl.yJsTOmztwUCT0ov7K.VoNDHioYD/wGpqbNP2U3kTwgB4B7Ica', '2');
INSERT INTO LANGILEA VALUES ('3', 'Ander', 'Txurru', 'ikusle1', '$2a$10$oFl.yJsTOmztwUCT0ov7K.VoNDHioYD/wGpqbNP2U3kTwgB4B7Ica', '3');

-- KATEGORIA
DELETE FROM KATEGORIA;
INSERT INTO KATEGORIA VALUES ('1', 'Osagarri pertsonalak');
INSERT INTO KATEGORIA VALUES ('2', 'Betaurrekoak');
INSERT INTO KATEGORIA VALUES ('3', 'Giltzak');
INSERT INTO KATEGORIA VALUES ('4', 'Teknologia');
INSERT INTO KATEGORIA VALUES ('5', 'Arropa eta osagarriak');
INSERT INTO KATEGORIA VALUES ('6', 'Bestelakoak');

-- KOKALEKUA
DELETE FROM KOKALEKUA;
INSERT INTO KOKALEKUA VALUES ('1', 'A', '1', '0');
INSERT INTO KOKALEKUA VALUES ('2', 'A', '2', '0');
INSERT INTO KOKALEKUA VALUES ('3', 'A', '3', '0');
INSERT INTO KOKALEKUA VALUES ('4', 'A', '4', '0');
INSERT INTO KOKALEKUA VALUES ('5', 'A', '5', '0');
INSERT INTO KOKALEKUA VALUES ('6', 'A', '6', '0');
INSERT INTO KOKALEKUA VALUES ('7', 'B', '1', '0');
INSERT INTO KOKALEKUA VALUES ('8', 'B', '2', '0');
INSERT INTO KOKALEKUA VALUES ('9', 'B', '3', '0');
INSERT INTO KOKALEKUA VALUES ('10', 'B', '4', '0');
INSERT INTO KOKALEKUA VALUES ('11', 'B', '5', '0');
INSERT INTO KOKALEKUA VALUES ('12', 'B', '6', '0');
INSERT INTO KOKALEKUA VALUES ('13', 'D', '1', '0');
INSERT INTO KOKALEKUA VALUES ('14', 'D', '2', '0');
INSERT INTO KOKALEKUA VALUES ('15', 'D', '3', '0');
INSERT INTO KOKALEKUA VALUES ('16', 'D', '4', '0');
INSERT INTO KOKALEKUA VALUES ('17', 'D', '5', '0');
INSERT INTO KOKALEKUA VALUES ('18', 'D', '6', '0');
INSERT INTO KOKALEKUA VALUES ('19', 'E', '1', '0');
INSERT INTO KOKALEKUA VALUES ('20', 'E', '2', '0');
INSERT INTO KOKALEKUA VALUES ('21', 'E', '3', '0');
INSERT INTO KOKALEKUA VALUES ('22', 'E', '4', '0');
INSERT INTO KOKALEKUA VALUES ('23', 'E', '5', '0');
INSERT INTO KOKALEKUA VALUES ('24', 'E', '6', '0');
INSERT INTO KOKALEKUA VALUES ('25', 'F', '1', '0');
INSERT INTO KOKALEKUA VALUES ('26', 'F', '2', '0');
INSERT INTO KOKALEKUA VALUES ('27', 'F', '3', '0');
INSERT INTO KOKALEKUA VALUES ('28', 'F', '4', '0');
INSERT INTO KOKALEKUA VALUES ('29', 'F', '5', '0');
INSERT INTO KOKALEKUA VALUES ('30', 'F', '6', '0');
INSERT INTO KOKALEKUA VALUES ('31', 'G', '1', '0');
INSERT INTO KOKALEKUA VALUES ('32', 'G', '2', '0');
INSERT INTO KOKALEKUA VALUES ('33', 'G', '3', '0');
INSERT INTO KOKALEKUA VALUES ('34', 'G', '4', '0');
INSERT INTO KOKALEKUA VALUES ('35', 'G', '5', '0');
INSERT INTO KOKALEKUA VALUES ('36', 'G', '6', '0');
INSERT INTO KOKALEKUA VALUES ('37', 'Logela', NULL, '1');

-- HARTZAILEA
DELETE FROM HARTZAILEA;
INSERT INTO HARTZAILEA VALUES ('1', NULL, '688729149', 'ramon.infante@email.com');
INSERT INTO HARTZAILEA VALUES ('2', NULL, '664146219', 'ismael.cortes@email.com');
INSERT INTO HARTZAILEA VALUES ('3', NULL, '612345678', NULL);
INSERT INTO HARTZAILEA VALUES ('4', NULL, '699887766', NULL);

-- JABEA
DELETE FROM JABEA;
INSERT INTO JABEA VALUES ('1', '11111111A', 'RAMON', 'INFANTE', NULL, NULL, NULL);
INSERT INTO JABEA VALUES ('2', '22222222B', 'ISMAEL', 'CORTES', NULL, NULL, NULL);
INSERT INTO JABEA VALUES ('3', '33333333C', 'AMAIA', 'ETXEBARRIA', NULL, NULL, NULL);
INSERT INTO JABEA VALUES ('4', '44444444D', 'LEIRE', 'MENDIZABAL', NULL, NULL, NULL);

-- ERAKUNDEA
DELETE FROM ERAKUNDEA;

-- ARTIKULUA
DELETE FROM ARTIKULUA;
INSERT INTO ARTIKULUA VALUES ('G-001-26', 'Kartera oria', 'Kartera larruzkoa, oria kolorekoa. Barruan txartel batzuk ditu.', 'aurkitua', '0', '2026-01-15', '2028-01-15', 'img_1778661117471.png', '1', '5');
INSERT INTO ARTIKULUA VALUES ('G-002-26', 'Umearen betaurrekoak', 'Betaurrekoak umearentzat, plastiko koloretsuak, markoa urdina.', 'aurkitua', '0', '2026-01-22', '2028-01-22', NULL, '2', '2');
INSERT INTO ARTIKULUA VALUES ('G-003-26', 'Giltzak Honda giltza batekin', 'Hiru etxe giltza eta Honda motako auto giltza bat, giltzetako metalezko orraztekin.', 'aurkitua', '0', '2026-02-03', '2028-02-03', NULL, '3', '8');
INSERT INTO ARTIKULUA VALUES ('G-004-26', 'Samsung Galaxy telefonoa', 'Samsung Galaxy serie bat, pantaila pitzatua, kargagailurik gabe aurkitu da.', 'aurkitua', '0', '2026-02-18', '2028-02-18', NULL, '4', '14');
INSERT INTO ARTIKULUA VALUES ('G-005-26', 'Bufanda grisa artilezkoa', 'Bufanda grisa, artile naturalez egina, mutur batean korapilo bat du.', 'aurkitua', '0', '2026-03-05', '2028-03-05', NULL, '5', '20');
INSERT INTO ARTIKULUA VALUES ('G-006-26', 'Zorro berdea dokumentuekin', 'Plastikozko zorro berdea, barruan dokumentu batzuk eta folio batzuk ditu.', 'aurkitua', '0', '2026-03-20', '2028-03-20', NULL, '6', '26');
INSERT INTO ARTIKULUA VALUES ('G-007-26', 'Aterkia beltza', 'Aterkia beltza, tamaina ertainekoa, helduleku makurtua du.', 'bueltatua', '0', '2026-04-02', '2028-04-02', NULL, '6', '32');

-- ERREKLAMAZIOA
DELETE FROM ERREKLAMAZIOA;
INSERT INTO ERREKLAMAZIOA VALUES ('1', '2026-02-20', 'irekita', 'Kartera beltza txartelekin eta diru pixka batekin', '1', '2', '1');
INSERT INTO ERREKLAMAZIOA VALUES ('2', '2026-03-10', 'irekita', 'Giltzak, etxekoak eta Volkswagen auto giltza bat', '2', '2', '3');
INSERT INTO ERREKLAMAZIOA VALUES ('3', '2026-04-05', 'ebatzita', 'Betaurrekoak graduatuak, urrezko markoa', '3', '2', '2');

-- EMANALDIA
DELETE FROM EMANALDIA;
INSERT INTO EMANALDIA VALUES ('1', '2026-04-10', 'Jabeak ekarri du NAN jatorrizkoa', NULL, 'G-007-26', '4', '2');

-- MUGIMENDUA
DELETE FROM MUGIMENDUA;
INSERT INTO MUGIMENDUA VALUES ('1', '2026-05-13 07:38:16', NULL, 'Artikulua sisteman erregistratu da A-5 kokalekuan', 'G-001-26', '2');
INSERT INTO MUGIMENDUA VALUES ('2', '2026-05-13 07:38:16', NULL, 'Artikulua sisteman erregistratu da A-2 kokalekuan', 'G-002-26', '2');
INSERT INTO MUGIMENDUA VALUES ('3', '2026-05-13 07:38:16', NULL, 'Artikulua sisteman erregistratu da B-2 kokalekuan', 'G-003-26', '3');
INSERT INTO MUGIMENDUA VALUES ('4', '2026-05-13 07:38:16', NULL, 'Artikulua sisteman erregistratu da D-2 kokalekuan', 'G-004-26', '2');
INSERT INTO MUGIMENDUA VALUES ('5', '2026-05-13 07:38:16', NULL, 'Artikulua sisteman erregistratu da E-2 kokalekuan', 'G-005-26', '3');
INSERT INTO MUGIMENDUA VALUES ('6', '2026-05-13 07:38:16', NULL, 'Artikulua sisteman erregistratu da F-2 kokalekuan', 'G-006-26', '2');
INSERT INTO MUGIMENDUA VALUES ('7', '2026-05-13 07:38:16', NULL, 'Artikulua sisteman erregistratu da G-2 kokalekuan', 'G-007-26', '2');

-- JAKINARAZPENA
DELETE FROM JAKINARAZPENA;

