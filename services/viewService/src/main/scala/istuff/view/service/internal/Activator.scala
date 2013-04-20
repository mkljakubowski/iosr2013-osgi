package istuff.view.service.internal

import org.osgi.framework._
import com.weiglewilczek.scalamodules._
import istuff.view.service.impl.{HTMLGenerateServlet, IndexViewResource}
import istuff.widget.service.api.WidgetService
import org.osgi.service.http.HttpService

import org.amdatu.template.processor.{TemplateProcessor, TemplateContext, TemplateEngine}
import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}
import java.io.File
import istuff.api.util.Loggable

class Activator extends BundleActivator with Loggable {
  var serviceRegistration: ServiceRegistration = _
  var httpService : ServiceFinder[HttpService] = null

  def start(context: BundleContext) {
//    val widgetServiceRef:((WidgetService => Any) => Option[Any]) = context findService withInterface[WidgetService] andApply _
//    val bundle = new IndexViewResource(widgetServiceRef)
//    serviceRegistration = context.createService(bundle)
//
//    httpService = context findService withInterface[HttpService]
//    httpService andApply { _.registerServlet("/hell",new HTMLGenerateServlet(context),null,null) } match {
//      case None => logger error("HTMLGenerator failed to register")
//      case _ => logger info("HTMLGenerator registered")
//    }
//
//    httpService andApply { _.registerResources("/","/",null) } match {
//      case None => logger error("resources failed to register")
//      case _ => logger info("resources registered")
//    }

  }

  def stop(context: BundleContext) {
//    httpService andUnget
  }
}