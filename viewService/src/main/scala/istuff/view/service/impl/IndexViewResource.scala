package istuff.view.service.impl

import javax.ws.rs._
import core.MediaType
import istuff.widget.service.api.WidgetService

@Path("")
class IndexViewResource(widgetServiceRef: (((WidgetService) => Any) => Option[Any])) {

  @GET @Produces(Array(MediaType.TEXT_HTML))
  def index : String = {
    "<html><head><title>iStuff</title></head><body>" +
    (widgetServiceRef { _.getAvailableWidgets } match {
        case None => "No widgets available"
        case Some(widgets:Seq[String]) => widgets.mkString
    }) +
    "</body></html>"
  }
}
