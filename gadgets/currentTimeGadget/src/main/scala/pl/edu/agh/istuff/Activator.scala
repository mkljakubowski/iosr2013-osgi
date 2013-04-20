package pl.edu.agh.istuff

import org.osgi.framework.{BundleContext, BundleActivator}
import com.weiglewilczek.scalamodules._
import org.osgi.service.http.HttpService
import istuff.api.util.{Loggable, ResourceApi}

class Activator extends BundleActivator with Loggable {
  var resourceApi : ServiceFinder[ResourceApi] = null
  def start(context: BundleContext) {
    resourceApi = context findService withInterface[ResourceApi]
    resourceApi andApply { _.registerServlet(new CurrentTimeServlet(context),"time") } match {
      case None => logger error ("Time servlet failed to register")
      case _ => logger info ("Time servlet registred")
    }
  }

  def stop(context: BundleContext) {
     resourceApi andUnget
  }
}
