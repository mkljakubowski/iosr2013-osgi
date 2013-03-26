package istuff.widget.service.internal

import _root_.istuff.widget.service.impl.WidgetServiceImpl
import org.osgi.framework._
import com.weiglewilczek.scalamodules._

class Activator extends BundleActivator {
  var serviceRegistration:ServiceRegistration = _

  def start(context: BundleContext) {
    val bundle = new WidgetServiceImpl
    serviceRegistration = context.createService(bundle)

  }

  def stop(context: BundleContext) {
  }
}