package org.example.margoclandataget;

import org.example.margoclandataget.Entity.Player;
import org.example.margoclandataget.Service.PlayerService;
import org.jsoup.nodes.Document;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.net.HttpURLConnection;
import java.net.URL;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.io.IOException;

import java.sql.SQLOutput;
import java.util.*;

import static java.lang.Thread.sleep;

@SpringBootApplication
public class MargoClanDataGetApplication {

    public static void main(String[] args) throws InterruptedException {
        SpringApplication.run(MargoClanDataGetApplication.class, args);



        /*List<Player> clanPlayers = new ArrayList<>();
        List<Player> everyPlayer = new ArrayList<>();
        List<String> urlGordionChars;
        clanPlayers = SeleniumGet.getUsersLinks("https://www.margonem.pl/guilds/view,gordion,9", clanPlayers);

        for(Player player: clanPlayers){
            System.out.println(player.getMainClanPlayerName());
            System.out.println("gracze:");
            BasicGetReq.getPlayerInfo(player.getUrl(), everyPlayer, player.getMainClanPlayerName());
            System.out.println("--------------------");
            sleep(2000);
        }

            PlayerService playerService = new PlayerService();
            playerService.saveAllPlayers(everyPlayer);




        for(Player player: players){
            player.setClan(SeleniumGet.getClan(player.getUrl(), xpath));
        }

        for(Player player: players){
            System.out.println(player.getName() + " " + player.getClan() + " " + player.getUrl() + " " + player.getPlayerid());
        }*/



        /*
        for(Map.Entry<String, String> entry : gordionChars.entrySet()){
            urlGordionChars = SeleniumGet.generateLinks(entry.getKey(), entry.getValue());
            System.out.println(urlGordionChars);
        }

    }

}
        */
    }
}