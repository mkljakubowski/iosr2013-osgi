package istuff.api.internal

import org.osgi.framework._
import com.weiglewilczek.scalamodules._
import istuff.api.util.ResourceApiImpl

class Activator extends BundleActivator {

  var serviceRegistration:ServiceRegistration = _

  def start(context: BundleContext) {
    val bundle = new ResourceApiImpl(context)
    serviceRegistration = context.createService(bundle)

  }

  def stop(context: BundleContext) {
  }
}