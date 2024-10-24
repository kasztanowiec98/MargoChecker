package org.example.margoclandataget.controller;

import com.google.gson.JsonArray;
import org.example.margoclandataget.Repository.EquipmentRepo;
import org.example.margoclandataget.Service.EquipmentService;
import org.json.JSONArray;
import org.jsoup.Connection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EquipmentController {

    private static EquipmentRepo equipmentRepo;

    private static EquipmentService equipmentService;

    public EquipmentController(EquipmentRepo equipmentRepo, EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
        this.equipmentRepo = equipmentRepo;
    }

    @GetMapping("/api/geteq")
    public static ResponseEntity<String> getEquipment(@RequestParam String charid){
        JSONArray jsonArray = new JSONArray(equipmentService.getAllItemsWithLatestInsertDate());
        return new ResponseEntity<>(jsonArray.toString(), HttpStatus.OK);
    }


}
