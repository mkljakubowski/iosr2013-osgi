package istuff.view.service.internal

import org.osgi.framework._
import com.weiglewilczek.scalamodules._
import istuff.view.service.impl.IndexViewResource
import istuff.widget.service.api.WidgetService
import org.osgi.service.http.HttpService

class Activator extends BundleActivator {
  var serviceRegistration: ServiceRegistration = _
  var httpService : ServiceFinder[HttpService] = null

  def start(context: BundleContext) {
    val widgetServiceRef:((WidgetService => Any) => Option[Any]) = context findService withInterface[WidgetService] andApply _
    val bundle = new IndexViewResource(widgetServiceRef)
    serviceRegistration = context.createService(bundle)

    httpService = context findService withInterface[HttpService]
    httpService andApply { _.registerResources("/","/htmls",null) } match {
      case None => println("res fail")
      case _ => println("res success")
    }

  }

  def stop(context: BundleContext) {
    httpService andUnget
  }
}