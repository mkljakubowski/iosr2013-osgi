package pl.edu.agh.istuff.time.internal

import org.osgi.framework.{BundleContext, BundleActivator}
import com.weiglewilczek.scalamodules._
import istuff.api.util.Loggable
import istuff.widget.service.api.WidgetService
import istuff.api.models.widget.WidgetDescriptor
import org.amdatu.template.processor.TemplateEngine
import istuff.database.service.api.Database
import pl.edu.agh.istuff.time.impl.CurrentTimeServlet

class Activator extends BundleActivator with Loggable {

  var widgetService : ServiceFinder[WidgetService] = _
  var templateEngine : ServiceFinder[TemplateEngine] = _
  var database : ServiceFinder[Database] = _
  var descriptor: WidgetDescriptor = _

  def start(context: BundleContext) {
    widgetService = context findService withInterface[WidgetService]
    templateEngine = context findService classOf[TemplateEngine]
    database = context findService classOf[Database]

    descriptor = new WidgetDescriptor("currentTime",1,"mikolaj",
      Map(("time", new CurrentTimeServlet(templateEngine,context.getBundle().getResource("index.html")))),
      List(""))

    widgetService andApply { _.registerWidget(descriptor, context) } match {
      case None => logger error "time widget failed to register"
      case _ => logger info "time widget registered"
    }
  }

  def stop(context: BundleContext) {
    widgetService andApply { _.unRegisterWidget(descriptor) }
    widgetService.andUnget
    templateEngine.andUnget
    database.andUnget
  }
}
