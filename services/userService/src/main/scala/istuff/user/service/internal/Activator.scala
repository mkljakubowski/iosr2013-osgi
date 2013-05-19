package istuff.user.service.internal

import istuff.api.util.Loggable
import org.osgi.framework._
import com.weiglewilczek.scalamodules._
import org.osgi.service.http.HttpService
import istuff.database.service.api.Database
import istuff.user.service.impl.LoginView

class Activator extends BundleActivator with Loggable {
  var httpService : ServiceFinder[HttpService] = null
  var dbService : ServiceFinder[Database] = null

  def start(context: BundleContext) {
    httpService = context findService withInterface[HttpService]
    dbService = context findService withInterface[Database]

    httpService andApply { _.registerServlet("/login",new LoginView(context, dbService),null,null) } match {
      case None => logger error("Login failed to register")
      case _ => logger info("Login registered")
    }

  }

  def stop(context: BundleContext) {
    httpService andUnget
  }
}
