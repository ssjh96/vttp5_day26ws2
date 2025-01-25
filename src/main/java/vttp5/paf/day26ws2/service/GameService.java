package vttp5.paf.day26ws2.service;

import java.io.StringReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.bson.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonArrayBuilder;
import jakarta.json.JsonObject;
import jakarta.json.JsonObjectBuilder;
import jakarta.json.JsonReader;
import vttp5.paf.day26ws2.repositories.GameRepo;

@Service
public class GameService {
    
    @Autowired
    private GameRepo gameRepo;

    public JsonObject getGames(String limit, String offset)
    {
        // Games array
        JsonArrayBuilder jsonGamesArrayBuilder = Json.createArrayBuilder();

        List<Document> docListOfGames = gameRepo.findGames(Integer.parseInt(limit), Integer.parseInt(offset));

        for (Document docGames : docListOfGames)
        {
            JsonReader jReader = Json.createReader(new StringReader(docGames.toJson()));
            JsonObject jsonGame = jReader.readObject();

            jsonGamesArrayBuilder.add(jsonGame);
        }
        
        JsonArray jGamesArray = jsonGamesArrayBuilder.build();

        // Total count
        long totalGames = gameRepo.gamesCount();

        // Current timestamp
        // Get current date and time
        LocalDateTime now = LocalDateTime.now();

        // Define the format 
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format current date and time
        String formattedNow = now.format(formatter);

        // Build result jsonObject
        JsonObject jResultObj =  Json.createObjectBuilder()
                                    .add("games", jGamesArray)
                                    .add("offset", offset)
                                    .add("limit", limit)
                                    .add("total", totalGames)
                                    .add("timestamp", formattedNow)
                                    .build();
        
        return jResultObj;
    }


    public JsonObject getGamesByRanking(String limit, String offset)
    {
        // Games array
        JsonArrayBuilder jsonGamesArrayBuilder = Json.createArrayBuilder();

        List<Document> docListOfGames = gameRepo.findGamesByRanking(Integer.parseInt(limit), Integer.parseInt(offset));

        for (Document docGames : docListOfGames)
        {
            JsonReader jReader = Json.createReader(new StringReader(docGames.toJson()));
            JsonObject jsonGame = jReader.readObject();

            jsonGamesArrayBuilder.add(jsonGame);
        }

        JsonArray jGamesArray = jsonGamesArrayBuilder.build();

        // Total count
        long totalGames = gameRepo.gamesCount();

        // Current timestamp
        // "timestamp", java.time.Instant.now().toString()
        // "timestamp", Timestamp.valueOf(LocalDateTime.now()).toString()

        // Get current date and time
        LocalDateTime now = LocalDateTime.now();

        // Define the format 
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        // Format current date and time
        String formattedNow = now.format(formatter);

        // Build result jsonObject
        JsonObject jResultObj =  Json.createObjectBuilder()
                                    .add("games", jGamesArray)
                                    .add("offset", offset)
                                    .add("limit", limit)
                                    .add("total", totalGames)
                                    .add("timestamp", formattedNow)
                                    .build();
    
        return jResultObj;
    }

    public JsonObject getGameById (String gameId)
    {
        // Query the repo for game by its ID
        Optional<Document> optGameDoc = gameRepo.findGameById(gameId);

        JsonObject jResult;

        // Handle non-existent game ID
        if(optGameDoc.isEmpty())
        {
            jResult = Json.createObjectBuilder()
                .add("Error", "GID: '" + gameId + "', does not exist in DB.")
                .build();

            return jResult;
        }
        
        // Get game document
        Document gameDoc = optGameDoc.get();
        
        jResult = Json.createObjectBuilder()
            .add("game_id", gameDoc.getObjectId("_id").toString())
            .add("name", gameDoc.getString("name"))
            .add("year", gameDoc.getInteger("year"))
            .add("ranking", gameDoc.getInteger("ranking"))
            .add("average", 0.0) // double placeholder for average
            .add("users_rated", gameDoc.getInteger("users_rated"))
            .add("url", gameDoc.getString("url"))
            .add("thumbnail", gameDoc.getString("image"))
            .add("timestamp", Timestamp.valueOf(LocalDateTime.now()).toString())
            .build();

        return jResult;
        // String gameDocString = gameDoc.toJson();

    }
    
}
