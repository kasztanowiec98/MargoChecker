package org.example.margoclandataget.Repository;

import jakarta.persistence.Id;
import org.example.margoclandataget.Entity.Equipment;
import org.example.margoclandataget.Entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface EquipmentRepo extends JpaRepository<Equipment, Long>{
    Equipment findFirstByOrderByInsertDateDesc();

    @Query("SELECT i FROM Equipment i WHERE i.insertDate = (SELECT MAX(i2.insertDate) FROM Equipment i2)")
    List<Equipment> findAllWithLatestInsertDate();
    List<Equipment> findByInsertDate(LocalDateTime insertDate);
}
