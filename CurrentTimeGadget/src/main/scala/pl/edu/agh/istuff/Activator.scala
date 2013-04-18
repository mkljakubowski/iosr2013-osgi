package pl.edu.agh.istuff

import org.osgi.framework.{BundleContext, BundleActivator}
import com.weiglewilczek.scalamodules._
import org.osgi.service.http.HttpService
import istuff.api.util.ResourceApi

class Activator extends BundleActivator {
  var resourceApi : ServiceFinder[ResourceApi] = null
  def start(context: BundleContext) {
    resourceApi = context findService withInterface[ResourceApi]
    resourceApi andApply { _.registerServlet(new CurrentTimeServlet(context),"time") } match {
      case None => println("res fail")
      case _ => println("res success")
    }
  }

  def stop(context: BundleContext) {
     resourceApi andUnget
  }
}
