package pl.edu.agh.istuff.gallery.internal

import org.osgi.framework.{BundleContext, BundleActivator}
import com.weiglewilczek.scalamodules._
import istuff.api.util.Loggable
import istuff.widget.service.api.WidgetService
import istuff.api.models.widget.WidgetDescriptor
import org.amdatu.template.processor.TemplateEngine
import istuff.database.service.api.Database
import pl.edu.agh.istuff.gallery.impl.GalleryServlet

class Activator extends BundleActivator with Loggable {

  var widgetService : WidgetService = _
  var templateEngine : TemplateEngine = _
  var database : Database = _
  var descriptor: WidgetDescriptor = _

  def start(context: BundleContext) {
    val name="gallery"

    descriptor = new WidgetDescriptor(name,1,"tomek",
      Map((name, new GalleryServlet(name, 1))),
      List("","images"), 400, 400, 100, 100)

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
        logger info "gallery widget registered"
      }
    }

  }

  def stop(context: BundleContext) {
    widgetService.unRegisterWidget(descriptor)
  }

}
