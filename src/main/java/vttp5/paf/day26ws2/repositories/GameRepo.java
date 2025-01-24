package vttp5.paf.day26ws2.repositories;

import java.util.List;

import org.bson.Document;
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

        // Set projections
        query.fields()
            .include(F_GID)
            .include(F_NAME)
            .exclude(F_OID);
        
        // Add sorting on "gid" in ascending order
        query.with(Sort.by(Sort.Direction.ASC, F_GID));
        query.limit(limit);
        query.skip(offset);

        return template.find(query, Document.class, C_GAMES);
     }


    /*
        db.getCollection("games")
            .find()
            .count()
     */

     public Long gamesCount()
     {
        Query query = new Query();

        return template.count(query, C_GAMES);
        // return template.count(query, Long.class);

     }
    
}
