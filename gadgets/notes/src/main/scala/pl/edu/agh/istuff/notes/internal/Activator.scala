package pl.edu.agh.istuff.notes.internal

import org.osgi.framework.{BundleContext, BundleActivator}
import com.weiglewilczek.scalamodules._
import istuff.api.util.Loggable
import istuff.widget.service.api.WidgetService
import istuff.api.models.widget.WidgetDescriptor
import org.amdatu.template.processor.TemplateEngine
import istuff.database.service.api.Database
import pl.edu.agh.istuff.notes.impl.NotesServlet

class Activator extends BundleActivator with Loggable {

  var widgetService : WidgetService = _
  var templateEngine : TemplateEngine = _
  var database : Database = _
  var descriptor: WidgetDescriptor = _

  def start(context: BundleContext) {
    val name="notes"

    descriptor = new WidgetDescriptor(name,1,"bursant",
      Map((name, new NotesServlet(name,1))),
      List(""))

    context watchServices withInterface[TemplateEngine]  andHandle {
      case AddingService(te, _) => {
        templateEngine = te
      }
    }

    context watchServices withInterface[Database]  andHandle {
      case AddingService(db, _) => {
        database = db
      }
    }

    context watchServices withInterface[WidgetService]  andHandle {
      case AddingService(ws, _) => {
        widgetService = ws
        widgetService.registerWidget(descriptor, context)
        logger info "notes widget registered"
      }
    }

  }

  def stop(context: BundleContext) {
    widgetService.unRegisterWidget(descriptor)
  }

}
