package istuff.database.service.impl

import com.mongodb._
import istuff.database.service.api._

/**
 * Created with IntelliJ IDEA.
 * User: bursant
 * Date: 4/19/13
 * Time: 11:23 AM
 * To change this template use File | Settings | File Templates.
 */
class DatabaseImpl extends DatabaseApi {

  var databaseAddress = "ds057847.mongolab.com"
  var databasePort = 57847
  var databaseName = "iosr-osgi-test"
  var user = "stefan"
  var password = Array('a', 'l', 'a', '1', '2', '3')

  var mongoClient = new MongoClient(databaseAddress, databasePort)
  var database = mongoClient.getDB(databaseName)

  var authenticationResult = database.authenticate(user, password)

  if(authenticationResult)
    println("Database " + databaseName + " connected.")
  else
    println("Database " + databaseName + " connection failure.")

  var collections = database.getCollectionNames().toArray();
  collections.foreach(println)

  def getData(collectionName: String, propertyName: String, propertyValue: String): String = {
    var collection = database.getCollection(collectionName)
    var query = new BasicDBObject(propertyName, propertyValue)
    var coursor = collection.find(query)
    var result = ""

    coursor.hasNext match {
      case false => result = ("Document not found in Mongo database.")
      case true => result = coursor.next().toString()
    }

    coursor.close()

    println("this is getData for: " + propertyName + ", " + propertyValue + ": " + result)
    result
  }

  def setData(collectionName: String, name: String, content: String) {
    var collection = database.getCollection(collectionName)
    var document = new BasicDBObject("name", name).append("content", content)
    collection.insert(document)
  }
}
