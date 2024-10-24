package org.example.margoclandataget.Repository;

import jakarta.transaction.Transactional;
import org.example.margoclandataget.Entity.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PlayerRepo extends JpaRepository<Player, Long>{
    Player findPlayerByCharidAndPlayerid(String charid, String playerid);
    Player findPlayerByName(String name);

    List<Player> findPlayersByClan(String clan);

    @Transactional
    void deletePlayerByPlayerid(String Playerid);

    List<Player> findByUrl(String url);

    @Query(value = "SELECT charid, name FROM Player", nativeQuery = true)
    List<Object[]> findAllCharidAndName();
}
