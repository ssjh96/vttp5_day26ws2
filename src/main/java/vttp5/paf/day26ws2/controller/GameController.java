package vttp5.paf.day26ws2.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import vttp5.paf.day26ws2.service.GameService;


@Controller
public class GameController {

    @Autowired
    GameService gameService;
    
    @GetMapping(path = "/games", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getGames(
            @RequestParam (value = "limit", defaultValue = "25") String limit,
            @RequestParam (value = "offset", defaultValue = "0") String offset)
    {
        JsonObject jResults = gameService.getGames(limit, offset);

        return ResponseEntity.ok(jResults.toString());
    }

    @GetMapping(path = "/games/ranks", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getGamesByRank(
        @RequestParam (value = "limit", defaultValue = "25") String limit,
        @RequestParam (value = "offset", defaultValue = "0") String offset) 
    {
        JsonObject jResults = gameService.getGamesByRanking(limit, offset);

        return ResponseEntity.ok(jResults.toString());
    }

    @GetMapping(path = "/games/{gameId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getMethodName(@PathVariable ("gameId") String gameId) 
    {
        JsonObject jResult = gameService.getGameById(gameId);

        return ResponseEntity.ok(jResult.toString());
    }
    
    
}
