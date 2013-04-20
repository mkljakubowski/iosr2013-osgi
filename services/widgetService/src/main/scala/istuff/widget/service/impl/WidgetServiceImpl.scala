package istuff.widget.service.impl

import _root_.istuff.widget.service.api.WidgetService
import istuff.api.models.widget._

class WidgetServiceImpl extends WidgetService {
  def registerWidget(wdsc : WidgetDescriptor) {}

  def getAvailableWidgets(): Seq[String] = List("A", "B")
}
