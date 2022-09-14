package org.example;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;

import java.util.Arrays;


/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ) {
        MongoClient client = new MongoClient("localhost",27017);
        client.getDatabase("Info");
        MongoDatabase infoDB = client.getDatabase("Info");
        infoDB.createCollection("Personal");
        MongoCollection<Document> personalCollection = infoDB.getCollection("Personal");
        BasicDBObject data = new BasicDBObject().append("name", "Stephen Hawking").append("year", 1955).append("country", "England");
        BasicDBObject data2 = new BasicDBObject().append("name", "Isaac Newton").append("year", 1955).append("country", "England");
        //personalCollection.insertOne(Document.parse(data.toJson()));
        Document parse = Document.parse(data.toJson());
        Document parse2 = Document.parse(data2.toJson());
        personalCollection.insertMany(Arrays.asList(parse,parse2));

        //CRUD: Create, Read, Update, Delete

        //Reading data from mongodb database
        FindIterable<Document> documents = personalCollection.find();
        for(Document doc: documents) { //iterating over documents
            System.out.println(doc.toJson()); //converting each document to json and pprinting the jsons.
        }

        FindIterable<Document>records = personalCollection.find(new BasicDBObject("name", "Stephen Hawking"));
        for(Document document: records) {
            System.out.println(document.toJson());
        }

        //Update
        Bson filter = Filters.eq("name","Stephen Hawking");
        Bson update = Updates.set("year",1966); //Setting the year field of Stephen Hawking to 1966.

        personalCollection.updateOne(filter,update);

        BasicDBObject obj = new BasicDBObject().append("name","Elon Musk").append("date",1979).append("country","USA").append("job","entrepreneur");
        Document d = Document.parse(obj.toJson());
        personalCollection.insertOne(d);


        //Update
        Bson myFilter = Filters.exists("job");
        Bson upd = Updates.set("child","Mike Must");
        personalCollection.updateOne(myFilter,upd);

        Bson deleteFilter = Filters.eq("name","Elon Musk");
        personalCollection.deleteOne(deleteFilter);

        Bson multipleDeletes = Filters.eq("country","England");
        personalCollection.deleteMany(multipleDeletes);


        personalCollection.insertOne(d);

        //infoDB.drop();

    }
}
