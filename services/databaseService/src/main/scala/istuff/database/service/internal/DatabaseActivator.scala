package istuff.database.service.internal

import org.osgi.framework._
import com.weiglewilczek.scalamodules._
import com.mongodb.MongoClient
import istuff.database.service.impl.DatabaseImpl

/**
 * Created with IntelliJ IDEA.
 * User: bursant
 * Date: 4/19/13
 * Time: 11:24 AM
 * To change this template use File | Settings | File Templates.
 */
class DatabaseActivator extends BundleActivator{
  var serviceRegistration: ServiceRegistration = _

  def start(context: BundleContext) {
    val bundle = new DatabaseImpl()
    serviceRegistration = context.createService(bundle)
    //bundle.setData("strings", "thanking", "thank you so much!")
    bundle.getData("strings", "name", "greeting")
    bundle.getData("strings", "name", "thanking")
  }

  def stop(context: BundleContext) {
  }
}
