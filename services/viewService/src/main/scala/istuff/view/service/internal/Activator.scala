package istuff.view.service.internal

import org.osgi.framework._
import com.weiglewilczek.scalamodules._
import istuff.view.service.impl.{MainPageView, IndexViewResource}
import istuff.widget.service.api.WidgetService
import org.osgi.service.http.HttpService

import org.amdatu.template.processor.{TemplateProcessor, TemplateContext, TemplateEngine}
import javax.servlet.http.{HttpServletResponse, HttpServletRequest, HttpServlet}
import java.io.File
import istuff.api.util.Loggable
import istuff.database.service.api.Database
import com.mongodb.DBCollection

class Activator extends BundleActivator with Loggable {
  var serviceRegistration: ServiceRegistration = _
  var httpService : ServiceFinder[HttpService] = null
  var dbService : ServiceFinder[Database] = null

  def start(context: BundleContext) {

    dbService = context findService withInterface[Database]

    val userWidgetColl:DBCollection = dbService andApply { _.getDB() } match {
      case Some(db) => db.getCollection("istuff.users.widgets")
      case _ => logger error("No DB"); null
    }

    httpService = context findService withInterface[HttpService]
    httpService andApply { _.registerServlet("/",new MainPageView(context, userWidgetColl),null,null) } match {
      case None => logger error("MainPage failed to register")
      case _ => logger info("MainPage registered")
    }

    httpService andApply { _.registerResources("/res", "/", null) } match {
      case None => logger error("MainPage res failed to register")
      case _ => logger info("MainPage res registered")
    }

    httpService andApply { _.registerResources("/res/js", "/js", null) } match {
      case None => logger error("MainPage res/js failed to register")
      case _ => logger info("MainPage res/js registered")
    }

    httpService andApply { _.registerResources("/res/css/images", "/css/ui-lightness/images", null) } match {
      case None => logger error("MainPage res/css/images failed to register")
      case _ => logger info("MainPage res/css/images registered")
    }

    httpService andApply { _.registerResources("/res/css", "/css/ui-lightness", null) } match {
      case None => logger error("MainPage res/css failed to register")
      case _ => logger info("MainPage res/css registered")
    }

  }

  def stop(context: BundleContext) {
    httpService andUnget
  }
}