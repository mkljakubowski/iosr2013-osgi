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

       /*
  def DatabaseImpl(){
    var collections = database.getCollectionNames().toArray();

    collections.foreach(println)
  }
*/

  def getData() {

  }

  def setData() {

  }
}
