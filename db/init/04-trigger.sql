-- =====================================================================
-- TRIGGERS
-- =====================================================================

DELIMITER //

-- ---------------------------------------------------------------------
-- 1. TRIGGER: MUGIMENDUAK
-- ---------------------------------------------------------------------

CREATE TRIGGER trg_historial_estado_artikulua
AFTER UPDATE ON ARTIKULUA
FOR EACH ROW
BEGIN
    IF OLD.egoera <> NEW.egoera THEN
        INSERT INTO MUGIMENDUA (mota, deskribapena, id_artikulua)
        VALUES (
            CASE
                WHEN NEW.egoera = 'bueltatua' THEN 'irteera_jabea'
                WHEN NEW.egoera = 'iraungita' THEN 'iraungitze_alerta'
                ELSE 'barne_mugimendua'
            END,
            CONCAT('Automatikoa: Egoera aldaketa (', OLD.egoera, ' -> ', NEW.egoera, ').'),
            NEW.id_artikulua
        );
    END IF;
END; //

-- ---------------------------------------------------------------------
-- 2. TRIGGER: Dataren baliotzea INSERT
-- ---------------------------------------------------------------------

CREATE TRIGGER trg_validar_fechas_ins
BEFORE INSERT ON ARTIKULUA
FOR EACH ROW
BEGIN
    IF NEW.iraungitze_data IS NOT NULL AND NEW.iraungitze_data < NEW.sarrera_data THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Errorea: Iraungitze-data ezin da sarrera-data baino lehenagokoa izan.';
    END IF;
END; //

-- ---------------------------------------------------------------------
-- 3. TRIGGER: Dataren baliotzea UPDATE
-- ---------------------------------------------------------------------

CREATE TRIGGER trg_validar_fechas_upd
BEFORE UPDATE ON ARTIKULUA
FOR EACH ROW
BEGIN
    IF NEW.iraungitze_data IS NOT NULL AND NEW.iraungitze_data < NEW.sarrera_data THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Errorea: Iraungitze-data ezin da sarrera-data baino lehenagokoa izan.';
    END IF;
END; //

-- ---------------------------------------------------------------------
-- 4. TRIGGER: Automatizazioa (EMANALDIA -> ARTIKULUA)
-- ---------------------------------------------------------------------

CREATE TRIGGER trg_emanaldia_eguneratu_artikulua
AFTER INSERT ON EMANALDIA
FOR EACH ROW
BEGIN
    UPDATE ARTIKULUA 
    SET egoera = 'bueltatua' 
    WHERE id_artikulua = NEW.id_artikulua;
END; //

-- ---------------------------------------------------------------------
-- 5. TRIGGER: Errore kontrola
-- ---------------------------------------------------------------------

CREATE TRIGGER trg_blokeatu_bueltatuak
BEFORE UPDATE ON ARTIKULUA
FOR EACH ROW
BEGIN
    IF OLD.egoera = 'bueltatua' AND NEW.egoera = 'bueltatua' THEN
        SIGNAL SQLSTATE '45000' 
        SET MESSAGE_TEXT = 'Errorea: Ezin da aldatu dagoeneko bueltatua izan den artikulu bat.';
    END IF;
END; //

DELIMITER ;