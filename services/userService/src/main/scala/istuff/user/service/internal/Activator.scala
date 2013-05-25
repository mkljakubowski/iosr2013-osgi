package istuff.user.service.internal

import istuff.api.util.Loggable
import org.osgi.framework._
import com.weiglewilczek.scalamodules._
import org.osgi.service.http.HttpService
import istuff.database.service.api.Database
import istuff.user.service.impl.{WidgetsView, RegisterView, LoginView}
import com.mongodb.DBCollection

class Activator extends BundleActivator with Loggable {
  var httpService : ServiceFinder[HttpService] = null
  var dbService : ServiceFinder[Database] = null

  def start(context: BundleContext) {
    httpService = context findService withInterface[HttpService]
    dbService = context findService withInterface[Database]

    val userColl:DBCollection = dbService andApply { _.getDB() } match {
      case Some(db) => db.getCollection("istuff.users")
      case _ => logger error("No DB"); null
    }

    val userWidgetColl:DBCollection = dbService andApply { _.getDB() } match {
      case Some(db) => db.getCollection("istuff.users.widgets")
      case _ => logger error("No DB"); null
    }

    httpService andApply { _.registerServlet("/login",new LoginView(context, userColl),null,null) } match {
      case None => logger error("Login failed to register")
      case _ => logger info("Login registered")
    }

    httpService andApply { _.registerServlet("/register",new RegisterView(context, userColl),null,null) } match {
      case None => logger error("Login failed to register")
      case _ => logger info("Login registered")
    }

    httpService andApply { _.registerServlet("/widgets",new WidgetsView(context, userWidgetColl),null,null) } match {
      case None => logger error("widgets failed to register")
      case _ => logger info("widgets registered")
    }

  }

  def stop(context: BundleContext) {
    httpService andUnget
  }
}
