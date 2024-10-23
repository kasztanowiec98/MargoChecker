package org.example.margoclandataget.Service;

import org.example.margoclandataget.Entity.Player;
import org.example.margoclandataget.Repository.PlayerRepo;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlayerService {
    private static PlayerRepo playerRepo;

    @Autowired
    public PlayerService(PlayerRepo playerRepo) {
        this.playerRepo = playerRepo;
    }

    public PlayerService() {
    }

    public List<Player> saveAllPlayers(List<Player> playerList){
        return playerRepo.saveAll(playerList);
    }

    public String getMainCharFromDoc(Document doc){
        String title = doc.title().trim();
        return title.contains(" -") ? title.substring(0, title.indexOf(" -")) : title;
    }

    public Player setPlayerData(Player player){
        return player;
    };

}
