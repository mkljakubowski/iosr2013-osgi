package istuff.view.service.internal

import org.osgi.framework._
import com.weiglewilczek.scalamodules._
import istuff.view.service.impl.MainPageView
import org.osgi.service.http.HttpService

import istuff.api.util.Loggable
import istuff.database.service.api.Database
import com.mongodb.DBCollection

class Activator extends BundleActivator with Loggable {

  def start(context: BundleContext) {


    context watchServices withInterface[Database]  andHandle {
      case AddingService(dbS, _) => {
        val dbService = dbS
        val db=dbService.getDB()
        val userWidgetColl=db.getCollection("istuff.users.widgets")

        registerResources(userWidgetColl,context)
      }
    }



  }

  def registerResources(userWidgetColl :DBCollection,context: BundleContext)    {
    context watchServices withInterface[HttpService]  andHandle {
      case AddingService(hsS, _) => {

        val httpService = hsS


        httpService registerServlet("/",new MainPageView(context, userWidgetColl),null,null)
        logger info("MainPageView registered")

        httpService registerResources("/res", "/", null)
        logger info("MainPage res/ resources registered")

        httpService registerResources("/res/js", "/js", null)
        logger info("MainPage res/js javascript resources registered")

        httpService registerResources("/res/css/images", "/css/ui-lightness/images", null)
        logger info("MainPage res/css/images registered")

        httpService registerResources("/res/css", "/css/ui-lightness", null)
        logger info("MainPage res/css registered")
      }
    }
  }

  def stop(context: BundleContext) {

  }
}