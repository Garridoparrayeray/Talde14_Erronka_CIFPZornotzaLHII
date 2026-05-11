-- Babes-kopia: 20260511_1031
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
INSERT INTO LANGILEA VALUES ('4', 'si', 'bi', 'ikuslea', '$2a$10$wleGcdByIGmsP63DOwYBtOgvA02uLX6uUPQ2JcQuOBppIdCVOg9EC', '3');
INSERT INTO LANGILEA VALUES ('5', 'Iker', 'Etxebarria', 'ikeretx', '$2a$10$ODRsX/R.iNWxQ4U3nFAYv.0DGVNRLqRE0dhUI/tnKW1cSKtRZ9qd6', '2');
INSERT INTO LANGILEA VALUES ('6', 'Login', 'Proba', 'logintest', '$2a$10$JlLn9AiYitucY/dB4y7t.uAHm73V46bTkn278A8p6YxdyrEVUiMoq', '2');
INSERT INTO LANGILEA VALUES ('7', 'UpdateBerria', 'ProbaB', 'updatetest', '$2a$10$RX1WHAEBsTZApISH7pNYK.qeSEFWuNLfH9FgdwpEknWhapbcRfpDe', '2');
INSERT INTO LANGILEA VALUES ('16', 'Ezabatu', 'Proba', 'ezabattest', '$2a$10$8r8GGSz8o.EmZclf3F7s8u6T9hvddrdiIbumi43TvC7P2Jxoc/n0m', '2');

-- KATEGORIA
DELETE FROM KATEGORIA;
INSERT INTO KATEGORIA VALUES ('10', 'EguneratutaOk');
INSERT INTO KATEGORIA VALUES ('12', 'ArtKat');
INSERT INTO KATEGORIA VALUES ('13', 'BilKat');
INSERT INTO KATEGORIA VALUES ('14', 'EgKat');
INSERT INTO KATEGORIA VALUES ('15', 'EzKat');
INSERT INTO KATEGORIA VALUES ('16', 'EguneratutaOk');
INSERT INTO KATEGORIA VALUES ('18', 'EguneratuBehar');
INSERT INTO KATEGORIA VALUES ('19', 'EzabatuKat');
INSERT INTO KATEGORIA VALUES ('20', 'ArtKat');
INSERT INTO KATEGORIA VALUES ('21', 'BilKat');
INSERT INTO KATEGORIA VALUES ('22', 'EgKat');
INSERT INTO KATEGORIA VALUES ('23', 'EzKat');
INSERT INTO KATEGORIA VALUES ('24', 'ErrKat');
INSERT INTO KATEGORIA VALUES ('26', 'TestKategoria');
INSERT INTO KATEGORIA VALUES ('27', 'EguneratuBehar');
INSERT INTO KATEGORIA VALUES ('28', 'EzabatuKat');
INSERT INTO KATEGORIA VALUES ('29', 'ArtKat');
INSERT INTO KATEGORIA VALUES ('30', 'BilKat');
INSERT INTO KATEGORIA VALUES ('31', 'EgKat');
INSERT INTO KATEGORIA VALUES ('32', 'EzKat');
INSERT INTO KATEGORIA VALUES ('33', 'ErrKat');

-- KOKALEKUA
DELETE FROM KOKALEKUA;
INSERT INTO KOKALEKUA VALUES ('1', 'A', '1', '0');
INSERT INTO KOKALEKUA VALUES ('2', 'A', '2', '0');
INSERT INTO KOKALEKUA VALUES ('3', 'A', '3', '0');
INSERT INTO KOKALEKUA VALUES ('4', 'A', '4', '0');
INSERT INTO KOKALEKUA VALUES ('5', 'A', '5', '0');
INSERT INTO KOKALEKUA VALUES ('6', 'A', '6', '0');
INSERT INTO KOKALEKUA VALUES ('7', 'B', '1', '0');
INSERT INTO KOKALEKUA VALUES ('8', 'B', '3', '0');
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
INSERT INTO KOKALEKUA VALUES ('38', 'A', '1', '0');
INSERT INTO KOKALEKUA VALUES ('39', 'B', '3', '0');
INSERT INTO KOKALEKUA VALUES ('41', 'X', '1', '0');
INSERT INTO KOKALEKUA VALUES ('42', 'A', '1', '0');
INSERT INTO KOKALEKUA VALUES ('43', 'B', '3', '0');
INSERT INTO KOKALEKUA VALUES ('45', 'X', '1', '0');
INSERT INTO KOKALEKUA VALUES ('46', 'A', '1', '0');
INSERT INTO KOKALEKUA VALUES ('47', 'B', '2', '0');
INSERT INTO KOKALEKUA VALUES ('49', 'X', '1', '0');

-- HARTZAILEA
DELETE FROM HARTZAILEA;
INSERT INTO HARTZAILEA VALUES ('1', NULL, '688729149', 'ramon.infante@email.com');
INSERT INTO HARTZAILEA VALUES ('2', NULL, '664146219', 'ismael.cortes@email.com');
INSERT INTO HARTZAILEA VALUES ('3', NULL, '612345678', NULL);
INSERT INTO HARTZAILEA VALUES ('4', NULL, '699887766', NULL);
INSERT INTO HARTZAILEA VALUES ('5', NULL, 'sadfafsa', NULL);
INSERT INTO HARTZAILEA VALUES ('6', NULL, '600000001', 'mikel@test.com');

-- JABEA
DELETE FROM JABEA;
INSERT INTO JABEA VALUES ('1', '11111111A', 'RAMON', 'INFANTE', NULL, NULL, NULL);
INSERT INTO JABEA VALUES ('2', '22222222B', 'ISMAEL', 'CORTES', NULL, NULL, NULL);
INSERT INTO JABEA VALUES ('3', '33333333C', 'AMAIA', 'ETXEBARRIA', NULL, NULL, NULL);
INSERT INTO JABEA VALUES ('4', '44444444D', 'LEIRE', 'MENDIZABAL', NULL, NULL, NULL);
INSERT INTO JABEA VALUES ('5', 'affatwrtergare', 'adfadsf', 'fadfsadf', NULL, NULL, NULL);
INSERT INTO JABEA VALUES ('6', '12345678A', 'Mikel', 'Uriarte', NULL, NULL, NULL);

-- ERAKUNDEA
DELETE FROM ERAKUNDEA;

-- ARTIKULUA
DELETE FROM ARTIKULUA;
INSERT INTO ARTIKULUA VALUES ('G-001-25', 'Zorroa', 'Larruzko zorroa gorria', 'aurkitua', '0', '2025-01-15', '2027-01-15', NULL, '10', '41');
INSERT INTO ARTIKULUA VALUES ('G-001-26', 'Kartera oria', 'Kartera larruzkoa, oria kolorekoa. Barruan txartel batzuk ez ditu.', 'aurkitua', '0', '2026-01-15', '2028-01-15', 'img_1778327724736.jpeg', NULL, '5');
INSERT INTO ARTIKULUA VALUES ('G-002-25', 'Betaurrekoak', 'Betaurrekoak berde', 'aurkitua', '0', '2025-01-15', '2027-01-15', NULL, NULL, NULL);
INSERT INTO ARTIKULUA VALUES ('G-002-26', 'Umearen betaurrekoak', 'Betaurrekoak umearentzat, plastiko koloretsuak, markoa urdina.', 'aurkitua', '0', '2026-01-22', '2028-01-22', 'img_1778327716722.jpeg', NULL, '2');
INSERT INTO ARTIKULUA VALUES ('G-003-25', 'GiltzeBerria', 'Giltza berri bat', 'aurkitua', '0', '2025-01-15', '2027-01-15', NULL, '12', NULL);
INSERT INTO ARTIKULUA VALUES ('G-003-26', 'Giltzak Honda giltza batekin', 'Hiru etxe giltza eta Honda motako auto giltza bat, giltzetako metalezko orraztekin.', 'aurkitua', '0', '2026-02-03', '2028-02-03', NULL, NULL, '8');
INSERT INTO ARTIKULUA VALUES ('G-004-25', 'Zorroa', 'Larruzko zorroa gorria', 'aurkitua', '0', '2025-01-15', '2027-01-15', NULL, '10', '41');
INSERT INTO ARTIKULUA VALUES ('G-004-26', 'Samsung Galaxy telefonoa', 'Samsung Galaxy serie bat, pantaila pitzatua, kargagailurik gabe aurkitu da.', 'bueltatua', '0', '2026-02-18', '2028-02-18', NULL, NULL, '14');
INSERT INTO ARTIKULUA VALUES ('G-005-25', 'Betaurrekoak', 'Betaurrekoak berde', 'aurkitua', '0', '2025-01-15', '2027-01-15', NULL, NULL, NULL);
INSERT INTO ARTIKULUA VALUES ('G-005-26', 'Bufanda grisa artilezkoa', 'Bufanda grisa, artile naturalez egina, mutur batean korapilo bat du.', 'aurkitua', '0', '2026-03-05', '2028-03-05', NULL, NULL, '20');
INSERT INTO ARTIKULUA VALUES ('G-006-25', 'GiltzeBerria', 'Giltza berri bat', 'aurkitua', '0', '2025-01-15', '2027-01-15', NULL, '12', NULL);
INSERT INTO ARTIKULUA VALUES ('G-006-26', 'Zorro berdea dokumentuekin', 'Plastikozko zorro berdea, barruan dokumentu batzuk eta folio batzuk ditu.', 'aurkitua', '0', '2026-03-20', '2028-03-20', NULL, NULL, '26');
INSERT INTO ARTIKULUA VALUES ('G-007-25', 'Zorroa', 'Larruzko zorroa gorria', 'aurkitua', '0', '2025-01-14', '2027-01-14', NULL, '12', '41');
INSERT INTO ARTIKULUA VALUES ('G-007-26', 'Aterkia beltza', 'Aterkia beltza, tamaina ertainekoa, helduleku makurtua du.', 'bueltatua', '0', '2026-04-02', '2028-04-02', NULL, NULL, '32');
INSERT INTO ARTIKULUA VALUES ('G-008-25', 'Betaurrekoak', 'Betaurrekoak berde', 'aurkitua', '0', '2025-01-14', '2027-01-14', NULL, '13', NULL);
INSERT INTO ARTIKULUA VALUES ('G-009-25', 'GiltzeBerria', 'Giltza berri bat', 'aurkitua', '0', '2025-01-14', '2027-01-14', NULL, '14', NULL);

-- ERREKLAMAZIOA
DELETE FROM ERREKLAMAZIOA;
INSERT INTO ERREKLAMAZIOA VALUES ('1', '2026-02-20', 'ebatzita', 'Kartera beltza txartelekin eta diru pixka batekin', '1', '2', NULL);
INSERT INTO ERREKLAMAZIOA VALUES ('2', '2026-03-10', 'irekita', 'Giltzak, etxekoak eta Volkswagen auto giltza bat', '2', '2', NULL);
INSERT INTO ERREKLAMAZIOA VALUES ('3', '2026-04-05', 'ebatzita', 'Betaurrekoak graduatuak, urrezko markoa', '3', '2', NULL);
INSERT INTO ERREKLAMAZIOA VALUES ('4', '2026-05-09', 'irekita', 'Larruzko zorro gorri bat', '6', '1', '14');
INSERT INTO ERREKLAMAZIOA VALUES ('5', '2026-05-09', 'irekita', 'Larruzko zorro gorri bat', '6', '1', '14');
INSERT INTO ERREKLAMAZIOA VALUES ('6', '2026-05-11', 'irekita', 'Larruzko zorro gorri bat', '6', '1', '24');

-- EMANALDIA
DELETE FROM EMANALDIA;
INSERT INTO EMANALDIA VALUES ('1', '2026-04-10', 'Jabeak ekarri du NAN jatorrizkoa', NULL, 'G-007-26', '4', '2');
INSERT INTO EMANALDIA VALUES ('2', '2026-05-09', 'werwetwtf', 'sinadura_1778328460077.pdf', 'G-004-26', '5', '1');

-- MUGIMENDUA
DELETE FROM MUGIMENDUA;
INSERT INTO MUGIMENDUA VALUES ('1', '2026-05-08 11:34:50', NULL, 'Artikulua sisteman erregistratu da A-5 kokalekuan', 'G-001-26', '2');
INSERT INTO MUGIMENDUA VALUES ('2', '2026-05-08 11:34:50', NULL, 'Artikulua sisteman erregistratu da A-2 kokalekuan', 'G-002-26', '2');
INSERT INTO MUGIMENDUA VALUES ('3', '2026-05-08 11:34:50', NULL, 'Artikulua sisteman erregistratu da B-2 kokalekuan', 'G-003-26', NULL);
INSERT INTO MUGIMENDUA VALUES ('4', '2026-05-08 11:34:50', NULL, 'Artikulua sisteman erregistratu da D-2 kokalekuan', 'G-004-26', '2');
INSERT INTO MUGIMENDUA VALUES ('5', '2026-05-08 11:34:50', NULL, 'Artikulua sisteman erregistratu da E-2 kokalekuan', 'G-005-26', NULL);
INSERT INTO MUGIMENDUA VALUES ('6', '2026-05-08 11:34:50', NULL, 'Artikulua sisteman erregistratu da F-2 kokalekuan', 'G-006-26', '2');
INSERT INTO MUGIMENDUA VALUES ('7', '2026-05-08 11:34:50', NULL, 'Artikulua sisteman erregistratu da G-2 kokalekuan', 'G-007-26', '2');
INSERT INTO MUGIMENDUA VALUES ('8', '2026-05-09 12:07:40', 'irteera_jabea', 'Automatikoa: Egoera aldaketa (aurkitua -> bueltatua).', 'G-004-26', NULL);
INSERT INTO MUGIMENDUA VALUES ('9', '2026-05-09 12:07:40', NULL, 'Artikulua adfadsf fadfsadf-ri eman zaio.', 'G-004-26', '1');
INSERT INTO MUGIMENDUA VALUES ('10', '2026-05-09 17:00:45', NULL, 'Artikulua sisteman erregistratu da: G-001-25', 'G-001-25', NULL);
INSERT INTO MUGIMENDUA VALUES ('11', '2026-05-09 17:00:45', NULL, 'Artikulua sisteman erregistratu da: G-002-25', 'G-002-25', NULL);
INSERT INTO MUGIMENDUA VALUES ('12', '2026-05-09 17:00:45', NULL, 'Artikulua sisteman erregistratu da: G-003-25', 'G-003-25', NULL);
INSERT INTO MUGIMENDUA VALUES ('14', '2026-05-09 17:00:48', NULL, 'Artikulua sisteman erregistratu da: G-004-25', 'G-004-25', NULL);
INSERT INTO MUGIMENDUA VALUES ('15', '2026-05-09 17:00:49', NULL, 'Artikulua sisteman erregistratu da: G-005-25', 'G-005-25', NULL);
INSERT INTO MUGIMENDUA VALUES ('16', '2026-05-09 17:00:49', NULL, 'Artikulua sisteman erregistratu da: G-006-25', 'G-006-25', NULL);
INSERT INTO MUGIMENDUA VALUES ('18', '2026-05-11 06:45:26', NULL, 'Artikulua sisteman erregistratu da: G-007-25', 'G-007-25', NULL);
INSERT INTO MUGIMENDUA VALUES ('19', '2026-05-11 06:45:26', NULL, 'Artikulua sisteman erregistratu da: G-008-25', 'G-008-25', NULL);
INSERT INTO MUGIMENDUA VALUES ('20', '2026-05-11 06:45:26', NULL, 'Artikulua sisteman erregistratu da: G-009-25', 'G-009-25', NULL);

-- JAKINARAZPENA
DELETE FROM JAKINARAZPENA;

