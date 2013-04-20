package istuff.widget.service.api

import istuff.api.models.widget.WidgetDescriptor
import org.osgi.framework.BundleContext

trait WidgetService {
  def registerWidget(widget : WidgetDescriptor, context: BundleContext)
  def unRegisterWidget(widget : WidgetDescriptor)
  def getAvailableWidgets() : Seq[WidgetDescriptor]
}
