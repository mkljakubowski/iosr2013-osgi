package istuff.api

import org.osgi.framework._
import util.ResourceApiImpl
import com.weiglewilczek.scalamodules._

class Activator extends BundleActivator {

  var serviceRegistration:ServiceRegistration = _

  def start(context: BundleContext) {
    val bundle = new ResourceApiImpl(context)
    serviceRegistration = context.createService(bundle)

  }

  def stop(context: BundleContext) {
  }
}