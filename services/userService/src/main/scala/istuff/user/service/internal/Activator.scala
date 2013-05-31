package istuff.user.service.internal

import istuff.api.util.Loggable
import org.osgi.framework._
import com.weiglewilczek.scalamodules._
import org.osgi.service.http.HttpService
import istuff.database.service.api.Database
import istuff.user.service.impl.{WidgetsView, RegisterView, LoginView}
import com.mongodb.DBCollection

class Activator extends BundleActivator with Loggable {

  def start(context: BundleContext) {

    context watchServices withInterface[Database] andHandle {

      case AddingService(databaseService, _) => {
        val db = databaseService getDB()
        val userColl = db getCollection ("istuff.users")
        val userWidgetColl = db getCollection ("istuff.users.widgets")
        registerResources(userColl, userWidgetColl, context)
      }

    }


  }

  def registerResources(userColl: DBCollection, userWidgetColl: DBCollection, context: BundleContext) {

    context watchServices withInterface[HttpService] andHandle {
      case AddingService(httpService, _) => {
        httpService registerServlet("/login", new LoginView(context, userColl), null, null)
        logger info ("Login page registered")
        httpService registerServlet("/register", new RegisterView(context, userColl), null, null)
        logger info ("Register page registered")
        httpService registerServlet("/widgets", new WidgetsView(context, userWidgetColl), null, null)
        logger info ("widgets registered")

      }
    }
  }

  def stop(context: BundleContext) {
  }
}
