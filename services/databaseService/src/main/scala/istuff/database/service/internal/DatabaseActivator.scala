package istuff.database.service.internal

import org.osgi.framework._
import com.weiglewilczek.scalamodules._
import com.mongodb.MongoClient
import istuff.database.service.impl.DatabaseImpl

class DatabaseActivator extends BundleActivator {
  var serviceRegistration: ServiceRegistration = _

  def start(context: BundleContext) {
    val dbBundle = new DatabaseImpl()
    serviceRegistration = context.createService(dbBundle)
//    dbBundle.setData("strings", "thanking", "thank you so much!")
//    dbBundle.getData("strings", "name", "greeting")
//    dbBundle.getData("strings", "name", "thanking")
  }

  def stop(context: BundleContext) {
  }
}
