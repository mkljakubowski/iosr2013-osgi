package istuff.widget.service.impl

import _root_.istuff.widget.service.api.WidgetService
import istuff.api.models.widget._
import istuff.api.util.Loggable
import org.osgi.framework.BundleContext
import com.weiglewilczek.scalamodules._
import istuff.api.models.widget.WidgetDescriptor
import org.osgi.service.http.HttpService

class WidgetServiceImpl extends WidgetService with Loggable {
  var registredWidgets = List.empty[(WidgetDescriptor, ServiceFinder[HttpService])]

  def registerWidget(widget : WidgetDescriptor, context: BundleContext) = registredWidgets.synchronized {
    val httpService = context findService withInterface[HttpService]

    widget.servlets.foreach{ servlet =>
      httpService andApply { _.registerServlet("/servlets/" + widget.name + "/" + servlet._1, servlet._2, null, null) } match {
        case None => logger error ("Servlet failed to register " + widget.name + " at " + "/servlets/" + widget.name + "/" + servlet._1 )
        case _ => logger info ("Servlet registered for gadget " + widget.name + " at " + "/servlets/" + widget.name + "/" + servlet._1 )
      }
    }

    widget.resources.foreach{ resource =>
      val path = resource match {
        case "" => "/"+widget.name
        case _ => "/"+widget.name+"/"+resource
      }
      httpService andApply { _.registerResources(path, "/"+resource, null) } match {
        case None => logger error("resources failed to register for " + widget.name + " at " + "/" + widget.name + "/" + resource)
        case _ => logger info("resources registered for " + widget.name + " at " + "/" + widget.name + "/" + resource)
      }
    }

    registredWidgets = (widget, httpService) :: registredWidgets
  }

  def unRegisterWidget(widget : WidgetDescriptor) = {
    registredWidgets.filter(_._1 == widget).headOption match {
      case Some(tp) => tp._2 andUnget
      case _ =>
    }
  }

  def getAvailableWidgets(): Seq[WidgetDescriptor] = registredWidgets.map(_._1)

}
