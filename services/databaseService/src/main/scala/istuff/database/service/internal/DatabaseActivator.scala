package istuff.database.service.internal

import org.osgi.framework._
import com.weiglewilczek.scalamodules._
import com.mongodb.MongoClient
import istuff.database.service.impl.DatabaseImpl
import istuff.api.util.Loggable

class DatabaseActivator extends BundleActivator with Loggable{
  var serviceRegistration: ServiceRegistration = _
  var mongoClient : MongoClient = _

  def start(context: BundleContext) {
    val databaseAddress = "ds057847.mongolab.com"
    val databasePort = 57847
    val databaseName = "iosr-osgi-test"
    val user = "stefan"
    val password = "ala123".toArray

    mongoClient = new MongoClient(databaseAddress, databasePort)
    val database = mongoClient.getDB(databaseName)

    database.authenticate(user, password) match {
      case true => logger info ("Database " + databaseName + " connected.")
      case false => logger info ("Database " + databaseName + " connection failure.")
    }

    val dbBundle = new DatabaseImpl(database)
    serviceRegistration = context.createService(dbBundle)
  }

  def stop(context: BundleContext) {
    mongoClient.close()
  }
}
