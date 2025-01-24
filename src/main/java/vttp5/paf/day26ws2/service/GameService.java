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
        
        // return jsonGamesArrayBuilder.build();

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
}
