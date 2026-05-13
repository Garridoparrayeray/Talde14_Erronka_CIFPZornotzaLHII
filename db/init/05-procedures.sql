
-- IRAUNGITAKO ARTIKULUAK KUDERATU ETA ERREKLAMAZIOAK EBATZI PROZEDURAK
DELIMITER //

CREATE PROCEDURE sp_iraungitakoak_kudeatu(OUT p_eguneratutako_kopurua INT)
BEGIN
    SELECT COUNT(*) INTO p_eguneratutako_kopurua
    FROM ARTIKULUA
    WHERE egoera = 'aurkitua' AND iraungitze_data < CURDATE();

    UPDATE ARTIKULUA
    SET egoera = 'iraungita'
    WHERE egoera = 'aurkitua' AND iraungitze_data < CURDATE();
    
END; //
DELIMITER ;



DELIMITER //

CREATE PROCEDURE sp_erreklamazioa_ebatzi(
    IN p_id_erreklamazioa INT,
    IN p_id_artikulua VARCHAR(50),
    IN p_id_langile INT,
    IN p_oharrak VARCHAR(255)
)
BEGIN
    DECLARE v_id_hartzailea INT;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION 
    BEGIN

        ROLLBACK;
    END;

    START TRANSACTION;
    
    SELECT id_hartzailea INTO v_id_hartzailea 
    FROM ERREKLAMAZIOA 
    WHERE id_erreklamazioa = p_id_erreklamazioa;
    
    UPDATE ERREKLAMAZIOA 
    SET errek_egoera = 'ebatzita' 
    WHERE id_erreklamazioa = p_id_erreklamazioa;
    
    INSERT INTO EMANALDIA (emate_data, oharrak, id_artikulua, id_hartzailea, id_langile)
    VALUES (CURDATE(), p_oharrak, p_id_artikulua, v_id_hartzailea, p_id_langile);
    
    COMMIT;
END; //

DELIMITER ;