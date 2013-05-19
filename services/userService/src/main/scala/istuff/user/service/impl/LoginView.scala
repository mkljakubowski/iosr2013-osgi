package istuff.user.service.impl

import org.osgi.framework.BundleContext
import javax.servlet.http.{Cookie, HttpServletResponse, HttpServletRequest, HttpServlet}
import istuff.api.util.Loggable
import com.weiglewilczek.scalamodules._
import istuff.database.service.api.Database
import org.amdatu.template.processor.{TemplateProcessor, TemplateContext, TemplateEngine}
import scala.Some
import istuff.api.models.widget.WidgetDescriptor

class LoginView (context:BundleContext, dbService : ServiceFinder[Database]) extends HttpServlet with Loggable {

  override def doPost(req : HttpServletRequest , resp : HttpServletResponse ) {
    if(req.getParameter("user") != null && req.getParameter("pwd") == "123"){
      resp.addCookie(new Cookie("user", req.getParameter("user")))
      resp.addCookie(new Cookie("auth", "true"))
      resp.setContentType("text/html;charset=UTF-8")
      resp.sendRedirect("/")
    } else {
      resp.addCookie(new Cookie("auth", "false"))
      resp.setContentType("text/html;charset=UTF-8")
      resp.sendRedirect("/login")
    }
  }

  override def doGet(req : HttpServletRequest , resp : HttpServletResponse ) {
    // Retrieve a Velocity implementation of the engine
    val eng = context findService classOf[TemplateEngine]

    // Create & fill the context
    var tcontext : TemplateContext = null
    eng andApply { _.createContext() }   match {
      case None => logger error("No key with that name!")
      case Some(x) =>   tcontext=x
    }

    var processor : TemplateProcessor  = null

    val url = context.getBundle().getResource("login.html")
    eng andApply { _.createProcessor(url)
    }   match {
      case None => logger error("No key with that name!")
      case Some(x) =>   processor=x
    }

    resp.setContentType("text/html;charset=UTF-8")
    resp.setContentType("text/html")
    val out = resp.getWriter()
    processor.generateStream(tcontext,out)
  }

}
