package org.example.margoclandataget.Service;

import org.example.margoclandataget.Entity.Equipment;
import org.example.margoclandataget.Entity.ItemMapper;
import org.example.margoclandataget.Repository.EquipmentRepo;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.*;

@Service
public class EquipmentService {

    @Autowired
    private EquipmentRepo equipmentRepo;

    public String createEqUrl(String world, String playerid) {
        return "https://mec.garmory-cdn.cloud/pl/" + world + "/" + (Integer.parseInt(playerid) % 128) + "/" + playerid + ".json";
    }

    public List<Equipment> getAllItemsWithLatestInsertDate() {
        return equipmentRepo.findAllWithLatestInsertDate();
    }
    public List<Equipment> createAndSaveEquipment(String urlString, String name) {
        String playerid = urlString.substring(urlString.lastIndexOf('/') + 1, urlString.indexOf('.', urlString.lastIndexOf('/')));
        List<Equipment> savedEquipmentList = new ArrayList<>();
        try {
            // Send GET request
            RestTemplate restTemplate = new RestTemplate();
            String response = restTemplate.getForObject(urlString, String.class);

            if (response == null) {
                throw new RuntimeException("Failed to fetch data from URL");
            }

            // Parse JSON
            JSONObject jsonObject = new JSONObject(response);

            // Iterate over JSON keys and create Equipment objects
            for (String key : jsonObject.keySet()) {
                JSONObject item = jsonObject.getJSONObject(key);

                // Get values
                int cl = item.optInt("cl", -1); // Default to -1 if not found
                String itemname = item.optString("name");
                String statStr = item.optString("stat", "");

                // Parse stat string
                List<String> parsedStats = parseStat(statStr);
                if (parsedStats == null || parsedStats.size() < 2) {
                    throw new RuntimeException("Failed to parse stat string");
                }

                String upgradelvl = parsedStats.get(0);
                String rzadkosc = parsedStats.get(1);

                // Map cl to typ using ItemClassMapper
                String typ = ItemMapper.getClassName(cl);
                if (typ == null) {
                    throw new RuntimeException("Invalid item class: " + cl);
                }

                // Create Equipment object
                Equipment equipment = new Equipment();
                equipment.setCharid(playerid); // or another relevant value
                equipment.setTyp(typ);
                equipment.setPlayername(name);
                equipment.setNazwaprzedmiotu(itemname);
                equipment.setUlepszenie(upgradelvl);
                equipment.setRzadkosc(rzadkosc);

                // Save to database and add to list
                savedEquipmentList.add(equipmentRepo.save(equipment));
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error processing equipment data");
        }

        return savedEquipmentList;
    }

    private List<String> parseStat(String statStr) {
        List<String> listadanych = new ArrayList<>();
        if (statStr == null || statStr.isEmpty()) {
            return null;
        }

        // Split the string into key-value pairs
        String[] pairs = statStr.split(";");
        Map<String, String> statMap = new HashMap<>();

        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                statMap.put(keyValue[0], keyValue[1]);
            }
        }

        // Get the enhancement_upgrade_lvl as a String
        String upgradeLvl = statMap.getOrDefault("enhancement_upgrade_lvl", "0");
        String rzadkosc = statMap.get("rarity");
        listadanych.add(upgradeLvl);
        listadanych.add(rzadkosc);
        return listadanych;
    }


}
