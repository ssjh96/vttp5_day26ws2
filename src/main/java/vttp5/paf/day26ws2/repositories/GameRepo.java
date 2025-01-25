package vttp5.paf.day26ws2.repositories;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.bson.Document;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public class GameRepo 
{
    @Autowired
    private MongoTemplate template;

    public static final String C_GAMES = "games";
    public static final String F_GID = "gid";
    public static final String F_NAME = "name";
    public static final String F_OID = "_id";
    public static final String F_RANKING = "ranking";

     /*
        db.getCollection("games")
            .find()
            .projection({
                "gid" : 1,
                "name" : 1,
                "_id" : 0 
            })
            .sort({"gid": 1})
            .limit(10)
            .skip(15)
     */

     public List<Document> findGames(Integer limit, Integer offset)
     {
        // Criteria criteria = Criteria.where(null);
        
        // Query query = new Query().with(Sort.by(Sort.Direction.ASC, "gid")); // empty query with no filtering criteria, but with sorting on "gid in asc"

        Query query = new Query(); // empty query with no filtering criteria, but with sorting on "gid in asc"

        // Query query = new Query().with(Sort.by(Sort.Direction.ASC, F_GID)).limit(limit).skip(offset);

        // Set projections
        query.fields()
            .include(F_GID)
            .include(F_NAME)
            .exclude(F_OID);
        
        // Add sorting on "gid" in ascending order
        query.with(Sort.by(Sort.Direction.ASC, F_GID));
        query.limit(limit);
        query.skip(offset);

        List<Document> games = template.find(query, Document.class, C_GAMES);

        return games;
     }


    /*
        db.getCollection("games")
            .find()
            .count()
     */

     public Long gamesCount()
     {
        // Query query = new Query();

        // return template.count(query, C_GAMES);
        
        return template.getCollection(C_GAMES).countDocuments();

     }

     /*
        db.getCollection("games")
            .find()
            .projection({
                "gid" : 1,
                "name" : 1,
                "_id" : 0 
            })
            .sort({"ranking": 1})
            .limit(10)
            .skip(15)
     */

     public List<Document> findGamesByRanking(Integer limit, Integer offset)
     {        
        Query query = new Query(); // empty query with no filtering criteria

        // Set projections
        query.fields()
            .include(F_GID)
            .include(F_NAME)
            .include(F_RANKING)
            .exclude(F_OID);
        
        // Add sorting on "gid" in ascending order
        query.with(Sort.by(Sort.Direction.ASC, F_RANKING));
        query.limit(limit);
        query.skip(offset);

        return template.find(query, Document.class, C_GAMES);
     }


    /*
        
     */
     public Optional<Document> findGameById(String gameId)
     {
        try
        {
            // Object Id takes in a String representation of a HexString because its human readabale and convenient
            ObjectId gameObjectId = new ObjectId(gameId);
            Criteria criteria = Criteria.where(F_OID).is(gameObjectId);

            Query query = Query.query(criteria);

            Document result = template.findOne(query, Document.class, C_GAMES);

            // ObjectId id = result.getObjectId("_id");

            return Optional.ofNullable(result);
        }
        catch (IllegalArgumentException e)
        {
            // In case of invalid hexstring, handle invalid ObjectId format
            System.out.println("Error: " + e.getMessage());
            return Optional.empty();
        }
        
     }

    // Formatting object id manually will not work because MongoDD java driver expects an ObjectId type not a formatted String
    //  private String formattedObjectId(String gameId)
    //  {
    //     String formattedObjectId = "'ObjectId(" + gameId + "')";
    //     return formattedObjectId;
    //  }
    
}
