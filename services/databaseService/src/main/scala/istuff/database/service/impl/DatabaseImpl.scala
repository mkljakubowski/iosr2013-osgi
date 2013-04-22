package istuff.database.service.impl

import com.mongodb._
import istuff.database.service.api._
import istuff.api.util.Loggable

class DatabaseImpl() extends Database with Loggable {

  val databaseAddress = "ds057847.mongolab.com"
  val databasePort = 57847
  val databaseName = "iosr-osgi-test"
  val user = "stefan"
  val password = "ala123".toArray

  val mongoClient = new MongoClient(databaseAddress, databasePort)
  val database = mongoClient.getDB(databaseName)

  database.authenticate(user, password) match {
    case true => logger info ("Database " + databaseName + " connected.")
    case false => logger info ("Database " + databaseName + " connection failure.")
  }

  def collections = database.getCollectionNames().toArray()

  def getData(collectionName: String, propertyName: String, propertyValue: String): Option[String] = database.synchronized {
    val collection = database.getCollection(collectionName)
    val query = new BasicDBObject(propertyName, propertyValue)
    val coursor = collection.find(query)

    val result = coursor.hasNext match {
      case false => None
      case true => Option(coursor.next().toString())
    }
    coursor.close()

    result
  }

  def setData(collectionName: String, name: String, content: String) = database.synchronized {
    val collection = database.getCollection(collectionName)
    val document = new BasicDBObject("name", name).append("content", content)
    collection.insert(document)
  }

}
