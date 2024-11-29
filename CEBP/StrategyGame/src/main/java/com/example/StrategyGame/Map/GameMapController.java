package com.example.StrategyGame.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/gameMap")
public class GameMapController {
    @PostMapping("/update-terrain")
    public ResponseEntity<String> updateTerrain(@RequestBody int[][] terrain) {
        System.out.println("Received terrain:");
        for (int[] row : terrain) {
            System.out.println(java.util.Arrays.toString(row));
        }

        return ResponseEntity.ok("Terrain updated successfully!");
    }
}
