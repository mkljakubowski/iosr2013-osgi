package istuff.widget.service.api

import istuff.api.models.widget.WidgetDescriptor

trait WidgetService {
  def registerWidget(wdsc : WidgetDescriptor)
  def getAvailableWidgets() : Seq[String]
}
