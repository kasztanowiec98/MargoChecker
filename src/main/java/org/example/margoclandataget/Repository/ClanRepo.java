package org.example.margoclandataget.Repository;

import org.example.margoclandataget.Entity.ClanEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClanRepo extends JpaRepository<ClanEntity, Long>{

    ClanEntity findByClanName(String clanName);
}
