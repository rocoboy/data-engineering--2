package com.example;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClientException;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.InsertManyResult;
import com.mongodb.client.result.InsertOneResult;
import com.mongodb.client.result.UpdateResult;


public class TestMongo {

	public static void main(String[] args) {
		String url = "mongodb://127.0.01:27017";
		try{
			MongoClient mongoClient = MongoClients.create(url);
			MongoDatabase db = mongoClient.getDatabase("labs");
			
			//Creamos una coleccion si no existe,
			MongoCollection<Document> collection = db.getCollection("personas");
			
            //Insertamos un documento
			Document data = new Document().append("name", "Juan").append("age", 22);
            InsertOneResult insertOneResult = collection.insertOne(data);
            System.out.println("\n--- insert one result ---");
            System.out.println(insertOneResult.getInsertedId());
  
            //Insertamos otro
            data = new Document().append("name", "Pedro").append("lastname", "Almodovar").append("age", 68);
            insertOneResult = collection.insertOne(data);
            System.out.println("\n--- insert one result ---");
            System.out.println(insertOneResult.getInsertedId());
           
            //Insertar varios al mismo tiempo
            List<Document> severalData = new ArrayList<Document>();
            severalData.add(new Document().append("name", "Elena").append("age", 33));
            severalData.add(new Document().append("name", "Ana").append("lastname", "Bolena").append("age", 35));
            InsertManyResult insertManyResult = collection.insertMany(severalData);
            System.out.println("\n--- insert many result ---");
            System.out.println(insertManyResult.getInsertedIds());
			
            // Consultar con un filtro
            Bson filter = Filters.eq("name", "Elena");
            FindIterable<Document> elementsFound = collection.find(filter);
            System.out.println("\n--- find(filter) result ---");
            for (Document document : elementsFound)
            	System.out.println(document);
            

            // Consultar toda la colección
            FindIterable<Document> allCollection = collection.find();
            System.out.println("\n--- find() result ---");
            for (Document document : allCollection)
            	System.out.println(document);
            
            		
            // Modificar un elemento de la colección
			filter = Filters.eq("name", "Ana");
            Bson newAge = Updates.set("age", 25);
            UpdateResult updateOne = collection.updateOne(filter, newAge);
            elementsFound = collection.find(filter);
            System.out.println("\n--- updateOne() result ---");
            System.out.println(updateOne.getMatchedCount());
            for (Document document : elementsFound)
            	System.out.println(document);
           
         
            // Borrar un elemento
			filter = Filters.eq("name", "Pedro");
            DeleteResult deleteResult = collection.deleteMany(filter);
            System.out.println("\n--- Number of deleted elements with filter ---");
            System.out.println(deleteResult.getDeletedCount());
			           			/*
			DeleteResult deleteResult = collection.deleteMany(Filters.empty());
            System.out.println("\n--- Number of deleted elements with empty filter ---");
            System.out.println(deleteResult.getDeletedCount());
           */
            // Borrar todo
			
			
		}
		catch (MongoClientException e) {
			System.out.println(e.getMessage());
		}
	}
}


