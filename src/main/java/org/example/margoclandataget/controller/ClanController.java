package org.example.margoclandataget.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.example.margoclandataget.Entity.Equipment;
import org.example.margoclandataget.Service.EquipmentService;
import org.openqa.selenium.json.Json;
import org.springframework.transaction.annotation.Transactional;
import org.checkerframework.checker.units.qual.A;
import org.example.margoclandataget.Entity.ClanEntity;
import org.example.margoclandataget.Entity.Player;
import org.example.margoclandataget.Repository.ClanRepo;
import org.example.margoclandataget.Repository.PlayerRepo;
import org.example.margoclandataget.Service.ClanService;
import org.example.margoclandataget.Service.PlayerService;
import org.hibernate.NonUniqueResultException;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.Connection.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Connection;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.*;
import org.example.margoclandataget.UsefullMethods;
import reactor.core.publisher.Flux;

import javax.validation.Valid;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class ClanController {

    @Autowired
    private ClanService clanService;

    @Autowired
    private PlayerService playerService;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private ClanRepo clanRepository;

    @Autowired
    private PlayerRepo playerRepo;

    JSONObject response = new JSONObject();
    JSONArray jsonArray = new JSONArray();


    @GetMapping("/api/saveEquipment")
    public ResponseEntity<String> saveEquipment(@RequestParam String world, @RequestParam String playerid, @RequestParam String name) {
        String url = equipmentService.createEqUrl(world, playerid);
        try {
            List<Equipment> equipmentList = equipmentService.createAndSaveEquipment(url,name);
            if (!equipmentList.isEmpty()) {
                return ResponseEntity.ok("Equipment saved: " + equipmentList.size() + " items.");
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("No equipment saved.");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }

    @GetMapping("/api/jdbc/getplayers") //sluzy do pobierania wszystkich postaci z danego klanu - dziala
    public ResponseEntity<String> getPlayers(@RequestParam String clan){
        JSONArray jsonArray = new JSONArray(playerRepo.findPlayersByClan(clan.toUpperCase()));
        return new ResponseEntity<>(jsonArray.toString(), HttpStatus.OK);
    }
    @PostMapping("/api/addUrlPlayerFromClan")
    public ResponseEntity<String> addUrlPlayerFromClan(@Valid @RequestBody Map<String, String> request) {
        int max_retry = 3;
        String url = request.get("clanUrl");
        List<List<String>> playerUrls = new ArrayList<>();
        JSONObject response = new JSONObject();

        try {
            Connection.Response jsoupResponse = Jsoup.connect(url).execute();
            if (jsoupResponse.statusCode() != 200) {
                response.put("message", "Invalid URL provided: " + url);
                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Type", "application/json");
                return new ResponseEntity<>(response.toString(), headers, HttpStatus.BAD_REQUEST);
            } else {
                Document doc = jsoupResponse.parse();
                Elements rows = doc.select("tbody tr");
                JSONArray playerData = new JSONArray();

                for (Element elem : rows) {
                    Elements eachtd = elem.select("td");
                    String charurl = eachtd.get(1).select("a").attr("href");
                    charurl = "https://margonem.pl" + charurl;
                    int commaIndex = charurl.indexOf(',');
                    int hashIndex = charurl.indexOf('#');
                    int charIndex = charurl.indexOf("char_");
                    int commaIndexCharId = charurl.indexOf(',', charIndex);
                    String playerid = charurl.substring(commaIndex + 1, hashIndex);
                    String charid = charurl.substring(charIndex + 5, commaIndexCharId);
                    playerUrls.add(List.of(charid, playerid));
                    JSONObject playerDataObj = new JSONObject();
                    playerDataObj.put("charurl", charurl);
                    playerData.put(playerDataObj);
                }

                for (List<String> playerUrl : playerUrls) {
                    String localhosturl = "http://localhost:8080/api/addplayer?charid=" + playerUrl.get(0) + "&playerid=" + playerUrl.get(1);
                    System.out.println(localhosturl);

                    int attempt = 0;
                    boolean success = false;

                    while (attempt < max_retry) {
                        try {
                            Jsoup.connect(localhosturl).get();
                            System.out.println("Next account " + System.currentTimeMillis());
                            success = true;
                            break;
                        } catch (IOException e) {
                            System.out.println("IOException during connection attempt " + (attempt + 1) + ": " + e.getMessage());
                            attempt++;
                            if (attempt >= max_retry) {
                                System.out.println("Failed to send data after " + max_retry + " attempts.");
                            } else {
                                try {
                                    Thread.sleep(5000);
                                } catch (InterruptedException ie) {
                                    Thread.currentThread().interrupt();
                                    System.out.println("Thread was interrupted, Failed to complete operation");
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            System.out.println("Exception during connection attempt " + (attempt + 1) + ": " + e.getMessage());
                            e.printStackTrace();
                            break;
                        }
                    }

                    if (!success) {
                        response.put("message", "Failed to process player data for URL: " + localhosturl);
                        return new ResponseEntity<>(response.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
                    }

                    try {
                        Thread.sleep(1500); // Opóźnienie między zapytaniami
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                        System.out.println("Thread was interrupted, Failed to complete operation");
                        response.put("message", "Thread was interrupted");
                        return new ResponseEntity<>(response.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
                    }
                }

                return new ResponseEntity<>(playerData.toString(), HttpStatus.OK);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.put("message", "Error processing data");
            return new ResponseEntity<>(response.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/api/addplayer")
    @Transactional
    public ResponseEntity<String> getAllChars(@RequestParam String charid, @RequestParam String playerid) {
        int attempt = 0;
        int maxRetry = 3;
        Document doc = null;
        String url = clanService.createMargonemPlayerUrl(playerid, charid, "gordion");

        try {
                if (playerRepo.findPlayerByCharidAndPlayerid(charid, playerid) != null) {
                    System.out.println("Player already exists" + playerid + " " + charid);
                }
            try {
                doc = clanService.getDocumentFromMargonemUrl(maxRetry, url);
            } catch (HttpStatusException e) {
                return ResponseEntity.status(e.getStatusCode()).body("HTTP error fetching URL. Status: " + e.getStatusCode());
            } catch (IOException e) {
                return ResponseEntity.status(500).body("IOException: " + e.getMessage());
            } catch (InterruptedException e) {
                return ResponseEntity.status(500).body("Request interrupted: " + e.getMessage());
            } catch (Exception e) {
                return ResponseEntity.status(500).body(e.getMessage()); // General exception handling
            }

            if (doc == null) {
                return new ResponseEntity<>("/clanlist  Failed to fetch data after " + maxRetry + " attempts.", HttpStatus.INTERNAL_SERVER_ERROR);
            }

            String mainClanChar = playerService.getMainCharFromDoc(doc);

            Element characterListDiv = doc.select("div.character-list").first();
            Elements isBan = doc.select(".profile-header__status");

            if (characterListDiv != null) {
                Elements characterRows = characterListDiv.select("li.char-row");

                for (Element characterRow : characterRows) {
                    String world = characterRow.select("input.chworld").val();

                    if ("Gordion".equals(world)) {
                        String nick = characterRow.select("input.chnick").val();
                        Player existingPlayer = playerRepo.findPlayerByName(nick);
                        String id = characterRow.select("input.chid").val();
                        String lvl = characterRow.select("input.chlvl").val();
                        String profname = characterRow.select("input.chprofname").val();
                        String guild = characterRow.select("input.chguild").val();
                        String playerUrl = "https://margonem.pl/profile/view," + playerid + "#char_" + id + ",gordion";
                        String equrl = equipmentService.createEqUrl("gordion", charid);
                        System.out.println("EQURL JEST TAKIE " + equrl);
                        equipmentService.createAndSaveEquipment(equrl, nick);
                        if (existingPlayer == null) {
                            Player player = new Player();
                            player.setPlayerDetails(nick, mainClanChar, lvl, guild.replace(" (założyciel)", "").toUpperCase(),
                                                    profname, playerUrl, playerid, isBan.text(), id);
                            playerRepo.save(player);
                        }
                        else{
                            existingPlayer.setPlayerDetails(nick, mainClanChar, lvl, guild.replace(" (założyciel)", "").toUpperCase(),
                                    profname, playerUrl, playerid, isBan.text(), id);
                            playerRepo.save(existingPlayer);
                        }
                    }
                }
            } else {
                System.out.println("Character list div not found.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
            //return " /clanlist  Error processing data.";
        }
        return null;
        //return " /clanlist  Data processed successfully.";
    }

    @GetMapping("/xd")
    public void printlist(){
        List<Object[]> results = playerRepo.findAllCharidAndName();
        for (Object[] row : results) {
            String charid = (String) row[0];
            String name = (String) row[1];
            System.out.println("charid: " + charid + ", name: " + name);
            String equrl = equipmentService.createEqUrl("gordion", charid);
            System.out.println("EQURL" + equrl);
            equipmentService.createAndSaveEquipment(equrl, name);
        }
    }
    @GetMapping("/api/listPlayersFromClan")
    public Flux<ServerSentEvent<String>> listPlayersFromClan(@RequestParam String url) {
        try {
            Document doc = Jsoup.connect(url).get();

            String title = doc.title().trim();
            String clanName = title.contains(" -") ? title.substring(0, title.indexOf(" -")) : title;

            Elements rows = doc.select("tbody tr");

            return Flux.fromIterable(rows)
                    .map(row -> {
                        Elements eachtd = row.select("td");

                        String id = eachtd.get(0).text();
                        String name = eachtd.get(1).select("a").text();
                        String charurl = eachtd.get(1).select("a").attr("href");
                        String level = eachtd.get(2).text();
                        String klasa = eachtd.get(3).text();
                        String ph = eachtd.get(4).text();

                        JSONObject playerData = new JSONObject();
                        playerData.put("id", id);
                        playerData.put("name", name);
                        playerData.put("charurl", charurl);
                        playerData.put("level", level);
                        playerData.put("klasa", klasa);
                        playerData.put("ph", ph);
                        playerData.put("clanname", clanName);


                        return ServerSentEvent.<String>builder()
                                .data(playerData.toString())
                                .build();
                    })
                    .delayElements(Duration.ofMillis(1000)); // Simulate streaming

        } catch (IOException e) {
            e.printStackTrace();
            return Flux.just(ServerSentEvent.<String>builder()
                    .data("error: Failed to fetch data from the provided URL.")
                    .event("error")
                    .build());
        }
    }

    @DeleteMapping("/api/deletePlayer")
    @Transactional
    public void deletePlayer(@RequestParam String url) {
        String playerid = clanService.getPlayerId(url);
        playerRepo.deletePlayerByPlayerid(playerid);
    }

    @PostMapping("/api/getPlayersFromClan")
    public ResponseEntity<String> getPlayersFromClan(@Valid @RequestBody Player player) throws InterruptedException {

        String url = player.getUrl();
        List<Player> playersFromClan = playerRepo.findByUrl(url);

        for (Player p : playersFromClan) {
            response.put("name", p.getName());
            jsonArray.put(response);
            Thread.sleep(2000);
        }

        // Constructing the response JSONObject
        response.put("players", jsonArray);

        return new ResponseEntity<>(response.toString(), HttpStatus.OK);
    }

    @PostMapping("/api/clanlistlong")
    public ResponseEntity<String> postfew(@Valid @RequestBody List<ClanEntity> clans, HttpServletRequest request){
        for(ClanEntity clanEntity: clans){
            clanRepository.save(clanEntity);
            String clientIpAddress = UsefullMethods.getClientIpAddress(request);
            System.out.println("Client IP Address: " + clientIpAddress);
            System.out.println("Clan name: " + clanEntity.getClanName());
        }
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");

        JSONObject response = new JSONObject();
        response.put("message", "Clan successfully added");

        System.out.println(response.toString());
        return new ResponseEntity<>(response.toString(),headers, HttpStatus.OK);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/api/clanlist")
    public ResponseEntity<String> post(@Valid @RequestBody ClanEntity clanEntity, HttpServletRequest request){
        if(clanRepository.findByClanName(clanEntity.getClanUrl()) != null){
            try{
                Connection.Response jsoupResponse = Jsoup.connect(clanEntity.getClanUrl()).execute();
                if (jsoupResponse.statusCode() != 200) {
                    response.put("message", "Invalid URL provided: " + clanEntity.getClanUrl());
                    HttpHeaders headers = new HttpHeaders();
                    headers.add("Content-Type", "application/json");
                    return new ResponseEntity<>(response.toString(), headers, HttpStatus.BAD_REQUEST);
                }
            } catch (Exception e) {
                response.put("message", "Unable to connect to URL: " + clanEntity.getClanUrl());
                HttpHeaders headers = new HttpHeaders();
                headers.add("Content-Type", "application/json");
                return new ResponseEntity<>(response.toString(), headers, HttpStatus.BAD_REQUEST);
            }

            response.put("message", "Clan - " + clanEntity.getClanName() + " - already exists");
            return new ResponseEntity<>(response.toString(), HttpStatus.BAD_REQUEST);
        }else {
            clanRepository.save(clanEntity);
            String clientIpAddress = UsefullMethods.getClientIpAddress(request);
            System.out.println("Client IP Address: " + clientIpAddress);
            System.out.println("Clan name: " + clanEntity.getClanName());

            JSONObject response = new JSONObject();
            response.put("message", "Clan - " + clanEntity.getClanName() + " - successfully added");

            System.out.println(response.toString());
            return new ResponseEntity<>(response.toString(), HttpStatus.OK);
        }
    }

    @GetMapping("/updateplayer")
    public String updatePlayer(@RequestParam String charid, @RequestParam String playerid){
        Player player = new Player();
        player = playerRepo.findPlayerByCharidAndPlayerid(charid, playerid);

        player.setMainClanPlayerName("test");
        playerRepo.save(player);
        return "Player updated";
    }

}
