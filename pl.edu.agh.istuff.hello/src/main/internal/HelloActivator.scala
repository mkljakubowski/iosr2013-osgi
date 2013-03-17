package internal

import org.osgi.framework._
import com.weiglewilczek.scalamodules._
import api.HelloService

class HelloActivator extends BundleActivator {
  var serviceRegistration:ServiceRegistration = _

  def start(context: BundleContext) {
    println("STARTING")
    val polite = new HelloService {
      def sayHello: String = "I greet you good sir!"
    }

    serviceRegistration = context createService polite

    println("REGISTERED")
  }

  def stop(context: BundleContext) {
    println("STOPPED")
    println("UNREGISTERED")
  }
}