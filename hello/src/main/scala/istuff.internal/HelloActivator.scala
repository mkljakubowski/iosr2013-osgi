package internal

import _root_.istuff.api.HelloService
import _root_.istuff.hello.rest.HelloResource
import org.osgi.framework._
import com.weiglewilczek.scalamodules._

class HelloActivator extends BundleActivator {
  var serviceRegistration:ServiceRegistration = _

  def start(context: BundleContext) {
    val bundle = new HelloResource
    serviceRegistration = context.createService(bundle)

  }

  def stop(context: BundleContext) {
  }
}