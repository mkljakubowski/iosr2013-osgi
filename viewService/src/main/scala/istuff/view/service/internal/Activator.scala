package istuff.view.service.internal

import org.osgi.framework._
import com.weiglewilczek.scalamodules._
import istuff.view.service.impl.IndexViewResource
import istuff.widget.service.api.WidgetService

class Activator extends BundleActivator {
  var serviceRegistration: ServiceRegistration = _

  def start(context: BundleContext) {
    val widgetServiceRef:((WidgetService => Any) => Option[Any]) = context findService withInterface[WidgetService] andApply _
    val bundle = new IndexViewResource(widgetServiceRef)
    serviceRegistration = context.createService(bundle)
  }

  def stop(context: BundleContext) {
  }
}